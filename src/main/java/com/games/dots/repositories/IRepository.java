package com.games.dots.repositories;

public interface IRepository<T> {
	T get(String id);
	T Create();
	void Remove(String id);
}
