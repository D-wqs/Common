package com.stamper.yx.common.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * Sqlite数据源配置
 * @author D-wqs
 * @data 2019/10/28 14:28
 */
@Configuration
@MapperScan(basePackages = "com.stamper.yx.common.mapper.sqlite",sqlSessionTemplateRef = "sqliteSqlSessionTemplate")
public class SqliteDataSourceConfig {
    /**
     * 数据源对象
     * @return
     */
    @Bean(name = "sqliteDataSource")
    @ConfigurationProperties(prefix = "datasource.sqlite")
    public DataSource sqliteDataSource(){return DataSourceBuilder.create().build();}

    /**
     * sqlSessionFactory
     * @param dataSource    数据源
     * @return
     * @throws Exception
     */
    @Bean(name = "sqliteSqlSessionFactory")
    public SqlSessionFactory sqliteSessionFactory(@Qualifier("sqliteDataSource")DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean=new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapping/sqlite/*.xml"));
        return bean.getObject();
    }

    /**
     * sqliteTransactionManager
     * @param dataSource
     * @return
     */
    @Bean(name = "sqliteTransactionManager")
    public DataSourceTransactionManager sqliteTransactionManager(@Qualifier("sqliteDataSource")DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
    @Bean(name = "sqliteSqlSessionTemplate")
    public SqlSessionTemplate sqliteSqlSessionTemplate(@Qualifier("sqliteSqlSessionFactory")SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
