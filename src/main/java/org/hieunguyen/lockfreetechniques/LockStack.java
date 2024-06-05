package org.hieunguyen.lockfreetechniques;

public class LockStack implements IStack<Integer> {
    Pointer<Integer> head;
    long counter = 0;

    public LockStack() {
        head = new Pointer<>(null);
    }

    @Override
    public synchronized Integer pop() {
        counter++;
        Pointer<Integer> ptr;
//        synchronized (this) {
        if (head == null) {
            return null;
        }
        ptr = head;
        head = head.next;
//        }
        return ptr.value;
    }

    @Override
    public synchronized boolean push(Integer value) {
        Pointer<Integer> pointer = new Pointer<>(value);
        counter++;
//        synchronized (this) {
        pointer.next = head;
        head = pointer;
//        }
        return true;
    }

    @Override
    public long getCounter() {
        return counter;
    }
}
