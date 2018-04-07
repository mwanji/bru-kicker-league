package brukickerleague;

import j2html.tags.ContainerTag;

import java.util.Comparator;
import java.util.List;

import static brukickerleague.Bootstrap.*;
import static j2html.TagCreator.*;

public class EloTemplate {

  public String renderElo(List<EloRating> ratings) {
    ratings.sort(Comparator.comparing(EloRating::getRatingCurrent).reversed());

    return new Page("ELO Ranking",
      h1(attrs(".display-3.mb-4"), "ELO Ranking"),
      table(attrs(".table"),
        thead(
          th(),
          th("Rating"),
          th("This Week"),
          th("Last Week")
        ),
        each(ratings, rating -> tr(
          td(a(rating.getPlayerName()).withHref(Urls.player(rating.getPlayerName()))),
          td(rating.getRatingCurrent().toString()),
          ratingTd(rating.hasDiffCurrent(), rating.getDiffCurrent()),
          ratingTd(rating.hasDiffLastWeek(), rating.getDiffLastWeek())
        ))
      )
    ).render();
  }

  private ContainerTag ratingTd(boolean condition, Integer rating) {
    return iffElse(condition,
      iffElse(rating > 0,
        td(attrs(".text-success"),
          icon("arrow-top"),
          text(rating.toString())
        ),
        iffElse(rating < 0,
          td(attrs(".text-danger"),
            icon("arrow-bottom"),
            text(Integer.toString(Math.abs(rating)))
          ),
          td("0")
        )
      ),
      td()
    );
  }
}
