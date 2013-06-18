package net.anzix.imprempta.api;

/**
 * Interface to handle specific value in an untyped map (eg. site).
 */
public interface PropertyHandler<T> {

    public T get();

    public void set(T value);
}
