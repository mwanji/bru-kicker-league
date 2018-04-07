package brukickerleague;

import j2html.tags.ContainerTag;
import j2html.tags.DomContent;
import j2html.tags.EmptyTag;
import lombok.AllArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static j2html.TagCreator.*;

@AllArgsConstructor
public class MatchTemplate {

  private final Match match;

  public String render() {
    String team1Score = Integer.toString(match.getTeam1Score());
    String team2Score = Integer.toString(match.getTeam2Score());

    return new Page("Match",
      div(attrs(".row"),
        div(attrs(".col-9"),
          h1(attrs(".display-3"), text("Match "))
        ),
        div(attrs(".col.text-muted.text-center"), DateTimeFormatter.ISO_LOCAL_DATE.format(match.getCreatedAt()))
      ),
      form(attrs(".form-inline"),
        label(attrs(".w-75.h3"),
          iff(match.didTeam1Crawl(), crawling("")),
          iff(match.isTeam1Winner(), winnerIcon(match.getTeam1Player1(), "")),
          span(attrs(".mr-2"), match.getTeam1FullName()),
          iff(match.isTeam1Winner() && match.team1HasPlayer2(), winnerIcon(match.getTeam1Player2(), ""))
        ),
        button(attrs(".btn.btn-dark.w-25.btn-lg"), team1Score).withType(match.hasEnded() ? "button" : "submit")
      ).withMethod("post").withAction(Urls.goal(match, "1")),
      form(attrs(".form-inline.mt-3"),
        label(attrs(".w-75.h3"),
          iff(match.didTeam2Crawl(), crawling("")),
          iff(match.isTeam2Winner(), winnerIcon(match.getTeam2Player1(), "")),
          span(attrs(".mr-2"), match.getTeam2FullName()),
          iff(match.isTeam2Winner() && match.team2HasPlayer2(), winnerIcon(match.getTeam2Player2(), ""))
        ),
        button(attrs(".btn.btn-light.w-25.btn-lg"), team2Score).withType(match.hasEnded() ? "button" : "submit")
      ).withMethod("post").withAction(Urls.goal(match, "2")),
      iff(!match.hasEnded(),
        form(attrs(".form-inline.mt-3"),
          button(attrs(".btn.btn-secondary.w-100.btn-lg"), "End Match")
        ).withMethod("post").withAction(Urls.end(match))
      )
    ).render();
  }

  public static DomContent singleMatch(Match match) {
    String url = Urls.match(match);

    return div(attrs(".mb-3"),
      div(attrs(".btn-group.w-100"),
        a(attrs(".btn.btn-dark.w-50"),
          iff(!match.hasEnded(), span(attrs(".badge.badge-danger.mr-3"), "LIVE")),
          iff(match.didTeam1Crawl(), crawling("-light")),
          iff(match.isTeam1Winner(), winnerIcon(match.getTeam1FullName(), "-light", true)),
          text(match.getTeam1FullName()),
          span(attrs(".badge.badge-light.ml-2"), Integer.toString(match.getTeam1Score()))
        ).withHref(url),
        a(attrs(".btn.btn-light.w-50"),
          iff(match.didTeam2Crawl(), crawling("")),
          iff(match.isTeam2Winner(), winnerIcon(match.getTeam2FullName(), "", true)),
          text(match.getTeam2FullName()),
          span(attrs(".badge.badge-dark.ml-2"), Integer.toString(match.getTeam2Score()))
        ).withHref(url)
      )
    );
  }

  private static DomContent winnerIcon(String teamFullName, String qualifier) {
    return winnerIcon(teamFullName, qualifier, false);
  }

  private static DomContent winnerIcon(String teamFullName, String qualifier, boolean useDefault) {
    List<DomContent> icons = new ArrayList<>();
    if (teamFullName.contains("Kathleen")) {
      icons.add(helmet(qualifier));
    }
    if (teamFullName.contains("Glenn")) {
      icons.add(bobaFett(qualifier));
    }
    if (teamFullName.contains("Michael")) {
      icons.add(sword(qualifier));
    }
    if (teamFullName.contains("Jan")) {
      icons.add(rockNRoll(qualifier));
    }
    if (teamFullName.contains("Moandji")) {
      icons.add(ghost(qualifier));
    }
    if (icons.isEmpty() && useDefault) {
      icons.add(victoryBadge());
    }

    return span(attrs(".mr-1"), icons.toArray(new DomContent[0]));
  }

  private static ContainerTag victoryBadge() {
    return Bootstrap.icon("badge");
  }

  private static EmptyTag crawling(String qualifier) {
    return svg("/crawling" + qualifier + ".svg", "Crawling");
  }

  private static EmptyTag helmet(String qualifier) {
    return svg("/helmet" + qualifier + ".svg", "Kathleen");
  }

  private static EmptyTag bobaFett(String qualifier) {
    return svg("/boba-fett" + qualifier + ".svg", "Glenn");
  }

  private static EmptyTag sword(String qualifier) {
    return svg("/sword" + qualifier + ".svg", "Michael");
  }

  private static EmptyTag rockNRoll(String qualifier) {
    return svg("/rock-n-roll" + qualifier + ".svg", "Jan");
  }

  private static EmptyTag ghost(String qualifier) {
    return svg("/ghost" + qualifier + ".svg", "Moandji");
  }

  private static EmptyTag svg(String src, String title) {
    return img().withSrc(src).withAlt(title).withTitle(title).withStyle("height: 1.5em");
  }
}
