package com.games.dots.logic;

import com.games.dots.ui.entities.Coordinates;

public class Dot {
	private String m_color;
	private Coordinates m_coordinates;
	public String getColor() {
		return m_color;
	}
	public void setColor(String m_color) {
		this.m_color = m_color;
	}
	public Coordinates getCoordinates() {
		return m_coordinates;
	}
	public void setCoordinates(Coordinates m_coordinates) {
		this.m_coordinates = m_coordinates;
	}
}
