package com.buildworld.engine.interfaces;

public interface IFactory<T extends IFactory<T>> {
    T make();
}
