package kr.java.jpa.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration // Component Scan
@EnableTransactionManagement // Transaction
public class JPAConfig {
    @Bean
    public DataSource dataSource() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        // JDBC
        DriverManagerDataSource ds = new DriverManagerDataSource();

        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl(dotenv.get("DB_URL"));
        ds.setUsername(dotenv.get("DB_USERNAME"));
        ds.setPassword(dotenv.get("DB_PASSWORD"));

        return ds;
    }

    // EntityManagerFactory <- EntityManger <- JPA 구동
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();

        // DB 설정
        emf.setDataSource(dataSource());
        // 엔터티 스캔할 패키지를 지정
        emf.setPackagesToScan("kr.java.jpa.model.entity");
        // Hibernate 구현체로 지정
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();

        // DDL 자동 생성 활성화
        adapter.setGenerateDdl(true);
        // 로그 출력
        adapter.setShowSql(true);

        emf.setJpaVendorAdapter(adapter);

        Properties props = new Properties();
        // MySQL 연결을 위한 세팅
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        // 스키마 자동 업데이트 전략
//        props.setProperty("hibernate.hbm2ddl.auto", "create");
        props.setProperty("hibernate.hbm2ddl.auto", "update");
        // create ?, create-drop ?, [update***], [validate, none]

        // SQL 자동 포맷팅
        props.setProperty("hibernate.format_sql", "true");
        // SQL 엔터티 주석 추가
        props.setProperty("hibernate.use_sql_comments", "true");

        emf.setJpaProperties(props);
        return emf;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager txManager = new JpaTransactionManager(emf);
        return txManager;
    }
}
