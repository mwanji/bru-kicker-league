package brukickerleague;

import spark.Spark;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.OptionalInt;

import static spark.Spark.*;

public class Application {

  public static void main(String[] args) {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    MatchesController matchesController = new MatchesController(validator);

    deploymentPort().ifPresent(Spark::port);

    get("/match", (req, res) -> new MatchCreationTemplate().render());
    post("/match", matchesController::createMatch);
    get("/match/:altId", (req, res) -> req.params("altId"));
  }

  private static OptionalInt deploymentPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      return OptionalInt.of(Integer.parseInt(processBuilder.environment().get("PORT")));
    }
    return OptionalInt.empty();
  }

}
