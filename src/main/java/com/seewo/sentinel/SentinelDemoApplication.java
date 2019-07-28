package com.seewo.sentinel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.cvte.psd.conf.core.spring.annotation.EnableApolloConfig;
import com.seewo.honeycomb.web.annotation.EnableWeb;

@EnableApolloConfig
@EnableWeb
@SpringBootApplication
public class SentinelDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SentinelDemoApplication.class, args);
    }
}
