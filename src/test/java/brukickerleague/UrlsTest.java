package brukickerleague;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UrlsTest {

  @Test
  public void match() {
    Match match = mock(Match.class);
    when(match.getId()).thenReturn(15L);

    assertEquals("/match", Urls.match());
    assertEquals("/match/15", Urls.match(match));
    assertEquals("/match/15/goal/2", Urls.goal(match, "2"));
    assertEquals("/match/15/end", Urls.end(match));
  }
}