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

    Db db = initDb();
    runDbMigrations();

    deploymentPort().ifPresent(Spark::port);

    staticFiles.location("web");

    initMatchController(validator, db);
    StandingsController standingsController = new StandingsController(db);
    get("/", standingsController::getStandings);
    get("elo", standingsController::getEloRatings);

    PlayerController playerController = new PlayerController(db);
    get(Urls.player(":name"), playerController::getPlayer);
    get(Urls.awards(), playerController::getAwards);

    exception(IllegalArgumentException.class, (ex, req, res) -> {
      res.status(404);
      res.body(ex.getMessage());
    });
  }

  private static void initMatchController(Validator validator, Db db) {
    MatchController matchController = new MatchController(validator, db);
    path(Urls.match(), () -> {
      get("", matchController::createMatchForm);
      post("", matchController::createMatch);
      get("/:id", matchController::showMatch);
      post("/:id/goal/:teamId", matchController::addGoal);
      post("/:id/end", matchController::endMatch);
    });
    get(Urls.matches(), matchController::showMatches);
  }

  private static Db initDb() {
    HashMap<String, Object> persistenceProperties = new HashMap<>();
    persistenceProperties.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
    persistenceProperties.put("javax.persistence.jdbc.url", databaseUrl());
    persistenceProperties.put("javax.persistence.provider", "org.hibernate.jpa.HibernatePersistenceProvider");
    persistenceProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL9Dialect");
    persistenceProperties.put("hibernate.connection.url", databaseUrl());
    persistenceProperties.put("hibernate.hikari.connectionTimeout", "20000");
    persistenceProperties.put("hibernate.hikari.minimumIdle", "1");
    persistenceProperties.put("hibernate.hikari.maximumPoolSize", "20");
    persistenceProperties.put("hibernate.hikari.idleTimeout", "300000");
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("bruKickerLeague", persistenceProperties);
    return new Db(entityManagerFactory);
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
