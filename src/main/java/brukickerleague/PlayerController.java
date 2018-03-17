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
    db.inTx(tx -> tx.query(Match.class, "Match.byPlayer", name)).stream()
      .forEach(match -> {
        LocalDate localDate = match.getCreatedAt().toLocalDate();
        matches.computeIfAbsent(localDate, (date) -> new ArrayList<>());
        matches.get(localDate).add(match);
      });

    return new PlayerTemplate(name, matches).render();
  }
}
