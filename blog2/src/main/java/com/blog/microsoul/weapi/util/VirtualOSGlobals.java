package com.blog.microsoul.weapi.util;


import com.blog.json.JSONObject;
import com.blog.json.JsonList;
import com.blog.json.JsonMap;
import com.blog.microsoul.weapi.hssf.ArrayString;
import com.blog.microsoul.weapi.hssf.HSSFParser;
import com.blog.microsoul.weapi.poi.POIHelper;
import com.blog.microsoul.weapi.poi.POIParser;
import com.blog.microsoul.weapi.poi.hssf.HSSFPOIHelper;
import com.blog.weapi.MDC;
import com.swetake.util.Qrcode;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jbarcode.JBarcode;
import org.jbarcode.encode.BarcodeEncoder;
import org.jbarcode.encode.Code128Encoder;
import org.jbarcode.encode.EAN13Encoder;
import org.jbarcode.paint.EAN13TextPainter;
import org.jbarcode.paint.TextPainter;
import org.jbarcode.paint.WidthCodedPainter;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by openxtiger.org
 * User: Administrator
 * Date: 2008-7-19
 * Time: 0:34:19
 */
public class VirtualOSGlobals {
    /**
     * Location of the jiveHome directory. All configuration files should be
     * located here.
     */
    private static String home = null;
    private static String knowledges = "";
    private static String weSQLDBDir = null;
    private static boolean preLoadResources = false;

    public static boolean failedLoading = false;


    private static Locale locale = null;
    private static TimeZone timeZone = null;
    private static DateFormat dateFormat = null;
    private static DateFormat dateTimeFormat = null;
    private static DateFormat timeFormat = null;

    public static void init() {
        /*
        try {
            Class.forName("com.microsoul.script.VosSC");
        } catch (ClassNotFoundException e) {
            System.exit(0);
            e.printStackTrace();
        }
        */

    }


    public static String getKnowledges() {
        return knowledges;
    }

    public static void setKnowledges(String knowledges) {
        VirtualOSGlobals.knowledges = knowledges;
    }

    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.reset();

            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();

        StringBuilder md5StrBuff = new StringBuilder();

        for (byte aByteArray : byteArray) {
            if (Integer.toHexString(0xFF & aByteArray).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & aByteArray));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & aByteArray));
        }

        return md5StrBuff.toString();
    }

    /**
     * Returns the location of the <code>home</code> directory.
     *
     * @return the location of the home dir.
     */
    public static String getHomeDirectory() {
        return home;
    }

    /**
     * Sets the location of the <code>home</code> directory. The directory must exist and the
     * user running the application must have read and write permissions over the specified
     * directory.
     *
     * @param pathname the location of the home dir.
     */
    public static void setHomeDirectory(String pathname) {
        File mh = new File(pathname);
        // Do a permission check on the new home directory
        if (!mh.exists()) {
        } else if (!mh.canRead() || !mh.canWrite()) {

        } else {
            home = pathname;
        }
    }

    public static boolean isPreLoadResources() {
        return preLoadResources;
    }

    public static void setPreLoadResources(boolean preLoadResources) {
        VirtualOSGlobals.preLoadResources = preLoadResources;
    }

    public static Locale getLocale() {
        return Locale.getDefault();
    }

    public static String formatDate(Date date) {
        DateFormat instance = DateFormat.getDateInstance(DateFormat.MEDIUM, getLocale());
        instance.setTimeZone(getTimeZone());
        return instance.format(date);
    }

    public static TimeZone getTimeZone() {
        return TimeZone.getDefault();
    }

    public static String parseString(Object v) {
        return v == null ? "" : ((String) v).trim();
    }

    public static String toString(Object v) {
        if (v == null) return "";
        if (v instanceof Number) return String.valueOf(v);
        return (String) v;
    }

    public static String pad(String v, int len) {
        if (v.length() > len) {
            return v;
        }
        return "00000000000".substring(0, len - v.length()) + v;
    }

    public static String string2Unicode(String s) {
        try {
            StringBuilder out = new StringBuilder("");
            byte[] bytes = s.getBytes("unicode");
            for (int i = 2; i < bytes.length - 1; i += 2) {
                out.append("\\u");
                String str = Integer.toHexString(bytes[i + 1] & 0xff);
                for (int j = str.length(); j < 2; j++) {
                    out.append("0");
                }
                String str1 = Integer.toHexString(bytes[i] & 0xff);
                out.append(str1);
                out.append(str);
            }
            return out.toString().toLowerCase();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String unicode2String(String unicodeStr) {
        StringBuilder sb = new StringBuilder();
        String str[] = unicodeStr.toUpperCase().split("\\\\U");
        for (String aStr : str) {
            if (aStr.equals("")) continue;
            char c = (char) Integer.parseInt(aStr.trim(), 16);
            sb.append(c);
        }
        return sb.toString();
    }

    public static double toDouble(Object v) {
        if (v == null) return 0.0;
        if (v instanceof Number) return (Double) v;
        return parseDouble((String) v, 0.0);
    }

    public static int toInt(Object v) {
        if (v == null) return 0;
        if (v instanceof Number) return ((Double) v).intValue();
        return parseInt((String) v, 0);
    }

    public static String parseString(String v) {
        return v == null ? "" : ((String) v).trim();
    }

    public static long parseLong(String v, long defaultValue) {
        if (v == null || v.trim().equals(""))
            return defaultValue;
        try {
            return Long.parseLong(v);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static double parseDouble(String v, double defaultValue) {
        if (v == null || v.trim().equals(""))
            return defaultValue;
        try {
            return Double.parseDouble(v);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static int parseInt(String v, int defaultValue) {
        if (v == null || v.trim().equals(""))
            return defaultValue;
        try {
            return Integer.parseInt(v);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static float parseFloat(String v, float defaultValue) {
        if (v == null || v.trim().equals(""))
            return defaultValue;
        try {
            return Float.parseFloat(v);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static int parseInt(Object v, int defaultValue) {
        if (v == null) return defaultValue;
        if (v instanceof String) {
            return parseInt((String) v, defaultValue);
        }
        try {
            return (Integer) v;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static long parseLong(Object v, long defaultValue) {
        if (v == null) return defaultValue;
        if (v instanceof String) {
            return parseLong((String) v, defaultValue);
        }
        try {
            return (Integer) v;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static double round(double v) {
        int scale = 2;
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double round(double v, int scale) {
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static ArrayList<String> split(String regex, String input) {//进行正则表达式分割
        Pattern pt = Pattern.compile(regex);
        Matcher mc = pt.matcher(input);
        int index = 0;
        ArrayList<String> matchList = new ArrayList<String>();
        while (mc.find()) {//匹配字符
            String match = input.subSequence(index, mc.start()).toString();
            matchList.add(match);
            matchList.add(mc.group(1));
            index = mc.end();
        }
        if (index <= input.length() - 1) {
            matchList.add(input.subSequence(index, input.length()).toString());
        }
        return matchList;
    }

    public static String format(String input, Map value) {
        Pattern pt = Pattern.compile("\\$\\{(.*?)\\}");
        Matcher mc = pt.matcher(input);
        int index = 0;
        String matchList = "";
        while (mc.find()) {
            String match = input.subSequence(index, mc.start()).toString();
            matchList += (match);
            matchList += (parseString(value.get(mc.group(1))));
            index = mc.end();
        }
        if (index <= input.length() - 1) {
            matchList += (input.subSequence(index, input.length()).toString());
        }
        return matchList;
    }

    private static String directory;

    public static String getDirectory() {
        return VirtualOSGlobals.directory;
    }

    public static void setDirectory(String directory) {
        VirtualOSGlobals.directory = directory;
    }

    private static String host;

    public static String getHost() {
        return VirtualOSGlobals.host;
    }

    public static void setHost(String host) {
        VirtualOSGlobals.host = host;
    }

    private static String filespot;


    public static String getFilespot() {
        return VirtualOSGlobals.filespot;
    }

    private static int uuid = 10000;

    private final static char[] digits = {
            '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f', 'g', 'h',
            'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z'
    };

    public static String toUnsignedString(int i, int shift) {
        char[] buf = new char[32];
        int charPos = 32;
        int radix = 1 << shift;
        int mask = radix - 1;
        do {
            buf[--charPos] = digits[i & mask];
            i >>>= shift;
        } while (i != 0);

        return new String(buf, charPos, (32 - charPos));
    }

    public static synchronized int getUID() {
        uuid++;
        if (uuid > 99999) {
            uuid = 10000;
        }
        return uuid;
    }

    public static synchronized String getUUID() {
        SimpleDateFormat time = new SimpleDateFormat("yyyyMMddHHmmss");
        return (time.format(new Date()) + VirtualOSGlobals.getUID());
    }

    public static synchronized String getUUID32() {
        SimpleDateFormat time = new SimpleDateFormat("yyMMddHHmmss");
        String l = (time.format(new Date()) + VirtualOSGlobals.getUID());
        return toUnsignedString(Integer.parseInt(l.substring(0, 9)), 5)
                + toUnsignedString(Integer.parseInt((new Random().nextInt(9) + 1)
                + l.substring(9, l.length())), 5);
    }

    public static void setFilespot(String filespot) {
        VirtualOSGlobals.filespot = filespot;
    }

    public static String buildUrl(String url) {
        return "http://" + VirtualOSGlobals.host + url;
    }

    public static String buildFilespot(String url) {
        return url.startsWith("http") ? url : VirtualOSGlobals.filespot + url;
    }

    public static String getWeSQLDirectory() {
        return weSQLDBDir;
    }

    public static void setWeSQLDirectory(String directory) {
        weSQLDBDir = directory;
    }

    public static BufferedImage bulidBarCode(int type,
                                             String str,
                                             boolean isShowText,
                                             double height,
                                             double wideRatio,
                                             int fontSize
    )
            throws Exception {
        BarcodeEncoder barcodeEncoder;
        TextPainter textPainter;
        switch (type) {
            case 0:
                barcodeEncoder = EAN13Encoder.getInstance();
                textPainter = EAN13TextPainter.getInstance();
                break;
            default:
                barcodeEncoder = Code128Encoder.getInstance();
                textPainter = FontBaseLineTextPainter.getInstance();

                break;

        }

        JBarcode jBarcode = new JBarcode(barcodeEncoder,
                WidthCodedPainter.getInstance(), textPainter);
        jBarcode.setBarHeight(height);
        jBarcode.setWideRatio(wideRatio);
        if (type == 1 && isShowText) {
            jBarcode.setShowText(false);
        } else {
            jBarcode.setShowText(isShowText);
        }
        BufferedImage bufferedImage = jBarcode.createBarcode(str);
        if (type == 1 && isShowText) {
            textPainter.paintText(bufferedImage, str, fontSize);
        }

        /*jBarcode.setXDimension(dimension);*/
        return bufferedImage;
    }


    public static BufferedImage bulidQRCode(int version,
                                            int size,
                                            char type,
                                            int offsetWidth,
                                            int offsetHeight,
                                            String code,
                                            File logo,
                                            int logoSize
    ) throws Exception {
        Qrcode qrcodeHandler = new Qrcode();
        qrcodeHandler.setQrcodeErrorCorrect('M');
        qrcodeHandler.setQrcodeEncodeMode(type);
        qrcodeHandler.setQrcodeVersion(version);

        byte[] contentBytes = code.getBytes("utf-8");

        int pixoff = 2;
        if (contentBytes.length > 0 && contentBytes.length < 800) {
            boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);

            int imgSize = codeOut.length * size + 4;
            BufferedImage bufImg = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_RGB);
            Graphics2D gs = bufImg.createGraphics();
            gs.clearRect(0, 0, imgSize, imgSize);

            gs.setPaint(java.awt.Color.WHITE);
            gs.fillRect(0, 0, imgSize, imgSize);

            gs.setPaint(java.awt.Color.BLACK);

            for (int i = 0; i < codeOut.length; i++) {
                for (int j = 0; j < codeOut.length; j++) {
                    if (codeOut[j][i]) {
                        gs.fillRect(j * size + offsetWidth + pixoff, i * size + offsetHeight + pixoff, size, size);
                    }
                }
            }

            if (logo != null && logo.exists()) {
                Image src = ImageIO.read(logo);
                int width = src.getWidth(null);
                int height = src.getHeight(null);
                if (width > logoSize) width = logoSize;
                if (height > logoSize) height = logoSize;

                Image image = src.getScaledInstance(width, height,
                        Image.SCALE_SMOOTH);
                BufferedImage tag = new BufferedImage(width, height,
                        BufferedImage.TYPE_INT_RGB);
                Graphics g = tag.getGraphics();
                g.drawImage(image, 0, 0, null);

                g.dispose();
                src = image;
                gs.drawImage(src, (imgSize - width) / 2, (imgSize - height) / 2, width, height, null);
            }
            gs.dispose();
            bufImg.flush();
            return bufImg;
        } else {
            throw new Exception("QRCode content bytes length = " + contentBytes.length + " not in [0, 800].");
        }
    }


    /*public static String decodeBarcode(String qrcodePicfilePath) {
        File imageFile = new File(qrcodePicfilePath);
        BufferedImage image;
        try {
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            System.out.println("Decoding failed, read QRCode image error: " + e.getMessage());
            return null;
        }
        QRCodeDecoder decoder = new QRCodeDecoder();
        return new String(decoder.decode(new J2SEImageGucas(image)));
    }

    static class J2SEImageGucas implements QRCodeImage {
        BufferedImage image;

        public J2SEImageGucas(BufferedImage image) {
            this.image = image;
        }

        public int getWidth() {
            return image.getWidth();
        }

        public int getHeight() {
            return image.getHeight();
        }

        public int getPixel(int x, int y) {
            return image.getRGB(x, y);
        }
    }*/

    public static void export(HttpServletRequest request, HttpServletResponse response, Object o) throws IOException {
        export(request, response, new Object[]{o}, null);
    }

    public static void export(HttpServletRequest request, HttpServletResponse response, Object[] o) throws IOException {
        export(request, response, o, null);
    }

    public static void export(HttpServletRequest request, HttpServletResponse
            response, Object[] o, String outFileName) throws IOException {
        String tpl = request.getParameter("tpl");
        String fileName = outFileName == null ? request.getParameter("fileName") : outFileName;
        String lang = request.getParameter("lang") == null ? "" : "_" + request.getParameter("lang");
        if (tpl == null) return;
        File f = new File(VirtualOSGlobals.directory + "/templates", tpl + lang + ".xls");
        try {
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(f));
            HSSFWorkbook wb = new HSSFWorkbook(fs);

            HSSFParser parser;
            File pf = f.getParentFile();
            for (int i = o.length - 1; i >= 0; i--) {
                parser = new HSSFParser();
                if (o[i] == null) {
                    wb.removeSheetAt(i);
                    continue;
                }
                parser.parse(wb, o[i], i);

                parser.process(o[i], pf);
            }


            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            for (int sheetNum = 0; sheetNum < wb.getNumberOfSheets(); sheetNum++) {
                Sheet sheet = wb.getSheetAt(sheetNum);
                /*PrintSetup ps = sheet.getPrintSetup();

               sheet.setAutobreaks(true);
               sheet.setPrintGridlines(true);
               ps.setFitHeight((short) 1);
               ps.setFitWidth((short) 1);*/

                for (Row r : sheet) {
                    for (Cell c : r) {
                        if (c.getCellType() == Cell.CELL_TYPE_FORMULA) {
                            c.setCellFormula(c.getCellFormula());
                            evaluator.evaluateFormulaCell(c);
                        }
                    }
                }
            }
            StringBuilder sb = new StringBuilder(50);
            sb.append("attachment2;  filename=").append(fileName == null || fileName.trim().equals("") ? tpl : fileName).append(".xls");
            response.setContentType("application/x-msdownload;charset=UTF-8");
            response.setHeader("Content-Disposition", new String(sb.toString()
                    .getBytes(), "ISO8859-1"));
            ServletOutputStream out = response.getOutputStream();
            wb.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Runtime.getRuntime().gc();
        }

        /*ServletOutputStream out = response.getOutputStream();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", null);
        wb.write(out);
        out.flush();*/
    }


    private static Map<String, Mime> mimes;
    private static Map<String, Mime> mimeTypes;

    public static Mime getMime(String mime) {
        if (mimes == null) {
            initmime();
        }
        return mimes.get(mime);
    }

    private static void initmime() {
        mimes = new HashMap<String, Mime>();
        mimeTypes = new HashMap<String, Mime>();
        URL url = VirtualOSGlobals.class.getClassLoader().getResource("com/microsoul/weapi/util/mimes.xml");
        SAXReader saxReader = new SAXReader();
        saxReader.setEncoding("UTF-8");
        try {
            Document xml = saxReader.read(url);
            Element el = xml.getRootElement();
            List<Element> cs = el.elements();
            Mime m;
            for (Element c : cs) {
                m = new Mime();
                m.setExtension(c.attributeValue("extension"));
                m.setMimeType(c.attributeValue("mime-type"));
                m.setCls(c.attributeValue("cls"));
                m.setText(c.attributeValue("text"));
                mimes.put(m.getExtension(), m);
                mimeTypes.put(m.getMimeType(), m);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public static void export2(HttpServletRequest request, HttpServletResponse response, Object o) throws IOException {
        export2(request, response, new Object[]{o}, null, null);
    }

    public static void export2(HttpServletRequest request, HttpServletResponse response, Object[] o) throws IOException {
        export2(request, response, o, null, null);
    }

    public static void export2(HttpServletRequest request, HttpServletResponse
            response, Object[] o, String outFileName) throws IOException {
        export2(request, response, o, outFileName, null);
    }

    public static void export2(String url, HttpServletRequest request, HttpServletResponse
            response, Object[] o, String outFileName, String dtpl) throws IOException {
        String tpl = dtpl == null ? request.getParameter("tpl") : dtpl;
        String lang = request.getParameter("lang") == null ? "" : "_" + request.getParameter("lang");
        try {
            log(url + "/templates/" + tpl + lang + ".xls" + "===" + VirtualOSGlobals.directory + "/templates" + "===" + tpl + lang + ".xls");
            FileUtils.copyURLToFile(new URL(url + "/templates/" + tpl + lang + ".xls"), new File(VirtualOSGlobals.directory + "/templates", tpl + lang + ".xls"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        export2(request, response, o, outFileName, dtpl);
        File destination = new File(VirtualOSGlobals.directory + "/templates", tpl + lang + ".xls");
        destination.delete();
    }

    /**
     * 导出excel
     *
     * @param path   templates下的文件路径
     * @param lang   语言格式
     * @param o      导出对象
     * @param target 导出路径
     */
    public static void export(String path, String lang, Object[] o, String target) {
        if (path == null) return;
        File f = path.contains(".xls") ? new File(path) : new File(VirtualOSGlobals.directory + "/templates/", path + lang + ".xls");
        try {
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(f));
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            int insidePictureCount = wb.getAllPictures().size();
            POIParser parser;
            File pf = f.getParentFile();
            for (int i = o.length - 1; i >= 0; i--) {//循环数据
                parser = new POIParser();//创建一个分析类
                if (o[i] == null) {
                    wb.removeSheetAt(i);
                    continue;
                }
                parser.parse(wb, o[i], i);//进入分析

                parser.process(o[i], pf, insidePictureCount);
            }


            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            for (int sheetNum = 0; sheetNum < wb.getNumberOfSheets(); sheetNum++) {
                Sheet sheet = wb.getSheetAt(sheetNum);
                /*PrintSetup ps = sheet.getPrintSetup();

               sheet.setAutobreaks(true);
               sheet.setPrintGridlines(true);
               ps.setFitHeight((short) 1);
               ps.setFitWidth((short) 1);
               */

                for (Row r : sheet) {
                    for (Cell c : r) {
                        if (c.getCellType() == Cell.CELL_TYPE_FORMULA) {
                            c.setCellFormula(c.getCellFormula());
                            evaluator.evaluateFormulaCell(c);
                        }
                    }
                }
            }
            File file = new File(target);
            FileOutputStream out = new FileOutputStream(file);
            wb.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void export2(HttpServletRequest request, HttpServletResponse
            response, Object[] o, String outFileName, String dtpl) throws IOException {
        String tpl = dtpl == null ? request.getParameter("tpl") : dtpl;
        String fileName = outFileName == null ? request.getParameter("fileName") : outFileName;
        String lang = request.getParameter("lang") == null ? "" : "_" + request.getParameter("lang");
        if (tpl == null) return;
        File f = new File(VirtualOSGlobals.directory + "/templates", tpl + lang + ".xls");
        try {
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(f));
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            int insidePictureCount = wb.getAllPictures().size();
            POIParser parser;
            File pf = f.getParentFile();
            for (int i = o.length - 1; i >= 0; i--) {//循环数据
                parser = new POIParser();//创建一个分析类
                if (o[i] == null) {
                    wb.removeSheetAt(i);
                    continue;
                }
                parser.parse(wb, o[i], i);//进入分析


                parser.process(o[i], pf, insidePictureCount);
            }


            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            for (int sheetNum = 0; sheetNum < wb.getNumberOfSheets(); sheetNum++) {
                Sheet sheet = wb.getSheetAt(sheetNum);
                /*PrintSetup ps = sheet.getPrintSetup();

               sheet.setAutobreaks(true);
               sheet.setPrintGridlines(true);
               ps.setFitHeight((short) 1);
               ps.setFitWidth((short) 1);
               */

                for (Row r : sheet) {
                    for (Cell c : r) {
                        if (c.getCellType() == Cell.CELL_TYPE_FORMULA) {
                            c.setCellFormula(c.getCellFormula());
                            evaluator.evaluateFormulaCell(c);
                        }
                    }
                }
            }
            StringBuilder sb = new StringBuilder(50);
            sb.append("attachment;  filename=").append(fileName == null || fileName.trim().equals("") ? tpl : fileName).append(".xls");
            response.setContentType("application/x-msdownload;charset=UTF-8");
            response.setHeader("Content-Disposition", new String(sb.toString()
                    .getBytes(), "ISO8859-1"));
            ServletOutputStream out = response.getOutputStream();
            wb.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*ServletOutputStream out = response.getOutputStream();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", null);
        wb.write(out);
        out.flush();*/
    }

    private static void createComment(Drawing drawing, Cell cell, Row row, String dataIndex) {
        Comment comment1 = drawing.createCellComment(new HSSFClientAnchor(0, 0, 0, 0, (short) cell.getColumnIndex(),
                row.getRowNum(), (short) (cell.getColumnIndex() + 1), row.getRowNum() + 1));
        comment1.setString(new HSSFRichTextString(dataIndex));
        cell.setCellComment(comment1);
    }

    public static void export3(HttpServletRequest request, HttpServletResponse
            response, Map o, Map paras) throws IOException {
        String fileName = request.getParameter("tpl");
        try {

            HSSFWorkbook wb = new HSSFWorkbook();

            Sheet sheet = wb.createSheet();

            JsonList heads = (JsonList) paras.get("heads");
            JsonList bodys = (JsonList) paras.get("bodys");
            boolean showNO = paras.get("showNO") != null &&
                    paras.get("showNO") != JSONObject.NULL &&
                    (Boolean) paras.get("showNO");

            int pageScale = 60;
            if (paras.get("pageScale") != null &&
                    paras.get("pageScale") != JSONObject.NULL) {
                pageScale = (Integer) paras.get("pageScale");
            }

            Object printSummary = paras.get("printSummary");

            Integer headCols = (Integer) paras.get("headCols");
            if (headCols == null) {
                headCols = 3;
            }
            Integer mindex = (Integer) paras.get("mindex");
            if (mindex == null) {
                mindex = 0;
            }
            mindex += mindex % headCols;

            Row row = sheet.createRow(0);

            int cpos = showNO ? 1 : 0;

            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, Math.max(headCols * 2, bodys.size()) - 1 + cpos));

            HSSFFont font = wb.createFont();
            font.setFontHeightInPoints((short) 24);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

            HSSFCellStyle style = wb.createCellStyle();

            style.setFont(font);
            style.setAlignment(CellStyle.ALIGN_CENTER);
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

            Cell cell = row.createCell(0);
            cell.setCellValue((String) paras.get("title"));
            cell.setCellStyle(style);
            row.setHeight((short) 800);

            Map<Integer, Map> htds = new HashMap<Integer, Map>();

            for (Object head1 : heads) {
                Map head = (Map) head1;
                Integer printIndex = (Integer) head.get("printIndex");
                if (printIndex == null) continue;
                htds.put(printIndex, head);
            }
            if (showNO) {
                sheet.setColumnWidth(0, 6 * 256);
            }
            for (int j = Math.max(headCols * 2, bodys.size()) - 1; j >= 0; j--) {
                if (j < bodys.size()) {
                    Map b = (Map) bodys.get(j);
                    if (b.get("downloadWidth") != null && b.get("downloadWidth") != JSONObject.NULL) {
                        sheet.setColumnWidth(j + cpos, (Integer) b.get("downloadWidth") * 256);
                        continue;
                    }
                }

                sheet.setColumnWidth(j + cpos, 20 * 256);
            }

            HSSFCellStyle ts = wb.createCellStyle();
            ts.setAlignment(CellStyle.ALIGN_RIGHT);
            ts.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            HSSFFont tfont = wb.createFont();
            tfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            ts.setFont(tfont);

            HSSFCellStyle vs = wb.createCellStyle();
            vs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            Drawing drawing = sheet.createDrawingPatriarch();

            int idx = 1;
            for (int i = 0; i < mindex / headCols; i++) {
                row = sheet.createRow(2 + i);
                row.setHeight((short) 400);
                for (int j = 0; j < headCols * 2; j += 2) {
                    Map head = htds.get(idx++);
                    if (head == null) continue;

                    cell = row.createCell(j + cpos);
                    cell.setCellValue(head.get("header") + ":");
                    cell.setCellStyle(ts);

                    cell = row.createCell(j + cpos + 1);
                    cell.setCellValue(((String) head.get("value")).replace("&#160;", ""));
                    cell.setCellStyle(vs);
                    createComment(drawing, cell, row, (String) head.get("dataIndex"));
                }
            }

            int startRow = 3 + mindex / headCols;
            sheet.createFreezePane(0, startRow + 1);

            row = sheet.createRow(startRow);
            row.setHeight((short) 400);

            ts = wb.createCellStyle();
            ts.setAlignment(CellStyle.ALIGN_CENTER);
            ts.setBorderTop(HSSFCellStyle.BORDER_THIN);
            ts.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            ts.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
            ts.setBorderRight(HSSFCellStyle.BORDER_THIN);
            ts.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            ts.setFont(tfont);

            List list = (List) o.get("list");
            Row srow = sheet.createRow(startRow + list.size() + 1);
            srow.setHeight((short) 400);
            if (showNO) {
                cell = row.createCell(0);
                cell.setCellStyle(ts);
            }
            if (printSummary != null && printSummary != JSONObject.NULL) {
                cell = srow.createCell(cpos);
                HSSFCellStyle tsn = wb.createCellStyle();
                tsn.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
                tsn.setAlignment(CellStyle.ALIGN_RIGHT);
                cell.setCellStyle(tsn);
                cell.setCellValue((String) printSummary);
            }
            for (int i = 0; i < bodys.size(); i++) {
                Map b = (Map) bodys.get(i);
                cell = row.createCell(i + cpos);
                cell.setCellStyle(ts);
                cell.setCellValue((String) b.get("header"));
                createComment(drawing, cell, row, (String) b.get("dataIndex"));
                if (b.get("summaryName") != null && b.get("summaryName") != JSONObject.NULL) {
                    cell = srow.createCell(i + cpos);
                    String cs = CellReference.convertNumToColString(i + cpos);

                    cell.setCellFormula("sum(" + cs + (startRow + 2) + ":" + cs + (startRow + 1 + list.size()) + ")");
                }
            }

            cell = row.createCell(bodys.size() + cpos);

            createComment(drawing, cell, row, "<<list");
            cell.setCellValue(" ");

            if (list.size() > 0) {
                cell = srow.createCell(bodys.size() + cpos);
                cell.setCellValue(" ");
                createComment(drawing, cell, srow, ">>list");
            }

            HSSFCellStyle tsl = wb.createCellStyle();
            tsl.setBorderTop(HSSFCellStyle.BORDER_THIN);
            tsl.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            tsl.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            tsl.setBorderRight(HSSFCellStyle.BORDER_THIN);
            tsl.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

            HSSFCellStyle tsc = wb.createCellStyle();
            tsc.cloneStyleFrom(tsl);
            tsc.setAlignment(CellStyle.ALIGN_CENTER);

            HSSFCellStyle tsr = wb.createCellStyle();
            tsr.cloneStyleFrom(tsl);
            tsr.setAlignment(CellStyle.ALIGN_RIGHT);

            HSSFCellStyle tsn = wb.createCellStyle();
            tsn.setVerticalAlignment(CellStyle.VERTICAL_CENTER);


            short h = 400;
            if (paras.get("downloadHeight") != null && paras.get("downloadHeight") != JSONObject.NULL) {
                h = (short) ((Integer) paras.get("downloadHeight") * 128);
            }

            for (int r = 0; r < list.size(); r++) {
                Map d = (Map) list.get(r);
                row = sheet.createRow(startRow + r + 1);
                row.setHeight(h);
                if (showNO) {
                    cell = row.createCell(0);
                    cell.setCellStyle(tsl);
                    cell.setCellValue(r + 1);
                }
                POIHelper poiHelper = new HSSFPOIHelper(wb, sheet, drawing);
                CreationHelper creationHelper = wb.getCreationHelper();
                for (int i = 0; i < bodys.size(); i++) {
                    Map b = (Map) bodys.get(i);
                    cell = row.createCell(i + cpos);
                    Object v = d.get(b.get("dataIndex"));

                    if (b.get("formula") != null) {
                        cell.setCellStyle(tsl);
                        parseFormula((String) b.get("formula"), row, cell, cpos, v);
                        continue;
                    }
                    if (b.get("settle") != null) {
                        cell.setCellStyle(tsl);
                        parseSettle((String) b.get("settle"), row, cell, cpos, v);
                        continue;
                    }

                    if (b.get("picture") != null) {
                        cell.setCellStyle(tsl);
                        String picture = (String) b.get("picture");
                        if (v == null || ((String) v).equals("")) continue;
                        File f;
                        f = new File(VirtualOSGlobals.getDirectory(), (String) v);

                        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
                        if (!f.exists()) continue;
                        try {
                            BufferedImage bufferImg = ImageIO.read(f);
                            ImageIO.write(bufferImg, "jpg", byteArrayOut);
                            String[] pics = picture.split(",");
                            createPicture(poiHelper, creationHelper, i + cpos,
                                    startRow + r + 1, pics,
                                    byteArrayOut.toByteArray(), bufferImg.getWidth(), bufferImg.getHeight(),
                                    pics.length > 0 && pics[0].equals("1"), 0);
                        } catch (Exception e) {
                            //
                        }
                        continue;
                    }


                    if (v == null) {
                        cell.setCellStyle(tsl);
                        continue;
                    }

                    if (v instanceof String) {
                        if ("center".equals(b.get("align")))
                            cell.setCellStyle(tsc);
                        else if ("right".equals(b.get("align")))
                            cell.setCellStyle(tsr);
                        else
                            cell.setCellStyle(tsl);
                        cell.setCellValue((String) v);
                    } else {
                        cell.setCellStyle(tsl);
                        cell.setCellValue(((Number) v).doubleValue());
                    }
                }
            }

            wb.setPrintArea(0, "$A:$" + CellReference.convertNumToColString(Math.max(headCols * 2 - 1 + cpos, bodys.size())));
            sheet.getPrintSetup().setScale((short) pageScale);

            StringBuilder sb = new StringBuilder(50);
            sb.append("attachment;  filename=").append(fileName).append(".xls");
            response.setContentType("application/x-msdownload;charset=UTF-8");
            response.setHeader("Content-Disposition", new String(sb.toString()
                    .getBytes(), "ISO8859-1"));
            ServletOutputStream out = response.getOutputStream();
            wb.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Runtime.getRuntime().gc();
        }

            /*ServletOutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", null);
            wb.write(out);
            out.flush();*/
    }

    private static void parseFormula(String formula, Row row, Cell cell, int cpos, Object v) {
        //CellReference.convertNumToColString(i);
        ArrayList<String> vs = VirtualOSGlobals.split("[\\$\\#]\\{([^\\}]*)\\}", formula);

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

    private static void parseSettle(String settle, Row row, Cell cell, int cpos, Object v) {
        String r;
        switch (settle.charAt(0)) {
            case '~':
                int i = (Integer) v;

                settle = settle.substring(1);
                String[] ss = settle.split("\\^");
                if (ss.length < i) r = "";
                else r = ss[i];
                cell.setCellValue(r);
                return;
        }
    }

    private static void createPicture(POIHelper poiHelper,
                                      CreationHelper creationHelper,
                                      int col, int row,
                                      String[] fun,
                                      byte[] bytes,
                                      int width, int height,
                                      boolean withS, int offset) {
        if (bytes == null) return;
        //int r = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
        //System.out.println("s:" + width + "," + height);
        if (withS) {
            int type = fun.length >= 4 + offset ? VirtualOSGlobals.parseInt(fun[3 + offset], 0) : 0;
            //type= 0,
            int w = fun.length >= 5 + offset ? VirtualOSGlobals.parseInt(fun[4 + offset], width) : width;
            int h = fun.length >= 6 + offset ? VirtualOSGlobals.parseInt(fun[5 + offset], height) : height;
            switch (type) {
                case 1:   //with width
                    h = height * w / width;
                    break;
                case 2: // with height
                    h = w;
                    w = width * h / height;
                    break;
                case 3: // in squal
                    h = fun.length >= 6 + offset ? h : w;
                    if (width > w || height > h) {
                        if (w / width > h / height) {
                            w = width * h / height;
                        } else {
                            h = height * w / width;
                        }
                    } else {
                        w = width;
                        h = height;
                    }
                    break;
            }
            poiHelper.createPicture(col, row,
                    fun.length >= 2 + offset ? VirtualOSGlobals.parseInt(fun[1 + offset], 0) : 0,
                    fun.length >= 3 + offset ? VirtualOSGlobals.parseInt(fun[2 + offset], 0) : 0,
                    w,
                    h,
                    bytes,
                    0
            );
        } else {
            ClientAnchor anchor = creationHelper.createClientAnchor();
            anchor.setRow1(row);
            anchor.setRow2(row + (fun.length >= 3 ? VirtualOSGlobals.parseInt(fun[2], 0) : 0));
            anchor.setCol1(col);
            anchor.setCol2(col + (fun.length >= 2 ? VirtualOSGlobals.parseInt(fun[1], 0) : 0));
            anchor.setDx1(fun.length >= 4 ? VirtualOSGlobals.parseInt(fun[3], 0) : 0);
            anchor.setDy1(fun.length >= 5 ? VirtualOSGlobals.parseInt(fun[4], 0) : 0);
            anchor.setDx2(fun.length >= 6 ? VirtualOSGlobals.parseInt(fun[5], 0) : 0);
            anchor.setDy2(fun.length >= 7 ? VirtualOSGlobals.parseInt(fun[6], 0) : 0);


            poiHelper.createPicture(anchor, width, height, bytes, 0);
        }

    }

    public static void xmlToMap(Element element, Map<String, String> result) {
        List<Element> elementList = element.elements();
        if (elementList.size() > 0) {
            for (Element e : elementList) {
                xmlToMap(e, result);
            }
        } else {
            result.put(element.getName(), element.getText());
        }
    }

    public static Map<String, Object> parse(InputStream stream) throws IOException {
        POIFSFileSystem fs = new POIFSFileSystem(stream);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        Sheet sheet = wb.getSheetAt(0);
        int pStartRow = sheet.getFirstRowNum(), pEndRow = sheet.getLastRowNum();
        JsonMap<String, Object> o = new JsonMap<String, Object>();
        Row row;
        Cell cell;
        boolean isList = false;
        boolean isEndList = false;
        boolean isStartList = false;
        JsonMap<String, Object> cmap = new JsonMap<String, Object>();
        Map<Integer, String> ccmap = new HashMap<Integer, String>();

        ArrayList<JsonMap<String, Object>> list = new ArrayList<JsonMap<String, Object>>();
        for (int i = pStartRow; i <= pEndRow; i++) {
            row = sheet.getRow(i);
            if (row == null) {
                continue;

            }
            if (!isList) {
                ccmap = new HashMap<Integer, String>();
            }

            for (int j = row.getFirstCellNum(), k = row.getLastCellNum(); j <= k; j++) {
//                CellAddress cellAddress = new CellAddress(i,j);
                Comment comment = sheet.getCellComment(i,j);
                if (comment != null) {

                    String s = comment.getString().toString();
                    if (s != null && !s.trim().equals("")) {
                        if (s.startsWith("<<")) {
                            isList = true;
                            isStartList = true;
                            list = new ArrayList<JsonMap<String, Object>>();
                            o.put(s.substring(2), list);
                        } else if (s.startsWith(">>")) {
                            isList = false;
                            isEndList = true;
                        } else {
                            ccmap.put(j, s);
                        }
                    }
                }
            }

            if (isStartList) {
                isStartList = false;
                continue;
            }

            if (isEndList) {
                isEndList = false;
                for (int j = row.getFirstCellNum(), k = row.getLastCellNum(); j <= k; j++) {
                    cell = row.getCell(j);
//                    CellAddress cellAddress = new CellAddress(i,j);
                    Comment comment = sheet.getCellComment(i,j);
                    if (comment != null) {

                        String s = comment.getString().toString();
                        if (s != null && !s.trim().equals("") && !s.startsWith(">>")) {
                            setCellValue(cell, o, s);
                        }
                    }
                }
            } else if (isList) {
                cmap = new JsonMap<String, Object>();
                for (int j = row.getFirstCellNum(), k = row.getLastCellNum(); j <= k; j++) {
                    cell = row.getCell(j);
                    if (cell != null && ccmap.get(j) != null) {
                        setCellValue(cell, cmap, ccmap.get(j));
                    }
                }
                list.add(cmap);
            } else {
                for (int j = row.getFirstCellNum(), k = row.getLastCellNum(); j <= k; j++) {
                    cell = row.getCell(j);
                    if (cell != null && ccmap.get(j) != null) {
                        setCellValue(cell, o, ccmap.get(j));
                    }
                }
            }
        }
        return o;
    }

    private static void setCellValue(Cell cell, Map<String, Object> o, String key) {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                o.put(key, cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_NUMERIC:
                o.put(key, cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                o.put(key, cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_BLANK:
                o.put(key, "");

        }
    }

    public static String lang(String str1, String str2) {
        return "zh_CN".equals(MDC.getUser() == null || MDC.getUser().getLang() == null ? "zh_CN" : MDC.getUser().getLang()) ? str1 : str2;
    }

    public static String encoding(String v) {
        try {
            return v == null ? null : new String(v.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static void zip(HttpServletRequest request, HttpServletResponse
            response, ArrayList<String[]> objs, String outFileName) throws IOException {
        String fileName = outFileName == null ? request.getParameter("fileName") : outFileName;

        try {

            StringBuilder sb = new StringBuilder(50);
            sb.append("attachment;  filename=").append(fileName).append(".zip");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", new String(sb.toString()
                    .getBytes(), "ISO8859-1"));
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());

            for (String[] f : objs) {
                zos.putNextEntry(new ZipEntry(f[0]));
                FileInputStream fis = new FileInputStream(f[1]);
                byte[] buffer = new byte[1024];
                int r;
                while ((r = fis.read(buffer)) != -1) {
                    zos.write(buffer, 0, r);
                }
                fis.close();
            }
            zos.flush();
            zos.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Runtime.getRuntime().gc();
        }


    }

    private static long logTime = 0;
    private static long startlogTime = 0;

    public static void startLog() {
        logTime = startlogTime = System.currentTimeMillis();
    }

    public static void log(String s) {
        System.out.println((System.currentTimeMillis() - logTime) + "s\n" + s);
        logTime = System.currentTimeMillis();
    }

    public static void endLog() {
        System.out.println("Total:" + (System.currentTimeMillis() - startlogTime) + "s");
    }
}
