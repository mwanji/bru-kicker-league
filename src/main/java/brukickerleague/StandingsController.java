package brukickerleague;

import lombok.AllArgsConstructor;
import spark.Request;
import spark.Response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class StandingsController {

  private final Db db;

  public Object getStandings(Request req, Response res) {
    List<Match> liveMatches = db.query(Match.class, "Match.notEnded");
    ZonedDateTime.now().with(WeekFields.ISO.getFirstDayOfWeek());
    List<Match> fortnightlyMatches = db.query(Match.class, "Match.betweenDates", ZonedDateTime.now().with(WeekFields.ISO.getFirstDayOfWeek()).with(LocalTime.MIN), ZonedDateTime.now());
    Standings standings = new Standings(fortnightlyMatches);

    LocalDate startOfAwardWeek = LocalDate.now().minusDays(7).with(WeekFields.ISO.getFirstDayOfWeek());
    List<Award> awards = db.query(Award.class, "Award.typeForPeriod", Award.Type.PLAYER_OF_THE_WEEK, startOfAwardWeek);
    Award award;
    if (awards.isEmpty()) {
      ZonedDateTime startDate = ZonedDateTime.now().minusDays(7).with(WeekFields.ISO.getFirstDayOfWeek()).with(LocalTime.MIN);
      Standings awardStandings = new Standings(db.query(Match.class, "Match.betweenDates", startDate, startDate.plusDays(6)));
      Optional<String> name = awardStandings.getTopPlayer();
      if (name.isPresent()) {
        award = db.save(new Award(Award.Type.PLAYER_OF_THE_WEEK, name.get(), startDate.toLocalDate(), startDate.toLocalDate().plusDays(6)));
      } else {
        award = null;
      }
    } else {
      award = awards.get(0);
    }

    return new StandingsTemplate(liveMatches, standings, Optional.ofNullable(award)).render();
  }
}
