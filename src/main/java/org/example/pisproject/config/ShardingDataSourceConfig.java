package org.example.pisproject.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.broadcast.config.BroadcastRuleConfiguration;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.algorithm.core.config.AlgorithmConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableReferenceRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.keygen.KeyGenerateStrategyConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Configuration
public class ShardingDataSourceConfig {

/*
 This configuration sets up sharding for the `users`, `orders`, `shopping_cart`, `order_items`, and `cart_items` tables
 across three MySQL databases (ds0, ds1, ds2).

 All user-related data is distributed based on `user_id` to ensure that records tied to the same user reside in the same
 database node, enabling efficient joins and scalability.

 Binding table groups (`orders` with `order_items`, and `shopping_cart` with `cart_items`) ensure consistent joins
 within the same shard.

 ShardingSphere provides built-in distributed primary key generators. One of those is SNOWFLAKE.
 Snowflake algorithm is used here for distributed primary key generation across shards.

 The `products` table is configured as a broadcast table, meaning it is automatically replicated across all shards.
 This allows consistent reads and joins without needing to shard or manually synchronize it.

 I wrote below with the help of official documentation:
 https://shardingsphere.apache.org/document/current/en/user-manual/shardingsphere-jdbc/java-api/rules/sharding/
*/

    @Value("${WORKER_ID:1}")
    private int workerId;

    @Value("${MYSQL_REPLICAS:3}")
    private int replicaCount;

    @Bean
    public DataSource dataSource() throws SQLException {

        Map<String, DataSource> dataSourceMap = new HashMap<>();

        // Loop Creating Shards. These become your horizontal shards, each storing part of the rows from sharded tables.
        for (int i = 0; i < replicaCount; i++) {
            HikariConfig config = new HikariConfig();
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            config.setJdbcUrl("jdbc:mysql://mysql-shop-"+i+".mysql-shop:3306/shop" +
                    "?createDatabaseIfNotExist=true&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8");
            config.setUsername("root");
            config.setPassword("root");
            dataSourceMap.put("ds" + i, new HikariDataSource(config));
        }

        // Sharding rule
        ShardingRuleConfiguration shardingRule = new ShardingRuleConfiguration();

        // These define how each table is distributed across shards
        // users is sharded by id, the rest by user_id
        List<ShardingTableRuleConfiguration> tables = List.of(
                createTableRule("users", "id", "user_inline"),
                createTableRule("shopping_cart", "user_id", "other_inline"),
                createTableRule("cart_items", "user_id", "other_inline"),
                createTableRule("orders", "user_id", "other_inline"),
                createTableRule("order_items", "user_id", "other_inline")
        );

        shardingRule.getTables().addAll(tables);

        // Binding tables are logically related tables that should be sharded together using the same key and strategy.
        // orders and order_items are almost always joined: SELECT * FROM orders o JOIN order_items i ON o.id = i.order_id
        // You’re telling ShardingSphere: Make sure order_items is on the same shard as its order.
        shardingRule.getBindingTableGroups().addAll(List.of(
                new ShardingTableReferenceRuleConfiguration("order_ref", "orders,order_items"),
                new ShardingTableReferenceRuleConfiguration("cart_ref", "shopping_cart,cart_items")
        ));

        // Broadcast rule. A table that is replicated on every shard.
        BroadcastRuleConfiguration broadcastRule = new BroadcastRuleConfiguration(List.of("products"));

        // Sharding algorithms
        Properties userInlineProps = new Properties();

        // INLINE is a simple built-in algorithm where you define an expression to decide which shard a row belongs to
        userInlineProps.setProperty("algorithm-expression", "ds${id % " + replicaCount + "}");
        shardingRule.getShardingAlgorithms().put("user_inline",
                new AlgorithmConfiguration("INLINE", userInlineProps));

        Properties otherInlineProps = new Properties();

        otherInlineProps.setProperty("algorithm-expression", "ds${user_id % " + replicaCount + "}");
        shardingRule.getShardingAlgorithms().put("other_inline",
                new AlgorithmConfiguration("INLINE", otherInlineProps));

        /*
            INLINE	    Simple sharding using expressions
            HASH_MOD	Hash-based modulo sharding
            RANGE	    Use ranges to assign rows to shards
            COMPLEX     Multiple sharding keys
         */

        // Snowflake key generator
        Properties snowflakeProps = new Properties();

        snowflakeProps.setProperty("worker-id", String.valueOf(workerId));
        shardingRule.getKeyGenerators().put("snowflake",
                new AlgorithmConfiguration("SNOWFLAKE", snowflakeProps));

        return ShardingSphereDataSourceFactory.createDataSource(
                dataSourceMap,
                List.of(shardingRule, broadcastRule),
                new Properties()
        );
    }

    private ShardingTableRuleConfiguration createTableRule(String tableName, String shardingColumn, String algorithmName) {
        String actualDataNodes = "ds${0.." + (replicaCount - 1) + "}." + tableName; // Define Data Nodes (Shards)
        // example:  ds0.orders, ds1.orders, ds2.orders
        ShardingTableRuleConfiguration config = new ShardingTableRuleConfiguration(tableName, actualDataNodes);
        config.setDatabaseShardingStrategy(new StandardShardingStrategyConfiguration(shardingColumn, algorithmName));
        config.setKeyGenerateStrategy(new KeyGenerateStrategyConfiguration("id", "snowflake"));
        return config;
    }
}