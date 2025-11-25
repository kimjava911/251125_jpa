package kr.java.jpa.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jca.support.LocalConnectionFactoryBean;
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
        emf.setPackagesToScan("kr.java.jpa.entity");
        // Hibernate 구현체로 지정
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();

        emf.setJpaVendorAdapter(adapter);

        Properties props = new Properties();
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");

        emf.setJpaProperties(props);
        return emf;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager txManager = new JpaTransactionManager(emf);
        return txManager;
    }
}
