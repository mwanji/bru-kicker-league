package brukickerleague;

import lombok.AllArgsConstructor;
import spark.Request;
import spark.Response;

import java.util.List;

@AllArgsConstructor
public class StandingsController {

  private final Db db;

  public Object getStandings(Request req, Response res) {
    List<Match> liveMatches = db.inTx(tx -> tx.all(Match.class, "createdAt").stream().filter(match -> !match.hasEnded());
    List<Match> fortnightlyMatches = db.inTx(tx -> tx.query(Match.class, "Match.ended"));
    Standings standings = new Standings(fortnightlyMatches);

    return new StandingsTemplate(liveMatches, standings).render();
  }
}
