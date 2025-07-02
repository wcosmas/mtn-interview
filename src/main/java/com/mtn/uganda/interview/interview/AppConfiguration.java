package com.mtn.uganda.interview.interview;

import jdk.jfr.Recording;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfiguration {

    @Value("${app.mock.data.path}")
    private String mockDataPath;

    @Value("${app.mock.api.delay}")
    private int apiDelay;

    @Value("${app.pagination.default-page-size}")
    private int defaultPageSize;

    @Value("${app.pagination.max-page-size}")
    private int maxPageSize;




    public String getMockDataPath() {
        return mockDataPath;
    }

    public int getApiDelay() {
        return apiDelay;
    }

    public int getDefaultPageSize() {
        return defaultPageSize;
    }

    public int getMaxPageSize() {
        return maxPageSize;
    }

}