package brukickerleague;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;

import static lombok.AccessLevel.*;

@Getter
@NoArgsConstructor(access = PROTECTED)
@NamedQueries({
  @NamedQuery(name = "Match.byAltId", query = "from Match m where m.altId = :altId")
})
@Entity
class Match {

  @Id
  @GeneratedValue
  private Long id;

  @NotBlank
  @Column(updatable = false)
  private final String altId = UUID.randomUUID().toString();
  @NotNull
  @Column(updatable = false)
  private final ZonedDateTime createdAt = ZonedDateTime.now();

  @NotBlank
  private String team1Player1;
  private String team1Player2;
  @NotBlank
  private String team2Player1;
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
}
