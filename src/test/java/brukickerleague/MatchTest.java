package brukickerleague;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class MatchTest {

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  public void should_be_invalid_when_missing_required_properties() {
    Match match = new Match();

    Set<ConstraintViolation<Match>> constraintViolations = validator.validate(match);

    assertThat(constraintViolations, hasSize(3));
  }

  @Test
  public void should_be_invalid_when_duplicate_names() {
    Match match = new Match("Name 1", "Name 1", "Name 2", null);

    Set<ConstraintViolation<Match>> constraintViolations = validator.validate(match);

    assertThat(constraintViolations, hasSize(1));
  }

  @Test
  public void team2_should_crawl_when_loses_with_0() {
    Match match = new Match("t1p1", null, "t2p1", null);
    match.addGoal("1");
    match.end();

    assertFalse(match.didTeam1Crawl());
    assertTrue(match.didTeam2Crawl());
  }

  @Test
  public void team2_should_crawl_when_loses_10_to_1() {
    Match match = new Match("t1p1", null, "t2p1", null);
    match.addGoal("1");
    match.addGoal("1");
    match.addGoal("1");
    match.addGoal("1");
    match.addGoal("1");
    match.addGoal("1");
    match.addGoal("1");
    match.addGoal("1");
    match.addGoal("1");
    match.addGoal("1");
    match.addGoal("2");
    match.end();

    assertFalse(match.didTeam1Crawl());
    assertTrue(match.didTeam2Crawl());
  }

  @Test
  public void team1_should_crawl_when_loses_with_0() {
    Match match = new Match("t1p1", null, "t2p1", null);
    match.addGoal("2");
    match.end();

    assertFalse(match.didTeam2Crawl());
    assertTrue(match.didTeam1Crawl());
  }

  @Test
  public void team1_should_crawl_when_loses_10_to_1() {
    Match match = new Match("t1p1", null, "t2p1", null);
    match.addGoal("2");
    match.addGoal("2");
    match.addGoal("2");
    match.addGoal("2");
    match.addGoal("2");
    match.addGoal("2");
    match.addGoal("2");
    match.addGoal("2");
    match.addGoal("2");
    match.addGoal("2");
    match.addGoal("1");
    match.end();

    assertFalse(match.didTeam2Crawl());
    assertTrue(match.didTeam1Crawl());
  }
}
