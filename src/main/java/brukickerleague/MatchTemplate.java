package brukickerleague;

import j2html.tags.ContainerTag;
import j2html.tags.DomContent;
import j2html.tags.EmptyTag;
import lombok.AllArgsConstructor;

import java.time.format.DateTimeFormatter;

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
          text(match.getTeam1FullName())
        ),
        button(attrs(".btn.btn-dark.w-25.btn-lg"), team1Score).withType(match.hasEnded() ? "button" : "submit")
      ).withMethod("post").withAction(Urls.goal(match, "1")),
      form(attrs(".form-inline.mt-3"),
        label(attrs(".w-75.h3"),
          iff(match.didTeam2Crawl(), crawling("")),
          text(match.getTeam2FullName())
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
          iff(match.isTeam1Winner(), iffElse(useHelmet(match.getTeam1FullName()), helmet("-light"), victoryIcon())),
          text(match.getTeam1FullName()),
          span(attrs(".badge.badge-light.ml-2"), Integer.toString(match.getTeam1Score()))
        ).withHref(url),
        a(attrs(".btn.btn-light.w-50"),
          iff(match.didTeam2Crawl(), crawling("")),
          iff(match.isTeam2Winner(), iffElse(useHelmet(match.getTeam2FullName()), helmet(""), victoryIcon())),
          text(match.getTeam2FullName()),
          span(attrs(".badge.badge-dark.ml-2"), Integer.toString(match.getTeam2Score()))
        ).withHref(url)
      )
    );
  }

  private static ContainerTag victoryIcon() {
    return Bootstrap.icon("badge.mr-2");
  }

  private static EmptyTag crawling(String qualifier) {
    return svg("/crawling" + qualifier + ".svg", "Crawling");
  }

  private static EmptyTag helmet(String qualifier) {
    return svg("/helmet" + qualifier + ".svg", "Helmet");
  }

  private static EmptyTag svg(String src, String title) {
    return img(attrs(".mr-3")).withSrc(src).withAlt(title).withTitle(title).withStyle("height: 1.5em");
  }

  private static boolean useHelmet(String teamFullName) {
    return teamFullName.contains("Kathleen");
  }
}
