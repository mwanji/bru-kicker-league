package brukickerleague;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static javax.persistence.EnumType.*;
import static lombok.AccessLevel.*;

@Entity
@NamedQueries({
  @NamedQuery(name = "Award.typeForPeriod", query = "from Award a where a.awardType = ?1 and a.startedAt = ?2")
})
@NoArgsConstructor(access = PROTECTED)
public class Award {

  public enum Type {
    PLAYER_OF_THE_WEEK
  }

  @Id
  @SequenceGenerator(name = "award_id_seq", sequenceName = "award_id_seq")
  @GeneratedValue(generator = "award_id_seq")
  private Long id;
  @Enumerated(STRING)
  @Column(updatable = false)
  @NotNull
  private Award.Type awardType;
  @Column(updatable = false)
  @NotNull
  @Getter
  private String playerName;
  @Column(updatable = false)
  @NotNull
  private LocalDate startedAt;
  @Column(updatable = false)
  @NotNull
  private LocalDate endedAt;

  public Award(@NotNull Award.Type awardType, @NotNull String playerName, @NotNull LocalDate startedAt, @NotNull LocalDate endedAt) {
    this.awardType = awardType;
    this.playerName = playerName;
    this.startedAt = startedAt;
    this.endedAt = endedAt;
  }
}
