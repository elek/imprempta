package net.anzix.imprempta.api;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation with a type for injecting different type based on generic type.
 */
@BindingAnnotation
@Retention(RetentionPolicy.RUNTIME)
public @interface Type {
    Class value();
}
