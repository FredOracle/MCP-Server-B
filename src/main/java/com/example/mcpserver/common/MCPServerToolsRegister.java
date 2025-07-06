package com.example.mcpserver.common;

import com.example.mcpserver.service.MarkdownService;
import com.example.mcpserver.service.PDFService;
import java.time.LocalDateTime;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MCPServerToolsRegister {
  @Bean
  ToolCallbackProvider registerTools(PDFService pdfService, MarkdownService markdownService) {
    System.out.println(LocalDateTime.now() + "注册MCP服务列表 Tools:" );
    System.out.println( "                                         " + pdfService.getClass().getSimpleName());
    System.out.println( "                                         " + markdownService.getClass().getSimpleName());
    return MethodToolCallbackProvider.builder().toolObjects(pdfService, markdownService).build();
  }


}
