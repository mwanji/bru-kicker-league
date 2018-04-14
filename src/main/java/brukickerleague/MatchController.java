package brukickerleague;

import lombok.AllArgsConstructor;
import spark.Request;
import spark.Response;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Stream;

import static java.time.DayOfWeek.*;
import static java.time.temporal.TemporalAdjusters.*;
import static java.util.stream.Collectors.*;

@AllArgsConstructor
public class MatchController {

  private final Validator validator;
  private final Db db;

  public String showMatch(Request req, Response res) {
    return db.by(Match.class, "id", req.params("id"))
      .map(match -> new MatchTemplate(match).render())
      .orElseThrow(() -> new IllegalArgumentException("Match not found"));
  }

  public String showMatches(Request req, Response res) {
    List<Match> matches = db.query(Match.class, "Match.betweenDates", ZonedDateTime.now().with(previous(MONDAY)).with(LocalTime.MIN).minusDays(7), ZonedDateTime.now());
    Map<LocalDate, List<Match>> matchesByDay = new HashMap<>();

    matches.forEach(match -> matchesByDay.computeIfAbsent(match.getCreatedAt().toLocalDate(), date -> new ArrayList<>()).add(match));

    return new MatchTemplate(matchesByDay).renderMatches();
  }

  public String createMatchForm(Request req, Response res) {
    return new MatchCreationTemplate(Collections.emptyMap(), getPlayers()).render();
  }

  public String createMatch(Request req, Response res) {
    Match match = new Match(req.queryMap("team1").value("player1"), req.queryMap("team1").value("player2"), req.queryMap("team2").value("player1"), req.queryMap("team2").value("player2"));

    Set<ConstraintViolation<Match>> constraintViolations = validator.validate(match);

    if (constraintViolations.isEmpty()) {
      match = db.save(match);
      res.redirect(Urls.match(match));
      return null;
    } else {
      Map<String, String> errors = constraintViolations.stream().collect(toMap(cv -> cv.getPropertyPath().toString(), ConstraintViolation::getMessage));
      return new MatchCreationTemplate(errors, getPlayers()).render();
    }
  }

  public Object addGoal(Request req, Response res) {
    return db.inTx(tx -> tx.by(Match.class, "id", req.params("id"))
      .map(m -> {
        m.addGoal(req.params("teamId"));
        res.redirect(Urls.match(m));
        return tx.save(m);
      })
      .orElseThrow(() -> new IllegalArgumentException("No match found")));
  }

  public Object endMatch(Request req, Response res) {
    Match match1 = db.inTx(tx -> {
      Match match = tx.by(Match.class, "id", req.params("id"))
        .orElseThrow(() -> new IllegalArgumentException("No match found"));
      match.end();
      tx.save(match);
      return match;
    });

    res.redirect(Urls.match(match1));
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
