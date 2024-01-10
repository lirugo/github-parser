package com.lirugo.github.parser.aspect;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class ExecutionTimeAspect implements Ordered {

    @Around("@annotation(com.lirugo.github.parser.annotations.ExecutionTime)")
    public Object executionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        if (log.isDebugEnabled()) {
            var stopWatch = new StopWatch();
            stopWatch.start();
            var proceed = joinPoint.proceed();
            stopWatch.stop();
            log.debug("{} executed in {} ms", joinPoint.getSignature(), stopWatch.getTotalTime(
                TimeUnit.MILLISECONDS));
            return proceed;
        }
        return joinPoint.proceed();
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
