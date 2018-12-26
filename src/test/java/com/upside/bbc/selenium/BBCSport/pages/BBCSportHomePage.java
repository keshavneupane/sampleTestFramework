package com.upside.bbc.selenium.BBCSport.pages;

import com.upside.bbc.selenium.BBCSport.Constants;
import com.upside.bbc.selenium.DriverBase;

public class BBCSportHomePage extends DriverBase {

    // Load Home page
    public static void loadHomePage() throws Exception {
        getDriver().get(Constants.BASE_URL);
    }
    // navigate to sports section
    public static void sportHomePage() throws Exception {
        clickByxpath(Constants.GOTO_SPORT);
    }
}
