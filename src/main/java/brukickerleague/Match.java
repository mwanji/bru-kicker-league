package brukickerleague;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;

import static lombok.AccessLevel.*;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@NamedQueries({
  @NamedQuery(name = "Match.betweenDates", query = "from Match m where m.createdAt >= ?1 and m.createdAt <= ?2 and m.endedAt is not null"),
  @NamedQuery(name = "Match.ended", query = "from Match m where m.endedAt is not null order by createdAt desc"),
  @NamedQuery(name = "Match.notEnded", query = "from Match m where m.endedAt is null order by createdAt desc"),
  @NamedQuery(name = "Match.byPlayer", query = "from Match m where m.team1Player1 = ?1 or m.team1Player2 = ?1 or m.team2Player1 = ?1 or m.team2Player2 = ?1 order by m.createdAt desc"),
  @NamedQuery(name = "Player.names", query = "select m.team1Player1, m.team2Player1, m.team1Player2, m.team2Player2 from Match m")
})
@ValidMatch
class Match {

  @Id
  @SequenceGenerator(name = "match_id_seq", sequenceName = "match_id_seq")
  @GeneratedValue(generator = "match_id_seq")
  private Long id;

  @NotBlank
  @Column(updatable = false, unique = true)
  private final String altId = UUID.randomUUID().toString();
  @NotNull
  @Column(updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private final ZonedDateTime createdAt = ZonedDateTime.now();
  @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private ZonedDateTime endedAt;

  @NotBlank
  @Length(max = 50)
  private String team1Player1;
  @Length(max = 50)
  private String team1Player2;
  @NotBlank
  @Length(max = 50)
  private String team2Player1;
  @Length(max = 50)
  private String team2Player2;
  @Setter
  @Min(0)
  private int team1Score;
  @Setter
  @Min(0)
  private int team2Score;

  Match(String team1Player1, String team1Player2, String team2Player1, String team2Player2) {
    this.team1Player1 = team1Player1;
    this.team1Player2 = team1Player2;
    this.team2Player1 = team2Player1;
    this.team2Player2 = team2Player2;
  }

  public void addGoal(String teamId) {
    if ("1".equals(teamId)) {
      team1Score++;
    } else if ("2".equals(teamId)) {
      team2Score++;
    }
  }

  public void end() {
    endedAt = ZonedDateTime.now();
  }

  public boolean hasEnded() {
    return endedAt != null;
  }

  public boolean team1HasPlayer2() {
    return team1Player2 != null && !team1Player2.trim().isEmpty();
  }

  public boolean team2HasPlayer2() {
    return team2Player2 != null && !team2Player2.trim().isEmpty();
  }

  public String getTeam1FullName() {
    return team1Player1 + (team1Player2 != null && !team1Player2.isEmpty() ? " / " + team1Player2 : "");
  }

  public String getTeam2FullName() {
    return team2Player1 + (team2Player2 != null && !team2Player2.isEmpty() ? " / " + team2Player2 : "");
  }

  public boolean isTeam1Winner() {
    return hasEnded() && team1Score > team2Score;
  }
}
