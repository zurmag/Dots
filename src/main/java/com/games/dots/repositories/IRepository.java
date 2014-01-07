package com.games.dots.repositories;

public interface IRepository<T> {
	T get(String id);
	T add(T obj);
	void remove(String id);
}
