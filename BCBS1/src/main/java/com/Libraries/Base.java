package com.Libraries;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;



public class Base {

	final static Logger logger = LogManager.getLogger(Base.class);

	private static JavaPropertiesManager readProperty;
	public static MainLibrary myLibrary;
	private static String browser;
	private static String isAutoSendEmail;
	private static String isScreenShotOpen;
	private static JavaPropertiesManager readProperty2;

	public static WebDriver driver;

	@BeforeClass
	public void beforeAllTestStarts() {
		readProperty = new JavaPropertiesManager("src/test/resources/config.properties");
		browser = readProperty.readProperty("browserType");
		isAutoSendEmail = readProperty.readProperty("autoEmail");
		isScreenShotOpen = readProperty.readProperty("isScreenShotOpen");

		myLibrary = new MainLibrary(driver);
		readProperty2 = new JavaPropertiesManager("src/test/resources/dynamicConfig.properties");
		String tempTime = myLibrary.getCurrentTime();
		readProperty2.setProperty("sessionTime", tempTime);
		logger.info("Session Time: " + tempTime);

	}

	@AfterClass
	public void afterAllTestCompleted() {
		if (driver != null) {
			driver.quit();
		}

		// Sending all the reports, screenshots and log files via email
		List<String> screenshots = new ArrayList<>();
		EmailManager emailSender = new EmailManager();
		emailSender.attachmentFiles.add("target/logs/log4j-selenium.log");
		emailSender.attachmentFiles.add("target/logs/Selenium-Report.html");
		screenshots = myLibrary.automaticallyAttachErrorImgToEmail();
		if (screenshots.size() != 0) {
			for (String attachFile : screenshots) {
				emailSender.attachmentFiles.add(attachFile);
			}
		}
		if (isAutoSendEmail.contains("true")) {
			emailSender.sendEmail(emailSender.attachmentFiles);
		}
		
		logger.info("Error is ");
	}

	@BeforeMethod
	public WebDriver beforeEachTestStart() {
		driver = myLibrary.startLocalBrowser(browser);

		return driver;
	}

	@AfterMethod
	public void afterEachTestEnd(ITestResult result) {
		try {
			if (ITestResult.FAILURE == result.getStatus()) {
				myLibrary.captureScreenshot(result.getName(), "target/screenshots/");
			}
			Thread.sleep(5 * 1000);

			driver.close(); // close the browser
			driver.quit(); // kills/deletes the driver object

		} catch (Exception e) {
			logger.error("Error: ", e);
		}

	}

}
