package com.example.mcpserver.repositories;

import com.example.mcpserver.entities.Demo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface DemoRepository <T extends Demo, ID extends Serializable> extends JpaRepository<T,ID> {
}
