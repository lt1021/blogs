package com.blog.base.util;


import com.blog.base.bean.IdImageNameBean;
import com.blog.base.bean.ImageBean;
import com.blog.base.mapper.ImageMapper;
import com.blog.json.JSONException;
import com.blog.json.JSONObject;
import com.blog.microsoul.weapi.hssf.ArrayString;
import com.blog.microsoul.weapi.poi.POIHelper;
import com.blog.microsoul.weapi.poi.POIParser;
import com.blog.microsoul.weapi.poi.hssf.HSSFPOIHelper;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author lt
 * @date 2021/3/5 10:24
 */
public class GlobalUtil {
    private static String directory;
    private static String imageDirectory;
    private static String appstore;
    private static String appstoreInsatll;
    private static boolean isDebug = false;
    private static CellStyle cellStyle;
    private static int currColIndex;
    private static int uuid = 10000;
    private static final char[] digits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    public static final char UNDERLINE = '_';

    public GlobalUtil() {
    }

    public static Long toLong(Object v) {
        if (v == null) {
            return 0L;
        } else if (v instanceof Integer) {
            return ((Integer)v).longValue();
        } else {
            return v instanceof Number ? (Long)v : parseLong((String)v, 0L);
        }
    }

    public static long getId(long id1, long id2) {
        return id1 > 0L ? id1 : id2;
    }

    public static Double toDouble(Object s) {
        double v = 0.0D;
        if (s != null) {
            try {
                v = Double.parseDouble(toString(s));
            } catch (Exception var4) {
            }
        }

        return v;
    }

    public static String toString(Object s) {
        return s == null ? "" : s.toString().trim();
    }

    public static long parseLong(String v, long defaultValue) {
        if (v != null && !v.trim().equals("")) {
            try {
                return Long.parseLong(v);
            } catch (Exception var4) {
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public static Integer toInteger(Object v) {
        if (v == null) {
            return 0;
        } else {
            return v instanceof Number ? (Integer)v : parseInteger((String)v, 0);
        }
    }

    public static int parseInteger(String v, int defaultValue) {
        if (v != null && !v.trim().equals("")) {
            try {
                return Integer.parseInt(v);
            } catch (Exception var3) {
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public static int parseInt(Object v, int defaultValue) {
        if (v == null) {
            return defaultValue;
        } else {
            return v instanceof Number ? (Integer)v : parseInteger((String)v, defaultValue);
        }
    }

    public static double parseDouble(String v, double defaultValue) {
        if (v != null && !v.trim().equals("")) {
            try {
                return Double.parseDouble(v);
            } catch (Exception var4) {
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public static int intValue(Double v) {
        return v.intValue();
    }

    public static boolean isEmpty(String r) {
        return r == null || "".equals(r.trim());
    }

    public static int zeroInt(int v, int defaultValue) {
        return v == 0 ? defaultValue : v;
    }

    public static double zeroDouble(double v, double defaultValue) {
        return v == 0.0D ? defaultValue : v;
    }

    public static String nullString(String str, String defaultValue) {
        return str != null && !"".equals(str.trim()) ? str : defaultValue;
    }

    public static boolean isDebug() {
        return isDebug;
    }

    public static void setDebug(boolean isDebug) {
        isDebug = isDebug;
    }

    public static String toJSString(String v) {
        return v == null ? "" : v.replaceAll("\"", "\\\\\"");
    }

    public static String quote(String v) {
        return v == null ? "" : "\"" + v.replaceAll("\"", "\\\\\"") + "\"";
    }

    public static long getUnixTime() {
        return System.currentTimeMillis() / 1000L;
    }

    public static long getUnixDate() {
        Calendar c = Calendar.getInstance();
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        c.set(14, 0);
        return c.getTimeInMillis() / 1000L;
    }

    public static String getUnixDateTime(long unixDate, String format) {
        if (unixDate == 0L) {
            return "";
        } else {
            SimpleDateFormat dateFormater = new SimpleDateFormat(format);
            return dateFormater.format(new Date(unixDate * 1000L));
        }
    }

    public static String toJson(Object o) {
        try {
            return JSONObject.toString(o);
        } catch (JSONException var2) {
            return "";
        }
    }

    public static void setCellStyle(CellStyle cs) {
        cellStyle = cs;
    }

    public static CellStyle initSheet(Workbook wb, Sheet sheet, String title, String[] headers, int[] widths) {
        return initSheet(wb, sheet, title, headers, widths, 2, false);
    }

    public static CellStyle initSheet(Workbook wb, Sheet sheet, String title, String[] headers, int[] widths, boolean isBorder) {
        return initSheet(wb, sheet, title, headers, widths, 2, isBorder);
    }

    public static CellStyle initSheet(Workbook wb, Sheet sheet, String title, String[] headers, int[] widths, int freezePane, boolean isBorder) {
        CellStyle ts = wb.createCellStyle();
        ts.setVerticalAlignment((short)1);
        ts.setAlignment((short)2);
        cellStyle = wb.createCellStyle();
        cellStyle.setVerticalAlignment((short)1);
        Row row = sheet.createRow(0);
        row.setHeight((short)600);
        setValue(row, 0, title, ts);
        createMerged(sheet, 0, 0, 0, headers.length - 1);
        sheet.createFreezePane(0, freezePane, 0, freezePane);
        row = sheet.createRow(1);
        row.setHeight((short)400);
        int widthsSize = widths.length;

        for(int i = 0; i < headers.length; ++i) {
            setValue(row, i, headers[i], ts);
            sheet.setColumnWidth(i, i < widthsSize ? widths[i] * 256 : 2560);
        }

        if (isBorder) {
            cellStyle.setBorderBottom((short)1);
            cellStyle.setBorderTop((short)1);
            cellStyle.setBorderLeft((short)1);
            cellStyle.setBorderRight((short)1);
        }

        return cellStyle;
    }

    public static String numToCol(int colIndex) {
        return CellReference.convertNumToColString(colIndex);
    }

    public static Cell setValue(Row row, Object object) {
        return setValue(row, -1, object, (CellStyle)null);
    }

    public static Cell setValue(Row row, int colIndex, Object object) {
        return setValue(row, colIndex, object, (CellStyle)null);
    }

    public static Cell setValue(Row row, int colIndex, Object object, CellStyle ts) {
        if (colIndex >= 0) {
            currColIndex = colIndex;
        } else {
            colIndex = ++currColIndex;
        }

        Cell c = row.createCell(colIndex);
        if (ts != null) {
            c.setCellStyle(ts);
        } else {
            c.setCellStyle(cellStyle);
        }

        if (object instanceof Double) {
            c.setCellValue((Double)object);
        } else if (object instanceof Integer) {
            c.setCellValue((double)(Integer)object);
        } else {
            c.setCellValue((String)object);
        }

        return c;
    }

    public static Cell setFormula(Row row, int colIndex, String formula) {
        return setFormula(row, colIndex, formula, (CellStyle)null);
    }

    public static Cell setFormula(Row row, int colIndex, String formula, CellStyle ts) {
        Cell c = row.createCell(colIndex);
        if (ts != null) {
            c.setCellStyle(ts);
        } else {
            c.setCellStyle(cellStyle);
        }

        formula = formula.replaceAll("%", CellReference.convertNumToColString(colIndex));
        c.setCellFormula(formula);
        return c;
    }

    public static void createMerged(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
        CellRangeAddress r = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
        sheet.addMergedRegion(r);
    }

    public static HSSFPOIHelper getPOIHelper(Workbook workbook, HSSFSheet sheet) {
        return new HSSFPOIHelper(workbook, sheet, sheet.createDrawingPatriarch());
    }

    public static Cell setPicture(HSSFPOIHelper poiHelper, Row row, String picture) {
        return setPicture(poiHelper, row, -1, picture);
    }

    public static Cell setPicture(HSSFPOIHelper poiHelper, Row row, int colIndex, String picture) {
        return setPicture(poiHelper, row, colIndex, picture, 0);
    }

    public static Cell setPicture(HSSFPOIHelper poiHelper, Row row, String picture, int insidePictureCount) {
        return setPicture(poiHelper, row, -1, picture, insidePictureCount);
    }

    public static Cell setPicture(HSSFPOIHelper poiHelper, Row row, int dxRow, int dxCol, String picture) {
        return setPicture(poiHelper, row, dxRow, dxCol, picture, 0);
    }

    public static Cell setPicture(HSSFPOIHelper poiHelper, Row row, int dxRow, int dxCol, String picture, int insidePictureCount) {
        int colIndex = ++currColIndex;
        Cell c = row.createCell(colIndex);
        File f = new File(getImageDirectory(), picture);
        if (!f.exists()) {
            return c;
        } else {
            try {
                ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
                BufferedImage bufferImg = ImageIO.read(f);
                ImageIO.write(bufferImg, "jpg", byteArrayOut);
                ClientAnchor anchor = new HSSFClientAnchor();
                anchor.setRow1(row.getRowNum());
                anchor.setRow2(row.getRowNum() + dxRow);
                anchor.setCol1(colIndex);
                anchor.setCol2(colIndex + dxCol);
                anchor.setDx1(5);
                anchor.setDy1(5);
                anchor.setDx2(5);
                anchor.setDy2(5);
                poiHelper.createPicture(anchor, bufferImg.getWidth(), bufferImg.getHeight(), byteArrayOut.toByteArray(), insidePictureCount);
            } catch (IOException var12) {
                var12.printStackTrace();
            }

            return c;
        }
    }

    public static Cell setPicture(HSSFPOIHelper poiHelper, Row row, int colIndex, String picture, int insidePictureCount) {
        if (colIndex >= 0) {
            currColIndex = colIndex;
        } else {
            colIndex = ++currColIndex;
        }

        Cell c = row.createCell(colIndex);
        File f = new File(getImageDirectory(), picture);
        if (!f.exists()) {
            return c;
        } else {
            try {
                ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
                BufferedImage bufferImg = ImageIO.read(f);
                ImageIO.write(bufferImg, "jpg", byteArrayOut);
                ClientAnchor anchor = new HSSFClientAnchor();
                anchor.setRow1(row.getRowNum());
                anchor.setRow2(row.getRowNum());
                anchor.setCol1(colIndex);
                anchor.setCol2(colIndex);
                anchor.setDx1(5);
                anchor.setDy1(5);
                anchor.setDx2(5);
                anchor.setDy2(5);
                poiHelper.createPicture(anchor, bufferImg.getWidth(), bufferImg.getHeight(), byteArrayOut.toByteArray(), insidePictureCount);
            } catch (IOException var10) {
                var10.printStackTrace();
            }

            return c;
        }
    }

    public static String getCurrDateTime() {
        return (new SimpleDateFormat("yyyy-MM-dd HH:mm")).format(new Date());
    }

    public static String leftPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        } else {
            int pads = size - str.length();
            return pads <= 0 ? str : (pads > 8192 ? leftPad(str, size, String.valueOf(padChar)) : padding(pads, padChar).concat(str));
        }
    }

    public static String leftPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        } else {
            if (isEmpty(padStr)) {
                padStr = " ";
            }

            int padLen = padStr.length();
            int strLen = str.length();
            int pads = size - strLen;
            if (pads <= 0) {
                return str;
            } else if (padLen == 1 && pads <= 8192) {
                return leftPad(str, size, padStr.charAt(0));
            } else if (pads == padLen) {
                return padStr.concat(str);
            } else if (pads < padLen) {
                return padStr.substring(0, pads).concat(str);
            } else {
                char[] padding = new char[pads];
                char[] padChars = padStr.toCharArray();

                for(int i = 0; i < pads; ++i) {
                    padding[i] = padChars[i % padLen];
                }

                return (new String(padding)).concat(str);
            }
        }
    }

    private static String padding(int repeat, char padChar) throws IndexOutOfBoundsException {
        if (repeat < 0) {
            throw new IndexOutOfBoundsException("Cannot pad a negative amount: " + repeat);
        } else {
            char[] buf = new char[repeat];

            for(int i = 0; i < buf.length; ++i) {
                buf[i] = padChar;
            }

            return new String(buf);
        }
    }

    public static void main(String[] args) {
        System.out.println(new Date(getUnixDate() * 1000L));
    }

    public static void export2(HttpServletRequest request, HttpServletResponse response, Object o) throws IOException {
        export2(request, response, new Object[]{o}, (String)null, (String)null);
    }

    public static void export2(HttpServletRequest request, HttpServletResponse response, Object[] o) throws IOException {
        export2(request, response, o, (String)null, (String)null);
    }

    public static void export2(HttpServletRequest request, HttpServletResponse response, Object[] o, String outFileName) throws IOException {
        export2(request, response, o, outFileName, (String)null);
    }

    public static void export2(HttpServletRequest request, HttpServletResponse response, Object[] o, String outFileName, String dtpl) throws IOException {
        String tpl = dtpl == null ? request.getParameter("tpl") : dtpl;
        String fileName = outFileName == null ? request.getParameter("fileName") : outFileName;
        String lang = request.getParameter("lang") == null ? "" : "_" + request.getParameter("lang");
        if (tpl != null) {
            File f = new File(getDirectory() + "/templates", tpl + lang + ".xls");

            try {
                POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(f));
                HSSFWorkbook wb = new HSSFWorkbook(fs);
                int insidePictureCount = wb.getAllPictures().size();
                File pf = f.getParentFile();

                for(int i = o.length - 1; i >= 0; --i) {
                    POIParser parser = new POIParser();
                    if (o[i] == null) {
                        wb.removeSheetAt(i);
                    } else {
                        parser.parse(wb, o[i], i);
                        parser.process(o[i], pf, insidePictureCount);
                    }
                }

                FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

                for(int sheetNum = 0; sheetNum < wb.getNumberOfSheets(); ++sheetNum) {
                    Sheet sheet = wb.getSheetAt(sheetNum);
                    Iterator var17 = sheet.iterator();

                    while(var17.hasNext()) {
                        Row r = (Row)var17.next();
                        Iterator var19 = r.iterator();

                        while(var19.hasNext()) {
                            Cell c = (Cell)var19.next();
                            if (c.getCellType() == 2) {
                                c.setCellFormula(c.getCellFormula());
                                evaluator.evaluateFormulaCell(c);
                            }
                        }
                    }
                }

                StringBuilder sb = new StringBuilder(50);
                sb.append("attachment;  filename=").append(fileName != null && !fileName.trim().equals("") ? fileName : tpl).append(".xls");
                response.setContentType("application/x-msdownload;charset=UTF-8");
                response.setHeader("Content-Disposition", new String(sb.toString().getBytes(), "ISO8859-1"));
                ServletOutputStream out = response.getOutputStream();
                wb.write(out);
                out.flush();
                out.close();
            } catch (Exception var24) {
                var24.printStackTrace();
            } finally {
                Runtime.getRuntime().gc();
            }

        }
    }

    private static void createComment(boolean showComment, Drawing drawing, Cell cell, Row row, String dataIndex) {
        if (showComment) {
            Comment comment1 = drawing.createCellComment(new HSSFClientAnchor(0, 0, 0, 0, (short)cell.getColumnIndex(), row.getRowNum(), (short)(cell.getColumnIndex() + 1), row.getRowNum() + 1));
            comment1.setString(new HSSFRichTextString(dataIndex));
            cell.setCellComment(comment1);
        }
    }

    public static void zip(HttpServletRequest request, HttpServletResponse response, List<Map> objs, String outFileName) throws IOException {
        String fileName = outFileName == null ? request.getParameter("fileName") : outFileName;

        try {
            StringBuilder sb = new StringBuilder(50);
            sb.append("attachment;  filename=").append(fileName).append(".zip");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", new String(sb.toString().getBytes(), "ISO8859-1"));
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
            Iterator var7 = objs.iterator();

            while(var7.hasNext()) {
                Map f = (Map)var7.next();
                zos.putNextEntry(new ZipEntry((String)f.get("n")));
                FileInputStream fis = new FileInputStream((String)f.get("p"));
                byte[] buffer = new byte[1024];

                int r;
                while((r = fis.read(buffer)) != -1) {
                    zos.write(buffer, 0, r);
                }

                fis.close();
            }

            zos.flush();
            zos.close();
        } catch (Exception var15) {
            var15.printStackTrace();
        } finally {
            Runtime.getRuntime().gc();
        }

    }

    private static void parseSettle(String settle, Row row, Cell cell, int cpos, Object v) {
        switch(settle.charAt(0)) {
            case '~':
                int i = (Integer)v;
                settle = settle.substring(1);
                String[] ss = settle.split("\\^");
                String r;
                if (ss.length < i) {
                    r = "";
                } else {
                    r = ss[i];
                }

                cell.setCellValue(r);
                return;
            default:
        }
    }

    private static void createPicture(POIHelper poiHelper, CreationHelper creationHelper, int col, int row, String[] fun, byte[] bytes, int width, int height, boolean withS, int offset, int insidePictureCount) {
        if (bytes != null) {
            if (withS) {
                int type = fun.length >= 4 + offset ? parseInt(fun[3 + offset], 0) : 0;
                int w = fun.length >= 5 + offset ? parseInt(fun[4 + offset], width) : width;
                int h = fun.length >= 6 + offset ? parseInt(fun[5 + offset], height) : height;
                switch(type) {
                    case 1:
                        h = height * w / width;
                        break;
                    case 2:
                        h = w;
                        w = width * w / height;
                        break;
                    case 3:
                        h = fun.length >= 6 + offset ? h : w;
                        if (width <= w && height <= h) {
                            w = width;
                            h = height;
                        } else if (w / width > h / height) {
                            w = width * h / height;
                        } else {
                            h = height * w / width;
                        }
                }

                poiHelper.createPicture(col, row, fun.length >= 2 + offset ? parseInt(fun[1 + offset], 0) : 0, fun.length >= 3 + offset ? parseInt(fun[2 + offset], 0) : 0, w, h, bytes, insidePictureCount);
            } else {
                ClientAnchor anchor = creationHelper.createClientAnchor();
                anchor.setRow1(row);
                anchor.setRow2(row + (fun.length >= 3 ? parseInt(fun[2], 0) : 0));
                anchor.setCol1(col);
                anchor.setCol2(col + (fun.length >= 2 ? parseInt(fun[1], 0) : 0));
                anchor.setDx1(fun.length >= 4 ? parseInt(fun[3], 0) : 0);
                anchor.setDy1(fun.length >= 5 ? parseInt(fun[4], 0) : 0);
                anchor.setDx2(fun.length >= 6 ? parseInt(fun[5], 0) : 0);
                anchor.setDy2(fun.length >= 7 ? parseInt(fun[6], 0) : 0);
                poiHelper.createPicture(anchor, width, height, bytes, insidePictureCount);
            }

        }
    }

    public static ArrayList<String> split(String regex, String input) {
        Pattern pt = Pattern.compile(regex);
        Matcher mc = pt.matcher(input);
        int index = 0;

        ArrayList matchList;
        for(matchList = new ArrayList(); mc.find(); index = mc.end()) {
            String match = input.subSequence(index, mc.start()).toString();
            matchList.add(match);
            matchList.add(mc.group(1));
        }

        if (index <= input.length() - 1) {
            matchList.add(input.subSequence(index, input.length()).toString());
        }

        return matchList;
    }

//    private static void parseFormula(String formula, Row row, Cell cell, int cpos, Object v) {
//        ArrayList<String> vs = split("[\\$\\#]\\{([^\\}]*)\\}", formula);
//        if (vs.size() > 0) {
//            String f = (String)vs.get(0);
//            if (f.startsWith("$") && f.length() >= 2) {
//                switch(f.charAt(1)) {
//                    case '$':
//                    case '&':
//                        String b = f.substring(2);
//                        int idx = b.indexOf("+");
//                        if (idx > 0) {
//                            b = b.substring(0, idx).trim() + "." + f.charAt(1) + "+" + b.substring(idx + 1);
//                        } else {
//                            b = b.trim() + "." + f.charAt(1);
//                        }
//
//                        vs.add("#" + b);
//                        vs.add(")");
//                        f = "SUM(";
//                        break;
//                    case ':':
//                        vs.add(f.substring(1));
//                        f = "";
//                        break;
//                    case '=':
//                        f = f.substring(2);
//                }
//            }
//
//            StringBuilder b = new StringBuilder();
//            ArrayList<String> strs = new ArrayList();
//            ArrayList<ArrayString> vars = new ArrayList();
//            strs.add(f);
//            int i = 2;
//
//            int i;
//            for(i = vs.size(); i < i; i += 2) {
//                f = (String)vs.get(i);
//                strs.add(f);
//            }
//
//            i = 1;
//
//            for(i = vs.size(); i < i; i += 2) {
//                ArrayString s = new ArrayString(((String)vs.get(i)).split("[\\(\\),\\]]"));
//                vars.add(s);
//            }
//
//            i = 0;
//
//            while(i < strs.size()) {
//                b.append((String)strs.get(i));
//                String[] dvars = ((ArrayString)vars.get(i)).getStrs();
//                String ff = dvars[0];
//                switch(ff.charAt(0)) {
//                    case '@':
//                        ff = ff.substring(1);
//                        String[] rc = ff.split("@");
//                        if (rc.length == 2) {
//                            if (rc[0].equals("!")) {
//                                b.append(CellReference.convertNumToColString(cell.getColumnIndex()));
//                            } else {
//                                b.append(CellReference.convertNumToColString(Integer.valueOf(rc[0]) - 1 + cpos));
//                            }
//
//                            if (rc[1].equals("!")) {
//                                b.append(row.getRowNum() + 1);
//                            } else {
//                                b.append(rc[1]);
//                            }
//                        }
//                    default:
//                        ++i;
//                }
//            }
//
//            cell.setCellFormula(b.toString());
//        }
//
//    }
private static void parseFormula(String formula, Row row, Cell cell, int cpos, Object v) {
    //CellReference.convertNumToColString(i);
    ArrayList<String> vs = GlobalUtil.split("[\\$\\#]\\{([^\\}]*)\\}", formula);

    if (vs.size() > 0) {
        String f = vs.get(0);
        if (f.startsWith("$") && f.length() >= 2) {
            switch (f.charAt(1)) {
                case '=':
                    f = f.substring(2);
                    break;
                case ':':
                    vs.add(f.substring(1));
                    f = "";
                    break;
                case '$':
                case '&':
                    String b;
                    b = f.substring(2);
                    int idx = b.indexOf("+");
                    if (idx > 0) {
                        b = b.substring(0, idx).trim() + "." + f.charAt(1) + "+" + b.substring(idx + 1);
                    } else {
                        b = b.trim() + "." + f.charAt(1);
                    }

                    vs.add("#" + b); //#list +-1  //#list.$+-1
                    vs.add(")");
                    f = "SUM(";
                    break;
            }
        }
        StringBuilder b = new StringBuilder();
        String ff;
        String rr;
        ArrayList<String> strs = new ArrayList<String>();
        ArrayList<ArrayString> vars = new ArrayList<ArrayString>();
        strs.add(f);

        for (int i = 2, l = vs.size(); i < l; i += 2) {
            f = vs.get(i);
            strs.add(f);
        }
        for (int i = 1, l = vs.size(); i < l; i += 2) {
            ArrayString s = new ArrayString(vs.get(i).split("[\\(\\),\\]]"));
            vars.add(s);
        }
        String[] dvars;
        for (int i = 0; i < strs.size(); i++) {
            b.append(strs.get(i));
            dvars = vars.get(i).getStrs();
            ff = dvars[0];

            switch (ff.charAt(0)) {
                case '@': // get POS @row,col
                    ff = ff.substring(1);
                    String[] rc = ff.split("@");
                    if (rc.length == 2) {
                        if (rc[0].equals("!")) b.append(CellReference.convertNumToColString(cell.getColumnIndex()));
                        else b.append(CellReference.convertNumToColString(Integer.valueOf(rc[0]) - 1 + cpos));

                        if (rc[1].equals("!")) b.append(row.getRowNum() + 1);
                        else b.append(rc[1]);

                    }
            }
        }
        cell.setCellFormula(b.toString());
    }


}

    public static double round(double v) {
        if (Double.isInfinite(v)) {
            return 0.0D;
        } else {
            int scale = 2;
            BigDecimal b = new BigDecimal(Double.toString(v));
            BigDecimal one = new BigDecimal("1");
            return b.divide(one, scale, 4).doubleValue();
        }
    }

    public static double round(double v, int scale) {
        try {
            if (Double.isInfinite(v)) {
                return 0.0D;
            } else {
                BigDecimal b = new BigDecimal(Double.toString(v));
                BigDecimal one = new BigDecimal("1");
                return b.divide(one, scale, 4).doubleValue();
            }
        } catch (Exception var5) {
            System.err.println("err:" + v);
            throw var5;
        }
    }

    public static String getDirectory() {
        return directory;
    }

    public static void setDirectory(String directory) {
        directory = directory;
    }

    public static String getImageDirectory() {
        return imageDirectory + (imageDirectory.endsWith("/") ? "" : "/");
    }

    public static void setImageDirectory(String imageDirectory) {
        imageDirectory = imageDirectory;
    }

    public static String getAppstore() {
        return appstore;
    }

    public static void setAppstore(String appstore) {
        appstore = appstore;
    }

    public static String getAppstoreInsatll() {
        return appstoreInsatll;
    }

    public static void setAppstoreInsatll(String appstoreInsatll) {
        appstoreInsatll = appstoreInsatll;
    }

    public static String createPictureThumb(MultipartFile fileItem, String p, ImageMapper mapper) {
        String path = getImageDirectory();
        String nExt = "";
        File dir = new File(path + p);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (!fileItem.isEmpty()) {
            String name = fileItem.getOriginalFilename();
            int idx = name.indexOf(".");
            String nkey = name;
            if (idx >= 0) {
                nkey = name.substring(0, idx);
                nExt = name.substring(idx + 1);
            }

            File file = new File(path + p + "/" + nkey + ".jpg");

            try {
                if ("png".equalsIgnoreCase(nExt)) {
                    Thumbnails.of(new InputStream[]{fileItem.getInputStream()}).addFilter((img) -> {
                        int w = img.getWidth();
                        int h = img.getHeight();
                        BufferedImage newImage = new BufferedImage(w, h, 1);
                        Graphics2D graphic = newImage.createGraphics();
                        graphic.setColor(Color.white);
                        graphic.fillRect(0, 0, w, h);
                        graphic.drawRenderedImage(img, (AffineTransform)null);
                        graphic.dispose();
                        return newImage;
                    }).scale(1.0D).outputFormat("jpg").outputQuality(1.0D).toFile(file);
                } else {
                    Thumbnails.of(new InputStream[]{fileItem.getInputStream()}).scale(1.0D).outputFormat("jpg").outputQuality(1.0D).toFile(file);
                }

                Thumbnails.of(new File[]{file}).size(150, 150).outputFormat("jpg").outputQuality(1.0D).toFile(new File(path + p + "/", nkey + "_150.jpg"));
            } catch (Exception var11) {
                var11.printStackTrace();
            }

            ImageBean bean = new ImageBean();
            bean.setCode(nkey);
            bean.setPicture(p + "/" + nkey + ".jpg");
            bean.setThumb(p + "/" + nkey + "_150.jpg");
            mapper.updateImageByCode(bean);
            return nkey;
        } else {
            return "";
        }
    }

    public static String createPicture(CommonsMultipartFile[] files, Long id, String p, String name, ImageMapper mapper) {
        String path = getImageDirectory();
        File dir = new File(path + p);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        CommonsMultipartFile[] var7 = files;
        int var8 = files.length;

        for(int var9 = 0; var9 < var8; ++var9) {
            CommonsMultipartFile fileItem = var7[var9];
            if (!fileItem.isEmpty()) {
                File file = new File(path + p + "/" + name + ".jpg");

                try {
                    Thumbnails.of(new InputStream[]{fileItem.getFileItem().getInputStream()}).scale(1.0D).outputFormat("jpg").outputQuality(1.0D).toFile(file);
                } catch (Exception var13) {
                    var13.printStackTrace();
                }

                IdImageNameBean bean = new IdImageNameBean();
                bean.setName(name);
                bean.setId(id);
                bean.setPicture(p + "/" + name + ".jpg");
                mapper.updateImageById(bean);
                return bean.getPicture();
            }
        }

        return "";
    }

    public static String createPicture(MultipartFile fileItem, String p, String name) {
        String path = getImageDirectory();
        File dir = new File(path + p);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (!fileItem.isEmpty()) {
            File file = new File(path + p + "/" + name + ".jpg");

            try {
                Thumbnails.of(new InputStream[]{fileItem.getInputStream()}).scale(1.0D).outputFormat("jpg").outputQuality(1.0D).toFile(file);
            } catch (Exception var7) {
                var7.printStackTrace();
            }

            return p + "/" + name + ".jpg";
        } else {
            return "";
        }
    }

    public static String createPicture(MultipartFile fileItem, String path) {
        String directory = getImageDirectory();
        String p = path.substring(0, path.lastIndexOf("/"));
        File dir = new File(directory + p);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (!fileItem.isEmpty()) {
            File file = new File(directory + path);

            try {
                Thumbnails.of(new InputStream[]{fileItem.getInputStream()}).scale(1.0D).outputFormat("jpg").outputQuality(1.0D).toFile(file);
            } catch (Exception var7) {
                var7.printStackTrace();
            }

            return path;
        } else {
            return "";
        }
    }

    public static String toUnsignedString(int i, int shift) {
        char[] buf = new char[32];
        int charPos = 32;
        int radix = 1 << shift;
        int mask = radix - 1;

        do {
            --charPos;
            buf[charPos] = digits[i & mask];
            i >>>= shift;
        } while(i != 0);

        return new String(buf, charPos, 32 - charPos);
    }

    public static synchronized int getUID() {
        ++uuid;
        if (uuid > 99999) {
            uuid = 10000;
        }

        return uuid;
    }

    public static synchronized String getUUID() {
        SimpleDateFormat time = new SimpleDateFormat("yyyyMMddHHmmss");
        return time.format(new Date()) + getUID();
    }

    public static synchronized String getUUID32() {
        SimpleDateFormat time = new SimpleDateFormat("yyMMddHHmmss");
        String l = time.format(new Date()) + getUID();
        return toUnsignedString(Integer.parseInt(l.substring(0, 9)), 5) + toUnsignedString(Integer.parseInt((new Random()).nextInt(9) + 1 + l.substring(9, l.length())), 5);
    }

    public static double divide(Object a, Object b) {
        double ret = 0.0D;
        if (!toString(a).equals("") && !toString(b).equals("")) {
            BigDecimal e = new BigDecimal(toString(a));
            BigDecimal f = new BigDecimal(toString(b));
            if (toDouble(f) > 0.0D) {
                ret = e.divide(f, 4, 1).doubleValue();
            }
        }

        return ret;
    }

    public static String read(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        StringBuilder buffer = new StringBuilder();

        for(String line = reader.readLine(); line != null; line = reader.readLine()) {
            buffer.append(line).append("\n");
        }

        return buffer.toString();
    }

    public static void copy(File source, File target) {
        FileChannel in = null;
        FileChannel out = null;
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        if (target.lastModified() > source.lastModified()) {
            try {
                inStream = new FileInputStream(source);
                outStream = new FileOutputStream(target);
                in = inStream.getChannel();
                out = outStream.getChannel();
                in.transferTo(0L, in.size(), out);
            } catch (IOException var15) {
                var15.printStackTrace();
            } finally {
                try {
                    if (inStream != null) {
                        inStream.close();
                    }

                    if (in != null) {
                        in.close();
                    }

                    if (outStream != null) {
                        outStream.close();
                    }

                    if (out != null) {
                        out.close();
                    }
                } catch (IOException var14) {
                    var14.printStackTrace();
                }

            }

        }
    }

    public static void unzip(InputStream in, File target) {
        try {
            OutputStream out = new BufferedOutputStream(new FileOutputStream(target));
            byte[] buffer = new byte[2048];

            int nBytes;
            while((nBytes = in.read(buffer)) > 0) {
                out.write(buffer, 0, nBytes);
            }

            out.flush();
            out.close();
            in.close();
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    public static String camelToUnderline(String param) {
        if (param != null && !"".equals(param.trim())) {
            int len = param.length();
            StringBuilder sb = new StringBuilder(len);

            for(int i = 0; i < len; ++i) {
                char c = param.charAt(i);
                if (Character.isUpperCase(c)) {
                    sb.append('_');
                    sb.append(Character.toLowerCase(c));
                } else {
                    sb.append(c);
                }
            }

            return sb.toString();
        } else {
            return "";
        }
    }

    public static String underlineToCamel(String param) {
        if (param != null && !"".equals(param.trim())) {
            int len = param.length();
            StringBuilder sb = new StringBuilder(len);

            for(int i = 0; i < len; ++i) {
                char c = param.charAt(i);
                if (c == '_') {
                    ++i;
                    if (i < len) {
                        sb.append(Character.toUpperCase(param.charAt(i)));
                    }
                } else {
                    sb.append(c);
                }
            }

            return sb.toString();
        } else {
            return "";
        }
    }

    public static String getLang(String lang) {
        return lang != null && !lang.equals("") ? lang : ThreadMapUtil.getLanguage();
    }

    public static String changeLang(String lang) {
        String clang = ThreadMapUtil.getLanguage();
        ThreadMapUtil.setLanguage(lang);
        return clang;
    }

    public static void restoreLang(String lang) {
        ThreadMapUtil.setLanguage(lang);
    }

    public static List subPage(List data, int offset, int total, int limit) {
        if (offset >= total) {
            offset = total - total % limit;
        }

        return data.subList(offset, Math.min(offset + limit, total));
    }

    public static String md5(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        Base64.Encoder base64Encoder = Base64.getEncoder();
        return base64Encoder.encodeToString(md5.digest(text.getBytes("utf-8")));
    }

    public static boolean checkMD5(String rawString, String oldString) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return md5(rawString).equals(oldString);
    }
}
