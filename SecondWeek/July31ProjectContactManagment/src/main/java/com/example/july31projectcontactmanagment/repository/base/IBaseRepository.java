package com.example.july31projectcontactmanagment.repository.base;

import java.util.List;

public interface IBaseRepository<IEntity> {
    void add(IEntity entity);

    void update(IEntity entity);

    void delete(int id);

    List<IEntity> getAll();

    IEntity getById(int id);
}
