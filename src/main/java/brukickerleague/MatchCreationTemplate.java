package brukickerleague;

import java.util.Collections;
import java.util.Map;

import static brukickerleague.Bootstrap.*;
import static j2html.TagCreator.*;

public class MatchCreationTemplate {

  private final Map<String, String> errors;

  MatchCreationTemplate() {
    this(Collections.emptyMap());
  }

  MatchCreationTemplate(Map<String, String> errors) {
    this.errors = errors;
  }

  public String render() {
    return new Page("New Match",
      h1("New Match"),
      iff(errors.containsKey("duplicatePlayer"), div(attrs("#duplicatePlayer.alert.alert-danger"), errors.get("duplicatePlayer")).attr("role", "alert")),
      h2("Team 1"),
      form(
        formGroup(
          label("Player 1").attr("for", "team1[player1]"),
          input(attrs("#team1[player1]")).withName("team1[player1]").withClasses("form-control", ifInvalidInput("team1Player1", errors)).isRequired().attr("maxlength", 50),
          ifInvalidHelpText("team1Player1", errors)
        ),
        formGroup(
          label("Player 2").attr("for", "team1[player2]"),
          input(attrs("#team1[player2].form-control")).withName("team1[player2]").attr("maxlength", 50)
        ),
        h2("Team 2"),
        formGroup(
          label("Player 1").attr("for", "team2[player1]"),
          input(attrs("#team2[player1]")).withName("team2[player1]").withClasses("form-control", ifInvalidInput("team2Player1", errors)).isRequired().attr("maxlength", 50),
          ifInvalidHelpText("team2Player1", errors)
        ),
        formGroup(
          label("Player 2").attr("for", "team2[player2]"),
          input(attrs("#team2[player2].form-control")).withName("team2[player2]").attr("maxlength", 50)
        ),
        submit()
      ).withMethod("post")
    ).render();
  }
}
