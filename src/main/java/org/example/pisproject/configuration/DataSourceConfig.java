package org.example.pisproject.configuration;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    // Configuration properties for each shard
    @Bean
    @ConfigurationProperties("spring.datasource.shard1")
    public DataSourceProperties shard1Properties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.shard2")
    public DataSourceProperties shard2Properties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.shard3")
    public DataSourceProperties shard3Properties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.shard4")
    public DataSourceProperties shard4Properties() {
        return new DataSourceProperties();
    }

    // Create actual connections to each database shard
    @Bean
    public DataSource shard1DataSource() {
        return shard1Properties().initializeDataSourceBuilder().build();
    }

    @Bean
    public DataSource shard2DataSource() {
        return shard2Properties().initializeDataSourceBuilder().build();
    }

    @Bean
    public DataSource shard3DataSource() {
        return shard3Properties().initializeDataSourceBuilder().build();
    }

    @Bean
    public DataSource shard4DataSource() {
        return shard4Properties().initializeDataSourceBuilder().build();
    }

    // Create a routing datasource that will switch between shards
    @Bean
    public DataSource routingDataSource() {
        Map<Object, Object> dataSources = new HashMap<>();
        dataSources.put("shard1", shard1DataSource());
        dataSources.put("shard2", shard2DataSource());
        dataSources.put("shard3", shard3DataSource());
        dataSources.put("shard4", shard4DataSource());

        ShardRoutingDataSource routingDataSource = new ShardRoutingDataSource();
        routingDataSource.setTargetDataSources(dataSources);
        routingDataSource.setDefaultTargetDataSource(shard1DataSource()); // Fallback shard

        return routingDataSource;
    }

    // Set up transaction management
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(routingDataSource());
    }

    // Configure the entity manager factory for JPA
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(routingDataSource())
                .packages("org.example.pisproject.entity")
                .persistenceUnit("shardedPersistenceUnit")
                .build();
    }

    // Make this our primary DataSource that the application will use
    @Primary
    @Bean
    public DataSource dataSource() {
        // LazyConnectionDataSourceProxy improves performance by only
        // connecting to the database when actually needed
        return new LazyConnectionDataSourceProxy(routingDataSource());
    }
}