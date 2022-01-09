package com.example.librarymanagementsystem.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class StaticResourceConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String filePath = "E:/Final_Year_Semesters/EIRLS/Assignment/Library_Management_System/src/main/resources/static/uploads";

        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:/"+filePath)
                .setCachePeriod(0);
    }
}