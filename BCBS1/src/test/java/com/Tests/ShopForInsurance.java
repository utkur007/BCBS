package com.Tests;

import org.testng.annotations.Test;
 
import com.Libraries.Base;
import com.pages.CareFirstPage;
import com.pages.HomePage;

public class ShopForInsurance extends Base {

	HomePage HomePageTest = new HomePage();
	CareFirstPage CareFirstTest = new CareFirstPage();
	
	@Test
	public void InsuranceShopTest() {
		HomePageTest.BCBSHomePage();
		HomePageTest.ShopForInsurance();
		CareFirstTest.CareFirstHomePage();
	}
	
}

