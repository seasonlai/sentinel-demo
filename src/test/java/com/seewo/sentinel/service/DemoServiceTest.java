package com.seewo.sentinel.service;

import com.seewo.sentinel.SentinelDemoApplication;
import com.seewo.sentinel.dto.DemoDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {SentinelDemoApplication.class})
public class DemoServiceTest {

    @Autowired
    private DemoService demoService;


    @Test
    public void flowTest() throws IOException {
        for (int i = 0; i < 20; i++) {
            Thread t = new Thread(() -> {
                demoService.flow(new DemoDto());
                Random random2 = new Random();
                try {
                    TimeUnit.MILLISECONDS.sleep(random2.nextInt(50));
                } catch (InterruptedException e) {
                    // ignore
                }
            });
            t.setName("task-" + i);
            t.start();
        }
        System.in.read();
    }

    @Test
    public void degradeTest() {
        for (int i = 0; i < 30; i++) {
            try {
                DemoDto dto = new DemoDto();
                dto.setName("season");
                demoService.circuitBreak(dto);
            } catch (Exception e) {

            }
        }

    }


}

