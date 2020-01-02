package com.pages;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;


import com.Libraries.Base;

public class CareFirstPage extends Base {

	final static Logger logger = LogManager.getLogger(CareFirstPage.class);
	private By Plans_Coverage = By.xpath("//*[@id=\"hammer-events\"]/div[1]/div[2]/ul/li[2]/a/span[1]");
	private By DoNotShowAgainBtn = By.id("doNotShowModal");
	private By Individual_Family_Plans = By.partialLinkText("Individual & Family Plans");
	
	
	public void CareFirstHomePage() {
		
		driver.findElement(DoNotShowAgainBtn).click();
		logger.info("Do not Show again Btn clicked");
		
		driver.findElement(Plans_Coverage).click();
		logger.info("Plans&Coverage btn is clicked");
		
		driver.findElement(Individual_Family_Plans).click();
		logger.info("Individual & Family Plans Button has been clicked");
		
	}

}
