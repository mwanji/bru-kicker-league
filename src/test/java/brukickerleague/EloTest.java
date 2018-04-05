package brukickerleague;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static brukickerleague.Elo.*;
import static java.math.BigDecimal.*;
import static org.junit.jupiter.api.Assertions.*;

class EloTest {

  @Test
  public void should_calculate_expected_score_according_to_rating() {
    assertEquals(new BigDecimal("0.79"), expectedScore(1613, 1388));
    assertEquals(new BigDecimal("0.69"), expectedScore(1613, 1477));
    assertEquals(new BigDecimal("0.51"), expectedScore(1613, 1609));
    assertEquals(new BigDecimal("0.54"), expectedScore(1613, 1586));
    assertEquals(new BigDecimal("0.50"), expectedScore(1600, 1600));
    assertEquals(new BigDecimal("0.35"), expectedScore(1613, 1720));
  }

  @Test
  public void should_calculate_rating_diff() {
    assertEquals(90, ratingDiff(new BigDecimal("0.10"), true).intValue());
    assertEquals(-10, ratingDiff(new BigDecimal("0.10"), false).intValue());
    assertEquals(100, ratingDiff(ZERO, true).intValue());
    assertEquals(0, ratingDiff(ZERO, false).intValue());
    assertEquals(0, ratingDiff(ONE, true).intValue());
    assertEquals(-100, ratingDiff(ONE, false).intValue());
    assertEquals(50, ratingDiff(new BigDecimal("0.50"), true).intValue());
    assertEquals(-50, ratingDiff(new BigDecimal("0.50"), false).intValue());
  }
}
