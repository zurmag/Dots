package com.games.dots.utilities;

import java.io.IOException;
import java.util.Properties;

public class ConfigurationManager {
	private Properties m_properies;
	public ConfigurationManager(){
		m_properies = new Properties();
		 
    	try {
               //load a properties file from class path, inside static method
    		m_properies.load(ConfigurationManager.class.getClassLoader().getResourceAsStream("config.properties"));
 
    	} catch (IOException ex) {
    		ex.printStackTrace();
        }
	}
	
	public String getFbAppId(){
		return m_properies.getProperty("FbAppId");
	}
	
	public String getFbSecretKey(){
		return m_properies.getProperty("FbSecretKey");
	}
	
	public String getFbCanvasPage (){
		return m_properies.getProperty("FbCanvasPage");
	}
	
	public String getFbCanvasUrl(){
		return m_properies.getProperty("FbCanvasUrl");
	}
	
	
}
