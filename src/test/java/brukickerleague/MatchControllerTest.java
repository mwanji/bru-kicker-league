package brukickerleague;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import spark.QueryParamsMap;
import spark.Request;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

class MatchControllerTest {

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  public void should_validate_duplicate_players() {
    Db db = mock(Db.class);
    Request req = mock(Request.class);
    QueryParamsMap params = mock(QueryParamsMap.class);
    when(req.queryMap("team1")).thenReturn(params);
    when(req.queryMap("team2")).thenReturn(params);
    when(params.value("player1")).thenReturn("dup");

    String html = new MatchController(validator, db).createMatch(req, null);

    Document document = Jsoup.parse(html);
    Element element = document.selectFirst("#duplicatePlayer");

    assertThat("Missing duplicate player error message", element, notNullValue());
  }
}