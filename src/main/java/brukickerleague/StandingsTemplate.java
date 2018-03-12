package brukickerleague;

import lombok.AllArgsConstructor;

import java.util.List;

import static j2html.TagCreator.*;

@AllArgsConstructor
public class StandingsTemplate {

  private final List<Match> matches;

  public String render() {
    return new Page("Standings",
      h1("Standings"),
      each(matches, match -> div(attrs(".mb-3"), match.getTeam1FullName() + " " + match.getTeam1Score() + " - " + match.getTeam2FullName() + " " + match.getTeam2Score()))
    ).render();
  }
}
