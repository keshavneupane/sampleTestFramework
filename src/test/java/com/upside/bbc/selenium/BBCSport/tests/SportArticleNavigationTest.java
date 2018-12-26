package com.upside.bbc.selenium.BBCSport.tests;


import com.upside.bbc.selenium.DriverBase;
import com.upside.bbc.selenium.BBCSport.Constants;
import com.upside.bbc.selenium.BBCSport.pages.BBCSportHomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

// groups  helps to group test cases easily for execution

@Test(groups = {"BBCSportStory"})
public class SportArticleNavigationTest extends DriverBase {

    // load the home page and click on Sport tab from the top navigation

    @Test(priority = 1)
    public void navigateToSport() throws Exception{
        LOG.info("Load Home Page");
        BBCSportHomePage.loadHomePage();
        if((isElementDisplayedxpath(Constants.GOTO_SPORT)))
            //clickByxpath(Constants.GOTO_SPORT);
            BBCSportHomePage.sportHomePage();
    }

    // Check if the promo article appears on the top under sports section

    @Test(priority = 2)
    public void checkFirstPromoArticle() throws Exception {
        Assert.assertTrue(isElementDisplayedCSS(Constants.PROMOARTICLE_CSS));

    }

    /* click  and verify the promo article opens when clicking on the article
     by comparing the url on href and url of the article opened.
     */

    @Test(priority = 3)
    public void clickFirstPromoArticle() throws Exception {
        String href=getAttributeValue(Constants.PROMOARTICLE_CSS,"href");
        System.out.println(href);
        clickByClass(Constants.PROMOARTICLE_CSS);
        String CurrentURL=getURLCurrent();
        System.out.println(CurrentURL);
        if (href.equals(CurrentURL)){
            Assert.assertTrue(true, "CurrentURL is the desired href URL");
        } else {
            Assert.assertTrue(false, "Check the Article URL");
        }
    }

}
