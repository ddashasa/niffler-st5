package guru.qa.niffler.test;

import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_RECIEVED;
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_SEND;
import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.UserQueueExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.pageobject.PeoplePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@WebTest
@ExtendWith(UserQueueExtension.class)
public class PeoplePageTest extends BaseTest {
  private PeoplePage peoplePage;

  @BeforeEach
  void openPeoplePage() {
    Selenide.open("http://127.0.0.1:3000/people");
    peoplePage = new PeoplePage();
    peoplePage.ensurePageLoaded();
  }

  @Test
  public void testFriendIsListed(@User(WITH_FRIENDS) UserJson testUser) {
    String username = testUser.username();
    assertTrue(peoplePage.isUserListed(username));
    assertEquals("You are friends", peoplePage.getUserActions(username).getText());
    assertTrue(peoplePage.getRemoveFriendButton(username).exists());
  }

  @Test
  public void testSentInvitationIsListed(@User(INVITATION_SEND) UserJson testUser) {
    String username = testUser.username();
    assertTrue(peoplePage.isUserListed(username));
    assertEquals("Pending invitation", peoplePage.getUserActions(username).getText());
  }

  @Test
  public void testRecievedInvitationIsListed(@User(INVITATION_RECIEVED) UserJson testUser) {
    String username = testUser.username();
    assertTrue(peoplePage.isUserListed(username));
    assertTrue(peoplePage.getSubmitInvitationButton(username).exists());
    assertTrue(peoplePage.getDeclineInvitationButton(username).exists());
  }
}
