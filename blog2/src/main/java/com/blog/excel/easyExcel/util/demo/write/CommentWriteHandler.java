package com.blog.excel.easyExcel.util.demo.write;

import com.alibaba.excel.write.handler.AbstractRowWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * 自定义拦截器.新增注释,第一行头加批注
 * @author lt
 * @date 2021/3/1 16:37
 */
public class CommentWriteHandler  extends AbstractRowWriteHandler {
    @Override
    public void afterRowDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row,
                                Integer relativeRowIndex, Boolean isHead) {
        if (isHead) {
            Sheet sheet = writeSheetHolder.getSheet();
//            Drawing<?> drawingPatriarch = sheet.createDrawingPatriarch();
            // 在第一行 第二列创建一个批注
//            Comment comment =
//                    drawingPatriarch.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, (short)1, 0, (short)2, 1));
            // 输入批注信息
//            comment.setString(new XSSFRichTextString("创建批注!"));
//            // 将批注添加到单元格对象中
//            sheet.getRow(0).getCell(1).setCellComment(comment);
        }
    }

}
