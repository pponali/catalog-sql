package com.scaler.config;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DroolsConfig {

    private static final String RULES_PATH = "rules/";
    
    @Bean
    public KieContainer kieContainer() {
        KieServices kieServices = KieServices.Factory.get();

        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        
        // Add all validation rules
        kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_PATH + "category-validation.drl"));
        kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_PATH + "product-features.drl"));
        kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_PATH + "catalog-validation.drl"));
        kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_PATH + "merchant-validation.drl"));
        kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_PATH + "channel-validation.drl"));

        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        
        // Check for errors
        if (kieBuilder.getResults().hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
            throw new RuntimeException("Build Errors:\n" + kieBuilder.getResults().toString());
        }
        
        KieModule kieModule = kieBuilder.getKieModule();
        return kieServices.newKieContainer(kieModule.getReleaseId());
    }
}
