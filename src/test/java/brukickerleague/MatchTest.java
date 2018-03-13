package brukickerleague;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

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
}