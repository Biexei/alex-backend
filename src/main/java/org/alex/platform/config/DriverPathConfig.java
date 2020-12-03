package org.alex.platform.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DriverPathConfig {
    @Value("${driver.chrome}")
    private String chrome;
    @Value("${driver.firefox}")
    private String firefox;
    @Value("${driver.ie}")
    private String ie;

    public String getChrome() {
        return chrome;
    }

    public void setChrome(String chrome) {
        this.chrome = chrome;
    }

    public String getFirefox() {
        return firefox;
    }

    public void setFirefox(String firefox) {
        this.firefox = firefox;
    }

    public String getIe() {
        return ie;
    }

    public void setIe(String ie) {
        this.ie = ie;
    }
}
