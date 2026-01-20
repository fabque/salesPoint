package com.ar.sales.point.infrastructure.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.ar.sales.point.infrastructure.persistence.h2.repositories",
        entityManagerFactoryRef = "h2EntityManager",
        transactionManagerRef = "h2TransactionManager"
)
@EntityScan(basePackages = "com.ar.sales.point.infrastructure.persistence.h2")
public class H2DataSourceConfig {

    private final Environment env;

    public H2DataSourceConfig(Environment env) {
        this.env = env;
    }

    @Bean
    @ConfigurationProperties("app.datasource.h2")
    public DataSourceProperties h2DataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource h2DataSource(@Qualifier("h2DataSourceProperties") DataSourceProperties props) {
        return props.initializeDataSourceBuilder().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean h2EntityManager(
            EntityManagerFactoryBuilder builder,
            @Qualifier("h2DataSource") DataSource dataSource) {

        Map<String, Object> props = new HashMap<>();

        // Leer propiedades de hibernate desde application.properties (prefijo app.datasource.h2.hibernate)
        String hbm2ddl = env.getProperty("app.datasource.h2.hibernate.hbm2ddl", env.getProperty("app.datasource.h2.hibernate.hbm2ddl.create-drop", "create-drop"));
        String dialect = env.getProperty("app.datasource.h2.hibernate.dialect", "org.hibernate.dialect.H2Dialect");

        props.put("hibernate.hbm2ddl.auto", hbm2ddl);
        props.put("hibernate.dialect", dialect);

        return builder
                .dataSource(dataSource)
                .packages("com.ar.sales.point.infrastructure.persistence.h2") // entidades SalePoint y SalePointCost
                .persistenceUnit("h2PU")
                .properties(props)
                .build();
    }

    @Bean
    public PlatformTransactionManager h2TransactionManager(
            @Qualifier("h2EntityManager") LocalContainerEntityManagerFactoryBean h2EntityManager) {
        return new JpaTransactionManager(h2EntityManager.getObject());
    }
}
