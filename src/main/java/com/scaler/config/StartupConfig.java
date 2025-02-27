package com.scaler.config;

import com.scaler.util.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class StartupConfig {

    @Autowired
    private DemoService demoService;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void loadData() {
        demoService.setup();
    }
}
