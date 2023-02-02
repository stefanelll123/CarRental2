package com.example.carrental.internal;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Result<TSuccess> {
    private Optional<TSuccess> value;
    private Optional<ResultError> error;

    private Result(TSuccess value, ResultError error) {
        this.value = Optional.ofNullable(value);
        this.error = Optional.ofNullable(error);
    }

    public static <U> Result<U> ok(U value) {
        return new Result<>(value, null);
    }

    public static <U> Result<U> error(ResultError error) {
        return new Result<>(null, error);
    }

    public boolean isError() {
        return error.isPresent();
    }

    public boolean isSuccessful() {
        return error.isEmpty();
    }

    public TSuccess getValue() {
        return value.get();
    }

    public ResultError getError() {
        return error.get();
    }

    public <U> Result<U> onSuccess(Function<TSuccess, U> mapper) {
        if (this.isError()) {
            return Result.error(this.error.get());
        }

        try {
            return Result.ok(mapper.apply(value.get()));
        }
        catch (Exception e) {
            return Result.error(new ResultError(e.getMessage()));
        }
    }

    public Result<TSuccess> tap(Consumer<TSuccess> consumer) {
        if (this.isSuccessful()) {
            consumer.accept(this.getValue());
        }

        return this;
    }

    public Result<TSuccess> onError(Consumer<ResultError> consumer) {
        if (this.isError()) {
            consumer.accept(this.getError());
        }

        return this;
    }

    public Result<TSuccess> checkIf(Predicate<TSuccess> predicate, ResultError error) {
        if (this.isError()) {
            return Result.error(this.error.get());
        }

        if (!predicate.test(this.value.orElse(null))) {
            return Result.error(error);
        }

        return this;
    }

    public static <U> Result<U> from(Supplier<U> supplier) {
        try {
            return Result.ok(supplier.get());
        }
        catch (Exception e) {
            return Result.error(new ResultError(e.getMessage()));
        }
    }
    public <U> Result<U> bind(Function<TSuccess, Result<U>> mapper) {
        if (this.isError()) {
            return Result.error(this.error.get());
        }

        try {
            return mapper.apply(value.get());
        }
        catch (Exception e) {
            return Result.error(new ResultError(e.getMessage()));
        }
    }
}
