package com.gmail.rgizmalkov.edu.projects.vo;

import java.util.HashSet;
import java.util.Set;

public class NodeDifference {
    String title;
    boolean isEquals;
    int difSizeFirst;
    Set<TaskResult> missFirst;
    int difSizeSecond;
    Set<TaskResult> missSecond;

    public NodeDifference(
            String firstNodeName, String secondNodeName,
            Set<TaskResult> first, Set<TaskResult> second) {

        this.title = String.format("Compare difference btw two nodes [%s] and [%s].", firstNodeName, secondNodeName);
        this.isEquals = equals(first, second);
        this.missFirst = diff(first, second);
        this.missSecond = diff(second, first);
        this.difSizeFirst = missFirst.size();
        this.difSizeSecond = missSecond.size();
    }

    private Set<TaskResult> diff(Set<TaskResult> first, Set<TaskResult> second){
        Set<TaskResult> smokeOne = new HashSet<>(first);
        Set<TaskResult> smokeTow = new HashSet<>(second);

        smokeOne.removeAll(smokeTow);

        return smokeOne;
    }

    private boolean equals(Set<?> set1, Set<?> set2){

        if(set1 == null || set2 ==null){
            return false;
        }

        if(set1.size()!=set2.size()){
            return false;
        }

        return set1.containsAll(set2);

    }

    public String getTitle() {
        return title;
    }

    public NodeDifference setTitle(String title) {
        this.title = title;
        return this;
    }

    public boolean isEquals() {
        return isEquals;
    }

    public NodeDifference setEquals(boolean equals) {
        isEquals = equals;
        return this;
    }

    public int getDifSizeFirst() {
        return difSizeFirst;
    }

    public NodeDifference setDifSizeFirst(int difSizeFirst) {
        this.difSizeFirst = difSizeFirst;
        return this;
    }

    public Set<TaskResult> getMissFirst() {
        return missFirst;
    }

    public NodeDifference setMissFirst(Set<TaskResult> missFirst) {
        this.missFirst = missFirst;
        return this;
    }

    public int getDifSizeSecond() {
        return difSizeSecond;
    }

    public NodeDifference setDifSizeSecond(int difSizeSecond) {
        this.difSizeSecond = difSizeSecond;
        return this;
    }

    public Set<TaskResult> getMissSecond() {
        return missSecond;
    }

    public NodeDifference setMissSecond(Set<TaskResult> missSecond) {
        this.missSecond = missSecond;
        return this;
    }
}
