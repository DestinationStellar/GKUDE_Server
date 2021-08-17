package com.tsingle.gkude_server.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.tsingle.gkude_server.dao")
public class MyBatisConfig {
}
