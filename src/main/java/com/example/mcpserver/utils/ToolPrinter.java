package com.example.mcpserver.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ToolPrinter {

  @Autowired
  private ToolCallbackProvider toolCallbackProvider;

  @PostConstruct
  public void printAllTools() {
    System.out.println("已注册的工具列表：");
    for (ToolCallback tool : toolCallbackProvider.getToolCallbacks()) {
      System.out.println("工具名称: " + tool.getToolDefinition().name());
      System.out.println("工具描述: " + tool.getToolDefinition().description());
      System.out.println("工具方法: " + tool.getToolDefinition().inputSchema());
      System.out.println("------------------------------------");
    }
  }
}
