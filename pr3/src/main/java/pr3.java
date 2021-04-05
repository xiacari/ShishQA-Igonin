import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;

public class pr3 {
    private WebDriver driver;
    private String baseUrl;


    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", "src\\test\\java\\chromedriver.exe");
        driver = new ChromeDriver();
        baseUrl = "https://sandbox.cardpay.com/MI/cardpayment2.html?orderXml=PE9SREVSIFdBTExFVF9JRD0nODI5OScgT1JERVJfTlVNQkVSPSc0NTgyMTEnIEFNT1VOVD0nMjkxLjg2JyBDVVJSRU5DWT0nRVVSJyAgRU1BSUw9J2N1c3RvbWVyQGV4YW1wbGUuY29tJz4KPEFERFJFU1MgQ09VTlRSWT0nVVNBJyBTVEFURT0nTlknIFpJUD0nMTAwMDEnIENJVFk9J05ZJyBTVFJFRVQ9JzY3NyBTVFJFRVQnIFBIT05FPSc4NzY5OTA5MCcgVFlQRT0nQklMTElORycvPgo8L09SREVSPg==&sha512=998150a2b27484b776a1628bfe7505a9cb430f276dfa35b14315c1c8f03381a90490f6608f0dcff789273e05926cd782e1bb941418a9673f43c47595aa7b8b0d";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void testConfirmedTestCase() throws Exception {
        Actions action = new Actions(driver);
        driver.get(baseUrl);
        driver.findElement(By.id("input-card-number")).sendKeys("4000000000000002");
        driver.findElement(By.id("input-card-holder")).sendKeys("IVAN IGONIN");
        new Select(driver.findElement(By.id("card-expires-month"))).selectByVisibleText("07");
        new Select(driver.findElement(By.id("card-expires-year"))).selectByVisibleText("2037");
        driver.findElement(By.id("input-card-cvc")).sendKeys("777");
        WebElement cvcHint = driver.findElement(By.xpath("//*[@id=\"cvc-hint-toggle\"]"));
        action.moveToElement(cvcHint).build().perform();
        driver.findElement(By.id("action-submit")).click();
        driver.findElement(By.id("success")).submit();
        assertEquals(driver.findElement(By.xpath("//div[@id='payment-item-status']/div[2]")).getText(), "Confirmed");
        assertEquals(driver.findElement(By.xpath("//div[@id='payment-item-cardholder']/div[2]")).getText(), "IVAN IGONIN");
        assertEquals(driver.findElement(By.id("payment-item-total-amount")).getText(), "291.86");

    }

    @Test
    public void testDeclinedTestCase() throws Exception {
        driver.get(baseUrl);
        driver.findElement(By.id("input-card-number")).sendKeys("5555555555554444");
        driver.findElement(By.id("input-card-holder")).sendKeys("IVAN IGONIN");
        new Select(driver.findElement(By.id("card-expires-month"))).selectByVisibleText("07");
        new Select(driver.findElement(By.id("card-expires-year"))).selectByVisibleText("2037");
        driver.findElement(By.id("input-card-cvc")).sendKeys("007");
        driver.findElement(By.id("action-submit")).click();
        driver.findElement(By.id("failure")).submit();
        assertEquals(driver.findElement(By.xpath("//div[@id='payment-item-status']/div[2]")).getText(), "Declined by issuing bank");
        assertEquals(driver.findElement(By.xpath("//div[@id='payment-item-cardholder']/div[2]")).getText(), "IVAN IGONIN");
        assertEquals(driver.findElement(By.id("payment-item-total-amount")).getText(), "291.86");
    }

    @Test
    public void testAuthorizedTestCase() throws Exception {
        driver.get(baseUrl);
        driver.findElement(By.id("input-card-number")).sendKeys("4000000000000077");
        driver.findElement(By.id("input-card-holder")).sendKeys("IVAN IGONIN");
        new Select(driver.findElement(By.id("card-expires-month"))).selectByVisibleText("07");
        new Select(driver.findElement(By.id("card-expires-year"))).selectByVisibleText("2037");
        driver.findElement(By.id("input-card-cvc")).sendKeys("007");
        driver.findElement(By.id("action-submit")).click();

        assertEquals(driver.findElement(By.xpath("//div[@id='payment-item-status']/div[2]")).getText(), "Confirmed");
        assertEquals(driver.findElement(By.xpath("//div[@id='payment-item-cardholder']/div[2]")).getText(), "IVAN IGONIN");
        assertEquals(driver.findElement(By.id("payment-item-total-amount")).getText(), "291.86");
    }

    @Test
    public void testCardSnotValidTestCase() throws Exception {
        driver.get(baseUrl);
        driver.findElement(By.id("input-card-number")).sendKeys("0000000000000036");
        driver.findElement(By.id("input-card-holder")).sendKeys("IVAN IGONIN");
        new Select(driver.findElement(By.id("card-expires-month"))).selectByVisibleText("07");
        new Select(driver.findElement(By.id("card-expires-year"))).selectByVisibleText("2037");
        driver.findElement(By.id("input-card-cvc")).sendKeys("007");
        driver.findElement(By.id("action-submit")).click();

        assertEquals(driver.findElement(By.xpath("//div[@id='card-holder-field']/div/label")).getText(), "Cardholder name is not valid");
        assertEquals(driver.findElement(By.xpath("//div[@id='card-number-field']/div/label")).getText(), "Card number is not valid");
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {

    }

}