package com.blog.microsoul.weapi.hssf;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * Created by openxtiger.org
 * User: smTiger
 * Date: 2009-1-7
 * Time: 23:33:07
 */
public class Column {
    private HSSFCell startCell;
    private HSSFCell endCell;
    private int titleRows;
    private int dataRows;
    private HSSFSheet sheet;
    private boolean repeatTitle;
    private int count = 0;
    private int listTop;
    private int cols = 1;
    private int listCols = 0;
    private int pageOffset = 0;
    private int pageRows = 0;

    public HSSFCell getStartCell() {
        return startCell;
    }

    public void setStartCell(HSSFCell startCell) {
        this.startCell = startCell;
    }

    public HSSFCell getEndCell() {
        return endCell;
    }

    public void setEndCell(HSSFCell endCell) {
        this.endCell = endCell;
        this.count = endCell.getRowIndex() - this.startCell.getRowIndex() + 1;
    }

    public int getTitleRows() {
        return titleRows;
    }

    public void setTitleRows(int titleRows) {
        this.titleRows = titleRows;
    }

    public int getDataRows() {
        return dataRows;
    }

    public void setDataRows(int dataRows) {
        this.dataRows = dataRows;
    }

    public void setSheet(HSSFSheet sheet) {
        this.sheet = sheet;
    }

    public HSSFSheet getSheet() {
        return sheet;
    }

    public void setRepeatTitle(boolean repeatTitle) {
        this.repeatTitle = repeatTitle;
    }

    public boolean isRepeatTitle() {
        return repeatTitle;
    }

    public int getCount() {
        return count;
    }

    public void setListTop(int listTop) {
        this.listTop = listTop;
    }

    public int getListTop() {
        return listTop;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int getCols() {
        return cols;
    }

    public void setListCols(int listCols) {
        this.listCols = listCols;
    }

    public int getListCols() {
        return listCols;
    }

    public int getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(int pageOffset) {
        this.pageOffset = pageOffset;
    }

    public int getPageRows() {
        return pageRows;
    }

    public void setPageRows(int pageLine) {
        this.pageRows = pageLine * ((repeatTitle ? this.titleRows : 0) + dataRows);
    }

    public int getPageCount(int count) {
        return count == 0 ? 1 : count * ((repeatTitle ? this.titleRows : 0) + dataRows) / pageRows;
    }
}
