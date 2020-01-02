package com.Libraries;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.remote.Augmenter;

import com.google.common.io.Files;
import com.pages.HomePage;



public class MainLibrary {
	final static Logger logger = LogManager.getLogger(HomePage.class);

	private WebDriver driver;
	public List<String> errorScreenshots;
	private boolean isScreenShotOpen = false;
	
	public MainLibrary(WebDriver _driver) {
		driver = _driver;
	}

	public List<String> automaticallyAttachErrorImgToEmail() {
		List<String> fileNames = new ArrayList<>();
		JavaPropertiesManager propertyReader = new JavaPropertiesManager("src/test/resources/dynamicConfig.properties");
		String tempTimeStamp = propertyReader.readProperty("sessionTime");
		String numberTimeStamp = tempTimeStamp.replaceAll("_", "");
		long testStartTime = Long.parseLong(numberTimeStamp);

		// first check if error-screenshot folder has file
		File file = new File("target/screenshots");
		if (file.isDirectory()) {
			if (file.list().length > 0) {
				File[] screenshotFiles = file.listFiles();
				for (int i = 0; i < screenshotFiles.length; i++) {
					// checking if file is a file, not a folder
					if (screenshotFiles[i].isFile()) {
						String eachFileName = screenshotFiles[i].getName();
						logger.info("Testing file names: " + eachFileName);
						int indexOf20 = searchSubstringInString("20", eachFileName);
						String timeStampFromScreenshotFile = eachFileName.substring(indexOf20,
								eachFileName.length() - 4);
						logger.info("Testing file timestamp: " + timeStampFromScreenshotFile);
						String fileNumberStamp = timeStampFromScreenshotFile.replaceAll("_", "");
						long screenshotfileTime = Long.parseLong(fileNumberStamp);

						testStartTime = Long.parseLong(numberTimeStamp.substring(0, 14));
						screenshotfileTime = Long.parseLong(fileNumberStamp.substring(0, 14));
						if (screenshotfileTime > testStartTime) {
							fileNames.add("target/screenshots/" + eachFileName);
						}
					}
				}

			}
		}
		errorScreenshots = fileNames;
		return fileNames;
	}
	
	public void enterTextField(WebElement element, String value) {
		try {
			element.clear();
			element.sendKeys(value);
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
	}
	
	
	public int searchSubstringInString(String target, String message) {
		int targetIndex = 0;
		for (int i = -1; (i = message.indexOf(target, i + 1)) != -1;) {
			targetIndex = i;
			break;
		}
		return targetIndex;
	}
	
	public WebDriver startLocalBrowser(String browser) {
		if (browser.contains("Chrome")) {
			driver = startChromeBrowser();
		} else if (browser.contains("Firefox")) {
			// start Firefox browser
			driver = startFireFoxBrowser();
			
		} else {
			// other browsers we don't support with this version of library
			logger.info("Ops, Sorry, we don't support the browser: [" + browser + "], please contact Automation Team.");
		}
		return driver;
	}
	
	private WebDriver startChromeBrowser() {
		try {
			System.setProperty("webdriver.chrome.driver", "src/main/resources/browsers/chromedriver.exe");
			driver = new ChromeDriver();
			logger.info("Chrome browser is starting...");
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.manage().window().maximize();
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		return driver;
	}
	
	private WebDriver startFireFoxBrowser() {
		try {
			System.setProperty("webdriver.gecko.driver", "src/main/resources/browsers/geckodriver.exe");
			driver = new FirefoxDriver();
			logger.info("Firefox browser is starting...");
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.manage().window().maximize();
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		return driver;
	}
	
	public String getCurrentTime() {
		String finalTimeStamp = null;
		Date date = new Date();
		String tempTime = new Timestamp(date.getTime()).toString();
		// logger.info("original time stamp is: [" +tempTime+ "]");
		finalTimeStamp = tempTime.replace(":", "_").replace(" ", "_").replace(".", "_").replace("-", "_");
		// logger.info("updated time stamp is: [" +finalTimeStamp+ "]");
		// tempTime.replace(':', '_').replace(' ', '_').replace('.', '_');
		return finalTimeStamp;
	}
	
	private String checkDirectory(String inputPath) {
		File file = new File(inputPath);
		String abPath = file.getAbsolutePath();
		File file2 = new File(abPath);
		if (!file2.exists()) {
			if (file2.mkdirs()) {
				logger.info("folders created...");
			} else {
				logger.info("folders Not created...");
			}

		}
		return abPath;
	}
	
	public String captureScreenshot(String screenshotFileName, String filePath) {
		String screenshotPath = null;
		if (isScreenShotOpen == true) {
			driver = new Augmenter().augment(driver);
		}
		String timestamp = getCurrentTime();
		try {
			if (!filePath.isEmpty()) {
				checkDirectory(filePath);
				screenshotPath = filePath + screenshotFileName + timestamp + ".png";
			} else {
				checkDirectory("target/screenshots/");
				screenshotPath = "target/screenshots/" + screenshotFileName + timestamp + ".png";
			}

			File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			Files.copy(srcFile, new File(screenshotPath));
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		logger.info("Screenshot Captured: " + screenshotPath);
		return screenshotPath;
	}
	
	public WebDriver switchToWindow(int index) {

		try {
			Set<String> allBrowsers = driver.getWindowHandles();
			Iterator<String> iterator = allBrowsers.iterator();
			List<String> windowHandles = new ArrayList<>();
			while (iterator.hasNext()) {
				String window = iterator.next();
				windowHandles.add(window);
			}
			// switch to index N
			driver.switchTo().window(windowHandles.get(index));
			// highlightElement(By.tagName("body"));
		} catch (Exception e) {
			logger.error("Error: ", e);
			assertTrue(false);
		}
		return driver;
	}
	
	public Alert isAlertPresent() {
		Alert alert = null;
		try {
			alert = driver.switchTo().alert();
			logger.info("Alert detected: {" + alert.getText() + "}");
		} catch (Exception e) {
			logger.info("Alert Not detected!");
		}
		return alert;
	}
	
	
	
	
	
	
	
	
	
	
}
