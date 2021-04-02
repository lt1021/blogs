package com.blog.microsoul.weapi.hssf;


import com.blog.microsoul.weapi.util.VirtualOSGlobals;
import org.apache.poi.hssf.usermodel.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by openxtiger.org
 * User: smTiger
 * Date: 2009-1-7
 * Time: 10:26:40
 */
public class HSSFParser {
    private Stack<Processer> processers;
    private HashMap<String, Processer> pMaps;
    private HashMap<String, Column> cMaps;

    public int parse(HSSFWorkbook wb, Object obj, int idx) {
        HSSFSheet sheet = wb.getSheetAt(idx);
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

        processers = new Stack<Processer>();
        pMaps = new HashMap<String, Processer>();
        cMaps = new HashMap<String, Column>();

        /*List lst = wb.getAllPictures();
    for (Object aLst : lst) {
        HSSFPictureData pict = (HSSFPictureData) aLst;
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        *//*int r = wb.addPicture(pict.getData(), HSSFWorkbook.PICTURE_TYPE_JPEG);
            System.out.println(r);*//*
            HSSFPicture picture = patriarch.createPicture(new HSSFClientAnchor(), 1);
            picture.resize();

        }*/
        /*HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        HSSFPicture picture = patriarch.createPicture(new HSSFClientAnchor(), 1);
            picture.resize();*/

        /*for (Object aLst : wb.getAllPictures()) {
            System.out.println("dddd");
        }*/

        //1.parse lists;
        parseList(wb, sheet);

        //parse cells
        int pStartRow = sheet.getFirstRowNum(), pEndRow = sheet.getLastRowNum();
        HSSFRow sourceRow;
        HSSFCell sourceCell;
        Processer p = new Processer();
        processers.push(p);
        p.init(obj, sheet, wb, patriarch);
        Processer root = p;

        p.setName("ROOT", null);


        for (int i = pStartRow; i <= sheet.getLastRowNum(); i++) {
            sourceRow = sheet.getRow(i);
            if (sourceRow == null) {
                continue;
            }
            for (int j = sourceRow.getFirstCellNum(), k = sourceRow.getLastCellNum(); j <= k; j++) {

                sourceCell = sourceRow.getCell(j);
                if (sourceCell == null) {
                    continue;
                }

                makeCellStyle(sourceCell.getCellStyle(), sourceCell, obj, wb);

                if (sourceCell.getCellType() != HSSFCell.CELL_TYPE_STRING) continue;
                String v = sourceCell.getRichStringCellValue().getString().trim();
                p = makeKey(v, p, sourceRow, sourceCell, p.getObject(), sheet, wb, patriarch);
                if (p.getNextRow() > 0) {
                    i = p.getNextRow() - 1;
                    break;
                }
            }

        }
        //remove lists
        for (Column cc : cMaps.values()) {
            for (int i = cc.getEndCell().getRowIndex(), l = cc.getStartCell().getRowIndex(); i >= l; i--) {
                sheet.removeRow(sheet.getRow(i));
            }
        }
        return 0;
    }

    private void makeCellStyle(HSSFCellStyle style, HSSFCell targetCell, Object obj, HSSFWorkbook wb) {
        String v = style.getDataFormatString();
        v = v.replaceAll("\\\\", "");
        ArrayList<String> vs = VirtualOSGlobals.split("~([^\\}]*)~", v);
        if (vs.size() == 1) return;

        StringBuilder b = new StringBuilder();
        if (obj instanceof Map) {
            Map o = (Map) obj;
            int i, l = vs.size();
            for (i = 0; i < l; i += 2) {
                b.append(vs.get(i));
                b.append(o.get(vs.get(i + 1)));
            }
            if (i < l) {
                b.append(vs.get(l - 1));
            }
            style.setDataFormat(wb.createDataFormat().getFormat(b.toString()));
            targetCell.setCellStyle(style);
        }

    }

    /**
     * Analysis key
     * ${key}
     * $=     HSSF_FORMULA
     * $~()   HSSF_PICTURE
     * $!     HSSF_EVAL
     * $[< (TitleRows,DataRows,ListTop,Cols,ListCols,RepeatTitle)]    use external list
     * $<    use external list
     * $>
     * $]
     * <p/>
     * #{key} numeric key
     *
     * @param v
     * @param p
     * @param sourceRow
     * @param targetCell
     * @param obj
     * @param sheet
     * @param wb
     * @param patriarch
     * @return
     */
    private Processer makeKey(String v, Processer p, HSSFRow sourceRow, HSSFCell targetCell, Object obj,
                              HSSFSheet sheet, HSSFWorkbook wb, HSSFPatriarch patriarch) {
        ArrayList<String> vs = VirtualOSGlobals.split("[\\$\\#]\\{([^\\}]*)\\}", v);
        p.setNextRow(0);
        if (vs.size() > 0) {
            String f = vs.get(0);
            int style = 0;
            boolean iskey = false;
            boolean inner = true;
            if (f.startsWith("$") && f.length() >= 2) {
                iskey = true;
                Column cc;
                switch (f.charAt(1)) {
                    case '=':
                        f = f.substring(2);
                        style = Cell.HSSF_FORMULA;
                        break;
                    case '~':
                        f = f.substring(2);
                        String[] fs = f.split("[\\(\\),]");
                        if (fs.length == 0) return p;

                        Cell c = new Cell();
                        c.setStyle(Cell.HSSF_PICTURE);
                        c.setTargetCell(targetCell);
                        c.setStrings(fs);
                        c.setVariables(new ArrayString[]{new ArrayString(fs)});
                        p.addCell(c);
                        targetCell.setCellValue(new HSSFRichTextString());
                        return p;
                    case '!':
                        f = f.substring(2);
                        style = Cell.HSSF_EVAL;
                        break;
                    case '[':
                        inner = false;
                        if (f.length() >= 3 && f.charAt(2) == '<') {

                            f = f.substring(3);
                            String[] fun = f.split("[\\(\\),]");
                            if (fun.length == 0) return p;
                            cc = new Column();
                            cc.setSheet(sheet);
                            cc.setStartCell(targetCell);
                            cc.setTitleRows(fun.length >= 2 ? VirtualOSGlobals.parseInt(fun[1], 0) : 0);
                            cc.setDataRows(fun.length >= 3 ? VirtualOSGlobals.parseInt(fun[2], 0) : 0);
                            cc.setListTop(fun.length >= 4 ? VirtualOSGlobals.parseInt(fun[3], 0) : 0);
                            cc.setCols(fun.length >= 5 ? VirtualOSGlobals.parseInt(fun[4], 1) : 1);
                            cc.setListCols(fun.length >= 6 ? VirtualOSGlobals.parseInt(fun[5], 1) : sourceRow.getPhysicalNumberOfCells());
                            cc.setRepeatTitle(fun.length >= 7 && "TRUE".equalsIgnoreCase(fun[6]));
                            p.setColumn(cc);
                            targetCell.setCellValue(new HSSFRichTextString());
                            return p;
                        }
                    case '<':
                        f = f.substring(2);
                        String[] fun = f.split("[\\(\\),\\]]");
                        if (fun.length == 0) return p;

                        Processer p1 = new Processer();
                        p1.init(sheet, wb, patriarch);
                        p.addChild(p1);

                        p1.setParent(p);
                        pMaps.put(fun[0], p1);
                        String[] ns = fun[0].split("\\.");
                        String n = ns.length == 0 ? fun[0] : ns[ns.length - 1];
                        p1.setName(n, obj);
                        p1.setArgs(fun);

                        if (!inner) {
                            int row = sourceRow.getRowNum();
                            cc = cMaps.get(fun[0]);
                            if (cc != null) {
                                int cnt = cc.getEndCell().getRowIndex() - cc.getStartCell().getRowIndex() - 1;
                                int last = 0;
                                HSSFRow r;
                                if (cnt > 1) {
                                    sheet.shiftRows(row + 1, sheet.getLastRowNum(), cnt - 1, false, false);

                                    for (int i = cc.getStartCell().getRowIndex() + 1, l = cc.getEndCell().getRowIndex(), j = 0; i < l; i++, j++) {
                                        r = cc.getSheet().getRow(i);
                                        last = HSSFTools.copyRow(r, row + j, cc.getStartCell().getRowIndex(), l, r.getFirstCellNum(), r.getFirstCellNum() + r.getPhysicalNumberOfCells(),
                                                targetCell.getColumnIndex(), sheet, wb);
                                    }
                                } else {
                                    r = cc.getSheet().getRow(cc.getStartCell().getRowIndex() + 1);
                                    last = HSSFTools.copyRow(r, row, cc.getStartCell().getRowIndex() + 1, cc.getStartCell().getRowIndex() + 1, r.getFirstCellNum(), r.getFirstCellNum() + r.getPhysicalNumberOfCells(),
                                            targetCell.getColumnIndex(), sheet, wb);
                                }
                                p.setNextRow(row + cc.getTitleRows());
                                if (f.endsWith("]")) {
                                    last += targetCell.getColumnIndex();
                                    HSSFRow lastRow = sheet.getRow(row + cnt - 1);
                                    HSSFCell lastCell = lastRow.getCell(last);
                                    if (lastCell == null) {
                                        lastCell = lastRow.createCell(last);
                                    }
                                    lastCell.setCellValue(new HSSFRichTextString("$]"));
                                }
                                cc.setListTop(targetCell.getColumnIndex());
                                cc.setRepeatTitle(fun.length >= 2 && "TRUE".equalsIgnoreCase(fun[1]));
                                cc.setStartCell(targetCell);
                            }
                        } else {
                            cc = new Column();
                            cc.setSheet(sheet);
                            cc.setStartCell(targetCell);
                            cc.setTitleRows(fun.length >= 2 ? VirtualOSGlobals.parseInt(fun[1], 0) : 0);
                            cc.setDataRows(fun.length >= 3 ? VirtualOSGlobals.parseInt(fun[2], 0) : 0);
                            cc.setListTop(fun.length >= 4 ? VirtualOSGlobals.parseInt(fun[3], 0) : 0);
                            cc.setListCols(fun.length >= 5 ? VirtualOSGlobals.parseInt(fun[4], 0) : 0);
                            cc.setRepeatTitle(fun.length >= 6 && "TRUE".equalsIgnoreCase(fun[5]));
                            cc.setPageOffset(fun.length >= 7 ? VirtualOSGlobals.parseInt(fun[6], 0) : 0);
                            cc.setPageRows(fun.length >= 8 ? VirtualOSGlobals.parseInt(fun[7], 0) : 0);
                        }
                        p1.setColumn(cc);
                        processers.push(p1);
                        targetCell.setCellValue(new HSSFRichTextString());
                        return p1;
                    case ']':
                    case '>':
                        targetCell.setCellValue(new HSSFRichTextString());
                        if (f.length() >= 3 && f.charAt(2) == ']') {
                            p = processers.peek();
                            if (p.getColumn() != null) {
                                p.getColumn().setEndCell(targetCell);
                                p.setLastEndCell(targetCell);
                                return p;
                            }
                        } else {
                            p = processers.pop();
                            if (p.getColumn() != null) {
                                p.getColumn().setEndCell(targetCell);
                                p.setLastEndCell(targetCell);
                                return p.getParent();
                            }
                        }
                }
            }

            if (vs.size() == 1 && !iskey)
                return p;

            if (v.trim().startsWith("#")) {
                style = Cell.HSSF_NUMERIC;
            }

            Cell c = new Cell();

            c.setStyle(style);
            c.setTargetCell(targetCell);

            ArrayList<String> strs = new ArrayList<String>();
            ArrayList<ArrayString> vars = new ArrayList<ArrayString>();

            strs.add(f);

            for (int i = 2, l = vs.size(); i < l; i += 2) {
                f = vs.get(i);
                /* if (f.startsWith("\\"))
              f = f.substring(1);*/
                strs.add(f);
            }
            String[] ss = new String[strs.size()];
            c.setStrings(strs.toArray(ss));

            for (int i = 1, l = vs.size(); i < l; i += 2) {
                //System.out.println(vs.get(i));
                ArrayString s = new ArrayString(vs.get(i).split("[\\(\\),\\]]"));
                vars.add(s);
            }
            ArrayString[] as = new ArrayString[vars.size()];
            c.setVariables(vars.toArray(as));

            p.addCell(c);
            targetCell.setCellValue(new HSSFRichTextString());
        }
        return p;
    }

    private void parseList(HSSFWorkbook wb, HSSFSheet sheet) {
        int pStartRow = sheet.getFirstRowNum(), pEndRow = sheet.getLastRowNum();
        HSSFRow sourceRow;
        HSSFCell sourceCell;
        for (int i = pStartRow; i <= pEndRow; i++) {
            sourceRow = sheet.getRow(i);
            if (sourceRow == null) {
                continue;
            }
            for (int j = sourceRow.getFirstCellNum(), k = sourceRow.getLastCellNum(); j <= k; j++) {

                sourceCell = sourceRow.getCell(j);
                if (sourceCell == null) {
                    continue;
                }
                if (sourceCell.getCellType() != HSSFCell.CELL_TYPE_STRING) continue;
                String v = sourceCell.getRichStringCellValue().getString().trim();
                makeListKey(v, i, sheet, sourceCell);
            }
        }
    }

    /**
     * external list
     * start tag: $[[(name,titleRows,dataRows)
     * end tag:   $]]
     *
     * @param v     row value
     * @param row   row
     * @param sheet sheet
     * @param cell  cell
     */
    private void makeListKey(String v, int row, HSSFSheet sheet, HSSFCell cell) {
        ArrayList<String> vs = VirtualOSGlobals.split("\\$([\\[\\]]{2}.*)", v);   //
        Column c;
        if (vs.size() > 1) {
            String f = vs.get(1);
            if (f.length() <= 3) return;
            switch (f.charAt(1)) {
                case '[':
                    f = f.substring(2);
                    String[] fun = f.split("[\\(\\),]");
                    if (fun.length != 3) break;
                    c = new Column();
                    c.setSheet(sheet);
                    c.setStartCell(cell);
                    c.setTitleRows(VirtualOSGlobals.parseInt(fun[1], 0));
                    c.setDataRows(VirtualOSGlobals.parseInt(fun[2], 0));
                    //System.out.println(fun[0] + "," + fun[1] + "," + fun[2]);
                    cMaps.put(fun[0], c);
                    break;
                case ']':
                    f = f.substring(2);
                    c = cMaps.get(f);
                    if (c == null) break;
                    c.setEndCell(cell);
                    break;
            }
            cell.setCellValue(new HSSFRichTextString());
        }
    }

    public int process(Object obj, File pf) {
        //System.out.println("\n----------------------------process-----------------------------");
        Processer p = processers.pop();
        return p.process(pMaps, obj, pf);
    }


}
