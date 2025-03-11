package org.example.pageObjects;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.example.util.SeleniumHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class YahooPage
{
    public ExtentTest test;
    private final SeleniumHelper helper;
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(xpath = "//input[@type='text']")
    private WebElement searchBox;

    @FindBy(xpath = "//li[@data-id='result-quotes-0']")
    private WebElement TSLAFirstSuggestion;

    @FindBy(xpath = "//ul[@role='listbox']")
    private List<WebElement> autoSuggestionsList;


    @FindBy(css = "[data-test='left-summary-table'] span[data-field='regularMarketPrice']")
    private WebElement stockPrice;

    @FindBy(xpath = "//td[@data-test='PREV_CLOSE-value']/span")
    private WebElement previousClose;

    @FindBy(xpath = "//td[@data-test='TD_VOLUME-value']/span")
    private WebElement volume;

    @FindBy(xpath = "//span[@data-testid='qsp-pre-price']")
    private WebElement preMarketPrice;

    @FindBy(xpath = "//span[@data-testid='qsp-price']")
    private WebElement atCloseMarketPrice;

    @FindBy(xpath = "//li[@class='yf-1jj98ts'][1]")
    private WebElement previousClosePrice;

    @FindBy(xpath = "//li[@class='yf-1jj98ts'][2]")
    private WebElement openPrice;

    @FindBy(xpath = "//li[@class='yf-1jj98ts'][3]")
    private WebElement bidPrice;

    @FindBy(xpath = "//li[@class='yf-1jj98ts'][4]")
    private WebElement dayRange;

    @FindBy(xpath = "//li[@class='yf-1jj98ts'][5]")
    private WebElement stockVolume;
    @FindBy(xpath = "//li[@class='yf-1jj98ts'][6]")
    private WebElement marketCap;




    String  url = "https://finance.yahoo.com/quote/TSLA/";
    String expectedStockPrice = "200";

    public YahooPage(WebDriver driver)
    {
        this.helper = new SeleniumHelper(driver);
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }


    /**
     * This method is used to verify TSLA stock
     * First it will search the TSLA stock
     * Then it will verify the auto suggestion and click on it
     * Then it will verify the stock price, previous close and volume etc
     * @return
     */


    public boolean verifyTSLAStock(String stockName)
    {
        boolean isVerifyTSLAStock = false;
        try
        {
            test.log(Status.INFO, "Starting to verify TSLA stock");
            helper.isDisplayed(searchBox,20);
            test.log(Status.INFO, "SearchBox Displayed Successfully.");
            helper.sendKeys(searchBox, stockName,20);
            test.log(Status.INFO, "Entered the Stockname in the searchbox.");
            helper.isDisplayed(TSLAFirstSuggestion,20);
            test.log(Status.INFO, "Autosuggestion displayed in the searchbox.");
            helper.click(TSLAFirstSuggestion,20);
            test.log(Status.INFO, "Clicked on the first auto suggestion.");

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5000));
            helper.isDisplayed(atCloseMarketPrice,20);

            String stockPriceText = helper.getText(atCloseMarketPrice, 10);
            test.log(Status.INFO, "Stock Price: " + stockPriceText);

            try {
                double stockPrice = Double.parseDouble(stockPriceText);
                // Verifying the stock price
                if (stockPrice > 200) {
                    test.log(Status.PASS, "Stock price is greater than 200.");
                } else {
                    test.log(Status.FAIL, "Stock price is less than 200.");
                }
            } catch (NumberFormatException e) {
                test.log(Status.FAIL, "Failed to parse stock price: " + stockPriceText);
            }

            //Verifying other details

            helper.isDisplayed(previousClosePrice,20);
            String previousClosePriceDetails = helper.getText(previousClosePrice,20);
            test.log(Status.INFO, "Previous Close Price: "+previousClosePriceDetails);

            helper.isDisplayed(openPrice,20);
            String openPriceDetails = helper.getText(openPrice,20);
            test.log(Status.INFO, "Open Price: "+openPriceDetails);

            helper.isDisplayed(bidPrice,20);
            String bidPriceDetails = helper.getText(bidPrice,20);
            test.log(Status.INFO, "Bid Price: "+bidPriceDetails);

            helper.isDisplayed(dayRange,20);
            String dayRangeDetails = helper.getText(dayRange,20);
            test.log(Status.INFO, "DayRange details: "+dayRangeDetails);

            helper.isDisplayed(stockVolume,20);
            String stockVolumeDetails = helper.getText(stockVolume,20);
            test.log(Status.INFO, "Stock Volume: "+stockVolumeDetails);

            helper.isDisplayed(marketCap,20);
            String marketCapDetails = helper.getText(marketCap,20);
            test.log(Status.INFO, "MarketCap Details: "+marketCapDetails);


            isVerifyTSLAStock = true;


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return isVerifyTSLAStock;
    }

}
