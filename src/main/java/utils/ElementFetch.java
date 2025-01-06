package utils;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import base.baseTest;

public class ElementFetch {

	public WebElement getWebElement(String type,String value) {
		switch(type) {
		case "xpath":
			return baseTest.driver.findElement(By.xpath(value));
		case "css":
			return baseTest.driver.findElement(By.xpath(value));
		default:
			return null;
		}


	}
	
	public List<WebElement> getWebElements(String type,String value) {
		switch(type) {
		case "xpath":
			return baseTest.driver.findElements(By.xpath(value));
		case "css":
			return baseTest.driver.findElements(By.xpath(value));
		default:
			return null;
		}


	}
}
