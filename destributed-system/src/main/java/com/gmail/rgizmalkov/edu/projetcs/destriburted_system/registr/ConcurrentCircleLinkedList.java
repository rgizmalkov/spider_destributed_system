package com.gmail.rgizmalkov.edu.projetcs.destriburted_system.registr;

import lombok.NonNull;

import java.util.Iterator;
import java.util.Objects;

public abstract class ConcurrentCircleLinkedList<T> implements Iterator<T> {

    private class Node<Type> {
        Node<Type> next;
        Type value;

        Node(Type value) {
            this.value = value;
        }

        void postConstruct() {
            Objects.requireNonNull(next, "Next link in circle array cannot be null!");
            Objects.requireNonNull(value, "Current value in circle array cannot be null!");
        }
    }

    protected final Object lock = new Object();

    protected final int size;

    protected volatile Node<T> currentNode;

//    public static<T> ConcurrentCircleLinkedList<T> circle(T... values){
//        return new ConcurrentCircleLinkedList<>(values);
//    }

    @SafeVarargs
    protected ConcurrentCircleLinkedList(@NonNull T... values) {
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


    public T nextValue() {
        T retVal = currentNode.value;
        currentNode = currentNode.next;
        return retVal;
    }

}
