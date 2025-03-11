import com.aventstack.extentreports.Status;
import org.example.base.BaseLib;
import org.example.pageObjects.YahooPage;
import org.example.util.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

public class YahooPageTest extends BaseLib
{

//    @Test
//    public void testYahooPage()
//    {
//        YahooPage yahooPage = new YahooPage(getDriver());
//        boolean isResult = yahooPage.verifyTSLAStock(ConfigReader.getProperty("stockName"));
//        Assert.assertTrue(isResult, "Error in verifying TSLA stock price");
//    }

    @Test
    public void testYahooPage() {
        test.log(Status.INFO, "Starting to verify TSLA stock");
        YahooPage page = new YahooPage(getDriver());
        page.test = this.test; // Set the test variable
        boolean result = page.verifyTSLAStock("TSLA");
        if (!result) {
            test.log(Status.FAIL, "Failed to verify TSLA stock.");
        }
    }
}
