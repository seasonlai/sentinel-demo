package com.seewo.sentinel.controller;

import com.seewo.honeycomb.web.support.ApiResult;
import com.seewo.sentinel.dto.DemoDto;
import com.seewo.sentinel.service.DemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Resource
    private DemoService demoService;

    @GetMapping("/flow")
    public ApiResult flowRule(DemoDto demoDto) {
        return ApiResult.success(demoService.flow(demoDto));
    }

    @GetMapping("/param")
    @ResponseBody
    public String freqParamFlow(@RequestParam("uid") Long uid) {
        return demoService.freqParamFlow(uid);
    }

    @GetMapping("/circuit")
    public String circuitBreak(@RequestParam("name") String name) throws Exception {
        String result;
        try {
            DemoDto demoDto = new DemoDto();
            demoDto.setName(name);
            result = demoService.circuitBreak(demoDto);
        } catch (Exception e) {
            result = "exception";
        }
        return result;
    }

}
