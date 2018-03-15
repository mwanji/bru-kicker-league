package brukickerleague;

import lombok.AllArgsConstructor;

import java.util.List;

import static j2html.TagCreator.*;

@AllArgsConstructor
public class StandingsTemplate {

  private final List<Match> liveMatches;
  private final Standings standings;

  public String render() {
    return new Page("Standings",
      h1(attrs(".display-3.mb-4"), "Standings"),
      each(liveMatches, match -> div(attrs(".mb-3"),
        div(attrs(".btn-group.w-100"),
          a(attrs(".btn.btn-dark.w-50"),
            span(attrs(".badge.badge-danger.mr-3"), "LIVE"),
            span(match.getTeam1FullName()),
            span(attrs(".badge.badge-light.ml-2"), Integer.toString(match.getTeam1Score()))
          ).withHref("/match/" + match.getAltId()),
          a(attrs(".btn.btn-light.w-50"),
            span(match.getTeam2FullName()),
            span(attrs(".badge.badge-dark.ml-2"), Integer.toString(match.getTeam2Score()))
          ).withHref("/match/" + match.getAltId())
        )
      )),
      table(attrs(".table"),
        thead(
          tr(
            th(),
            th("Wins"),
            th("Losses")
          )
        ),
        each(standings.list(), standing ->
          tr(
            td(standing.getName()),
            td(Integer.toString(standing.getWins())),
            td(Integer.toString(standing.getLosses()))
          )
        )

      )
    ).render();
  }
}
