package com.example.mcpserver.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

@Service
public class ExcelService {

    @Tool(name = "excel-util-service.parse-excel", description = "Excel工具类")
    public List<Map<String, String>> parseExcel(String excel) {
        System.out.println(excel);
        Map<String, String> record = new HashMap<>();
        List<Map<String, String>> records = new ArrayList<>();
        record.put("name", "张三");
        record.put("age", "18");
        record.put("sex", "男");
        records.add(record);
        record = new HashMap<>();
        record.put("name", "李四");
        record.put("age", "19");
        record.put("sex", "女");
        records.add(record);

        System.out.println("解析Excel成功");
        return records;
    }

}
