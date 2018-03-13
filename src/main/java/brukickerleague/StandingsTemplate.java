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
        a(attrs(".btn.btn-dark.mr-4"),
          span(match.getTeam1FullName()),
          span(attrs(".badge.badge-light.ml-2"), Integer.toString(match.getTeam1Score()))
        ).withHref("/match/" + match.getAltId()),
        a(attrs(".btn.btn-light"),
          span(match.getTeam2FullName()),
          span(attrs(".badge.badge-dark.ml-2"), Integer.toString(match.getTeam2Score()))
        ).withHref("/match/" + match.getAltId())
      ))
    ).render();
  }
}
