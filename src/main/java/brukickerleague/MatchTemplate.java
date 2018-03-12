package brukickerleague;

import lombok.AllArgsConstructor;

import static j2html.TagCreator.*;

@AllArgsConstructor
public class MatchTemplate {

  private final Match match;

  public String render() {
    String baseUrl = "/match/" + match.getAltId();
    String team1Score = Integer.toString(match.getTeam1Score());
    String team2Score = Integer.toString(match.getTeam2Score());

    return new Page("Now Playing",
      h1("Now Playing"),
      form(attrs(".form-inline"),
        label(attrs(".w-75"), match.getTeam1FullName()),
        iffElse(match.hasEnded(),
          span(team1Score),
          button(attrs(".btn.btn-light.w-25.btn-lg"), team1Score).withType("submit")
        )
      ).withMethod("post").withAction(baseUrl + "/goal/1"),
      form(attrs(".form-inline.mt-3"),
        label(attrs(".w-75"), match.getTeam2FullName()),
        iffElse(match.hasEnded(),
          span(team2Score),
          button(attrs(".btn.btn-dark.w-25.btn-lg"), team2Score).withType("submit")
        )
      ).withMethod("post").withAction(baseUrl + "/goal/2"),
      iff(!match.hasEnded(),
        form(attrs(".form-inline.mt-3"),
          button(attrs(".btn.btn-primary.w-100.btn-lg"), "Finished")
        ).withMethod("post").withAction(baseUrl + "/end")
      )
    ).render();
  }
}
