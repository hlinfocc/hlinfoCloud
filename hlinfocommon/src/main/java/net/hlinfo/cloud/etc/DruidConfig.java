package net.hlinfo.cloud.etc;

import com.alibaba.druid.pool.DruidDataSource;
import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.util.Daos;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DruidConfig {
    @Bean(destroyMethod = "close", initMethod = "init")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource() {
        return new DruidDataSource();
    }

    /**
     * 配置Nutz Dao操作数据库
     * @param druidDataSource
     * @return
     */
    @Bean
    public Dao dao(DataSource druidDataSource) {
        Dao dao = new NutDao(druidDataSource);
        Daos.createTablesInPackage(dao, "net.hlinfo.cloud.entity", false);
        return dao;
    }
}
