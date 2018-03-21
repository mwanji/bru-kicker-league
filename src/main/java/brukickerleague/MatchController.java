package brukickerleague;

import lombok.AllArgsConstructor;
import spark.Request;
import spark.Response;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

@AllArgsConstructor
public class MatchController {

  private final Validator validator;
  private final Db db;

  public String showMatch(Request req, Response res) {
    return db.by(Match.class, "altId", req.params("altId"))
      .map(match -> new MatchTemplate(match).render())
      .orElseThrow(() -> new IllegalArgumentException("Match not found"));
  }

  public String createMatchForm(Request req, Response res) {
    return new MatchCreationTemplate(Collections.emptyMap(), getPlayers()).render();
  }

  public String createMatch(Request req, Response res) {
    Match match = new Match(req.queryMap("team1").value("player1"), req.queryMap("team1").value("player2"), req.queryMap("team2").value("player1"), req.queryMap("team2").value("player2"));

    Set<ConstraintViolation<Match>> constraintViolations = validator.validate(match);

    if (constraintViolations.isEmpty()) {
      db.save(match);
      res.redirect("/match/" + match.getAltId());
      return null;
    } else {
      Map<String, String> errors = constraintViolations.stream().collect(toMap(cv -> cv.getPropertyPath().toString(), ConstraintViolation::getMessage));
      return new MatchCreationTemplate(errors, getPlayers()).render();
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

  private Set<String> getPlayers() {
    return db.raw((tx, entityManager) -> {
      List<Object[]> results = entityManager.createNamedQuery("Player.names").getResultList();
      Set<String> names = new TreeSet<>();
      results.forEach(line -> Stream.of(line).map(Object::toString).forEach(names::add));
      return names;
    });
  }
}
