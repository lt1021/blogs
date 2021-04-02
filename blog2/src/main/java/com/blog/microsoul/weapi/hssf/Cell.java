package com.blog.microsoul.weapi.hssf;

import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 * Created by openxtiger.org
 * User: smTiger
 * Date: 2009-1-7
 * Time: 11:19:28
 */
public class Cell {
    public static final int HSSF_PICTURE = 1;
    public static final int HSSF_PICTURE1 = -1;
    public static final int HSSF_FORMULA = 2;
    public static final int HSSF_EVAL = 3;
    public static final int HSSF_NUMERIC = 4;
    public static final int HSSF_STYLE = 5;
    public static final int HSSF_ARRAY = 6;
    public static final int HSSF_SUM = 7;
    private String[] strings;
    private ArrayString[] variables;
    private HSSFCell targetCell;
    private HSSFCell lastTargetCell;
    private int style;
    private int topMargin;
    private int leftMargin;
    private Object obj;

    public void setStrings(String[] strs) {
        this.strings = strs;
    }

    public void setVariables(ArrayString[] vars) {
        this.variables = vars;
    }


    public void setTargetCell(HSSFCell targetCell) {
        this.targetCell = targetCell;
        this.lastTargetCell = targetCell;
    }

    public void setLastTargetCell(HSSFCell lastTargetCell) {
        this.lastTargetCell = lastTargetCell;
    }

    public String[] getStrings() {
        return strings;
    }

    public ArrayString[] getVariables() {
        return variables;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public int getStyle() {
        return style;
    }

    public HSSFCell getTargetCell() {
        return targetCell;
    }

    public HSSFCell getLastTargetCell() {
        return lastTargetCell;
    }

    public int getTopMargin() {
        return topMargin;
    }

    public void setTopMargin(int topMargin) {
        this.topMargin = topMargin;
    }

    public int getLeftMargin() {
        return leftMargin;
    }

    public void setLeftMargin(int leftMargin) {
        this.leftMargin = leftMargin;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
