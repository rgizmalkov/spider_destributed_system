package com.gmail.rgizmalkov.edu.projetcs.destriburted_system;

import com.gmail.rgizmalkov.edu.projetcs.destriburted_system.node.Node;
import com.gmail.rgizmalkov.edu.projetcs.destriburted_system.ymlpojos.DistributedSystemProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@SpringBootApplication
@Configuration
public class DistributedSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributedSystemApplication.class, args);
    }


    @Bean
    public String createNodeMap(DistributedSystemProperties properties) {
        System.out.println(properties);
        return "properties";
    }
}
