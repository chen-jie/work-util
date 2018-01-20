package com.chenjie.workutil.sql;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

@Intercepts({@Signature(
        type = StatementHandler.class,
        method = "parameterize",
        args = {Statement.class}
)})
public class LogSqlInterceptor implements Interceptor {

    private static final Logger LOGGER = Logger.getLogger(LogSqlInterceptor.class);
    // 排除某些路径
    private static List<String> exlucdeUrlList = null;

    @Override
    public Object intercept(Invocation invocation) throws Throwable
    {
        processLog(invocation);
        return invocation.proceed();
    }

    /**
     * 记录日志
     * @param invocation
     * @author cj
     */
    private void processLog(Invocation invocation)
    {
        // 从Spring中拿到Request
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String uri = "";
        if(servletRequestAttributes != null)
        {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            uri = request.getRequestURI();
            if(isExcludedUri(uri))
            {
                return;
            }
        }
        StatementHandler target = (StatementHandler)invocation.getTarget();
        BoundSql boundSql = target.getBoundSql();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if(parameterMappings == null || parameterMappings.isEmpty())
        {
            String sql = boundSql.getSql();
            LOGGER.warn(String.format("RequestUri [ %s ] is using sql without parameter:\n%s",uri,sql));
        }
    }

    @Override
    public Object plugin(Object target)
    {
        return Plugin.wrap(target,this);
    }

    @Override
    public void setProperties(Properties properties)
    {
        String excludeUrls = properties.getProperty("excludeUrls");
        if(excludeUrls != null)
        {
            exlucdeUrlList = new ArrayList<>();
            for (String url : excludeUrls.split(","))
            {
                // 将配置中的 *  替换成  .*  ，使得正则 Pattern.matches(exlucdeUrl,uri)正常运行
                exlucdeUrlList.add(url.replaceAll("\\*",".*"));
            }
        }
    }
    /**
     * 判断当前请求路径是否排除在外
     * @param uri  当前请求路径
     * @return
     * @author cj
     */
    private boolean isExcludedUri(String uri)
    {
        if(exlucdeUrlList != null && !exlucdeUrlList.isEmpty())
        {
            for (String exlucdeUrl : exlucdeUrlList)
            {
                if(Pattern.matches(exlucdeUrl,uri))
                {
                    return true;
                }
            }
        }
        return false;
    }
}
