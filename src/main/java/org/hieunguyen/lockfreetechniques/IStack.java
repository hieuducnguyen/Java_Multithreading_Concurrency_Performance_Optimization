package org.hieunguyen.lockfreetechniques;

public interface IStack<T> {

    T pop();

    boolean push(T pointer);

    long getCounter();
}
