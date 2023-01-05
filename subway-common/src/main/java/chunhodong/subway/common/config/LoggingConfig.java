package chunhodong.subway.common.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Configuration
@Aspect
public class LoggingConfig {
    private static final Logger logger = LoggerFactory.getLogger(LoggingConfig.class);

    @Pointcut("execution(* chunhodong.subway..ui.*.*(..))")
    public void logPointcut() {
    }

    @Around(value = "chunhodong.subway.common.config.LoggingConfig.logPointcut()")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        ResponseEntity response = (ResponseEntity) joinPoint.proceed();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        logger.info("code={},method={},uri={}", response.getStatusCode(), request.getMethod(), request.getRequestURI());
        return response;
    }
}
