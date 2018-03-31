package brukickerleague;

public class Urls {

  public static String match() {
    return "/match";
  }

  public static String match(Match match) {
    return match() + "/" + match.getId();
  }

  public static String goal(Match match, String teamId) {
    return match(match) + "/goal/" + teamId;
  }

  public static String end(Match match) {
    return match(match) + "/end";
  }

  private Urls() {
  }
}