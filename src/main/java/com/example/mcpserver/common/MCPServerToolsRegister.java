package com.example.mcpserver.common;


import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MCPServerToolsRegister {

//  @Bean
//  ToolCallbackProvider registerTools(PDFService pdfService, MarkdownService markdownService) {
//    System.out.println(LocalDateTime.now() + "注册MCP服务列表 Tools:");
//    System.out.println("                                         " + pdfService.getClass().getSimpleName());
//    System.out.println("                                         " + markdownService.getClass().getSimpleName());
//    return MethodToolCallbackProvider.builder().toolObjects(pdfService, markdownService).build();
//  }



  @Bean
  public ToolCallbackProvider toolCallbackProvider(ListableBeanFactory beanFactory) {
    List<Object> toolObjects = beanFactory.getBeansOfType(Object.class)
        .values().stream()
        .filter(bean -> hasToolAnnotatedMethod(bean)
        ).collect(Collectors.toList());

    return MethodToolCallbackProvider.builder().toolObjects(toolObjects.toArray()).build();
  }


  private boolean hasToolAnnotatedMethod(Object bean) {
    Class<?> clazz = AopProxyUtils.ultimateTargetClass(bean);
    for (Method method : clazz.getDeclaredMethods()) {
      if (method.isAnnotationPresent(Tool.class)) {
        return true;
      }
    }
    return false;
  }


}
