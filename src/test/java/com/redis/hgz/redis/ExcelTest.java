package com.redis.hgz.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Test;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.fastjson2.JSON;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExcelTest {
    private static final String BASE_PATH = "D:\\excel";

    private static final String SEPARATOR = "\\";

    private static final String READ_FILE_NAME = "datas.xlsx";
    // private static final String READ_FILE_NAME = "company.xlsx";

    private static final String SUFFIX = ".xlsx";

    @Test
    public void synchronousRead() {
        String fileName = BASE_PATH + SEPARATOR + READ_FILE_NAME;

        // 这里 也可以不指定class，返回一个list，然后读取第一个sheet 同步读取会自动finish
        CustomAnalysisEventListener listener = new CustomAnalysisEventListener(0);
        List<Map<Integer, String>> allReadData =
            EasyExcel.read(fileName, listener).extraRead(CellExtraTypeEnum.MERGE).sheet().headRowNumber(0).doReadSync();
        if (CollectionUtils.isEmpty(allReadData)) {
            return;
        }
        mergeExcelData(allReadData, listener.cellExtraList, 0);
        int length = allReadData.get(0).size();
        log.info("every row length:{}", length);
        List<Map<Integer, String>> reduceTitleData = allReadData.stream()
            .filter(item -> !Objects.equals(allReadData.get(0).get(0), item.get(0))).collect(Collectors.toList());
        Map<String, List<Map<Integer, String>>> reduceTitleDataMap =
            reduceTitleData.stream().collect(Collectors.groupingBy(item -> item.get(0)));

        for (Map<Integer, String> outerItem : reduceTitleData) {
            List<Map<Integer, String>> companyData = reduceTitleDataMap.get(outerItem.get(0));
            if (CollectionUtils.isEmpty(companyData)) {
                continue;
            }
            String writeFileName = BASE_PATH + SEPARATOR + companyData.get(0).get(0) + SUFFIX;
            log.info("读取到数据:{}", JSON.toJSONString(outerItem));
            List<List<String>> writeData = new ArrayList<>();
            for (Map<Integer, String> innerItem : companyData) {
                Set<Map.Entry<Integer, String>> entrySet = innerItem.entrySet();
                List<String> row = new ArrayList<>();
                entrySet.forEach(item -> row.add(item.getValue()));
                writeData.add(row);
            }
            EasyExcel.write(writeFileName).sheet(companyData.get(0).get(0)).doWrite(writeData);
        }
    }

    private void mergeExcelData(List<Map<Integer, String>> excelData, List<CellExtra> cellExtraList, int headRowNum) {
        cellExtraList.forEach(cellExtra -> {
            int firstRowIndex = cellExtra.getFirstRowIndex() - headRowNum;
            int lastRowIndex = cellExtra.getLastRowIndex() - headRowNum;
            int firstColumnIndex = cellExtra.getFirstColumnIndex();
            int lastColumnIndex = cellExtra.getLastColumnIndex();

            // 获取初始值
            Object initValue = getInitValueFromList(firstRowIndex, firstColumnIndex, excelData);

            // 设置值
            for (int i = firstRowIndex; i <= lastRowIndex; i++) {
                for (int j = firstColumnIndex; j <= lastColumnIndex; j++) {
                    setInitValueToList(initValue, i, j, excelData);
                }
            }
        });
    }

    private void setInitValueToList(Object fieldValue, Integer rowIndex, Integer columnIndex,
        List<Map<Integer, String>> data) {
        Map<Integer, String> object = data.get(rowIndex);
        object.put(columnIndex, Objects.toString(fieldValue));
    }

    private Object getInitValueFromList(Integer firstRowIndex, Integer firstColumnIndex,
        List<Map<Integer, String>> excelData) {
        Map<Integer, String> object = excelData.get(firstRowIndex);
        return object.get(firstColumnIndex);
    }

    class CustomAnalysisEventListener extends AnalysisEventListener {
        private int headRowNum;

        public CustomAnalysisEventListener(int headRowNum) {
            this.headRowNum = headRowNum;
        }

        // private List<Object> list = new ArrayList<>();
        private List<CellExtra> cellExtraList = new ArrayList<>();

        @Override
        public void invoke(Object excelData, AnalysisContext analysisContext) {
            // log.info(" data -> {}", excelData);
            // list.add(excelData);
        }

        @Override
        public void extra(CellExtra extra, AnalysisContext context) {
            CellExtraTypeEnum type = extra.getType();
            if (type == CellExtraTypeEnum.MERGE) {
                if (extra.getRowIndex() >= headRowNum) {
                    cellExtraList.add(extra);
                }
            }
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {}

        // public List<Object> getList() {
        // return list;
        // }

        public List<CellExtra> getCellExtraList() {
            return cellExtraList;
        }
    }
}
