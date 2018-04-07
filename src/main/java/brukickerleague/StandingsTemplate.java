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
      each(liveMatches, MatchTemplate::singleMatch),
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
    ).withHref(Urls.player(name));
  }
}
