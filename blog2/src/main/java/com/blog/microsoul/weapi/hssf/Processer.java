package com.blog.microsoul.weapi.hssf;


import com.blog.microsoul.weapi.poi.POIHelper;
import com.blog.microsoul.weapi.poi.hssf.HSSFPOIHelper;
import com.blog.microsoul.weapi.util.VirtualOSGlobals;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;

import javax.imageio.ImageIO;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by openxtiger.org
 * User: smTiger
 * Date: 2009-1-7
 * Time: 10:36:22
 */
public class Processer {
    private static final HashMap<Class, BeanData> beanCache = new HashMap<Class, BeanData>();
    private Object obj, lastObj;
    private int nextRow;
    private Processer parent;
    private ArrayList<Processer> childs = new ArrayList<Processer>();
    private ArrayList<Cell> cells = new ArrayList<Cell>();
    private String name;
    private String[] args;
    private Column column;
    private HashMap<String, Method> readableProps = null;

    private HSSFCell lastStartCell;
    private HSSFCell firstStartCell;
    private HSSFCell lastEndCell;
    private int topMargin = 0;
    private int leftMargin = 0;
    private int lastMaxCount = 0;

    private HSSFSheet sheet;
    private HSSFWorkbook workbook;
    private HSSFPatriarch patriarch;

    private static final float PX_DEFAULT = 32.00f;
    private static final float PX_MODIFIED = 36.56f;
    private File tempDir;
    private CreationHelper creationHelper;
    private POIHelper poiHelper;

    public Object getObject() {
        return obj;
    }

    public void setNextRow(int row) {
        this.nextRow = row;
    }

    public int getNextRow() {
        return nextRow;
    }

    public void setSheet(HSSFSheet sheet) {
        this.sheet = sheet;
    }

    public void setWorkbook(HSSFWorkbook workbook) {
        this.workbook = workbook;
    }

    public void init(Object obj, HSSFSheet sheet, HSSFWorkbook workbook, HSSFPatriarch patriarch) {
        this.workbook = workbook;
        this.sheet = sheet;
        this.patriarch = patriarch;
        this.creationHelper = workbook.getCreationHelper();
        this.poiHelper = new HSSFPOIHelper(workbook, sheet, patriarch);
        setObject(obj);
    }

    public void init(HSSFSheet sheet, HSSFWorkbook workbook, HSSFPatriarch patriarch) {
        this.workbook = workbook;
        this.sheet = sheet;
        this.patriarch = patriarch;
        this.creationHelper = workbook.getCreationHelper();
        this.poiHelper = new HSSFPOIHelper(workbook, sheet, patriarch);
    }


    public void setColumn(Column column) {
        this.column = column;
        lastStartCell = HSSFTools.getCell(sheet, isRepeatTitle() ? 0 : getTitleRows(), column.getListTop(), column.getStartCell());
        if (this.parent != null) {
            topMargin = column.getStartCell().getRowIndex() - this.parent.getStartRow();
            //System.out.println(this.getName() + ":" + topMargin);
            leftMargin = column.getListTop() - this.parent.getListTop();
            if (column.getListCols() == 0 && this.parent.getColumn() != null) {
                column.setListCols(this.parent.getColumn().getListCols());
            }
        }
    }

    public void setLastEndCell(HSSFCell lastEndCell) {
        this.lastEndCell = HSSFTools.getCell(sheet, 0, this.column.getListTop(), lastEndCell);
    }

    public Column getColumn() {
        return column;
    }

    public void setObject(Object obj) {
        if (obj != null && obj instanceof List) {
            obj = ((List) obj).size() > 0 ? ((List) obj).get(0) : null;
        }
        this.obj = obj;
        if (obj != null && obj instanceof Map) {
            return;
        }
        try {
            if (obj != null) {
                readableProps = getBeanData(obj.getClass()).readableProps;
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
    }

    public void setLastObj(Object lastObj) {
        this.lastObj = lastObj;
    }

    protected static class BeanData {
        public BeanInfo beanInfo;
        public HashMap<String, Method> readableProps;
    }

    public static BeanData analyzeBean(Class clazz)
            throws IntrospectionException {
        BeanData bd = new BeanData();
        bd.beanInfo = Introspector.getBeanInfo(clazz, Object.class);
        PropertyDescriptor props[] = bd.beanInfo.getPropertyDescriptors();
        bd.readableProps = new HashMap<String, Method>();
        for (PropertyDescriptor prop : props) {
            if (prop.getReadMethod() != null) {
                bd.readableProps.put(prop.getName(),
                        prop.getReadMethod());
            }
        }
        return bd;
    }

    public static BeanData getBeanData(Class clazz)
            throws IntrospectionException {
        BeanData bd;
        synchronized (beanCache) {
            bd = beanCache.get(clazz);
            if (bd == null) {
                bd = analyzeBean(clazz);
                beanCache.put(clazz, bd);
            }
        }
        return bd;
    }

    public int process(HashMap<String, Processer> pMaps, Object obj, File pf) {
        lastObj = obj;
        tempDir = pf;
        return process(pMaps);
    }

    public int process(HashMap<String, Processer> pMaps) {
        Object obj = lastObj;

        int r = 0;
        int count = this.getCount();
        int cols = this.getCols();
        int listCols = this.getListCols();

        int drow = HSSFTools.getRow(count, cols);
        ArrayList ls;
        if (!(obj instanceof ArrayList)) {
            ls = new ArrayList();
            ls.add(obj);
        } else {
            ls = (ArrayList) obj;
        }
        if (ls.size() == 0) return 0;

        //System.out.println(this.getName() + ":count->" + count);
        int startrow = this.getLastStartRow();
        int rows = this.getCountRows();


        if (count > 1) {
            int startcol = this.getLastStartCol();
            //System.out.println(this.getName() + ":---------->lastStart<-----------" + startrow + "," + startcol);
            //System.out.println(this.getName() + ":---------->shiftRows<-----------" + lastMaxCount + "," + drow);

            if (drow > 1) {
                if (lastMaxCount == 0 && sheet.getLastRowNum() >= startrow + rows) {
                    sheet.shiftRows(startrow + rows, sheet.getLastRowNum(), rows * (drow - 1), true, false);
                } else if (drow > lastMaxCount && sheet.getLastRowNum() >= startrow + rows * lastMaxCount) {
                    sheet.shiftRows(startrow + rows * lastMaxCount, sheet.getLastRowNum(), rows * (drow - lastMaxCount), true, false);
                }
            }

            //System.out.println(this.getName() + ":---------->copyMergeds<-----------");
            HSSFTools.copyMergeds(startrow, startcol, rows, cols, listCols, count, sheet);

            //System.out.println(this.getName() + ":---------->copyList<-----------");
            HSSFTools.copyList(startrow, startcol, rows, cols, listCols, count, sheet, workbook);
        }
        //System.out.println(this.getName() + ":---------->copyStyle<-----------");
        if (drow != lastMaxCount && lastMaxCount != 0) {
            HSSFTools.copyStyle(startrow, rows, lastMaxCount, drow, sheet, workbook);
        }
        firstStartCell = lastStartCell;
        for (index = 0; index < count; index++) {
            //System.out.println(this.getName() + ":---------->processObject<-----------" + index);
            processObject(ls.get(index), false);

            for (Processer child : this.childs) {
                r = child.process(pMaps);
            }
            //System.out.println(this.getName() + ":----------->processVars<------------" + index);
            processVars(ls.get(index), pMaps);

            if (index < count - 1) {
                //System.out.println(this.getName() + ":setListNext   k=" + index + "," + (this.isRepeatTitle() ? this.getLastEndCell() : this.getLastStartCell()).getRowIndex());
                setListNext(this.isRepeatTitle() ? this.getLastEndCell() : this.getLastStartCell(), cols);
            }

            if (this.getName().equals("ROOT") && index == 0) break;
        }

        if (column != null && column.getPageOffset() > 0) {
            for (int i = 0; i < column.getPageCount(count); i++) {
                sheet.setRowBreak(column.getPageOffset() + i * column.getPageRows());
            }
        }
        return r;
    }

    public void setListNext(HSSFCell cell, int cols) {
        setListNext(cell, cols, false, false);
    }

    public void setListNext(HSSFCell cell, int cols, boolean isChild, boolean isCol) {
        if (cell == null) return;
        if (isChild) {
            if (isCol) {
                int r = HSSFTools.getRow(this.getCount(), cols);
                lastMaxCount = Math.max(r, lastMaxCount);
                //System.out.println(this.getName() + ":lastMaxCount->" + lastMaxCount);
            } else {
                lastMaxCount = 0;
                //System.out.println(this.getName() + ":lastMaxCount=0");
            }
        }
        boolean newRow = (index + 1) % cols == 0;
        if (newRow) {
            lastStartCell = HSSFTools.getCell(sheet, 1, cell);
        } else if (cols > 1) {
            lastStartCell = HSSFTools.getCell(sheet, 0, lastStartCell.getColumnIndex() + this.getListCols(), lastStartCell);
        }
        if (lastStartCell == null) return;


        if (isChild || this.isRepeatTitle()) {
            if (newRow) {
                lastEndCell = lastStartCell == null ? null : HSSFTools.getCell(sheet, this.getListCount() - 1, lastStartCell);
                for (Cell c : cells) {
                    c.setLastTargetCell(HSSFTools.getCell(sheet, c.getTopMargin(), lastStartCell.getColumnIndex() + c.getLeftMargin(), lastStartCell));
                }
            } else {
                //System.out.println(this.getName() + "---------------");
                for (Cell c : cells) {
                    c.setLastTargetCell(HSSFTools.getCell(sheet, 0, lastStartCell.getColumnIndex() + c.getLeftMargin(), c.getLastTargetCell()));
                }
            }

        } else {
            if (newRow) {
                for (Cell c : cells) {
                    c.setLastTargetCell(HSSFTools.getCell(sheet, this.getDataRows(), c.getLastTargetCell()));
                }
            } else {
                for (Cell c : cells) {
                    c.setLastTargetCell(HSSFTools.getCell(sheet, 0, c.getLastTargetCell()));
                }
            }
        }
        if (lastStartCell != null) {
            for (Processer child : this.childs) {
                if (!isChild && !newRow) {
                    //System.out.println(this.getName() + "." + child.getName() + ":setChildListNext----------------->");
                    child.setListNext(HSSFTools.getCell(sheet, -1,
                            lastStartCell.getColumnIndex() + child.getLeftMargin(), child.getFirstStartCell()), child.getCols(), true, true);
                } else {
                    //System.out.println(this.getName() + "." + child.getName() + ":setListNext----------------->");
                    child.setListNext(HSSFTools.getCell(sheet, child.getTopMargin(),
                            lastStartCell.getColumnIndex() + child.getLeftMargin(), lastStartCell), child.getCols(), true, false);
                }

            }
        }
    }

    public void processObject(Object obj, boolean isChild) {
        if (isChild) {
            lastObj = obj;
        }

        if (obj instanceof ArrayList) {
            ArrayList ls = ((ArrayList) obj);
            if (ls.size() == 0) return;
            obj = ls.get(0);

        }

        for (Processer child : this.childs) {
            if (obj instanceof Map) {
                child.processObject(((Map) obj).get(child.getName()), true);
                continue;
            }
            Method getMethod = readableProps.get(child.getName());
            if (getMethod != null) {
                try {
                    child.processObject(getMethod.invoke(obj), true);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public void processVars(Object obj, HashMap<String, Processer> pMaps) {
        String cal;
        String filePath;
        for (Cell c : cells) {
            StringBuilder b = new StringBuilder();
            String[] strs = c.getStrings();
            ArrayString[] vars = c.getVariables();
            if (c.getStyle() == Cell.HSSF_PICTURE || c.getStyle() == Cell.HSSF_PICTURE1) {
                try {
                    filePath = (String) ((Map) obj).get(vars[0].getStrs()[0]);
                    if (filePath == null) continue;
                    File f = new File(VirtualOSGlobals.getDirectory(), filePath);
                    if (!f.exists()) continue;
                    ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
                    java.awt.image.BufferedImage bufferImg = ImageIO.read(f);
                    ImageIO.write(bufferImg, "jpg", byteArrayOut);
                    if (obj instanceof Map) {
                        createPicture(c, byteArrayOut.toByteArray(), bufferImg.getWidth(), bufferImg.getHeight());
                        continue;
                    }
                    /*Method getMethod = readableProps.get(vars[0].getStrs()[0]);
                    if (getMethod != null) {
                        try {
                            createPicture(c, loadPicture((String) getMethod.invoke(obj)));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }


                continue;
            }

            String f;
            String[] dvars;
            HashMap<String, Method> ps = readableProps;
            if (c.getStyle() == Cell.HSSF_STYLE) {
                obj = c.getObj();
            }
            for (int i = 0, ls = vars.length; i < ls; i++) {

                Object oo = obj;
                b.append(strs[i]);

                dvars = vars[i].getStrs();
                f = dvars[0];
                cal = null;
                switch (f.charAt(0)) {
                    case '#':
                        f = f.substring(1);
                        int idx = f.indexOf("+");
                        if (idx > 0) {
                            cal = f.substring(idx + 1).trim();
                            f = f.substring(0, idx);
                        }

                        if ((idx = f.lastIndexOf(".")) >= 0) {
                            oo = pMaps.get(f.substring(0, idx));
                            f = f.substring(idx + 1);
                        } else {
                            oo = this;
                        }
                        if (!(oo instanceof Map)) {
                            try {
                                ps = getBeanData(oo.getClass()).readableProps;
                            } catch (IntrospectionException e) {
                                e.printStackTrace();
                                break;
                            }
                        }
                        break;
                    case '^':
                        String[] fas = f.split("~");
                        f = fas[0].substring(1);
                        idx = f.indexOf("+");
                        cal = "0";
                        if (idx > 0) {
                            cal = f.substring(idx + 1).trim();
                            f = f.substring(0, idx);
                        }
                        int v = 0;
                        if (obj instanceof Map) {
                            Object vo = ((Map) obj).get(f);
                            if (vo != null)
                                v = ((Long) vo).intValue();
                        } else {
                            Method getMethod = readableProps.get(f);
                            if (getMethod != null) {
                                try {
                                    v = (Integer) getMethod.invoke(obj);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        b.append(fas[v + VirtualOSGlobals.parseInt(cal, 0)]);
                        continue;

                }
                if (oo instanceof Map) {
                    Object r = ((Map) oo).get(f);
                    boolean a = false;
                    if (dvars.length > 1) {
                        if (r instanceof Number) {
                            if (dvars.length > 2 && ((Number) r).doubleValue() == VirtualOSGlobals.parseInt(dvars[2], 0)) {
                                c.setStyle(0);
                                b.append("");
                            } else {
                                b.append(VirtualOSGlobals.round(((Number) r).doubleValue(), VirtualOSGlobals.parseInt(dvars[1], 0)));
                            }

                            a = true;
                        } else {
                            if (dvars.length > 2 && r == null) {
                                c.setStyle(0);
                                b.append("");
                            }
                        }
                    }
                    if (!a && r != null)
                        b.append(r);
                } else {
                    Method getMethod = ps.get(f);
                    if (getMethod != null) {
                        try {
                            Object r = getMethod.invoke(oo);
                            boolean a = false;
                            if (dvars.length > 1) {
                                if (r instanceof Number) {
                                    b.append(VirtualOSGlobals.round(((Number) r).doubleValue(), VirtualOSGlobals.parseInt(dvars[1], 0)));
                                    a = true;
                                }
                            }
                            if (!a && r != null) {
                                b.append(cal == null ? r : Integer.parseInt(r.toString()) + Integer.parseInt(cal));
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (strs.length > vars.length) {
                b.append(strs[strs.length - 1]);
            }
            switch (c.getStyle()) {
                case Cell.HSSF_FORMULA:
                    c.getLastTargetCell().setCellFormula(b.toString());
                    //System.out.println(b.toString());
                    break;
                case Cell.HSSF_EVAL:
                    HSSFCell cell = c.getLastTargetCell();
                    cell.setCellFormula(b.toString());
                    HSSFFormulaEvaluator evaluator = new HSSFFormulaEvaluator(workbook);
                    evaluator.evaluateInCell(cell);
                    //cell.setCellType(valueType);
                    break;
                case Cell.HSSF_NUMERIC:
                    if (b.toString().equals("")) b.append("0");
                    if (b.toString().matches("^[-0-9]*$")) {
                        c.getLastTargetCell().setCellValue(Integer.parseInt(b.toString()));
                    } else if (b.toString().matches("^[-0-9.]*$")) {
                        c.getLastTargetCell().setCellValue(Double.parseDouble(b.toString()));
                    }

                    break;
                case Cell.HSSF_ARRAY:

                    break;
                default:
                    c.getLastTargetCell().setCellValue(new HSSFRichTextString(b.toString()));
                    break;
            }
        }
    }

    private void createPicture(Cell c, byte[] bytes, int width, int height) {
        if (bytes == null) return;
        int r = workbook.addPicture(bytes, HSSFWorkbook.PICTURE_TYPE_JPEG);
        int col = c.getLastTargetCell().getColumnIndex();
        int row = c.getLastTargetCell().getRowIndex();
        String[] fun = c.getStrings();


        ClientAnchor anchor = this.creationHelper.createClientAnchor();

        anchor.setCol1(col);
        anchor.setCol2(col + (fun.length >= 2 ? VirtualOSGlobals.parseInt(fun[1], 0) : 0));

        anchor.setRow1(row);
        anchor.setRow2(row + (fun.length >= 3 ? VirtualOSGlobals.parseInt(fun[2], 0) : 0));


        anchor.setDx1(fun.length >= 4 ? VirtualOSGlobals.parseInt(fun[3], 0) : 0);
        anchor.setDy1(fun.length >= 5 ? VirtualOSGlobals.parseInt(fun[4], 0) : 0);
        anchor.setDx2(fun.length >= 6 ? VirtualOSGlobals.parseInt(fun[5], 0) : 0);
        anchor.setDy2(fun.length >= 7 ? VirtualOSGlobals.parseInt(fun[6], 0) : 0);


        poiHelper.createPicture(anchor, width, height, bytes, 0);


    }

    /*private byte[] loadPicture(String filePath) {
        try {
            if (filePath == null) return null;
            File f = new File(com.microsoul.weapi.util.WeosoGlobals.getDirectory(), filePath);
            if (!f.exists())
                return null;
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
            java.awt.image.BufferedImage bufferImg = ImageIO.read(f);
            ImageIO.write(bufferImg, "jpg", byteArrayOut);
            return byteArrayOut.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

    public void setParent(Processer parent) {
        this.parent = parent;
    }


    public void addCell(Cell c) {
        c.setTopMargin(c.getTargetCell().getRowIndex() - this.getLastStartRow());
        c.setLeftMargin(c.getTargetCell().getColumnIndex() - this.getLastStartCol());
        this.cells.add(c);
    }

    public void addChild(Processer child) {
        this.childs.add(child);
    }

    public void setName(String name, Object obj) {
        this.name = name;
        if (obj == null) return;
        if (obj instanceof Map) {
            setObject(((Map) obj).get(name));
            return;
        }
        try {
            HashMap<String, Method> readableProps = getBeanData(obj.getClass()).readableProps;
            Method getMethod = readableProps.get(name);
            if (getMethod != null) {
                try {
                    setObject(getMethod.invoke(obj));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public Processer getParent() {
        return parent;
    }

    public HSSFCell getLastStartCell() {
        return lastStartCell;
    }

    public HSSFCell getLastEndCell() {
        return lastEndCell;
    }

    public int getTopMargin() {
        return topMargin;
    }

    public int getLeftMargin() {
        return leftMargin;
    }

    public HSSFCell getFirstStartCell() {
        return firstStartCell;
    }
/*---------public------------------*/

    private int index = 0;

    public int getIndex() {
        return index;
    }

    public int getNo() {
        return index + 1;
    }

    public int getCount() {
        if (lastObj == null) return 0;
        if (lastObj instanceof ArrayList) {
            return ((ArrayList) lastObj).size();
        }
        return 1;
    }

    public int getCountRows() {
        return isRepeatTitle() ? this.getListCount() : this.getDataRows();
    }

    public int getStart() {
        return this.getFirstStartRow() + 1;
    }

    public int getCurrRow() {
        return this.getStart() + getCountRows() * index;
    }

    public int getEnd() {
        return getStart() + getCountRows() * getCount() - 1;
    }

    public int getLastStartRow() {
        return lastStartCell == null ? 0 : lastStartCell.getRowIndex();
    }

    public int getLastStartCol() {
        return lastStartCell == null ? 0 : lastStartCell.getColumnIndex();
    }

    public int getFirstStartRow() {
        return firstStartCell == null ? 0 : firstStartCell.getRowIndex();
    }


    public int getLastMaxCount() {
        return lastMaxCount;
    }

    public int getLastEndRow() {
        return lastEndCell == null ? 0 : lastEndCell.getRowIndex();
    }

    public int getStartRow() {
        if (column == null) return 0;
        return column.getStartCell().getRowIndex();
    }

    public int getEndRow() {
        if (column == null) return 0;
        return column.getEndCell().getRowIndex();
    }

    public int getTitleRows() {
        if (column == null) return 0;
        return column.getTitleRows();
    }

    public int getListCols() {
        if (column == null) return 1;
        return column.getListCols();
    }

    public int getCols() {
        if (column == null) return 1;
        return column.getCols() <= 0 ? 1 : column.getCols();
    }

    public int getListTop() {
        if (column == null) return 1;
        return column.getListTop() <= 0 ? 0 : column.getListTop();
    }

    public int getDataRows() {
        if (column == null) return 1;
        return column.getDataRows();
    }

    public boolean isRepeatTitle() {
        return column != null && (column.isRepeatTitle() || column.getTitleRows() == 0);
    }

    public int getListCount() {
        if (column == null) return 0;
        return column.getCount();
    }

    /*---------------cac---------------*/

    private static double cacComplex(String str) {

        if (str.equals(""))
            return 0;
        //System.out.println("CAC:" + str);
        str = str.replaceAll("[\\[\\{]", "(").replaceAll("[\\]\\}]", ")");
        int cl = str.lastIndexOf('(');

        if (cl == -1)
            return cac(str);
        int cr = str.indexOf(')', cl);
        String left = str.substring(0, cl);
        String right = str.substring(cr + 1);
        String middle = str.substring(cl + 1, cr);

        return cacComplex(left + cac(middle) + right);
    }

    public static double cac(String str) {
        if (str.equals(""))
            return 0;

        int ml = str.indexOf('*');
        int dl = str.indexOf('/');

        if (ml == -1 && dl == -1) {
            return cacNoMD(str);
        }
        int index = ml == -1 ? dl : ml;

        // 4+5*6*7+8
        String left = str.substring(0, index);// 4+5
        String m1 = lastNumber(left);
        left = left.substring(0, left.length() - m1.length());
        String right = str.substring(index + 1);// 6*7+8
        String m2 = firstNumber(right);// m2:6
        right = right.substring(m2.length());// *7+8
        double d1 = Double.parseDouble(m1);
        double d2 = Double.parseDouble(m2);
        double tmp = 0;
        if (index == ml) {
            tmp = d1 * d2;
        } else {
            tmp = d1 / d2;
        }
        return cac(left + tmp + right);

    }

    private static String lastNumber(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = str.length() - 1; i >= 0; i--) {
            char c = str.charAt(i);
            if (!Character.isDigit(c) && c != '.')
                break;
            sb.append(c);
        }
        return sb.reverse().toString();
    }

    private static String firstNumber(String str) {
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c) && c != '.')
                break;
            sb.append(c);
        }
        return sb.toString();
    }

    private static double cacNoMD(String str) {
        double ret = 0;
        StringBuilder sb = new StringBuilder();
        char sign = '+';
        for (char c : (str + "+").toCharArray()) {
            if (!Character.isDigit(c) && c != '.') {
                if (sb.length() == 0)
                    continue;
                double tmp = Double.parseDouble(sb.toString());
                if (sign == '+') {
                    ret += tmp;
                } else {
                    ret -= tmp;
                }
                sb = new StringBuilder();
                sign = c;
            } else {
                sb.append(c);
            }
        }

        return ret;
    }

}
