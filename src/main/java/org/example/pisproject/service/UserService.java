package org.example.pisproject.service;


import lombok.RequiredArgsConstructor;
import org.example.pisproject.configuration.ShardRoutingDataSource;
import org.example.pisproject.entity.User;
import org.example.pisproject.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Decide the shard key based on user ID
     * userId % 4 = 0 → shard1
     * userId % 4 = 1 → shard2
     * userId % 4 = 2 → shard3
     * userId % 4 = 3 → shard4
     */
    private String resolveShard(Integer userId) {
        int shardNumber = (userId % 4) + 1;
        return "shard" + shardNumber;
    }

    @Transactional
    public User createUser(User user) {
        // Persist user first to get ID (if using auto-increment strategy, this requires extra logic)
        // For simplicity, assume ID is known or pre-generated
        String shardKey = resolveShard(2);
        ShardRoutingDataSource.setCurrentShard(shardKey);

        try {
            return userRepository.save(user);
        } finally {
            ShardRoutingDataSource.clearCurrentShard();
        }
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserById(Integer userId) {
        String shardKey = resolveShard(userId);
        ShardRoutingDataSource.setCurrentShard(shardKey);

        try {
            return userRepository.findById(userId);
        } finally {
            ShardRoutingDataSource.clearCurrentShard();
        }
    }

    @Transactional
    public void deleteUserById(Integer userId) {
        String shardKey = resolveShard(userId);
        ShardRoutingDataSource.setCurrentShard(shardKey);
        try {
            userRepository.deleteById(userId);
        } finally {
            ShardRoutingDataSource.clearCurrentShard();
        }
    }
}