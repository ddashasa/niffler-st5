package guru.qa.niffler.pageobject;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;

public class MainPage {
  protected final SelenideElement mainContainer = $("div.main-container");
  public MainPage ensurePageLoaded() {
    mainContainer.shouldBe(visible);
    return this;
  }
}
