package brukickerleague;

import lombok.Getter;
import lombok.ToString;

import java.util.*;

public class Standings {

  private final List<Standing> standings;

  public Standings(List<Match> matches) {
    Map<String, Standing> nameToStandings = new HashMap<>();
    for (Match match : matches) {
      nameToStandings.computeIfAbsent(match.getTeam1Player1(), Standing::new)
        .addMatch(match.isTeam1Winner());
      if (match.team1HasPlayer2()) {
        nameToStandings.computeIfAbsent(match.getTeam1Player2(), Standing::new)
          .addMatch(match.isTeam1Winner());
      }
      nameToStandings.computeIfAbsent(match.getTeam2Player1(), Standing::new)
        .addMatch(!match.isTeam1Winner());
      if (match.team2HasPlayer2()) {
        nameToStandings.computeIfAbsent(match.getTeam2Player2(), Standing::new)
          .addMatch(!match.isTeam1Winner());
      }
    }

    this.standings = new ArrayList<>(nameToStandings.values());
    Collections.sort(this.standings, (s1, s2) -> {
      if (s1.wins == s2.wins && s1.losses == s2.losses) {
        return 0;
      }
      if (s1.wins > s2.wins) {
        return -1;
      }
      if (s2.wins > s1.wins) {
        return 1;
      }
      if (s1.losses < s2.losses) {
        return -1;
      }
      return 1;
    });
  }

  public List<Standing> list() {
    return standings;
  }

  public Optional<String> getTopPlayer() {
    return standings.isEmpty() ? Optional.empty() : Optional.of(standings.get(0).name);
  }

  @Getter
  @ToString
  public static class Standing {

    private final String name;
    private int wins;
    private int losses;

    private Standing(String name) {
      this.name = name;
    }

    public void addMatch(boolean winner) {
      if (winner) {
        wins++;
      } else {
        losses++;
      }
    }
  }

}
