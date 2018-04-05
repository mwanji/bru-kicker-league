package brukickerleague;

import java.util.Map;

import static j2html.TagCreator.*;

public class EloTemplate {

  public String renderElo(Map<String, Integer> ratings) {
    return new Page("ELO Ranking",
      h1(attrs(".display-3.mb-4"), "ELO Ranking"),
      table(attrs(".table"),
        each(ratings.keySet(), player -> tr(
          td(player),
          td(ratings.get(player).toString())
        ))
      )
    ).render();
  }
}
