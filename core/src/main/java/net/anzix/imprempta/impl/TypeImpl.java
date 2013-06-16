package net.anzix.imprempta.impl;

import com.google.inject.BindingAnnotation;
import net.anzix.imprempta.api.Type;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 */
public class TypeImpl implements Type {
    Class type;

    public TypeImpl(Class type) {
        this.type = type;
    }

    @Override
    public Class value() {
        return type;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Type.class;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Type) {
            return ((Type) o).value().equals(this.value());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (127 * "value".hashCode()) ^ type.hashCode();
    }
}
