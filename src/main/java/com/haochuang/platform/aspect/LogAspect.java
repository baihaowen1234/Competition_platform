package com.haochuang.platform.aspect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/*切面   监听方法。上线后关闭*/
@Aspect
@Component
@Slf4j
public class LogAspect {

  /*日志*/
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  /*切web下面的全部方法和传入值*/
  @Pointcut("execution(* com.haochuang.platform.controller.*.*(..))")
  public void log() {
    logger.info("-------------------------------------------");
  }

  /*调用方法前*/
  @Before("log()")
  public void beforeget(JoinPoint joinPoint) {
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    HttpServletRequest request = attributes.getRequest();
    /*获取请求地址*/
    String url = request.getRequestURL().toString();

    /*获取IP地址*/
    String remoteAddr = request.getRemoteAddr();

    /*调用方法名字*/
    String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();

    /*请求参数*/
    Object[] Args = joinPoint.getArgs();

    Requestlog requestlog = new Requestlog(url, remoteAddr, classMethod, Args);
    logger.info("请求返回的所有值:{}", requestlog);
  }

  /*调用方法后*/
  @AfterReturning(returning = "object", pointcut = "log()")
  public void returndata(Object object) {
    logger.info("返回的网页或者值:{}", object);
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  private class Requestlog {
    private String url;
    private String remoteAddr;
    private String classMethod;
    private Object[] Args;

  }

}
