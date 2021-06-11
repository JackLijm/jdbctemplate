package com.example.billmanager.quartz.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.quartz.AdaptableJobFactory;

import javax.sql.DataSource;
@Slf4j
@Configuration
public class QuartzConfig {
    @Autowired
    private JobFactory jobFactory;

    @Autowired
    private AutowireCapableBeanFactory capableBeanFactory;

    private static final String DATASOURCE_NAME = "dataSource";

    /**
     * 数据源配置的前缀，必须与application.properties中配置的对应数据源的前缀一致
     */
    private static final String BUSINESS_DATASOURCE_PREFIX = "spring.datasource.druid.business";

    private static final String QUARTZ_DATASOURCE_PREFIX = "spring.datasource.druid.quartz";

    @Primary
    @Bean(name = DATASOURCE_NAME)
    @ConfigurationProperties(prefix = BUSINESS_DATASOURCE_PREFIX)
    public DruidDataSource druidDataSource() {
        return new DruidDataSource();
    }

    /**
     * @QuartzDataSource 注解则是配置Quartz独立数据源的配置
     */
    @Bean
    @QuartzDataSource
    @ConfigurationProperties(prefix = QUARTZ_DATASOURCE_PREFIX)
    public DataSource quartzDataSource(){
        return new DruidDataSource();
    }

    /**
     * 当触发器触发时，与之关联的任务被Scheduler中配置的JobFactory实例化，也就是每触发一次，就会创建一个任务的实例化对象
     * (如果缺省)则调用Job类的newInstance方法生成一个实例
     * (这里选择自定义)并将创建的Job实例化交给IoC管理
     * @return
     */
    @Bean
    public JobFactory jobFactory() {
        return new AdaptableJobFactory() {
            @Override
            protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
                Object jobInstance = super.createJobInstance(bundle);
                capableBeanFactory.autowireBean(jobInstance);
                log.info(jobInstance.toString());
                return jobInstance;
            }
        };
    }
}
