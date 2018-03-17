package brukickerleague;

import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static j2html.TagCreator.*;

@AllArgsConstructor
public class PlayerTemplate {

  private final String name;
  private final Map<LocalDate, List<Match>> matches;

  public String render() {
    return new Page(name,
      h1(attrs(".display-3.mb-4"), name),
      each(matches.keySet(), date -> join(
        h3(attrs(".mt-3.text-secondary"), DateTimeFormatter.ISO_LOCAL_DATE.format(date)),
        each(matches.get(date), MatchTemplate::singleMatch)
      ))
    ).render();
  }
}
