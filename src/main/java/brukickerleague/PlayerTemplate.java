package brukickerleague;

import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static j2html.TagCreator.*;

@AllArgsConstructor
public class PlayerTemplate {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  private final String name;
  private final Map<LocalDate, List<Match>> matches;
  private final List<Award> awards;

  public String render() {
    return new Page(name,
      h1(attrs(".display-3.mb-4"), name),
      iff(!awards.isEmpty(), join(
        h3(attrs(".mt-3.text-secondary"), "Awards"),
        each(awards, award -> div("Player of the week: " + DATE_FORMATTER.format(award.getStartedAt()) + " - " + DATE_FORMATTER.format(award.getEndedAt())))
      )),
      each(matches.keySet(), date -> join(
        h3(attrs(".mt-3.text-secondary"), DATE_FORMATTER.format(date)),
        each(matches.get(date), MatchTemplate::singleMatch)
      ))
    ).render();
  }
}
