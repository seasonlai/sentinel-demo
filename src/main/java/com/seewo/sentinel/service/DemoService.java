package com.seewo.sentinel.service;

import com.seewo.sentinel.dto.DemoDto;

public interface DemoService {

    String flow(DemoDto demoDto);
    
    String freqParamFlow(Long uid);

    String circuitBreak(DemoDto demoDto);
}

