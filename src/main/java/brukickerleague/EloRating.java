package brukickerleague;

import lombok.Data;

@Data
public class EloRating {
  private final String playerName;
  private Integer ratingCurrent;
  private Integer ratingLastWeek;
  private Integer rating2WeeksAgo;

  public boolean hasDiffCurrent() {
    return ratingLastWeek != null;
  }

  public Integer getDiffCurrent() {
    if (!hasDiffCurrent()) {
      return 0;
    }
    return ratingCurrent - ratingLastWeek;
  }

  public boolean hasDiffLastWeek() {
    return hasDiffCurrent() && rating2WeeksAgo != null;
  }

  public Integer getDiffLastWeek() {
    if (!hasDiffLastWeek()) {
      return 0;
    }

    return ratingLastWeek - rating2WeeksAgo;
  }
}
