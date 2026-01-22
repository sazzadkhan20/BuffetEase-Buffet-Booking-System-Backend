package com.BuffetEase.interfaces;

public interface IRepository<T>
{
    public  void find(int id);
    public  void findAll();
    public  void add(T t);
    public  void delete(int id);
    public  void update(T t);
}
