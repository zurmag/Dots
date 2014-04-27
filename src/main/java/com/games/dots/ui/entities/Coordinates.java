package com.games.dots.ui.entities;

import java.io.Serializable;

public class Coordinates implements Serializable{
	/**
	 * 
	 */
	
	public Coordinates() {
	}
	public Coordinates(int x, int y){
		this.x = x; this.y = y;
	}
	
	private static final long serialVersionUID = 1L;
	public int x;
	public int y;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordinates other = (Coordinates) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		return String.format("x: %s, y: %s", x, y); 
	}
}
