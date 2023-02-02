package com.example.carrental.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.example.carrental.dto.TokenResponse;
import com.example.carrental.dto.UserRequest;
import com.example.carrental.entity.RoleEnum;
import com.example.carrental.entity.User;
import com.example.carrental.internal.JwtConfiguration;
import com.example.carrental.internal.Result;
import com.example.carrental.internal.ResultError;
import com.example.carrental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtConfiguration jwtConfiguration;

    public Result<TokenResponse> login(UserRequest userRequest){
        return Result.from(() -> userRepository.findByEmail(userRequest.getEmail()))
                .checkIf(Optional::isPresent, new ResultError(HttpStatus.BAD_REQUEST, "Unable to find an account with this email or password"))
                .onSuccess(Optional::get)
                .bind(user -> Result.from(() -> createHash(userRequest.getPassword()))
                        .checkIf(Optional::isPresent, new ResultError())
                        .checkIf(passwordHash -> user.getPasswordHash().equals(passwordHash.get()),
                                new ResultError(HttpStatus.BAD_REQUEST, "Unable to find an account with this email or password"))
                        .onSuccess(passwordHash -> createJwt(user.getId(), user.getRole()))
                        .checkIf(Optional::isPresent, new ResultError())
                        .onSuccess(token -> new TokenResponse(token.get()))
                );
    }

    public Result register(UserRequest userRequest){
        return Result.from(() -> userRepository.findByEmail(userRequest.getEmail()))
                .checkIf(Optional::isEmpty, new ResultError(HttpStatus.BAD_REQUEST, "An account with this email already exists"))
                .onSuccess(user -> createHash(userRequest.getPassword()))
                .checkIf(Optional::isPresent, new ResultError())
                .onSuccess(passwordHash -> User.create(userRequest.getEmail(), passwordHash.get(), RoleEnum.Client))
                .onSuccess(user -> userRepository.save(user));
    }

    private Optional<String> createHash(String password) {
        MessageDigest algorithm = null;
        try {
            algorithm = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            return Optional.empty();
        }

        algorithm.update("carRental".getBytes(StandardCharsets.UTF_8));
        var result = algorithm.digest(password.getBytes(StandardCharsets.UTF_8));

        return Optional.of(Base64.getEncoder().encodeToString(result));
    }

    private Optional<String> createJwt(int userId, RoleEnum role) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtConfiguration.getSecret());
            String token = JWT.create()
                    .withIssuer("car-rental")
                    .withClaim("userId", userId)
                    .withClaim("role", RoleEnum.getLabel(role))
                    .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000))
                    .sign(algorithm);

            return Optional.of(token);
        } catch (JWTCreationException exception){
            return Optional.empty();
        }
    }
}

