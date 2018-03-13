package brukickerleague;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

public class MatchValidator implements ConstraintValidator<ValidMatch, Match> {

  @Override
  public boolean isValid(Match match, ConstraintValidatorContext context) {
    Set<String> names = new HashSet<>();
    boolean valid = names.add(match.getTeam1Player1());
    valid = names.add(match.getTeam2Player1());
    if (match.team1HasPlayer2()) {
      valid = names.add(match.getTeam1Player2());
    }
    if (match.team2HasPlayer2()) {
      valid = names.add(match.getTeam2Player2());
    }

    if (!valid) {
      context.disableDefaultConstraintViolation();
      context
        .buildConstraintViolationWithTemplate("The same player cannot be used twice!")
        .addPropertyNode("duplicatePlayer")
        .addConstraintViolation();
    }

    return valid;
  }
}
