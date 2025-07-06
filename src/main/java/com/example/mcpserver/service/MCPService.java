package com.example.mcpserver.service;

import com.example.mcpserver.entities.Demo;
import com.example.mcpserver.repositories.DemoRepository;
import jakarta.annotation.Resource;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class MCPService {

    @Resource
    private DemoRepository demoRepository;

    public String message(String message) {
        return "服务端已收到信息：【" + message + "】";
    }


    public Demo fetchDemo(String id) {
        return (Demo) demoRepository.findById(id).orElse(null);
    }

    public Demo saveDemo(Demo demo) {
        return (Demo) demoRepository.save(demo);
    }


}
