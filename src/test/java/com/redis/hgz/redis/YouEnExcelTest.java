package com.redis.hgz.redis;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Test;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson2.JSON;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class YouEnExcelTest {
    private static final String BASE_PATH = "D:\\youen";

    private static final String SEPARATOR = "\\";

    private static final String READ_FILE_NAME = "datas.xlsx";

    private static final String SUFFIX = ".xlsx";

    @Test
    public void synchronousRead() {
        String fileName = BASE_PATH + SEPARATOR + READ_FILE_NAME;

        // 这里 也可以不指定class，返回一个list，然后读取第一个sheet 同步读取会自动finish
        List<Map<Integer, String>> allReadData = EasyExcel.read(fileName).sheet().headRowNumber(2).doReadSync();
        if (CollectionUtils.isEmpty(allReadData)) {
            return;
        }
        Map<Integer, String> title = allReadData.get(0);
        allReadData.remove(0);
        List<String> dates = allReadData.stream().map(item -> item.get(0).substring(0, item.get(0).lastIndexOf("/")))
            .distinct().collect(Collectors.toList());
        List<Map<Integer, String>> allReadDataCopy = copy(allReadData);
        writeFile(allReadDataCopy, title, dates.get(0).replaceAll("/", "年") + "月");
        List<String> dateTos = new ArrayList<>();

        for (int i = 0; i < dates.size() - 1; i++) {
            String date = dates.get(i);
            dateTos.add(date);
            allReadDataCopy = copy(allReadData);
            for (String dateTo : dateTos) {
                allReadDataCopy.removeIf(integerStringMap -> integerStringMap.get(0).startsWith(dateTo));
            }
            writeFile(allReadDataCopy, title, dates.get(i + 1).replaceAll("/", "年") + "月");
        }
    }

    private void writeFile(List<Map<Integer, String>> allReadData, Map<Integer, String> title, String fileName) {
        List<Object> keys = allReadData.stream().map(item -> item.get(17)).distinct().collect(Collectors.toList());
        Map<Object, List<Map<Integer, String>>> allDataMap =
            allReadData.stream().collect(Collectors.groupingBy(item -> item.get(17)));
        Set<Map.Entry<Object, List<Map<Integer, String>>>> entry = allDataMap.entrySet();
        List<Object> removeKey = new ArrayList<>();
        for (Map.Entry<Object, List<Map<Integer, String>>> item : entry) {
            if (0 == item.getValue().size() % 2) {
                removeKey.add(item.getKey());
            }
        }
        removeKey.forEach(allDataMap::remove);
        List<Map<Integer, String>> finalData = new ArrayList<>();
        List<Map<Integer, String>> countData = new ArrayList<>();
        Map<Integer, String> map;
        for (Object key : keys) {
            List<Map<Integer, String>> matchedItems = allDataMap.get(key);
            if (CollectionUtils.isEmpty(matchedItems)) {
                continue;
            }

            String date = matchedItems.get(0).get(0);
            Map<Object, List<Map<Integer, String>>> matchedItemMap =
                matchedItems.stream().collect(Collectors.groupingBy(item -> item.get(0)));
            List<Map<Integer, String>> items = matchedItemMap.get(date);
            if (CollectionUtils.isEmpty(items)) {
                continue;
            }
            Map<Integer, String> matchedItem = matchedItems.get(0);
            if (1 < items.size()) {
                items.sort((first, second) -> Integer.parseInt(second.get(14)) - (Integer.parseInt(first.get(14))));
                matchedItem = items.get(0);
            }

            finalData.add(matchedItem);
        }

        List<Object> commodityIds =
            finalData.stream().map(item -> item.get(10)).distinct().collect(Collectors.toList());
        Map<Object, List<Map<Integer, String>>> commodityMap =
            finalData.stream().collect(Collectors.groupingBy(item -> item.get(10)));
        for (Object commodityId : commodityIds) {
            List<Map<Integer, String>> matchedItem = commodityMap.get(commodityId);
            map = new HashMap<>();
            map.put(0, matchedItem.get(0).get(10));
            map.put(1, matchedItem.get(0).get(11));
            map.put(2, matchedItem.get(0).get(12));
            map.put(3,
                matchedItem.stream().map(item -> Integer.parseInt(item.get(14))).reduce(Integer::sum).get().toString());
            countData.add(map);
        }
        finalData.add(0, title);

        map = new HashMap<>();
        map.put(0, "货品编号");
        map.put(1, "货品名称");
        map.put(2, "规格");
        map.put(3, "数量");
        countData.add(0, map);
        List<List<String>> writeData = new ArrayList<>();
        String writeFileName = BASE_PATH + SEPARATOR + fileName + SUFFIX;
        File file = new File(writeFileName);
        ExcelWriter excelWriter = EasyExcel.write(file).build();
        for (Map<Integer, String> outerItem : finalData) {
            log.info("读取到数据:{}", JSON.toJSONString(outerItem));

            Set<Map.Entry<Integer, String>> entrySet = outerItem.entrySet();
            List<String> row = new ArrayList<>();
            entrySet.forEach(item -> row.add(item.getValue()));
            writeData.add(row);
        }
        WriteSheet writeSheet1 =
            EasyExcel.writerSheet().sheetName("明细").registerWriteHandler(new FreezeRowColHandler(0, 1, 0, 1)).build();
        excelWriter.write(writeData, writeSheet1);

        writeData.clear();
        for (Map<Integer, String> outerItem : countData) {
            log.info("读取到数据:{}", JSON.toJSONString(outerItem));

            Set<Map.Entry<Integer, String>> entrySet = outerItem.entrySet();
            List<String> row = new ArrayList<>();
            entrySet.forEach(item -> row.add(item.getValue()));
            writeData.add(row);
        }
        WriteSheet writeSheet2 = EasyExcel.writerSheet().sheetName("汇总").build();
        excelWriter.write(writeData, writeSheet2);
        excelWriter.finish();
    }

    private List<Map<Integer, String>> copy(List<Map<Integer, String>> source) {
        List<Map<Integer, String>> distinct = new ArrayList<>();
        Map<Integer, String> temp;
        for (Map<Integer, String> outerItem : source) {
            temp = new HashMap<>(outerItem.size());
            Set<Map.Entry<Integer, String>> entrySet = outerItem.entrySet();
            for (Map.Entry<Integer, String> innerItem : entrySet) {
                temp.put(innerItem.getKey(), innerItem.getValue());
            }
            distinct.add(temp);
        }
        return distinct;
    }
}
