package com.pages;

import static org.testng.Assert.assertEquals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import com.Libraries.Base;

public class HomePage extends Base {

	final static Logger logger = LogManager.getLogger(HomePage.class);

	// #Locations in HomePage
	private By ShopForInsuranceBtn = By.id("block-homezipcodeblock");
	private By ZipCodeBox = By.id("edit-zip--2");
	private By Go = By.id("edit-submit-zip--2");
	private By CareFirst = By.partialLinkText("CareFirst BlueCross BlueShield");

	public void BCBSHomePage() {
		logger.info("directing to BCBS.com...");
		driver.get("https://www.bcbs.com");
		logger.info("lannded on BCBS.com");
		String ActualBCBSTitle = driver.getTitle();
		System.out.println("Actual Page title of this page is " + ActualBCBSTitle);
		assertEquals(ActualBCBSTitle, "Blue Cross Blue Shield");

	}

	public void ShopForInsurance() {
		driver.findElement(ShopForInsuranceBtn).click();
		logger.info("Clicked Shop for insurance button");

		driver.findElement(ZipCodeBox).click();
		driver.findElement(ZipCodeBox).sendKeys("22033");
		logger.info("ZipCode Written down");

		driver.findElement(Go).click();
		logger.info("Clicked Go Button");

		driver.findElement(CareFirst).click();
		logger.info("Care First Button is clicked ");

		myLibrary.switchToWindow(1);
		
		

	}

}
