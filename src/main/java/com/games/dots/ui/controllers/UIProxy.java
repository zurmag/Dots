package com.games.dots.ui.controllers;

import java.util.Observable;
import java.util.Observer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.games.dots.ui.entities.GameMessage;

public class UIProxy implements Observer {

	private SimpMessagingTemplate m_template;
	
	@Autowired
	public UIProxy(SimpMessagingTemplate template){
		this.m_template = template;
	}
	
	@Override
	public void update(Observable observable, Object arg) {
		GameMessage gameMessage = (GameMessage)arg;
		m_template.convertAndSend("/sub/games/" + gameMessage.gameId, gameMessage);
	}

}
