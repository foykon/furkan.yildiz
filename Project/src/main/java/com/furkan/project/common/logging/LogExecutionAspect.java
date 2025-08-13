package com.furkan.project.common.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogExecutionAspect {
    private static final Logger log = LoggerFactory.getLogger(LogExecutionAspect.class);

    @Around("(@within(com.furkan.project.common.logging.LogExecution) || @annotation(com.furkan.project.common.logging.LogExecution)) && within(com.furkan.project..service..*)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        if (!log.isDebugEnabled()) {
            return pjp.proceed();
        }
        long t0 = System.nanoTime();
        String sig = pjp.getSignature().toShortString();
        try {
            log.debug("⇢ {}", sig);
            Object out = pjp.proceed();
            long ms = (System.nanoTime() - t0) / 1_000_000;
            log.debug("⇠ {} ({} ms)", sig, ms);
            return out;
        } catch (Throwable ex) {
            long ms = (System.nanoTime() - t0) / 1_000_000;
            log.error("✗ {} failed ({} ms)", sig, ms, ex);
            throw ex;
        }
    }
}
