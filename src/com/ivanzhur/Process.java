package com.ivanzhur;

import java.util.Random;

public class Process {
    public int totalTime;
    public int burstTime; // Subject for random deviation to apply
    public int burstTimeDeviation;
    public int timeDone = 0;
    public int burstsCount = 0;
    public boolean completed = false;

    public Process(int totalTime, int burstTime, int burstTimeDeviation) {
        this.totalTime = totalTime;
        this.burstTime = burstTime;
        this.burstTimeDeviation = burstTimeDeviation;
    }

    public int getNextBurstTime(Random random) {
        if (random == null) return burstTime;

        int time = (burstTime - burstTimeDeviation) + random.nextInt() % (2 * burstTimeDeviation);
        return time > 0 ? time : 10;
    }
}
