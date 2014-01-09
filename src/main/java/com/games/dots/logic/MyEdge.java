package com.games.dots.logic;

import org.jgrapht.graph.DefaultWeightedEdge;

public class MyEdge extends DefaultWeightedEdge{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MyEdge()
	{
		
	}
	
	public Object getSource(){
		return super.getSource();
	}
	
	public Object getTarget(){
		return super.getTarget();
	}
	
	public double getWeight(){
		return super.getWeight();
	}
}
