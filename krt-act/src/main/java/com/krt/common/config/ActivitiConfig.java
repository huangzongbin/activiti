package com.krt.common.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.activiti.engine.*;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Activiti初始化
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月21日
 */
@Configuration
public class ActivitiConfig {

    /**
     * 流程引擎配置
     *
     * @param dataSource         数据源
     * @param transactionManager 事务管理器
     * @return ProcessEngineConfiguration
     */
    @Bean
    public ProcessEngineConfiguration processEngineConfiguration(DataSource dataSource, PlatformTransactionManager transactionManager) {
        SpringProcessEngineConfiguration processEngineConfiguration = new SpringProcessEngineConfiguration();
        processEngineConfiguration.setDataSource(dataSource);
        processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        processEngineConfiguration.setJobExecutorActivate(false);
        processEngineConfiguration.setTransactionManager(transactionManager);
        //流程图字体
        processEngineConfiguration.setActivityFontName("宋体");
        processEngineConfiguration.setAnnotationFontName("宋体");
        processEngineConfiguration.setLabelFontName("宋体");
        return processEngineConfiguration;
    }

    /**
     * 流程引擎，与spring整合使用factoryBean
     *
     * @param processEngineConfiguration 流程引擎
     * @return ProcessEngineFactoryBean
     */
    @Bean
    public ProcessEngineFactoryBean processEngine(ProcessEngineConfiguration processEngineConfiguration) {
        ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
        processEngineFactoryBean.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);
        return processEngineFactoryBean;
    }

    /**
     * Activiti的仓库服务类。所谓的仓库指流程定义文档的两个文件：bpmn文件和流程图片
     *
     * @param processEngine 流程引擎工厂
     * @return RepositoryService
     */
    @Bean
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    /**
     * Activiti的流程执行服务类。可以从这个服务类中获取很多关于流程执行相关的信息
     *
     * @param processEngine 流程引擎工厂
     * @return RuntimeService
     */
    @Bean
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    /**
     * Activiti的任务服务类。可以从这个类中获取任务的信息
     *
     * @param processEngine 流程引擎工厂
     * @return TaskService
     */
    @Bean
    public TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }

    /**
     * Activiti的查询历史信息的类。在一个流程执行完成后，这个对象为我们提供查询历史信息。
     *
     * @param processEngine 流程引擎工厂
     * @return HistoryService
     */
    @Bean
    public HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }

    /**
     * 表单服务类
     *
     * @param processEngine 流程引擎工厂
     * @return FormService
     */
    @Bean
    public FormService formService(ProcessEngine processEngine) {
        return processEngine.getFormService();
    }

    /**
     * 提供对流程角色数据进行管理的API，这些角色数据包括用户组、用户及它们之间的关系。
     *
     * @param processEngine 流程引擎工厂
     * @return IdentityService
     */
    @Bean
    public IdentityService identityService(ProcessEngine processEngine) {
        return processEngine.getIdentityService();
    }

    /**
     * 提供对流程引擎进行管理和维护的服务
     *
     * @param processEngine 流程引擎工厂
     * @return ManagementService
     */
    @Bean
    public ManagementService managementService(ProcessEngine processEngine) {
        return processEngine.getManagementService();
    }

    /**
     * 一个新增的服务，用于动态修改流程中的一些参数信息等，是引擎中的一个辅助的服务
     *
     * @param processEngine 流程引擎工厂
     * @return DynamicBpmnService
     */
    @Bean
    public DynamicBpmnService dynamicBpmnService(ProcessEngine processEngine) {
        return processEngine.getDynamicBpmnService();
    }

    /**
     * jackjson序列化
     *
     * @return ObjectMapper
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
