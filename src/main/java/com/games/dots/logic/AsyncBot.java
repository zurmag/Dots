package com.games.dots.logic;


import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.games.dots.ui.entities.GameMessage;

public abstract class AsyncBot implements Observer, IRobot{
	
	protected Game m_game;
	private Executor m_executor = new ThreadPoolExecutor(1,2,2,TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>());
	public AsyncBot(Game game) {
		game.onPlayerMove.addObserver(this);
		m_game = game;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		m_executor.execute(new AsyncTask(this, arg) {
			
			@Override
			public void run() {
				m_robot.onPlayerMove((GameMessage)m_data);
				
			}
		});
	}
	
	private abstract class AsyncTask implements Runnable{

		protected Object m_data;
		protected IRobot m_robot;
		public AsyncTask (IRobot robot, Object data){
			m_robot = robot;
			m_data = data;
		}
		
	}

}