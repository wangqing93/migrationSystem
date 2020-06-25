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

//@MapperScan(basePackages = {"titan.mapper"}, sqlSessionFactoryRef = "sqlSessionFactory1")
//public class MybatisDbSourceConfig {
//
//    @Autowired
//    @Qualifier("sourceDB")
//    private DataSource dataSource;
//
//
//    @Bean
//    @Primary
//    public SqlSessionFactory sqlSessionFactory1() throws Exception {
//        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
//        factoryBean.setDataSource(dataSource); // 使用titan数据源, 连接titan库
//
//        return factoryBean.getObject();
//
//    }
//
//    @Bean
//    @Primary
//    public SqlSessionTemplate sqlSessionTemplate1() throws Exception {
//        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory1()); // 使用上面配置的Factory
//        return template;
//    }
//}
@Configuration
@MapperScan(basePackages = "com.data.migration.dao.source", sqlSessionTemplateRef  = "sourceSqlSessionTemplate")
public class MybatisDbSourceConfig {

    @Bean(name = "sourceData")
    @ConfigurationProperties(prefix = "spring.datasource.source")
    @Primary
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "sourceSqlSessionFactory")
    @Primary
    public SqlSessionFactory sourceSqlSessionFactory(@Qualifier("sourceData") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        //添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            bean.setMapperLocations(resolver.getResources("classpath*:mappers/source/*.xml"));
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Bean(name = "sourceTransactionManager")
    @Primary
    public DataSourceTransactionManager sourceTransactionManager(@Qualifier("sourceData") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "sourceSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate sourceSqlSessionTemplate(@Qualifier("sourceSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
