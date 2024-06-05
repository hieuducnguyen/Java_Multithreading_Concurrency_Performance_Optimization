package org.hieunguyen.lockfreetechniques;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

public class LockFreeStack implements IStack<Integer> {
    AtomicReference<Pointer<Integer>> head;
    AtomicLong counter = new AtomicLong(0);

    public LockFreeStack() {
        Pointer<Integer> pointer = new Pointer<>(null);
        head = new AtomicReference<>(pointer);
    }

    @Override
    public Integer pop() {
        counter.incrementAndGet();
        while (true) {
            Pointer<Integer> pointer = head.get();
            if (pointer == null) {
                return null;
            }
            if (head.compareAndSet(pointer, pointer.getNext())) {
                return pointer.getValue();
            } else {
//                LockSupport.parkNanos(1);
            }
        }
    }

    @Override
    public boolean push(Integer value) {
        counter.incrementAndGet();
        Pointer<Integer> pointer = new Pointer<>(value);
        while (true) {
            Pointer<Integer> headPointer = head.get();
            pointer.next = headPointer;
            if (head.compareAndSet(headPointer, pointer)) {
                break;
            } else {
//                LockSupport.parkNanos(1);
            }
        }
        return true;
    }

    @Override
    public long getCounter() {
        return counter.get();
    }
}
