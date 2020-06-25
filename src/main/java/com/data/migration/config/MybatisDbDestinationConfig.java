package com.data.migration.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

//@Configuration
//@MapperScan(basePackages = {"other.mapper"}, sqlSessionFactoryRef = "sqlSessionFactory2")
//public class MybatisDbDestinationConfig {
//    @Autowired
//    @Qualifier("destinationDB")
//    private DataSource dataSource;
//
//    @Bean
//    public SqlSessionFactory sqlSessionFactory2() throws Exception {
//        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
//        factoryBean.setDataSource(dataSource);
//
//
//        return factoryBean.getObject();
//
//    }
//
//    @Bean
//    public SqlSessionTemplate sqlSessionTemplate2() throws Exception {
//        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory2());
//        return template;
//    }
//}

@Configuration
@MapperScan(basePackages = "com.data.migration.dao.destination", sqlSessionTemplateRef  = "destinationSqlSessionTemplate")
public class MybatisDbDestinationConfig {

    @Bean(name = "destinationData")
    @ConfigurationProperties(prefix = "spring.datasource.destination")
    @Primary
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "destinationSqlSessionFactory")
    @Primary
    public SqlSessionFactory destinationSqlSessionFactory(@Qualifier("destinationData") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        //添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            bean.setMapperLocations(resolver.getResources("classpath*:mappers/destination/*.xml"));
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Bean(name = "destinationTransactionManager")
    @Primary
    public DataSourceTransactionManager destinationTransactionManager(@Qualifier("destinationData") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "destinationSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate destinationSqlSessionTemplate(@Qualifier("destinationSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
