package guru.qa.niffler.test;

import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_RECIEVED;
import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.UserQueueExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.pageobject.FriendsPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@WebTest
@ExtendWith(UserQueueExtension.class)
public class FriendsPageTest extends BaseTest {
  private FriendsPage friendsPage;

  @BeforeEach
  void openFriendsPage() {
    Selenide.open("http://127.0.0.1:3000/friends");
    friendsPage = new FriendsPage();
    friendsPage.ensurePageLoaded();
  }

  @Test
  public void testFriendIsListed(@User(WITH_FRIENDS) UserJson testUser) {
    String username = testUser.username();
    assertTrue(friendsPage.isUserListed(username));
    assertEquals("You are friends", friendsPage.getUserActions(username).getText());
    assertTrue(friendsPage.getRemoveFriendButton(username).exists());
  }

  @Test
  public void testRecievedInvitationIsListed(@User(INVITATION_RECIEVED) UserJson testUser) {
    String username = testUser.username();
    assertTrue(friendsPage.isUserListed(username));
    assertTrue(friendsPage.getSubmitInvitationButton(username).exists());
    assertTrue(friendsPage.getDeclineInvitationButton(username).exists());
  }
}
