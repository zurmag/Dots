package com.games.dots.repositories;

public interface IRepository<T, ID_TYPE> {
	T get(ID_TYPE id);
	T get(String id);
	T add(ID_TYPE id, T obj);
	String add(T obj);
	void remove(ID_TYPE id);
	void remove(String id);
}
