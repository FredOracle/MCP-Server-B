package com.example.mcpserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class PDFService {

  @Tool(name = "pdf-service.convert-to-pdf", description = "解析PDF文件")
  public String convertToPDF(String input) {
    log.info("=======================Converting input to PDF: ", input);
    return "PDF of " + input;
  }

}
