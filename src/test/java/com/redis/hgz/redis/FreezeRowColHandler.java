package com.redis.hgz.redis;

import org.apache.poi.ss.usermodel.Sheet;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;

public class FreezeRowColHandler implements SheetWriteHandler {
    /**
     * cellNum:表示要冻结的列数； rowNum:表示要冻结的行数； firstCellNum:表示被固定列右边第一列的列号； firstRollNum :表示被固定行下边第一列的行号;
     */
    private int colSplit = 0, rowSplit = 1, firstCellNum = 0, firstRollNum = 1;

    public FreezeRowColHandler(int colSplit, int rowSplit, int firstCellNum, int firstRollNum) {
        this.colSplit = colSplit;
        this.rowSplit = rowSplit;
        this.firstCellNum = firstCellNum;
        this.firstRollNum = firstRollNum;
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        Sheet sheet = writeSheetHolder.getSheet();
        sheet.createFreezePane(colSplit, rowSplit, firstCellNum, firstRollNum);
    }
}
