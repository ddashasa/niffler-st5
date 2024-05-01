package guru.qa.niffler.test;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.pageobject.MainPage;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeOptions;

public abstract class BaseTest {

  static {
    Configuration.browserSize = "1920x1080";
  }

  @BeforeEach
  void setupAndDoLogin() {
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--disable-notifications");
    options.addArguments("--lang=en-GB");

    options.setExperimentalOption("prefs", Map.of(
        "credentials_enable_service", false,
        "profile.password_manager_enabled", false
    ));

    Configuration.browserCapabilities.setCapability(ChromeOptions.CAPABILITY, options);

    Selenide.open("http://127.0.0.1:3000");
    $("a[href*='redirect']").click();
    $("input[name='username']").setValue("dima");
    $("input[name='password']").setValue("12345");
    $("button[type='submit']").click();
    MainPage mainPage = new MainPage();
    mainPage.ensurePageLoaded();
  }
}
