package com.gmail.rgizmalkov.edu.projetcs.destriburted_system.registr;

import lombok.NonNull;

import java.util.Iterator;
import java.util.Objects;

public abstract class ConcurrentCircleArrayList<T> implements Iterator<T> {


    protected final Object lock = new Object();

    protected final int size;

    protected volatile Node<T> currentNode;

//    public static<T> ConcurrentCircleLinkedList<T> circle(T... values){
//        return new ConcurrentCircleLinkedList<>(values);
//    }

    @SafeVarargs
    protected ConcurrentCircleArrayList(@NonNull T... values) {
        this.size = values.length;
        buildArray(values);
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
