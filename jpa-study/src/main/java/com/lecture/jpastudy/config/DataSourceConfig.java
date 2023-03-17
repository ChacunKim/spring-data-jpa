package com.lecture.jpastudy.config;

import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;


@Configuration
public class DataSourceConfig {
    //JPA 사용을 위한 4개의 bean
    //Auto Configuration 을 하면 DataSource, JpaVendorAdapter,
    // LocalContainerEntityManagerFactoryBean, PlatformTransactionManager 네개의 빈이 자동으로 생성됨

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:~/test-jpa");
        dataSource.setUsername("kim");
        dataSource.setPassword("");

        return dataSource;
    }

    /**
     * @JpaVendorAdapter : JPA는 여러 구현체가 존재하기 때문에 구현체별 설정을 지원하기 위한 클래스이다.
     * Hibernate를 사용하기 때문에 HibernateJpaVendorAdapter를 사용한다.
     * @param jpaProperties
     * @return
     */
    //JPA는 인터페이스로, 여러 구현체가 있다. 그 구현체 중 어떤 구현체를 사용할건지 설정
    //HibernateJpaVendorAdapter를 사용.
    @Bean
    public JpaVendorAdapter jpaVendorAdapter(JpaProperties jpaProperties) {
        AbstractJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(jpaProperties.isShowSql());
        adapter.setDatabasePlatform(jpaProperties.getDatabasePlatform());
        adapter.setGenerateDdl(jpaProperties.isGenerateDdl());
        return adapter;
    }


    /**
     * @LocalContainerEntityManagerFactoryBean :
     * EntityManagerFactoryBean을 Spring에서 사용하기 위한 클래스
     * @param dataSource
     * @param jpaVendorAdapter
     * @param jpaProperties
     * @return
     */
    //Entity Manager: jpa에서 테이블 간 맵핑된 entity를 관리하는 역할.
    //Entity Manager Factory: Entity Manager Bean을 만드는 역할.

    @Bean   //entityManagerFactoryBean이 아니라 entityManagerFactory로 설정해야함.
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter,
                                                                       JpaProperties jpaProperties) {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.lecture.jpastudy.domain");
        em.setJpaVendorAdapter(jpaVendorAdapter);

        Properties properties = new Properties();
        properties.putAll(jpaProperties.getProperties());
        em.setJpaProperties(properties);

        return em;
    }


    //PlatformTransactionManager: @Transactional 과 관련. RDB의 transaction을 관리.
    @Bean
    public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());

        return transactionManager;
    }
}
