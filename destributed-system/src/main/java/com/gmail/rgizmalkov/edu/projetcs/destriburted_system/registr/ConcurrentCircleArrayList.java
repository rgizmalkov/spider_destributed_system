package com.gmail.rgizmalkov.edu.projetcs.destriburted_system.registr;

import lombok.NonNull;

import java.util.Iterator;
import java.util.Objects;

public class ConcurrentCircleArrayList<T> implements Iterator<T> {

    private class Node<Type> {
        Node<Type> next;
        Type value;

        Node(Type value) {
            this.value = value;
        }

        void postConstruct(){
            Objects.requireNonNull(next, "Next link in circle array cannot be null!");
            Objects.requireNonNull(value, "Current value in circle array cannot be null!");
        }
    }

    private final Object lock = new Object();

    private final int size;

    private volatile Node<T> currentNode;

    public static<T> ConcurrentCircleArrayList<T> circle(T... values){
        return new ConcurrentCircleArrayList<>(values);
    }

    @SafeVarargs
    private ConcurrentCircleArrayList(@NonNull T... values) {
        this.size = values.length;
        buildArray(values);
    }

    private void buildArray(T[] nodes) {
        Node<T> current = new Node<T>(nodes[0]);
        Node<T> first = current;
        this.currentNode = first;
        for (int nodeNumber = 1; nodeNumber < nodes.length; nodeNumber++) {
            Node<T> next = new Node<>(nodes[nodeNumber]);
            current.next = next;
            current.postConstruct();
            current = next;
        }
        current.next = first;
        current.postConstruct();
    }


    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public T next() {
        if (size == 1) return currentNode.value;
        synchronized (lock){
            T retVal = currentNode.value;
            currentNode = currentNode.next;
            return retVal;
        }
    }
}
