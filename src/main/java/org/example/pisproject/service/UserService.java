package org.example.pisproject.service;

import lombok.RequiredArgsConstructor;
import org.example.pisproject.entity.User;
import org.example.pisproject.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    @CacheEvict(value = "allUsers", allEntries = true)
    @CachePut(value = "users", key = "#result.id")
    public User createUser(User user) {
        user.setCreatedAt(LocalDate.now());
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#id")
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    @CacheEvict(value = "allUsers", allEntries = true)
    @CachePut(value = "users", key = "#result.id")
    public User updateUser(Long id, User updatedUser) {
        updatedUser.setId(id);
        return userRepository.save(updatedUser);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "allUsers")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "allUsers", allEntries = true),
            @CacheEvict(value = "users", key = "#id")
    })
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}