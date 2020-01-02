package com.Libraries;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pages.HomePage;


public class JavaPropertiesManager {
	final static Logger logger = LogManager.getLogger(HomePage.class);

	public String propertiesFile;
	public Properties prop;
	public OutputStream output;
	public InputStream input;
	
	public JavaPropertiesManager(String propertiesFilePath) {
		propertiesFile = propertiesFilePath;
		prop = new Properties();
	}
	
	public String readProperty(String key) {
		String value = null;
		try {
			input = new FileInputStream(propertiesFile);
			prop.load(input);
			value = prop.getProperty(key);
		} catch (Exception e) {
			logger.error("Error: ", e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (Exception e) {
					logger.error("Error: ", e);
				}
			}
		}
		return value;
	}
	
	public void setProperty(String key, String value){
		try{
			output = new FileOutputStream(propertiesFile);
			prop.setProperty(key, value);
			prop.store(output, null);
		}catch (Exception e)
		{
			logger.error("Error: ", e);
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (Exception e) {
					logger.error("Error: ", e);
				}
			}
		}		
	}
	
	
}
