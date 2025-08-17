package com.dhruv;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.dhruv")
public class WebDriverConfig {

    @Bean
    public WebDriver webDriver() {
        var driver = new FirefoxDriver();
        return driver;
    }
    
}
