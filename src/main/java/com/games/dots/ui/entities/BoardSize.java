package com.games.dots.ui.entities;

public enum BoardSize {
	Large(30), 
	Medium(20), 
	Small(10);
	
	public final int value;
	private BoardSize(int value){
		this.value = value;
	}
}
