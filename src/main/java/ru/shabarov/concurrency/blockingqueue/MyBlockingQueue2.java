package ru.shabarov.concurrency.blockingqueue;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyBlockingQueue2<E> implements BlockingQueue<E> {

    private Queue<E> queue = new LinkedList<>();

    private int maxSize;

    private Lock lock = new ReentrantLock();

    private Condition putCondition = lock.newCondition();
    private Condition takeCondition = lock.newCondition();

    public MyBlockingQueue2(int maxSize) {
        this.maxSize = maxSize;
    }

    public MyBlockingQueue2() {
        this.maxSize = 2;
    }

    @Override
    public void put(E e) throws InterruptedException {
        try {
            lock.lock();
            while (this.queue.size() == this.maxSize) {
                putCondition.await();
            }
            this.queue.add(e);
            takeCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public E take() throws InterruptedException {
        try {
            lock.lock();
            while (this.queue.isEmpty()) {
                takeCondition.await();
            }
            putCondition.signal();
            return this.queue.remove();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean add(E e) {
        return false;
    }

    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E remove() {
        return null;
    }

    @Override
    public E poll() {
        return null;
    }

    @Override
    public E element() {
        return null;
    }

    @Override
    public E peek() {
        return null;
    }

    @Override
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    @Override
    public int remainingCapacity() {
        return 0;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public int drainTo(Collection<? super E> c) {
        return 0;
    }

    @Override
    public int drainTo(Collection<? super E> c, int maxElements) {
        return 0;
    }
}
