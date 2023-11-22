package com.manager.social_network.common.mapper;

import java.util.List;

public interface GenericMapper<S, T> {
    T dtoToEntity(S dto);
    S entityToDto(T entity);
    List<S> listEntityToDto(List<T> entity);
    List<T> listDtoToEntity(List<S> dto);
}
