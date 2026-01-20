package com.ar.sales.point.infrastructure.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.ar.sales.point.infrastructure.persistence.postgres.repositories",
        entityManagerFactoryRef = "postgresEntityManager",
        transactionManagerRef = "postgresTransactionManager"
)
@EntityScan(basePackages = "com.ar.sales.point.infrastructure.persistence.postgres")
@SuppressWarnings("unused")
public class PostgresDataSourceConfig {

    private final Environment env;

    public PostgresDataSourceConfig(Environment env) {
        this.env = env;
    }

    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.postgres")
    public DataSourceProperties postgresDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource postgresDataSource(@Qualifier("postgresDataSourceProperties") DataSourceProperties props) {
        DataSource ds = props.initializeDataSourceBuilder().build();
        // Si usamos Hikari, forzar una SQL de inicialización de conexión para fijar zona horaria a UTC
        // Esto previene errores en Postgres cuando la JVM tiene una zona no reconocida por Postgres
        if (ds instanceof HikariDataSource hds) {
            // Si no hay ya un connectionInitSql configurado, establecerlo a UTC
            if (hds.getConnectionInitSql() == null || hds.getConnectionInitSql().isBlank()) {
                hds.setConnectionInitSql("SET TIME ZONE 'UTC'");
            }
        }
        return ds;
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean postgresEntityManager(
            EntityManagerFactoryBuilder builder,
            @Qualifier("postgresDataSource") DataSource dataSource) {

        Map<String, Object> props = new HashMap<>();

        String hbm2ddl = env.getProperty("app.datasource.postgres.hibernate.hbm2ddl", env.getProperty("app.datasource.postgres.hibernate.hbm2ddl.update", "update"));
        String dialect = env.getProperty("app.datasource.postgres.hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

        props.put("hibernate.hbm2ddl.auto", hbm2ddl);
        props.put("hibernate.dialect", dialect);

        // Optional: agregar hibernate.jdbc.time_zone si está configurado (por defecto UTC)
        String jdbcTimeZone = env.getProperty("app.datasource.postgres.hibernate.jdbc.time_zone", "UTC");
        if (!jdbcTimeZone.isBlank()) {
            // Forzar a un valor simple como 'UTC' para evitar que Postgres reciba zonas inválidas
            // (Hibernate acepta nombres IANA pero Postgres a veces no los reconoce en todas las versiones).
            props.put("hibernate.jdbc.time_zone", jdbcTimeZone);
        }

        return builder
                .dataSource(dataSource)
                .packages("com.ar.sales.point.infrastructure.persistence.postgres")
                .persistenceUnit("postgresPU")
                .properties(props)
                .build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager postgresTransactionManager(
            @Qualifier("postgresEntityManager") LocalContainerEntityManagerFactoryBean postgresEntityManager) {
        return new JpaTransactionManager(Objects.requireNonNull(postgresEntityManager.getObject()));
    }
}
