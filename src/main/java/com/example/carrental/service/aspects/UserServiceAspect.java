package com.example.carrental.service.aspects;

import com.auth0.jwt.JWT;
import com.example.carrental.dto.TokenResponse;
import com.example.carrental.internal.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class UserServiceAspect {
    private Map<Object, TokenResponse> cache;

    public UserServiceAspect() {
        cache = new HashMap<>();
    }

    @Around("execution(* com.example.carrental.service.UserService.login(..))")
    private Object cache(ProceedingJoinPoint joinPoint) throws Throwable {
        var hash = joinPoint.getArgs()[0].hashCode();

        if(cache.containsKey(hash) && cache.get(hash) != null) {
            var tokenResponse = cache.get(hash);
            var token = JWT.decode(tokenResponse.getToken());
            if (!token.getExpiresAt().before(new Date())) {
                return Result.ok(tokenResponse);
            }
        }

        var response = joinPoint.proceed();
        if (response instanceof Result && ((Result<?>) response).getValue() instanceof TokenResponse) {
            var token = (Result<TokenResponse>) response;
            cache.put(hash, token.getValue());
        }

        return response;
    }
}
