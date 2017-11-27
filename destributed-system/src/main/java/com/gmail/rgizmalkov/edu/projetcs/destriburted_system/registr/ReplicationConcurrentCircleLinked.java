package com.gmail.rgizmalkov.edu.projetcs.destriburted_system.registr;

public class ReplicationConcurrentCircleLinked<Type> extends ConcurrentCircleLinkedList<Type> {


    public static <T> ReplicationConcurrentCircleLinked<T> circle(T... values) {
        return new ReplicationConcurrentCircleLinked<>(values);
    }

    @SafeVarargs
    private ReplicationConcurrentCircleLinked(Type... values) {
        super(values);
    }

    @Override
    public Type next() {


    }
}
