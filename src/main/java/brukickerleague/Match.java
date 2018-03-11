package brukickerleague;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
class Match {

  private Long id;
  private final String altId = UUID.randomUUID().toString();
  private final Instant createdAt = Instant.now();

  @NotBlank
  private String team1Player1;
  private String team1Player2;
  @NotBlank
  private String team2Player1;
  private String team2Player2;

  @Setter
  private int team1Score;

  @Setter
  private int team2Score;
}
