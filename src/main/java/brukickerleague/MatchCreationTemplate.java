package brukickerleague;

import j2html.TagCreator;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Set;

import static brukickerleague.Bootstrap.*;
import static j2html.TagCreator.*;

@RequiredArgsConstructor
public class MatchCreationTemplate {

  private final Map<String, String> errors;
  private final Set<String> players;

  public String render() {
    return new Page("New Match",
      h1(attrs(".display-3"), "New Match"),
      iff(errors.containsKey("duplicatePlayer"), div(attrs("#duplicatePlayer.alert.alert-danger"), errors.get("duplicatePlayer")).attr("role", "alert")),
      form(
        datalist(attrs("#players"),
          each(players, TagCreator::option)
        ),
        h2(attrs(".bg-dark.text-white.p-4"), "Black Team"),
        formGroup(
          label("Player 1").attr("for", "team1[player1]"),
          input(attrs("#team1[player1]")).withName("team1[player1]").withClasses("form-control", ifInvalidInput("team1Player1", errors)).isRequired().attr("list", "players").attr("maxlength", 50),
          ifInvalidHelpText("team1Player1", errors)
        ),
        formGroup(
          label("Player 2").attr("for", "team1[player2]"),
          input(attrs("#team1[player2].form-control")).withName("team1[player2]").attr("list", "players").attr("maxlength", 50)
        ),
        h2(attrs(".bg-light.p-4"), "White Team"),
        formGroup(
          label("Player 1").attr("for", "team2[player1]"),
          input(attrs("#team2[player1]")).withName("team2[player1]").withClasses("form-control", ifInvalidInput("team2Player1", errors)).isRequired().attr("list", "players").attr("maxlength", 50),
          ifInvalidHelpText("team2Player1", errors)
        ),
        formGroup(
          label("Player 2").attr("for", "team2[player2]"),
          input(attrs("#team2[player2].form-control")).withName("team2[player2]").attr("list", "players").attr("maxlength", 50)
        ),
        submit()
      ).withMethod("post")
    ).render();
  }
}
