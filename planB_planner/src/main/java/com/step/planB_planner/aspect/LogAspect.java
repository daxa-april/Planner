package com.step.planB_planner.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @author Daria Pevets
 **/
@Aspect
@Component
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("@within(com.step.planB_planner.annotation.Log)")
    public void pointCut() {
    }

    /**
     * Этот метод перехватывает вызовы методов на контроллерах и обработчиках, имеющих аннотацию Log
     *
     * @param joinPoint ProceedingJoinPoint
     * @return результат перехваченного вызова
     */
    @Around("pointCut()")
    public Object loggingMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        LocalDateTime before = LocalDateTime.now();
        Object result = joinPoint.proceed(joinPoint.getArgs());
        LocalDateTime after = LocalDateTime.now();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String logMessage = """
                %s%s.%s:
                Request: %s
                Response: %s
                Time: %s""".formatted("\\n",
                className,
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()),
                result,
                Duration.between(before, after).toMillis());
        if (className.endsWith("Handler")) {
            logger.error(logMessage);
        } else {
            logger.info(logMessage);
        }
        return result;
    }
}
