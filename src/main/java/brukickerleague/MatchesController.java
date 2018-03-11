package brukickerleague;

import lombok.AllArgsConstructor;
import spark.Request;
import spark.Response;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@AllArgsConstructor
public class MatchesController {

  private final Validator validator;

  public Object createMatch(Request req, Response res) {
    Match match = new Match(0L, req.queryMap("team1").value("player1"), req.queryMap("team1").value("player2"), req.queryMap("team2").value("player1"), req.queryMap("team2").value("player2"), 0, 0);

    Set<ConstraintViolation<Match>> constraintViolations = validator.validate(match);

    if (constraintViolations.isEmpty()) {
      res.redirect("/match/" + match.getAltId());
      return null;
    } else {
      return new MatchCreationTemplate(constraintViolations).render();
    }
  }
}
