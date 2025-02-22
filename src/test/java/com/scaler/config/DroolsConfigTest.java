package com.scaler.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DroolsConfigTest {

    @Autowired
    private KieContainer kieContainer;

    @Test
    public void testDroolsConfiguration() {
        assertNotNull(kieContainer, "KieContainer should not be null");
        
        KieSession kieSession = kieContainer.newKieSession();
        assertNotNull(kieSession, "KieSession should not be null");
        
        // Clean up
        kieSession.dispose();
    }
}
