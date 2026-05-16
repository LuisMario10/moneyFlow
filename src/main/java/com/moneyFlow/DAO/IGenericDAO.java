package com.moneyFlow.DAO;

import java.util.List;

public interface IGenericDAO<T> {
    public int create(T t);
    public void update(T t);
    public void delete(int id);
    public T findByID(int id);
    public List<T> findAll();
}