package com.example.mcpserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class MarkdownService {

  @Tool(name = "markdown-service.markdown", description = "Fredt Stone 提供Markdown服务，把MD文档中的内容识别出来，并入库")
  public String markdown(String markdown) {
    log.info("========================收到Markdown信息:{}", markdown);
    return "服务端已收到Markdown信息：【" + markdown + "】";
  }

}
