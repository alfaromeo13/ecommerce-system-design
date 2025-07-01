package org.example.pisproject.configuration;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Slf4j
@Component
public class ShardSchemaInitializer {

    @Qualifier("shard1")
    private final DataSource shard1;

    @Qualifier("shard2")
    private final DataSource shard2;

    @Qualifier("shard3")
    private final DataSource shard3;

    @Qualifier("shard4")
    private final DataSource shard4;

    public ShardSchemaInitializer(
            @Qualifier("shard1DataSource") DataSource shard1,
            @Qualifier("shard2DataSource") DataSource shard2,
            @Qualifier("shard3DataSource") DataSource shard3,
            @Qualifier("shard4DataSource") DataSource shard4) {
        this.shard1 = shard1;
        this.shard2 = shard2;
        this.shard3 = shard3;
        this.shard4 = shard4;
    }

    @PostConstruct
    public void initialize() {
        log.info("Initializing schema and data on both shards...");

        try {
            runSqlScript(shard1, "database/schema.sql");
            runSqlScript(shard2, "database/schema.sql");
            runSqlScript(shard3, "database/schema.sql");
            runSqlScript(shard4, "database/schema.sql");

            log.info("Schema and data initialized on both shards!");
        } catch (Exception e) {
            log.error("ERROR: Failed to initialize shards", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    private void runSqlScript(DataSource dataSource, String scriptPath) throws Exception {
        Resource script = new ClassPathResource(scriptPath);
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, script);
        }
    }
}