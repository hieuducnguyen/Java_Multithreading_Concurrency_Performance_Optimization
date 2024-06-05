package org.hieunguyen.lockfreetechniques;

public class Pointer<T> {
    T value;
    Pointer<T> next;

    public Pointer(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Pointer<T> getNext() {
        return next;
    }

    public void setNext(Pointer<T> next) {
        this.next = next;
    }
}
