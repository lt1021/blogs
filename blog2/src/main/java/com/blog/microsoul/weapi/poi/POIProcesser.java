package com.blog.microsoul.weapi.poi;


import com.blog.base.util.GlobalUtil;
import com.blog.microsoul.weapi.poi.hssf.HSSFPOIHelper;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.impl.code128.EAN128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
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
 * Account: xtiger
 * Date: 2012-4-7
 */
public class POIProcesser {
    private static final HashMap<Class, BeanData> beanCache = new HashMap<Class, BeanData>();

    private Object obj;
    private List aobj;
    private ArrayList<POIVar> vars = new ArrayList<POIVar>();
    private String name;
    private HashMap<String, Method> rProps = null;


    private Sheet sheet;
    private Workbook workbook;
    private Drawing patriarch;
    private CreationHelper creationHelper;
    private POIHelper poiHelper;

    private POIList plist;
    private List<POIProcesser> siblings = new ArrayList<>();

    private StringBuilder endRows = new StringBuilder();
    private StringBuilder beginRows = new StringBuilder();
    private File templateDir;
    private int nextColStart = 0;  //如果是列循环，记录每次开始的列

    public void setNextColStart(int nextColStart) {
        this.nextColStart = nextColStart;
    }

    public void addSibling(POIProcesser sprocesser) {
        siblings.add(sprocesser);
        sprocesser.vars.addAll(vars);
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
        bd.readableProps = new HashMap<>();
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


    public Object getObject() {
        return obj;
    }

    public Object getCurrObject() {
        return aobj.get(index);
    }

    public void init(Object obj, Sheet sheet, Workbook workbook,
                     Drawing patriarch, POIList plist) {
        this.workbook = workbook;
        this.sheet = sheet;
        this.patriarch = patriarch;
        this.plist = plist;
        this.name = plist.getName();
        this.obj = obj;


        creationHelper = workbook.getCreationHelper();
        this.poiHelper = new HSSFPOIHelper(workbook, sheet, patriarch);

    }

    public void sreset(int colOffset) {
        this.plist.resetOffset(colOffset);
        for (POIVar c : vars) {
            c.reset().setTargetCell(POITools.getCell(sheet, 0,
                    c.getTargetCell().getColumnIndex() + colOffset, c.getTargetCell()));
        }

    }

    public void reset() {

        //System.out.println("reset----->" + this.name);
        for (POIVar c : vars) {
            c.setTargetCell(POITools.getCell(sheet, this.plist.getParent().getRealDataRows(),
                    c.getTargetCell().getColumnIndex(), c.getTargetCell()));
        }
        this.plist.reset(this.plist.getParent().getRealDataRows());

    }

    public int process(Object obj, HashMap<String, POIProcesser> pMaps, File pf, int insidePictureCount, int shiftCount) {
        templateDir = pf;
        if (obj == null) return -1;
        List ls;
        if (!(obj instanceof List)) {
            ls = new ArrayList();
            ls.add(obj);
        } else {
            ls = (List) obj;
        }

        if (siblings.size() > 0) {
            int cs = siblings.size();
            ArrayList<ArrayList> os = new ArrayList<ArrayList>();
            for (POIProcesser sibling : siblings) {
                os.add(new ArrayList());
            }

            for (int i = 0; i < ls.size(); i += cs) {
                for (int j = 0; j < cs; j++) {
                    if (i + j >= ls.size()) break;
                    os.get(j).add(ls.get(j + i));
                }
            }
            for (int j = 0; j < cs; j++) {
                siblings.get(j).process(os.get(j), pMaps, pf, insidePictureCount, shiftCount);
                sreset((j + 1) * plist.getColCount());
                for (POIList p : this.plist.getChildren()) {
                    p.getProcesser().sreset((j + 1) * plist.getColCount());
                }


            }
            return 0;
        }

        this.aobj = ls;
        int count = ls.size();

        //System.out.println("=============================" + this.getName() + ":begin===========================");

        if (count > this.plist.getCapacity() && shiftCount == 0) {
            //如果当前的数据行数，比模版设置的行数多，则要对模版的数据行数（endRow-startRow)进行扩展。

            if (this.plist.getParent() != null) {
                this.plist.getParent().resetRealDataRows(count - this.plist.getCapacity());
            }


            /*System.out.println(this.getName() + ":---------->shiftRows<-----------"
                    + (this.plist.getRowStart() + this.plist.getDataRows() * this.plist.getCapacity()));*/

            if (this.plist.getPageOffset() >= 0) {
                sheet.shiftRows(this.plist.getRowStart() + this.plist.getDataRows() * this.plist.getCapacity(),
                        sheet.getLastRowNum(),
                        (count - this.plist.getCapacity())
                                * this.plist.getDataRows(), true, false);


                for (POIList list : this.plist.getParent().getChildren()) {
                    if (list.getRowStart() > this.plist.getRowStart()) {
                        list.shiftRows((count - this.plist.getCapacity())
                                * this.plist.getDataRows());
                    }
                }
            }

            //int rowStart, int dataRows, int offset, int count,
            //System.out.println(this.getName() + ":---------->copyMergeds<-----------");
            //System.out.println(count - this.plist.getCapacity());
            POITools.copyMergeds(this.plist.getRowStart(),
                    this.plist.getDataRows(), this.plist.getDataRows() * this.plist.getCapacity(),
                    count - this.plist.getCapacity(), sheet);

            //System.out.println(this.getName() + ":---------->copyStyle<-----------");
            POITools.copyStyle(this.plist.getRowStart(), this.plist.getColStart(), this.plist.getColEnd(),
                    this.plist.getDataRows(), this.plist.getDataRows() * this.plist.getCapacity(),
                    count - this.plist.getCapacity(), sheet);
        }
        //System.out.println(this.getName() + ":---------->copyCell<-----------");
        poiHelper.copyCell(this.plist.getRowStart(), this.plist.getColStart(), this.plist.getColEnd(),
                this.plist.getDataRows(), this.plist.getDataRows(), count - 1);


        startRow = this.getStart();
        if (count > 0) {
            if (!(ls.get(0) instanceof Map)) {
                try {
                    rProps = getBeanData(ls.get(0).getClass()).readableProps;
                } catch (IntrospectionException e) {
                    e.printStackTrace();
                }
            }
        }
        for (index = 0; index < count; index++) {
            //当循环新行时，只将数据最多的子集扩张。其它的子集无需扩张。
            if (this.plist.getParent() != null && this.plist.getParent().getName().equals("root")) {
//                shiftCount = this.plist.getChildren().stream().filter(d -> ((Map) ls.get(index)).get(d.getName()) instanceof List)
//                        .map(d -> ((List) ((Map) ls.get(index)).get(d.getName())).size()).max(Integer::compareTo).get();
                this.plist.getChildren().sort((s1, s2) -> {
                    if (((Map) ls.get(index)).get(s2.getName()) == null) {
                        return 0;
                    }
                    if (((Map) ls.get(index)).get(s1.getName()) == null) {
                        return 1;
                    }
                    return Integer.compare(((List) ((Map) ls.get(index)).get(s2.getName())).size(), ((List) ((Map) ls.get(index)).get(s1.getName())).size());
                });
                shiftCount = 0;
            }

            for (POIList p : this.plist.getChildren()) {
                //当root下有多个集合循环时，则每次循环都要扩张单元格。
                if (p.getParent() != null && p.getParent().getName().equals("root")) {
                    shiftCount = 0;
                }
                p.getProcesser().process(((Map) ls.get(index)).get(p.getName()), pMaps, templateDir, insidePictureCount, shiftCount++);
            }

            //System.out.println(this.name + "==>" + ls.get(index));
            processVars(ls.get(index), pMaps, insidePictureCount);

            //reset var
            for (POIVar c : vars) {
                c.setLastTargetCell(POITools.getCell(sheet,
                        c.isFixed() ? this.plist.getDataRows() : this.plist.getRealDataRows(),
                        c.getLastTargetCell().getColumnIndex(), c.getLastTargetCell()));
            }


            //reset children's var
            for (POIList p : this.plist.getChildren()) {
                p.getProcesser().reset();
            }
            if (beginRows.length() > 0) {
                beginRows.append(",").append(startRow);
            } else {
                beginRows.append(startRow);
            }
            startRow += this.plist.getRealDataRows();
            if (endRows.length() > 0) {
                endRows.append(",").append(startRow - 1);
            } else {
                endRows.append(startRow - 1);
            }

            //如果当前实际的数据行数大于本行数的数据行数,根目录不计算
            if (this.plist.getRealDataRows() > this.plist.getDataRows() && !"root".equals(getName())) {
                int mergedRow = startRow - this.plist.getRealDataRows();
                //System.out.println(this.getName() + ":==========resetMergeds==========" + (mergedRow + "," + startRow + "," + this.plist.getRealDataRows() + "," + this.plist.getDataRows()));

                POITools.resetMergeds(mergedRow - 1,
                        this.plist.getDataRows() - 1, this.plist.getRealDataRows() - 1, sheet);
            }

            this.plist.resetRealDataRows(0);
        }

        //System.out.println("=============================" + this.getName() + ":end===========================\n");

        if (this.plist.getPageOffset() > 0) {
            int pageCount = this.plist.isCount2Page() ? count - 1 : this.plist.getPageCount(count);
            for (int i = 0; i < pageCount; i++) {
                sheet.setRowBreak(this.plist.getPageOffset() + i * this.plist.getPageRows());
            }
        }
        return 1;
    }


    private String getValue(Object obj, String name) {
        if (obj instanceof Map) {
            return (String) ((Map) obj).get(name);
        } else {
            Method getMethod = rProps.get(name);
            if (getMethod != null) {
                return (String) invoke(getMethod, obj);
            }
        }
        return "";
    }

    public void processVars(Object obj, HashMap<String, POIProcesser> pMaps, int insidePictureCount) {
        String cal;


        if (obj == null) return;
        int num = 0;
        BufferedImage tag = null;
        for (POIVar c : vars) {
            StringBuilder b = new StringBuilder();
            String[] strs = c.getStrings();
            ArrayString[] vars = c.getVariables();


            if (c.getStyle() == POIVar.TYPE_PICTURE || c.getStyle() == POIVar.TYPE_PICTURE1) {
                try {
                    String name = vars[0].getStrs()[0];
                    boolean withS = false;
                    File f;
                    if (name.startsWith("!")) {
                        name = name.substring(1);

                        name = getValue(obj, name);

                        if (name == null || name.equals("")) continue;
                        f = new File(GlobalUtil.getImageDirectory(), name);
                        withS = true;
                    } else if (name.startsWith("#")) {
                        name = name.substring(1);

                        name = getValue(obj, name);

                        if (name == null || name.equals("")) continue;
                        createBarcode(c, name);
                        continue;
                    } else if (name.startsWith("&")) {
                        name = name.substring(1);
                        if (name == null || name.equals("")) continue;
                        f = new File(templateDir, name);
                        withS = true;
                    } else {

                        name = getValue(obj, name);

                        if (num++ >= 100) {
                            num = 0;
                            System.gc();
                        }

                        if (name == null || name.equals("")) continue;
                        f = new File(GlobalUtil.getImageDirectory(), name);
                    }
                    //System.out.println(f.getAbsoluteFile());
                    if (!f.exists()) continue;

                    String extension = FilenameUtils.getExtension(f.getName());
                    ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();

                    BufferedImage bufferImg = ImageIO.read(f);

                    if ("png".equalsIgnoreCase(extension) || "jpeg".equalsIgnoreCase(extension) || "jpg".equalsIgnoreCase(extension)) {
                        tag = new BufferedImage(bufferImg.getWidth(), bufferImg.getHeight(), BufferedImage.TYPE_INT_BGR);
                        Graphics g = tag.getGraphics();
                        g.drawImage(bufferImg, 0, 0, null);
                        g.dispose();
                        bufferImg = tag;
                    }

                    ImageIO.write(bufferImg, extension, byteArrayOut);
                    //System.out.println(name);
                    createPicture(c, byteArrayOut.toByteArray(), bufferImg.getWidth(),
                            bufferImg.getHeight(), withS, 0, insidePictureCount);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                continue;
            }

            String f, s, fun;
            String[] dvars;
            if (c.getStyle() == POIVar.TYPE_STYLE) {
                obj = c.getObj();
            }
            int style = c.getStyle();
            for (int i = 0, ls = vars.length; i < ls; i++) {
                HashMap<String, Method> ps = rProps;
                Object oo = obj;
                //System.out.println(oo);
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
                            if (f.startsWith(":")) {
                                s = f.substring(1);
                                b.append(s).append(GlobalUtil.parseInt(invoke(ps.get("start"), oo), 0)
                                        + GlobalUtil.parseInt(cal, 0)).append(":");
                                b.append(s).append(GlobalUtil.parseInt(invoke(ps.get("end"), oo), 0)
                                        + GlobalUtil.parseInt(cal, 0));
                                continue;
                            }
                            if (f.startsWith("#")) {
                                if (f.length() == 1) f = ".SUM";
                                b.append(f.substring(1).toUpperCase()).append("(");
                                s = CellReference.convertNumToColString(c.getLastTargetCell().getColumnIndex());
                                b.append(s).append(GlobalUtil.parseInt(invoke(ps.get("start"), oo), 0)
                                        + GlobalUtil.parseInt(cal, 0)).append(":");
                                b.append(s).append(GlobalUtil.parseInt(invoke(ps.get("end"), oo), 0)
                                        + GlobalUtil.parseInt(cal, 0));
                                b.append(")");
                                continue;
                            }
                            if (f.startsWith("$") || f.startsWith("&")) {
                                s = f.length() > 1 ? f.substring(1) : CellReference.convertNumToColString(c.getLastTargetCell().getColumnIndex());

                                String rs = (String) invoke(f.startsWith("$") ? ps.get("endRows") : ps.get("beginRows"), oo);
                                String[] rss = rs.split(",");
                                StringBuilder rx = new StringBuilder();
                                for (String x : rss) {
                                    rx.append(s).append(GlobalUtil.parseInt(x, 0)
                                            + GlobalUtil.parseInt(cal, 0)).append(",");
                                }
                                if (rx.length() > 0) {
                                    rx.setLength(rx.length() - 1);
                                }
                                b.append(rx.toString());

                                continue;
                            }
                            b.append(GlobalUtil.parseInt(invoke(ps.get(f), oo), 0)
                                    + GlobalUtil.parseInt(cal, 0));
                            continue;
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
                        Object vo = ((Map) obj).get(f);
                        if (vo != null) {
                            if (vo instanceof Long) {
                                v = ((Long) vo).intValue();
                            } else if (vo instanceof Integer) {
                                v = (Integer) vo;
                            } else {
                                v = GlobalUtil.parseInt(vo, 0);
                            }
                        }

                        b.append(fas[v + GlobalUtil.parseInt(cal, 0)]);
                        continue;
                    case '$':
                        f = f.substring(1);

                        if ((idx = f.lastIndexOf(".")) >= 0) {
                            oo = pMaps.get(f.substring(0, idx)).getCurrObject();
                            f = f.substring(idx + 1);
                        } else {
                            oo = pMaps.get("root").getCurrObject();
                            //oo = obj;
                        }
                        break;
                    case ':':
                        f = f.substring(1);
                        idx = f.indexOf("+");
                        if (idx > 0) {
                            cal = f.substring(idx + 1).trim();
                            f = f.substring(0, idx);
                        }
                        idx = f.indexOf(":");
                        s = "";
                        if (idx > 0) {
                            s = f.substring(idx + 1).trim();
                            f = f.substring(0, idx);
                        }
                        //idx = s.indexOf("#");
                        fun = "SUM";
                        /*if (idx > 0) {
                            fun = s.substring(idx + 1).trim().toUpperCase();
                            f = f.substring(0, idx);
                        }*/

                        oo = pMaps.get(f);
                        try {
                            ps = getBeanData(oo.getClass()).readableProps;
                        } catch (IntrospectionException e) {
                            e.printStackTrace();
                            break;
                        }
                        b.append(fun).append("(");
                        s = s.equals("") ? CellReference.convertNumToColString(c.getLastTargetCell().getColumnIndex()) : s;
                        b.append(s).append(GlobalUtil.parseInt(invoke(ps.get("start"), oo), 0)
                                + GlobalUtil.parseInt(cal, 0)).append(":");
                        b.append(s).append(GlobalUtil.parseInt(invoke(ps.get("end"), oo), 0)
                                + GlobalUtil.parseInt(cal, 0));
                        b.append(")");

                        continue;
                    case '!':
                        if (f.length() == 1) {
                            b.append(c.getLastTargetCell().getRowIndex() + 1);
                            continue;
                        } else {

                            idx = f.indexOf("+");
                            if (idx > 0) {
                                cal = f.substring(idx + 1).trim();
                                f = f.substring(0, idx);
                            }
                            if (f.length() == 1) {
                                b.append(c.getLastTargetCell().getRowIndex() + 1 + GlobalUtil.parseInt(cal, 0));
                                continue;
                            } else {
                                f = f.substring(1);
                                oo = c;
                                try {
                                    ps = getBeanData(oo.getClass()).readableProps;
                                } catch (IntrospectionException e) {
                                    e.printStackTrace();
                                    break;
                                }
                            }
                        }
                        break;
                    case '?':
                        if (f.length() == 1) {
                            b.append(CellReference.convertNumToColString(c.getLastTargetCell().getColumnIndex()));
                            continue;
                        } else {

                            idx = f.indexOf("+");
                            if (idx > 0) {
                                cal = f.substring(idx + 1).trim();
                                f = f.substring(0, idx);
                            }
                            b.append(CellReference.convertNumToColString(c.getLastTargetCell().getColumnIndex() + GlobalUtil.parseInt(cal, 0)));
                        }
                        break;
                }

                if (oo instanceof Map) {
                    String[] fs = f.split("\\.");
                    Object r = ((Map) oo).get(fs[0]);
                    try {
                        style = re(r, dvars, b, style, pMaps, fs);
                    } catch (Exception e) {
                        System.out.println("value erroe!!!!:" + fs[0]);
                    }

                } else {
                    String[] fs = f.split("\\.");
                    Method getMethod = ps.get(fs[0]);
                    if (getMethod != null) {
                        Object r = invoke(getMethod, oo);
                        try {
                            style = re(r, dvars, b, style, pMaps, fs);
                        } catch (Exception e) {
                            System.out.println("value erroe!!!!:" + fs[0]);
                        }
                    }
                }
            }
            if (strs.length > vars.length) {
                b.append(strs[strs.length - 1]);
            }
            switch (style) {
                case POIVar.TYPE_FORMULA:
                    //当公式内的单元格大于30时会报错，所以拆分为多个
                    String[] split = b.toString().split(",");
                    if (split.length > 30) {
                        String[] fs = b.toString().split("\\(");
                        split = fs[1].split("\\)")[0].split(",");
                        String fm = "";
                        String cf = "";
                        for (int i = 0; i < split.length; i++) {
                            if (((i + 1) % 30) != 0) {
                                cf = (cf.isEmpty() ? "" : cf + ",") + split[i];
                            } else {
                                fm = (fm.isEmpty() ? "" : fm + "+") + fs[0] + "(" + cf + ")";
                                cf = "";
                            }
                        }
                        c.getLastTargetCell().setCellFormula(fm);
                    } else {
                        c.getLastTargetCell().setCellFormula(b.toString());
                    }
                    //System.out.println(b.toString());
                    break;
                case POIVar.TYPE_EVAL:
                    Cell cell = c.getLastTargetCell();
                    cell.setCellFormula(b.toString());

                    FormulaEvaluator evaluator = creationHelper.createFormulaEvaluator();
                    evaluator.evaluateInCell(cell);
                    //cell.setCellType(valueType);
                    break;
                case POIVar.TYPE_NUMERIC:
                    if (b.toString().equals("")) b.append("0");
                    if (b.toString().matches("^[-0-9]*$")) {
                        c.getLastTargetCell().setCellValue(Integer.parseInt(b.toString()));
                    } else if (b.toString().matches("^[-0-9.E]*$")) {
                        c.getLastTargetCell().setCellValue(Double.parseDouble(b.toString()));
                    }

                    break;
                case POIVar.TYPE_ARRAY:

                    break;
                default:
                    c.getLastTargetCell().setCellValue(creationHelper.createRichTextString(b.toString()));
                    break;
            }
        }
    }


    private int re(Object r, String[] dvars, StringBuilder b, int style, HashMap<String, POIProcesser> pMaps, String[] fs) {
        if (fs.length > 1) {
            if (r instanceof Map) {
                r = ((Map) r).get(fs[1]);
            } else {
                try {
                    HashMap<String, Method> ps = getBeanData(r.getClass()).readableProps;
                    Method getMethod = ps.get(fs[1]);
                    r = invoke(getMethod, r);
                } catch (IntrospectionException e) {
                    e.printStackTrace();
                }
            }

        }

        boolean a = false;
        if (dvars.length > 1) {
            if (dvars[1].startsWith("#")) {
                b.append(r == null ? GlobalUtil.parseDouble(dvars[1].substring(1), 0) : r);
            } else if (dvars[1].startsWith("$")) {
                String key = r instanceof Number ? String.valueOf(((Number) r).intValue() +
                        (dvars.length > 3 ? GlobalUtil.parseInt(dvars[3], 0) : 0))
                        : (String) r;
                if (dvars.length > 2) {
                    key = dvars[2] + key;
                }
                if (dvars[1].length() == 1) {
                    r = plist.get(key);
                } else {
                    POIProcesser pp = pMaps.get(dvars[1].substring(1));
                    if (pp != null)
                        r = pp.plist.get(key);
                }
                if (r == null) {
                    r = "";
                }
            } else if (r instanceof Number) {
                if (dvars.length > 2 && ((Number) r).doubleValue() == GlobalUtil.parseDouble(dvars[2], 0)) {
                    style = 0;
                    b.append("");
                } else {
                    int sc = GlobalUtil.parseInt(dvars[1], 0);
                    if (sc == 0) {
                        b.append(((Number) r).intValue());
                    } else {
                        b.append(GlobalUtil.round(((Number) r).doubleValue(), GlobalUtil.parseInt(dvars[1], 0)));
                    }

                }

                a = true;
            } else {
                if (dvars.length > 2 && r == null) {
                    style = 0;
                    b.append("");
                }
            }
        }
        if (!a && r != null)
            b.append(r);
        return style;
    }

    private Object invoke(Method getMethod, Object oo) {
        try {
            return getMethod.invoke(oo);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createBarcode(POIVar c, String name) {

        String[] fun = c.getStrings();
        /*if (fun.length >= 2) {
            code.setBarHeight(com.microsoul.weapi.util.WeosoGlobals.parseDouble(fun[1], 0));
        } else {
            code.setBarHeight(10);
        }*/

        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        final int dpi = 150;

        BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                byteArrayOut, "image/png",
                dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);

        Code128Bean bean;
        if (fun.length >= 4) {
            String codeType = fun[3];
            if (codeType.equalsIgnoreCase("ean13")) {
                bean = new EAN128Bean();
            } else if (codeType.equalsIgnoreCase("code128")) {
                bean = new Code128Bean();
            } else {
                return;
            }

            if (fun.length >= 3 && GlobalUtil.parseInt(fun[2], 0) == 0) {
                bean.setMsgPosition(HumanReadablePlacement.HRP_NONE);
            }
            // module宽度
            final double moduleWidth = UnitConv.in2mm(1.0f / dpi);
            bean.setModuleWidth(moduleWidth);
            bean.doQuietZone(false);

            bean.generateBarcode(canvas, name);
            try {
                canvas.finish();
                BufferedImage localBufferedImage = canvas.getBufferedImage();
                createPicture(c, byteArrayOut.toByteArray(), localBufferedImage.getWidth(),
                        localBufferedImage.getHeight(), true, 3, 0);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    private void createPicture(POIVar c, byte[] bytes, int width, int height,
                               boolean withS, int offset,
                               int insidePictureCount) {
        if (bytes == null) return;
        //int r = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
        int col = c.getLastTargetCell().getColumnIndex();
        int row = c.getLastTargetCell().getRowIndex();
        String[] fun = c.getStrings();
        //System.out.println("s:" + col + "," + row);
        if (withS) {
            int type = fun.length >= 4 + offset ? GlobalUtil.parseInt(fun[3 + offset], 0) : 0;
            //type= 0,
            int w = fun.length >= 5 + offset ? GlobalUtil.parseInt(fun[4 + offset], width) : width;
            int h = fun.length >= 6 + offset ? GlobalUtil.parseInt(fun[5 + offset], height) : height;
            //System.out.println(type);
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

            //System.out.println("e:" + w + "," + h);
            poiHelper.createPicture(col, row,
                    fun.length >= 2 + offset ? GlobalUtil.parseInt(fun[1 + offset], 0) : 0,
                    fun.length >= 3 + offset ? GlobalUtil.parseInt(fun[2 + offset], 0) : 0,
                    w,
                    h,
                    bytes,
                    insidePictureCount
            );
        } else {
            ClientAnchor anchor = this.creationHelper.createClientAnchor();
            anchor.setRow1(row);
            anchor.setRow2(row + (fun.length >= 3 ? GlobalUtil.parseInt(fun[2], 0) : 0));
            anchor.setCol1(col);
            anchor.setCol2(col + (fun.length >= 2 ? GlobalUtil.parseInt(fun[1], 0) : 0));
            anchor.setDx1(fun.length >= 4 ? GlobalUtil.parseInt(fun[3], 0) : 0);
            anchor.setDy1(fun.length >= 5 ? GlobalUtil.parseInt(fun[4], 0) : 0);
            anchor.setDx2(fun.length >= 6 ? GlobalUtil.parseInt(fun[5], 0) : 0);
            anchor.setDy2(fun.length >= 7 ? GlobalUtil.parseInt(fun[6], 0) : 0);


            poiHelper.createPicture(anchor, width, height, bytes, insidePictureCount);
        }

    }


    /*private byte[] loadPicture(String filePath) {
        try {
            if (filePath == null) return null;
            File f = new File(com.microsoul.weapi.util.WeosoGlobals.getDirectory(), filePath);
            if (!f.exists())
                return null;
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
            java.awt.image.BufferedImage bufferImg = ImageIO.read(f);
            System.out.println("Height:" + bufferImg.getHeight() + "Width:" + bufferImg.getWidth());
            ImageIO.write(bufferImg, "jpg", byteArrayOut);
            return byteArrayOut.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/


    public void addVar(POIVar c) {
        this.vars.add(c);
    }

    public String getName() {
        return name;
    }

    /*---------public------------------*/

    private int index = 0;
    private int startRow = 0;

    public int getIndex() {
        return index;
    }

    public int getNo() {
        return index + 1;
    }


    public int getCurr() {
        return startRow;
    }

    public int getCount() {
        if (aobj == null) return 0;
        return aobj.size();
    }

    public int getStart() {
        return this.plist.getRowStart() + 1;
    }

    public int getEnd() {
        return this.getStart() + this.plist.getDataRows() * getCount() + this.plist.getOffsets() - 1;
    }

    public String getEndRows() {
        return endRows.toString();
    }

    public String getBeginRows() {
        return beginRows.toString();
    }

    @Override
    public String toString() {
        return "POIProcesser{" +
                "name='" + name + '\'' +
                '}';
    }
}
