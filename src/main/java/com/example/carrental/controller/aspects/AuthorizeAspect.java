package com.example.carrental.controller.aspects;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.carrental.internal.JwtConfiguration;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;

@Aspect
@Component
public final class AuthorizeAspect {

    @Autowired
    private JwtConfiguration jwtConfiguration;

    @Around("execution(@Authorize * *.*(..))")
    private Object isAuthorized(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            String authorization = request.getHeader("authorization");

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();

            Authorize myAnnotation = method.getAnnotation(Authorize.class);

            if (authorization == null || !validateJwtToken(authorization, !myAnnotation.role().equals("") ? myAnnotation.role() : null)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ignored) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return joinPoint.proceed();
    }

    private boolean validateJwtToken(String token, String role) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtConfiguration.getSecret());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("car-rental")
                    .build();

            var decoded = verifier.verify(token);
            if (decoded.getExpiresAt().before(new Date())) {
                return false;
            }

            if (role != null && !decoded.getClaim("role").asString().equals(role)) {
                return false;
            }
        } catch (JWTVerificationException e) {
            return false;
        }

        return true;
    }
}
