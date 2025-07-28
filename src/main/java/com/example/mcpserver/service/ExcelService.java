package com.example.mcpserver.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.execution.ToolCallResultConverter;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;

@Service
public class ExcelService implements Tool {
    @Override
    public String name() {
        return "excel-util-service";
    }

    @Override
    public String description() {
        return "Excel 工具类";
    }

    @Override
    public boolean returnDirect() {
        return false;
    }

    @Override
    public Class<? extends ToolCallResultConverter> resultConverter() {
        return null;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
