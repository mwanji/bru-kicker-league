package brukickerleague;

import j2html.tags.DomContent;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

import static brukickerleague.Bootstrap.*;
import static j2html.TagCreator.*;

@AllArgsConstructor
public class StandingsTemplate {

  private final List<Match> liveMatches;
  private final Optional<Award> award;
  private final Standings standings;
  private final Standings standingsLastWeek;

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
      h2(attrs(".display-4.text-secondary"), "This Week"),
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
            td(playerLink(standing.getName(), true)),
            td(Integer.toString(standing.getWins())),
            td(Integer.toString(standing.getLosses()))
          )
        )
      ),
      h2(attrs(".display-4.text-secondary"), "Last Week"),
      table(attrs(".table"),
        thead(
          tr(
            th(),
            th("Wins"),
            th("Losses")
          )
        ),
        each(standingsLastWeek.list(), standing ->
          tr(
            td(playerLink(standing.getName(), false)),
            td(Integer.toString(standing.getWins())),
            td(Integer.toString(standing.getLosses()))
          )
        )
      )
    ).render();
  }

  private DomContent playerLink(String name, boolean showAward) {
    Boolean hasAward = award.map(a -> a.getPlayerName().equals(name)).orElse(Boolean.FALSE);
    return a(
      iffElse(hasAward && showAward, join(icon("star.text-warning").withTitle("Player of the Week"), " " + name), text(name))
    ).withHref("/player/" + name);
  }
}
