package com.scaler.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.scaler.util.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/setup")
public class DemoController {
    @Autowired
    private DemoService demoService;

    @GetMapping
    public void setup() throws JsonProcessingException {
        demoService.setup();
    }
}
