package brukickerleague;

import spark.Spark;

public class Application {

    public static void main(String[] args) {
        Spark.get("/match", (req, res) -> new MatchCreationTemplate().render());
    }
}
