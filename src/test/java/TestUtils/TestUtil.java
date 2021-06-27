package TestUtils;

import Utils.ExtentReportListner;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

@Listeners(ExtentReportListner.class)
public class TestUtil {

    @BeforeClass
    public void TestUtil() {

    }
}
