package guru.qa.niffler.pageobject;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;

public abstract class UserListPage {
  protected final SelenideElement userList = $("table.abstract-table");

  private SelenideElement findUsernameElement(String username) {
    return userList.$$("tr > td:nth-child(2)").findBy(text(username));
  }

  private SelenideElement findRowByUsername(String username) {
    return userList.$$("tr").findBy(text(username));
  }

  public boolean isUserListed(String username) {
    return findUsernameElement(username).exists();
  }

  public SelenideElement getUserActions(String username) {
    return findRowByUsername(username).$("td:nth-child(4)");
  }

  public SelenideElement getRemoveFriendButton(String username) {
    return getUserActions(username).$("div[data-tooltip-id='remove-friend'] button.button-icon_type_close");
  }

  public SelenideElement getSubmitInvitationButton(String username) {
    return getUserActions(username).$("div[data-tooltip-id='submit-invitation'] button.button-icon_type_submit");
  }

  public SelenideElement getDeclineInvitationButton(String username) {
    return getUserActions(username).$("div[data-tooltip-id='decline-invitation'] button.button-icon_type_close");
  }

  public UserListPage ensurePageLoaded() {
    userList.$("tbody > tr").shouldBe(visible);
    return this;
  }
}
