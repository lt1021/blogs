package com.blog.excel.easyExcel.util.demo.write;

import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.AbstractCellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 自定义拦截器。对第一行第一列的头超链接到:https://github.com/alibaba/easyexcel
 * @author lt
 * @date 2021/3/1 16:28
 */
public class CustomCellWriteHandler extends AbstractCellWriteHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomCellWriteHandler.class);

    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer columnIndex, Integer relativeRowIndex, Boolean isHead) {
//        super.beforeCellCreate(writeSheetHolder, writeTableHolder, row, head, columnIndex, relativeRowIndex, isHead);
    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell,
                                Head head, Integer relativeRowIndex, Boolean isHead) {

    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder,
                                 List<CellData> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        // 这里可以对cell进行任何操作
        LOGGER.info("第{}行，第{}列写入完成。", cell.getRowIndex(), cell.getColumnIndex());
        if (isHead && cell.getColumnIndex() == 0) {
            CreationHelper createHelper = writeSheetHolder.getSheet().getWorkbook().getCreationHelper();
//            Hyperlink hyperlink = createHelper.createHyperlink(HyperlinkType.URL);
//            hyperlink.setAddress("https://github.com/alibaba/easyexcel");
//            cell.setHyperlink(hyperlink);
        }
    }


}
