package com.example.tasklisttwo.web.mappers;

import java.util.List;

public interface Mappable <E, D>{

    D toDTO(E entity);

    List<D> toDTO(List<E> entities);

    E toEntity(D dto);

    List<E> toEntity(List<D> dto);

}
