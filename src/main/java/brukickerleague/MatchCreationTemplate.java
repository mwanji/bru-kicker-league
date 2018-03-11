package brukickerleague;

import static brukickerleague.Bootstrap.*;
import static j2html.TagCreator.*;

public class MatchCreationTemplate {

    public String render() {
        return new Page("New Match",
            h1("New Match"),
            h2("Team 1"),
            form(
                formGroup(
                    label("Player 1").attr("for", "team1[player1]"),
                    input(attrs("#team1[player1].form-control")).withName("team1[player1]")
                ),
                formGroup(
                    label("Player 2").attr("for", "team1[player2]"),
                    input(attrs("#team1[player2].form-control")).withName("team1[player2]")
                ),
                h2("Team 2"),
                formGroup(
                    label("Player 1").attr("for", "team2[player1]"),
                    input(attrs("#team2[player1].form-control")).withName("team2[player1]")
                ),
                formGroup(
                    label("Player 2").attr("for", "team2[player2]"),
                    input(attrs("#team2[player2].form-control")).withName("team2[player2]")
                ),
                submit()
            ).withMethod("post")
        ).render();
    }
}
