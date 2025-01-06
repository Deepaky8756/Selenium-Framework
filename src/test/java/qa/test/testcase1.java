package qa.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.baseTest;
import pageEvents.HomePageEvents;
import utils.ElementFetch;

public class testcase1 extends baseTest {
	ElementFetch ele = new ElementFetch();
	HomePageEvents hm = new HomePageEvents();
  @Test
  public void f1() {
	  hm.SignInButton();
  }
  @Test
  public void f2() {
	  boolean condition = true; // This is deliberately set to 'true'
      Assert.assertFalse(condition, "The condition is true, so the test will fail.");
  }
}
