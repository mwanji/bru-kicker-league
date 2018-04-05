package brukickerleague;

import java.math.BigDecimal;

import static java.math.BigDecimal.*;
import static java.math.RoundingMode.*;

public class Elo {

  public static BigDecimal expectedScore(int rating1, int rating2) {
    BigDecimal ratingDiff = BigDecimal.valueOf(rating2 - rating1);
    double pow = Math.pow(10, ratingDiff.divide(BigDecimal.valueOf(400), 4, HALF_UP).doubleValue());

    return ONE.divide(ONE.add(BigDecimal.valueOf(pow)), 2, HALF_UP);
  }

  public static Integer ratingDiff(BigDecimal expectedScore, boolean winner) {
    return (winner ? BigDecimal.ONE : BigDecimal.ZERO).subtract(expectedScore).multiply(BigDecimal.valueOf(100)).intValue();
  }
}
