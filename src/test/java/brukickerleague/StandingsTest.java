package brukickerleague;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

class StandingsTest {

  @Test
  public void should_sort_by_wins() {
    Match match1 = new Match("a", null, "b", "c");
    match1.addGoal("1");
    match1.end();
    Match match2 = new Match("c", null, "a", "b");
    match2.addGoal("2");
    match2.end();
    Match match3 = new Match("b", "a", "c", null);
    match3.addGoal("1");
    match3.end();
    Match match4 = new Match("b", "a", "c", null);
    match4.addGoal("2");
    match4.end();

    Standings standings = new Standings(Arrays.asList(match1, match2, match3, match4));

    assertThat(standings.list(), contains(standing("a", 3, 1), standing("b", 2, 2), standing("c", 1, 3)));
  }

  @Test
  public void should_sort_by_wins_and_then_losses() {
    Match match1 = new Match("a", "c", "b", null);
    match1.addGoal("1");
    match1.end();
    Match match2 = new Match("c", null, "a", "b");
    match2.addGoal("2");
    match2.end();
    Match match3 = new Match("b", "a", "d", null);
    match3.addGoal("1");
    match3.end();
    Match match4 = new Match("b", null, "a", "c");
    match4.addGoal("2");
    match4.end();

    Standings standings = new Standings(Arrays.asList(match1, match2, match3, match4));

    assertThat(standings.list(), contains(standing("a", 4, 0), standing("c", 2, 1), standing("b", 2, 2), standing("d", 0, 1)));
  }

  private Matcher<Standings.Standing> standing(String name, int wins, int losses) {
    return new BaseMatcher<Standings.Standing>() {
      @Override
      public boolean matches(Object item) {
        Standings.Standing standing = (Standings.Standing) item;
        return standing.getName().equals(name) && standing.getWins() == wins && standing.getLosses() == losses;
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("name=" + name + ", wins=" + wins + ", losses=" + losses);
      }
    };
  }
}