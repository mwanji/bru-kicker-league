package brukickerleague;

import javax.validation.Validation;
import javax.validation.Validator;

import static spark.Spark.get;
import static spark.Spark.post;

public class Application {

    public static void main(String[] args) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        MatchesController matchesController = new MatchesController(validator);

        get("/match", (req, res) -> new MatchCreationTemplate().render());
        post("/match", matchesController::createMatch);
        get("/match/:altId", (req, res) -> req.params("altId"));
    }
}
