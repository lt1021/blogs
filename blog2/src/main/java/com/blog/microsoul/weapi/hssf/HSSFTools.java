package com.blog.microsoul.weapi.hssf;

import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.record.formula.Ptg;
import org.apache.poi.hssf.record.formula.RefPtg;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * Created by openxtiger.org
 * User: smTiger
 * Date: 2009-1-17
 * Time: 12:59:08
 */
public class HSSFTools {

    public static int getRow(int index, int cols) {
        return (index - 1) / cols + 1;
    }

    public static int getCol(int index, int cols) {
        return index % cols == 0 ? cols : index % cols;
    }

    public static void copyMergeds(int startRow, int startCol, int listRows, int cols, int listCols, int count, HSSFSheet sheet) {
        //System.out.println(startRow + "," + listRows + "," + startCol + "," + listCols + "," + count);
        CellRangeAddress region;
        for (int i = 0, l = sheet.getNumMergedRegions(); i < l; i++) {
            region = sheet.getMergedRegion(i);
            int n = 0;

            if ((region.getFirstRow() >= startRow) &&
                    region.getLastRow() <= startRow + listRows &&
                    region.getFirstColumn() >= startCol &&
                    region.getLastColumn() <= startCol + listCols) {
                for (int j = 1; j < count; j++) {
                    int col = j % cols;
                    if (col == 0) n += listRows;
                    CellRangeAddress r = region.copy();

                    r.setFirstRow(region.getFirstRow() + n);
                    r.setLastRow(region.getLastRow() + n);
                    r.setFirstColumn(region.getFirstColumn() + col * listCols);
                    r.setLastColumn(region.getLastColumn() + col * listCols);

                    sheet.addMergedRegion(r);
                }
            }
        }
    }

    public static int copyList(int startRow, int startCol, int listRows, int cols, int listCols, int count, HSSFSheet sheet, HSSFWorkbook workbook) {
        int n = startRow;
        HSSFRow r;
        int last = 0;
        for (int i = 1; i < count; i++) {
            int col = i % cols;
            if (col == 0) n += listRows;
            for (int j = 0; j < listRows; j++) {
                r = sheet.getRow(j + startRow);
                if (r == null) continue;
                last = copyRow(r, n + j, startRow, startRow + listRows, startCol, startCol + listCols, col * listCols, sheet, workbook);
            }
        }
        return last;
    }

    public static void copyStyle(int startRow, int rows, int mrow, int drow, HSSFSheet sheet, HSSFWorkbook workbook) {
        HSSFRow r = sheet.getRow(startRow);
        int st = Math.min(mrow, drow) * rows + startRow;
        int ed = Math.max(mrow, drow) * rows + startRow;
        int stc = r.getFirstCellNum();
        int ste = r.getLastCellNum();
        HSSFRow targetRow;
        HSSFCell sourceCell, targetCell;
        for (int i = st; i < ed; i++) {
            r = sheet.getRow(i - rows);
            targetRow = sheet.getRow(i);
            if (targetRow == null)
                targetRow = sheet.createRow(i);
            for (int j = stc; j < ste; j++) {
                sourceCell = r.getCell(j);
                if (sourceCell == null) {
                    continue;
                }
                targetCell = targetRow.getCell(j);
                if (targetCell == null)
                    targetCell = targetRow.createCell(j);
                targetCell.setCellStyle(sourceCell.getCellStyle());
            }
        }
    }

    public static int copyRow(HSSFRow sourceRow, int tRowIndex, int startRow, int endRow, int startCol, int endCol, int tIndex, HSSFSheet sheet, HSSFWorkbook workbook) {
        //System.out.println(sourceRow.getRowNum() + "," + tRowIndex + "," + startCol + "," + endCol + "," + tIndex);
        HSSFCell sourceCell, targetCell;
        HSSFRow targetRow = sheet.getRow(tRowIndex);
        if (targetRow == null)
            targetRow = sheet.createRow(tRowIndex);

        targetRow.setHeight(sourceRow.getHeight());
        int cType;
        int last = 0;

        for (int j = startCol; j < endCol; j++) {
            sourceCell = sourceRow.getCell(j);
            if (sourceCell == null) {
                continue;
            }

            last++;
            targetCell = targetRow.getCell(j + tIndex);
            if (targetCell == null)
                targetCell = targetRow.createCell(j + tIndex);

            targetCell.setCellStyle(sourceCell.getCellStyle());
            sheet.setColumnWidth(j + tIndex, sheet.getColumnWidth(j));

            cType = sourceCell.getCellType();
            //targetCell.setCellType(cType);
            switch (cType) {
                case HSSFCell.CELL_TYPE_BOOLEAN:
                    targetCell.setCellValue(sourceCell.getBooleanCellValue());
                    break;
                case HSSFCell.CELL_TYPE_ERROR:
                    targetCell.setCellErrorValue(sourceCell.getErrorCellValue());
                    break;
                case HSSFCell.CELL_TYPE_FORMULA:
                    parseFormula(sourceCell, targetCell, startRow, endRow, workbook);
                    break;
                case HSSFCell.CELL_TYPE_NUMERIC:
                    targetCell.setCellValue(sourceCell.getNumericCellValue());
                    break;
                case HSSFCell.CELL_TYPE_STRING:
                    targetCell.setCellValue(sourceCell.getRichStringCellValue());
                    break;
            }
        }
        return last;
    }

    private static void parseFormula(HSSFCell sourceCell, HSSFCell targetCell, int startRow, int endRow, HSSFWorkbook workbook) {
        Ptg[] ptgs = HSSFFormulaParser.parse(sourceCell.getCellFormula(), workbook);
        boolean changed = false;
        //int row = sourceCell.getRowIndex();

        for (Ptg ptg : ptgs) {
            if (ptg instanceof RefPtg) {
                RefPtg rptg = (RefPtg) ptg;
                //System.out.println("----------------------" + rptg.getRow() + "," + rptg.toFormulaString() + "," + startRow + "," + endRow);
                if (rptg.getRow() >= startRow && rptg.getRow() <= endRow) {
                    rptg.setRow(rptg.getRow() + targetCell.getRowIndex() - sourceCell.getRowIndex());
                    changed = true;
                }
            }
        }
        if (changed) {
            targetCell.setCellFormula(HSSFFormulaParser.toFormulaString(workbook, ptgs));
        } else {
            targetCell.setCellFormula(sourceCell.getCellFormula());
        }
    }

    public static HSSFCell getCell(HSSFSheet sheet, int moveRows, HSSFCell targetCell) {
        return getCell(sheet, moveRows, targetCell.getColumnIndex(), targetCell);
    }

    public static HSSFCell getCell(HSSFSheet sheet, int moveRows, int col, HSSFCell targetCell) {
        HSSFRow row = sheet.getRow(targetCell.getRowIndex() + moveRows);
        if (row == null) {
            row = sheet.createRow(targetCell.getRowIndex() + moveRows);
        }
        HSSFCell cell = row.getCell(col);
        if (cell == null) {
            cell = row.createCell(col);
        }
        return cell;
    }
}
