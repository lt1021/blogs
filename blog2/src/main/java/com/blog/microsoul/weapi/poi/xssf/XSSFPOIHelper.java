package com.blog.microsoul.weapi.poi.xssf;

import com.blog.microsoul.weapi.poi.POIHelper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;



/**
 * @author xtiger (xtiger@microsoul.com) 2015-04-22 19:37
 */
public class XSSFPOIHelper implements POIHelper {
    private Workbook workbook;
    private Sheet sheet;

    public XSSFPOIHelper(Workbook workbook, Sheet sheet) {
        this.workbook = workbook;
        this.sheet = sheet;
    }

    public void createPicture(ClientAnchor anchor, int width, int height, byte[] pictureData) {
        int r = workbook.addPicture(pictureData, HSSFWorkbook.PICTURE_TYPE_JPEG);

    }

    @Override
    public void createPicture(ClientAnchor anchor, int width, int height, byte[] pictureData, int insidePictureCount) {

    }

    @Override
    public void createPicture(int col, int row, int px1, int py1, int width, int height, byte[] pictureData, int insidePictureCount) {

    }

    public void createPicture(int col, int row, int px1, int py1, int width, int height, byte[] pictureData) {

    }

    public void copyCell(int rowStart, int colStart, int colEnd, int dataRows, int offset, int count) {

    }

    private void parseFormula(Cell sourceCell, Cell targetCell, int startRow, int endRow) {


    }
}
