package brukickerleague;

import lombok.AllArgsConstructor;
import spark.Request;
import spark.Response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.WeekFields;
import java.util.*;

@AllArgsConstructor
public class StandingsController {

  private final Db db;

  public Object getStandings(Request req, Response res) {
    List<Match> liveMatches = db.query(Match.class, "Match.notEnded");
    ZonedDateTime.now().with(WeekFields.ISO.getFirstDayOfWeek());
    List<Match> thisWeekMatches = db.query(Match.class, "Match.betweenDates", ZonedDateTime.now().with(WeekFields.ISO.getFirstDayOfWeek()).with(LocalTime.MIN), ZonedDateTime.now());
    Standings thisWeekStandings = new Standings(thisWeekMatches);
    ZonedDateTime startOfLastWeek = ZonedDateTime.now().minusDays(7).with(WeekFields.ISO.getFirstDayOfWeek()).with(LocalTime.MIN);
    Standings lastWeekStandings = new Standings(db.query(Match.class, "Match.betweenDates", startOfLastWeek, startOfLastWeek.plusDays(6)));

    LocalDate startOfAwardWeek = LocalDate.now().minusDays(7).with(WeekFields.ISO.getFirstDayOfWeek());
    List<Award> awards = db.query(Award.class, "Award.typeForPeriod", Award.Type.PLAYER_OF_THE_WEEK, startOfAwardWeek);
    Award award;
    if (awards.isEmpty()) {
      Standings awardStandings = new Standings(db.query(Match.class, "Match.betweenDates", startOfLastWeek, startOfLastWeek.plusDays(6)));
      Optional<String> name = awardStandings.getTopPlayer();
      if (name.isPresent()) {
        award = db.save(new Award(Award.Type.PLAYER_OF_THE_WEEK, name.get(), startOfLastWeek.toLocalDate(), startOfLastWeek.toLocalDate().plusDays(6)));
      } else {
        award = null;
      }
    } else {
      award = awards.get(0);
    }

    return new StandingsTemplate(liveMatches, Optional.ofNullable(award), thisWeekStandings, lastWeekStandings).render();
  }

  public String getEloRatings(Request req, Response res) {
    List<Match> matches = db.all(Match.class, "createdAt");
    Map<String, Integer> playerRatings = new HashMap<>();
    matches.stream()
      .sorted(Comparator.comparing(Match::getCreatedAt))
      .filter(Match::hasEnded)
      .forEach(match -> {
        String team1Player1 = match.getTeam1Player1();
        defaultRating(team1Player1, playerRatings);
        String team2Player1 = match.getTeam2Player1();
        defaultRating(team2Player1, playerRatings);
        String team1Player2 = match.getTeam1Player2();
        String team2Player2 = match.getTeam2Player2();
        Map<String, Integer> playerIncrements = new HashMap<>();
        updateRatings(team1Player1, match.isTeam1Winner(), team2Player1, playerRatings, playerIncrements);
        if (match.team1HasPlayer2()) {
          defaultRating(team1Player2, playerRatings);
          updateRatings(team1Player2, match.isTeam1Winner(), team2Player1, playerRatings, playerIncrements);
          if (match.team2HasPlayer2()) {
            defaultRating(team2Player2, playerRatings);
            updateRatings(team1Player2, match.isTeam1Winner(), team2Player2, playerRatings, playerIncrements);
          }
        }
        if (match.team2HasPlayer2()) {
          updateRatings(team2Player2, match.isTeam2Winner(), team1Player1, playerRatings, playerIncrements);
        }
        playerIncrements.forEach((player, increment) -> playerRatings.compute(player, (p, i) -> i + increment));
      });

    List<Map.Entry<String, Integer>> list = new ArrayList<>(playerRatings.entrySet());
    list.sort(Map.Entry.comparingByValue());
    Collections.reverse(list);
    Map<String, Integer> sortedPlayerRatings = new LinkedHashMap<>();
    list.forEach(item -> sortedPlayerRatings.put(item.getKey(), item.getValue()));

    return new EloTemplate().renderElo(sortedPlayerRatings);
  }

  private void updateRatings(String player, boolean winner, String opponent, Map<String, Integer> playerRatings, Map<String, Integer> playerIncrements) {
    updateRating(player, winner, opponent, playerRatings, playerIncrements);
    updateRating(opponent, !winner, player, playerRatings, playerIncrements);
  }

  private void updateRating(String player, boolean winner, String opponent, Map<String, Integer> playerRatings, Map<String, Integer> playerIncrements) {
    Integer playerRating = playerRatings.get(player);
    Integer opponentRating = playerRatings.get(opponent);
    BigDecimal expectedScore = Elo.expectedScore(playerRating, opponentRating);
    Integer increment = Elo.ratingDiff(expectedScore, winner);
    playerIncrements.compute(player, (p, r) -> r == null ? increment : r + increment);
  }

  private void defaultRating(String player, Map<String, Integer> playerRatings) {
    playerRatings.putIfAbsent(player, 1000);
  }
}
