package com.twinkle.framework.datasource.plugin;

//import DynamicDataSourceProperties;
//import DataSourceConstants;
//import DynamicDataSourceContextHolder;
//import lombok.extern.slf4j.Slf4j;
//import org.aopalliance.intercept.Invocation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.util.StringUtils;
//import sun.plugin2.main.server.ResultHandler;
//
//import java.util.Properties;
//
///**
// * Function: Master Slave switch. For MyBatis. <br/>
// * Reason:	 TODO ADD REASON. <br/>
// * Date:     10/12/19 6:31 PM<br/>
// *
// * @author chenxj
// * @see
// * @since JDK 1.8
// */
//@Intercepts({
//        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
//                RowBounds.class, ResultHandler.class}),
//        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
//@Slf4j
//public class MasterSlaveAutoRoutingPlugin implements Interceptor {
//
//    @Autowired
//    private DynamicDataSourceProperties properties;
//
//    @Override
//    public Object intercept(Invocation invocation) throws Throwable {
//        Object[] args = invocation.getArgs();
//        MappedStatement ms = (MappedStatement) args[0];
//        boolean empty = true;
//        try {
//            empty = StringUtils.isEmpty(DynamicDataSourceContextHolder.peek());
//            if (empty) {
//                DynamicDataSourceContextHolder.push(getDataSource(ms));
//            }
//            return invocation.proceed();
//        } finally {
//            if (empty) {
//                DynamicDataSourceContextHolder.clear();
//            }
//        }
//    }
//
//    /**
//     * 获取动态数据源名称，重写注入 DbHealthIndicator 支持数据源健康状况判断选择
//     *
//     * @param mappedStatement mybatis MappedStatement
//     */
//    public String getDataSource(MappedStatement mappedStatement) {
//        String slave = DataSourceConstants.SLAVE;
//        if (properties.isHealth()) {
//            /*
//             * 根据从库健康状况，判断是否切到主库
//             */
//            boolean health = DbHealthIndicator.getDbHealth(DataSourceConstants.SLAVE);
//            if (!health) {
//                health = DbHealthIndicator.getDbHealth(DataSourceConstants.MASTER);
//                if (health) {
//                    slave = DataSourceConstants.MASTER;
//                }
//            }
//        }
//        return SqlCommandType.SELECT == mappedStatement.getSqlCommandType() ? slave : DataSourceConstants.MASTER;
//    }
//
//    @Override
//    public Object plugin(Object target) {
//        return target instanceof Executor ? Plugin.wrap(target, this) : target;
//    }
//
//    @Override
//    public void setProperties(Properties properties) {
//    }
//}
