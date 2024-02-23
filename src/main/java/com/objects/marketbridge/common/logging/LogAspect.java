package com.objects.marketbridge.common.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.stream.IntStream;

@Aspect
@Slf4j
@Profile({"local", "dev"})
@Component
public class LogAspect {

    @Pointcut("execution(* com.objects.marketbridge..*(..))")
    public void all() {
    }
    @Pointcut("execution(* com.objects.marketbridge..*Controller.*(..))")
    public void controller() {
    }
    @Pointcut("execution(* com.objects.marketbridge..*Service.*(..))")
    public void service(){}

    @Pointcut("execution(* com.objects.marketbridge..*RepositoryImpl.*(..))")
    public void repository(){}

    @Around("controller() || service() || repository()")
    public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Class clazz = methodSignature.getDeclaringType();
            Method method = methodSignature.getMethod();
            log.info(formatter("[CLASS] {}"), clazz.getSimpleName());
            log.info(formatter("[METHOD] {}"), method.getName());

            Object[] args = joinPoint.getArgs();
            IntStream.range(0, args.length)
                    .filter(i -> args[i] != null)
                    .forEach(i -> log.info(formatter("[PARAMETER_{}] {} {} = {}"),
                            i,
                            methodSignature.getParameterTypes()[i].getSimpleName(),
                            methodSignature.getParameterNames()[i],
                            args[i]));
            log.info(formatter("[RETURN] {}"),methodSignature.getReturnType().getSimpleName());
            log.info(formatter("[TIMER] {}ms"), timeMs);
            log.info("");
        }
    }

    public static String formatter(String msg) {
        StringBuilder sb = new StringBuilder();
        String prefix = String.valueOf(sb.append(msg.split("]")[0]).append("]"));
        sb.setLength(0);
        String body = msg.split("]")[1];
        String formatted = String.format("%-13s", prefix);

        return String.valueOf(sb.append(formatted).append(" ").append(body));
    }
}
