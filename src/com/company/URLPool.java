package com.company;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created by ngot on 31/12/2015.
 */
public class URLPool {
    private LinkedList<UrlDepthPair> pending;
    private Set<UrlDepthPair> seenUrls;
    private int maxDepth;
    private int waiting;

    public URLPool(int maxDepth) {
        pending = new LinkedList<>();
        seenUrls = new LinkedHashSet<>();
        this.maxDepth = maxDepth;
        waiting = 0;
    }

    /**
     * fetches a URL-depth pair from the pool and remove it from the list simultaneously
     */
    public synchronized  UrlDepthPair get() throws InterruptedException {
        while (pending.size() == 0) {
            waiting++;
            wait();
            waiting--;
        }
        return pending.removeFirst();
    }

    public synchronized void put(UrlDepthPair url) {
        if (url.getDepth() < maxDepth) {
            pending.addLast(url);
        }
        seenUrls.add(url);
        notify();
    }

    public synchronized boolean seen(UrlDepthPair url) {
        return seenUrls.contains(url);
    }

    public synchronized Set<UrlDepthPair> seen() {
        return seenUrls;
    }

    public synchronized int getMaxDepth() {
        return maxDepth;
    }

    public synchronized int getWaitingThreads() {
        return waiting;
    }
}
