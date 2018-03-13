package brukickerleague;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

@Retention(RUNTIME)
@Target(TYPE)
@Constraint(validatedBy = MatchValidator.class)
public @interface ValidMatch {
  String message() default "{brukickerleague.MatchValidator.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
