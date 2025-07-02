package com.mtn.uganda.interview.interview;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final MockDataService mockDataService;

    public UserService(UserRepository userRepository, MockDataService mockDataService) {
        this.userRepository = userRepository;
        this.mockDataService = mockDataService;
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return convertToDto(user);
    }

    public UserDto createUser(UserDto userDto) {

        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + userDto.getUsername());
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + userDto.getEmail());
        }

        User user = convertToEntity(userDto);
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    public String loadUsersFromMockData() {
        List<User> mockUsers = mockDataService.loadUsersFromMockData();

        int loadedCount = 0;
        int skippedCount = 0;

        for (User mockUser : mockUsers) {
            if (!userRepository.existsByUsername(mockUser.getUsername()) &&
                    !userRepository.existsByEmail(mockUser.getEmail())) {
                // Clear ID and timestamps to avoid optimistic locking conflicts
                mockUser.setId(null);
                mockUser.setCreatedAt(null);
                mockUser.setUpdatedAt(null);
                userRepository.save(mockUser);
                loadedCount++;
            } else {
                skippedCount++;
            }
        }

        String result = String.format("Loaded %d users, skipped %d duplicates", loadedCount, skippedCount);
        return result;
    }

    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());
        userDto.setWebsite(user.getWebsite());
        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setUpdatedAt(user.getUpdatedAt());

        return userDto;
    }

    private User convertToEntity(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setWebsite(userDto.getWebsite());
        user.setCreatedAt(userDto.getCreatedAt());
        user.setUpdatedAt(userDto.getUpdatedAt());

        return user;
    }
}