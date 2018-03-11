package brukickerleague;

import org.flywaydb.core.Flyway;
import spark.Spark;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;

import static spark.Spark.*;

public class Application {

  private static Map<String, String> environment;
  public static void main(String[] args) {
    environment = new ProcessBuilder().environment();

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    HashMap<String, String> persistenceProperties = new HashMap<>();
    persistenceProperties.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
    persistenceProperties.put("javax.persistence.jdbc.url", databaseUrl());
    persistenceProperties.put("javax.persistence.schema-generation.database.action", "drop-and-create");
    persistenceProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL9Dialect");
    persistenceProperties.put("javax.persistence.provider", "org.hibernate.jpa.HibernatePersistenceProvider");
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("bruKickerLeague", persistenceProperties);
    Db db = new Db(entityManagerFactory);
    //runDbMigrations();


    deploymentPort().ifPresent(Spark::port);

    MatchesController matchesController = new MatchesController(validator, db);
    get("/match", (req, res) -> new MatchCreationTemplate().render());
    post("/match", matchesController::createMatch);
    get("/match/:altId", matchesController::showMatch);
    post("/match/:altId/goal/:teamId", matchesController::addGoal);
  }

  private static OptionalInt deploymentPort() {
    if (environment.get("PORT") != null) {
      return OptionalInt.of(Integer.parseInt(environment.get("PORT")));
    }
    return OptionalInt.empty();
  }

  private static String databaseUrl() {
    return environment.get("JDBC_DATABASE_URL");
  }

  private static void runDbMigrations() {
    Flyway flyway = new Flyway();
    flyway.setDataSource(databaseUrl(), environment.get("JDBC_DATABASE_USERNAME"), environment.get("JDBC_DATABASE_PASSWORD"));
    flyway.migrate();

  }
}
