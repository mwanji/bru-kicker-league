package brukickerleague;

import lombok.AllArgsConstructor;
import spark.Request;
import spark.Response;

import java.util.List;

@AllArgsConstructor
public class StandingsController {

  private final Db db;

  public Object getStandings(Request req, Response res) {
    List<Match> matches = db.inTx(tx -> tx.all(Match.class, "createdAt"));

    return new StandingsTemplate(matches).render();
  }
}
