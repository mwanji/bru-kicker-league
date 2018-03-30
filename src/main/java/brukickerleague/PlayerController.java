package brukickerleague;

import lombok.AllArgsConstructor;
import spark.Request;
import spark.Response;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@AllArgsConstructor
public class PlayerController {

  private final Db db;

  public String getPlayer(Request req, Response res) {
    String name = req.params("name");
    LinkedHashMap<LocalDate, List<Match>> matches = new LinkedHashMap<>();
    List<Award> awards = db.all(Award.class, "playerName", name, "startedAt");
    db.inTx(tx -> tx.query(Match.class, "Match.byPlayer", name))
      .forEach(match -> {
        LocalDate localDate = match.getCreatedAt().toLocalDate();
        matches.computeIfAbsent(localDate, (date) -> new ArrayList<>());
        matches.get(localDate).add(match);
      });

    return new PlayerTemplate(name, matches, awards).render();
  }

  public String getAwards(Request req, Response res) {
    List<Award> awards = db.all(Award.class, "startedAt");

    return new PlayerTemplate(awards).renderAwards();
  }
}
