package brukickerleague;

import lombok.AllArgsConstructor;
import spark.Request;
import spark.Response;

import java.time.ZonedDateTime;
import java.util.List;

@AllArgsConstructor
public class StandingsController {

  private final Db db;

  public Object getStandings(Request req, Response res) {
    List<Match> liveMatches = db.all(Match.class, "endedAt", null, "createdAt");
    List<Match> fortnightlyMatches = db.inTx(tx -> tx.query(Match.class, "Match.betweenDates", ZonedDateTime.now().minusDays(14), ZonedDateTime.now()));
    Standings standings = new Standings(fortnightlyMatches);

    return new StandingsTemplate(liveMatches, standings).render();
  }
}
