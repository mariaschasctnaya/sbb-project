package com.tsystems.train.facade.converter;

import org.springframework.lang.NonNull;

@FunctionalInterface
public interface DtoConverter<S, T> {
    T convert(@NonNull S source);
}
