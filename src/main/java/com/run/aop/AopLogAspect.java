package com.run.aop;

import com.run.dao.SystemLogDao;
import com.run.model.SystemLog;
import com.run.utils.CommonUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Configuration
public class AopLogAspect {
    private static Logger logger = LoggerFactory.getLogger(AopLog.class);

    @Autowired
    private SystemLogDao systemLogDao;

    @Pointcut("@annotation(com.run.aop.AopLog)")
    public void controllerAspect() {
    }

    @After("controllerAspect()")
    public void doAfter(JoinPoint joinPoint) {
        try {
            logger.info("start record log ...");
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            SystemLog log = new SystemLog();
            log = getControllerMethodDescription(joinPoint, request, log);
            systemLogDao.save(log);
            logger.info(log.toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("record log error:{}", e.getMessage());
        }
    }

    public static SystemLog getControllerMethodDescription(JoinPoint joinPoint, HttpServletRequest request, SystemLog log) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methodArr = targetClass.getMethods();
        String description = "";
        String moduleName = "";
        String optType = "";
        String methodUrl = request.getRequestURI();
        String ip = CommonUtils.getIpAddr(request);
        String params = "";
        ParameterNameDiscoverer pnd = new DefaultParameterNameDiscoverer();
        for (Method mt : methodArr) {
            if (mt.getName().equals(methodName)) {
                Class[] clz = mt.getParameterTypes();
                if (clz.length == arguments.length) {
                    String[] parameterNames = pnd.getParameterNames(mt);
                    Map<String, Object> paramMap = new HashMap<>();
                    for (int i = 0; i < parameterNames.length; i++) {
                        paramMap.put(parameterNames[i], arguments[i]);
                    }
                    params = paramMap.toString();
                    description = mt.getAnnotation(AopLog.class).description();
                    moduleName = mt.getAnnotation(AopLog.class).moduleName();
                    optType = mt.getAnnotation(AopLog.class).operType();
                    break;
                }
            }
        }
        log.setParams(params);
        log.setMethodUrl(methodUrl);
        log.setIp(ip);
        log.setDescription(description);
        log.setOptType(optType);
        log.setModuleName(moduleName);
        log.setCreateDate(new Date());
        return log;
    }
}