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
      each(matches, match -> div(attrs(".mb-3"),
        a(attrs(".btn.btn-dark"),
          span(match.getTeam1FullName()),
          span(attrs(".badge.badge-light"), Integer.toString(match.getTeam1Score()))
        ).withHref("/match/" + match.getAltId()),
        span(" - "),
        a(attrs(".btn.btn-light"),
          span(match.getTeam2FullName()),
          span(attrs(".badge.badge-dark"), Integer.toString(match.getTeam2Score()))
        ).withHref("/match/" + match.getAltId())
      ))
    ).render();
  }
}
