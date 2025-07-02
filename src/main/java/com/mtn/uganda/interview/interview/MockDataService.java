package com.mtn.uganda.interview.interview;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class MockDataService {

    private final ObjectMapper objectMapper;
    private final AppConfiguration appConfig;

    public MockDataService(ObjectMapper objectMapper, AppConfiguration appConfig) {
        this.objectMapper = objectMapper;
        this.appConfig = appConfig;
    }

    public List<User> loadUsersFromMockData() {
        try {
            simulateApiDelay();

            ClassPathResource resource = new ClassPathResource("mock-data/users.json");
            InputStream inputStream = resource.getInputStream();

            List<User> users = objectMapper.readValue(inputStream,
                    new TypeReference<List<User>>() {});

            return users;

        } catch (IOException e) {
            throw new DataLoadException("Failed to load users from mock data", e);
        }
    }

    public List<Post> loadPostsFromMockData() {
        try {
            simulateApiDelay();

            ClassPathResource resource = new ClassPathResource("mock-data/posts.json");
            InputStream inputStream = resource.getInputStream();

            List<Post> posts = objectMapper.readValue(inputStream,
                    new TypeReference<List<Post>>() {});
            ;
            return posts;

        } catch (IOException e) {
            throw new DataLoadException("Failed to load posts from mock data", e);
        }
    }

    private void simulateApiDelay() {
        try {
            Thread.sleep(appConfig.getApiDelay());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}