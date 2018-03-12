package brukickerleague;

import lombok.AllArgsConstructor;
import spark.Request;
import spark.Response;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@AllArgsConstructor
public class MatchController {

  private final Validator validator;
  private final Db db;

  public String showMatch(Request req, Response response) {
    return db.by(Match.class, "altId", req.params("altId"))
      .map(match -> new MatchTemplate(match).render())
      .orElseThrow(() -> new IllegalArgumentException("No match found"));
  }

  public Object createMatch(Request req, Response res) {
    Match match = new Match(req.queryMap("team1").value("player1"), req.queryMap("team1").value("player2"), req.queryMap("team2").value("player1"), req.queryMap("team2").value("player2"));

    Set<ConstraintViolation<Match>> constraintViolations = validator.validate(match);

    if (constraintViolations.isEmpty()) {
      db.save(match);
      res.redirect("/match/" + match.getAltId());
      return null;
    } else {
      return new MatchCreationTemplate(constraintViolations).render();
    }
  }

  public Object addGoal(Request req, Response res) {
    Match match1 = db.inTx(tx -> {
      Match match = tx.by(Match.class, "altId", req.params("altId"))
        .orElseThrow(() -> new IllegalArgumentException("No match found"));
      match.addGoal(req.params("teamId"));
      tx.save(match);
      return match;
    });

    res.redirect("/match/" + match1.getAltId());
    return null;
  }

  public Object endMatch(Request req, Response res) {
    Match match1 = db.inTx(tx -> {
      Match match = tx.by(Match.class, "altId", req.params("altId"))
        .orElseThrow(() -> new IllegalArgumentException("No match found"));
      match.end();
      tx.save(match);
      return match;
    });

    res.redirect("/match/" + match1.getAltId());
    return null;
  }
}
