package net.anzix.imprempta.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Tag the execution of a transformer with a named phase.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Execute {
    Phase value();
}
