package brukickerleague;

import javax.validation.ConstraintViolation;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static brukickerleague.Bootstrap.formGroup;
import static brukickerleague.Bootstrap.submit;
import static j2html.TagCreator.*;

public class MatchCreationTemplate {

    private final Map<String, String> errors;

    MatchCreationTemplate() {
        this(Collections.emptySet());
    }

    MatchCreationTemplate(Set<ConstraintViolation<Match>> constraintViolations) {
        this.errors = constraintViolations.stream().collect(Collectors.toMap(cv -> cv.getPropertyPath().toString(), ConstraintViolation::getMessage));
    }

    public String render() {
        return new Page("New Match",
            h1("New Match"),
            h2("Team 1"),
            form(
                formGroup(
                    label("Player 1").attr("for", "team1[player1]"),
                        input(attrs("#team1[player1]")).withName("team1[player1]").withClasses("form-control", iff(errors.containsKey("team1Player1"), "is-invalid")).isRequired(),
                        iff(errors.containsKey("team1Player1"), div(attrs(".invalid-feedback"), errors.get("team1Player1")))
                ),
                formGroup(
                    label("Player 2").attr("for", "team1[player2]"),
                    input(attrs("#team1[player2].form-control")).withName("team1[player2]")
                ),
                h2("Team 2"),
                formGroup(
                    label("Player 1").attr("for", "team2[player1]"),
                        input(attrs("#team2[player1]")).withName("team2[player1]").withClasses("form-control", iff(errors.containsKey("team2Player1"), "is-invalid")).isRequired(),
                        iff(errors.containsKey("team2Player1"), div(attrs(".invalid-feedback"), errors.get("team2Player1")))
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
