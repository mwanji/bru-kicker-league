package brukickerleague;

import j2html.tags.ContainerTag;
import j2html.tags.DomContent;

import java.util.Comparator;
import java.util.List;

import static brukickerleague.Bootstrap.*;
import static j2html.TagCreator.*;

public class EloTemplate {

  public enum OrderBy {
    RATING, DIFF_CURRENT, DIFF_LAST_WEEK;

    public static OrderBy fromUrl(String value) {
      if (value.isEmpty()) {
        return RATING;
      }

      try {
        return OrderBy.valueOf(value);
      } catch (IllegalArgumentException e) {
        return RATING;
      }
    }
  }


  public String renderElo(List<EloRating> ratings, OrderBy orderBy) {
    Comparator<EloRating> comparator;
    switch (orderBy) {
      case DIFF_CURRENT:
        comparator = Comparator.comparing(EloRating::getDiffCurrent).reversed();
        break;
      case DIFF_LAST_WEEK:
        comparator = Comparator.comparing(EloRating::getDiffLastWeek).reversed();
        break;
      default:
        comparator = Comparator.comparing(EloRating::getRatingCurrent).reversed();
    }
    ratings.sort(comparator);

    return new Page("ELO Ranking",
      h1(attrs(".display-3.mb-4"), "ELO Ranking"),
      table(attrs(".table"),
        thead(
          th(attrs(".w-25")),
          th(attrs(".w-25"), ratingTh("Rating", OrderBy.RATING, orderBy)),
          th(attrs(".w-25"), ratingTh("This Week", OrderBy.DIFF_CURRENT, orderBy)),
          th(attrs(".w-25"), ratingTh("Last Week", OrderBy.DIFF_LAST_WEEK, orderBy))
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

  private DomContent ratingTh(String title, OrderBy reference, OrderBy orderBy) {
    return iffElse(reference == orderBy,
      join(title, icon("chevron-bottom.ml-2")),
      a(title).withHref("?orderBy=" + reference));
  }
}
