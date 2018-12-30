package com.buildworld.engine.utils;

/**
 * Borrowed from: http://www.captaindebug.com/2011/05/generic-factory-class-sample.html#.XCZcOlxKjIU
 * @param <T>
 */
public class Factory<T> {

    // T type MUST have a default constructor
    private final Class<T> type;

    public Factory(Class<T> type) {

        this.type = type;
    }

    /**
     * Use the factory to get the next instance.
     */
    public T make() {

        try {
            // assume type is a public class
            return type.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create the factory. Note that V can be T, but to demonstrate that
     * generic method are not generic classes, I've called it V and not T.
     * In using this method V becomes T.
     */
    public static <V> Factory<V> forType(Class<V> type) {

        return new Factory<>(type);
    }
}