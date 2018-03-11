package brukickerleague;

import lombok.AllArgsConstructor;

import static j2html.TagCreator.*;

@AllArgsConstructor
public class MatchTemplate {

  private final Match match;

  public String render() {
    return new Page("Now Playing",
      h1("Now Playing"),
      form(attrs(".form-inline"),
        label(attrs(".w-50"), match.getTeam1Player1()),
        button(attrs(".btn.btn-light.w-25"), Integer.toString(match.getTeam1Score())).withType("submit")
      ).withMethod("post").withAction("/match/" + match.getAltId() + "/goal/1"),
      form(attrs(".form-inline.mt-3"),
        label(attrs(".w-50"), match.getTeam2Player1()),
        button(attrs(".btn.btn-dark.w-25"), Integer.toString(match.getTeam2Score())).withType("submit")
      ).withMethod("post").withAction("/match/" + match.getAltId() + "/goal/2")
    ).render();
  }
}
