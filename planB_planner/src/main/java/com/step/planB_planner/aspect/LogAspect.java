package com.step.planB_planner.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @author Daria Pevets
 **/
@Slf4j
@Aspect
public class LogAspect {

    @Pointcut("@within(com.step.planB_planner.annotation.Log)")
    public void pointCut() {
    }

    /**
     * Этот метод перехватывает вызовы методов на контроллерах и обработчиках, имеющих аннотацию Log
     *
     * @param joinPoint ProceedingJoinPoint
     * @return результат перехваченного вызова
     */
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
            log.error(logMessage);
        } else {
            log.info(logMessage);
        }
        return result;
    }
}
