package com.example.mcpserver.controller;

import com.example.mcpserver.entities.Demo;
import com.example.mcpserver.service.MCPService;
import jakarta.annotation.Resource;
import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mcp/server")
public class SystemRestController {

  @Resource
  private MCPService mcpService;


  @PostMapping(value = "/message")
  public String message(@RequestBody String message) {
    System.out.println("使用HTTP API进行调用：   " +  message);
    return mcpService.message(message);
  }

  @PostMapping(value = "/fetch")
  public Demo fetch(@PathParam("id") String id) {
    return mcpService.fetchDemo(id);
  }


  @PostMapping(value = "/save")
  public Demo save(@RequestBody Demo demo) {
    return mcpService.saveDemo(demo);
  }


}
