package com.example.heroesandroid;

public class ThreadSync {

    private boolean ready = false;

    public ThreadSync() {}

    public synchronized boolean isReady() {
        return ready;
    }

    public synchronized void setReady(boolean ready) {
        this.ready = ready;
    }
}
