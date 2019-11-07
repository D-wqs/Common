package com.stamper.yx.common.config;

import com.stamper.yx.common.sys.AppConstant;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * mysql数据源配置
 * @author D-wqs
 * @data 2019/10/28 14:28
 */
@Configuration
@MapperScan(basePackages = "com.stamper.yx.common.mapper.mysql",sqlSessionTemplateRef = "mysqlSqlSessionTemplate")
@ConditionalOnExpression("${openType.mysql}")
public class MysqlDataSourceConfig {
    /**
     * 数据源对象
     * @return
     */
    @Bean(name = "mysqlDataSource")
    @ConfigurationProperties(prefix = "datasource.mysql")
    public DataSource mysqlDataSource(){
        return DataSourceBuilder.create().build();
    }

    /**
     * sqlSessionFactory
     * @param dataSource    数据源
     * @return
     * @throws Exception
     */
    @Bean(name = "mysqlSqlSessionFactory")
    public SqlSessionFactory mysqlSessionFactory(@Qualifier("mysqlDataSource")DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean=new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapping/mysql/*.xml"));
        return bean.getObject();
    }

    /**
     * mysqlTransactionManager
     * @param dataSource
     * @return
     */
    @Bean(name = "mysqlTransactionManager")
    @Primary
    public DataSourceTransactionManager mysqlTransactionManager(@Qualifier("mysqlDataSource")DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
    @Bean(name = "mysqlSqlSessionTemplate")
    public SqlSessionTemplate mysqlSqlSessionTemplate(@Qualifier("mysqlSqlSessionFactory")SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
