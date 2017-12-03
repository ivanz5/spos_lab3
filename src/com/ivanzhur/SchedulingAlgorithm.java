package com.ivanzhur;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SchedulingAlgorithm {

    private static Random random = new Random();
    private static List<Integer> estimateTime;
    private static final float a = 0.5f;

    public static Results run(int runtime, List<Process> processList, Results result) {
        int compTime = 0;
        int currentProcess = 0;
        int size = processList.size();
        int completed = 0;
        estimateTime = new ArrayList<>();
        for (int j = 0; j < processList.size(); j++) estimateTime.add(0);
        
        String resultsFile = "Summary-Processes";

        result.schedulingType = "Interactive";
        result.schedulingName = "Shortest Process Next";
        try {
            PrintStream out = new PrintStream(new FileOutputStream(resultsFile));

            Process process = processList.get(currentProcess);

            while (compTime < runtime) {
                if (process.timeDone == process.totalTime) {
                    process.completed = true;
                    completed++;
                    out.println("Process: " + currentProcess + " completed... (" + process.totalTime + ")");

                    // All processes completed
                    if (completed == size) {
                        result.computationTime = compTime;
                        out.close();
                        return result;
                    }
                }
                else {
                    int currentBurstTime = process.getNextBurstTime(random);

                    out.println("Process: " + currentProcess + " running... (" + process.totalTime + " " + currentBurstTime + " " + process.timeDone + ")");

                    process.timeDone += currentBurstTime;
                    process.burstsCount++;
                    if (process.timeDone > process.totalTime) process.timeDone = process.totalTime;

                    if (estimateTime.get(currentProcess) == 0) {
                        estimateTime.set(currentProcess, currentBurstTime);
                    }
                    else {
                        int newEstimatedTime = (int) (a * estimateTime.get(currentProcess) + (1 - a) * currentBurstTime);
                        estimateTime.set(currentProcess, newEstimatedTime);
                    }
                }

                // Select next process to run
                currentProcess = 0;
                int minBurstTime = Integer.MAX_VALUE;
                for (int i = 0; i < size; i++) {
                    process = processList.get(i);
                    if (process.completed) continue;
                    if (estimateTime.get(i) < minBurstTime) {
                        currentProcess = i;
                        minBurstTime = estimateTime.get(i);
                    }
                }

                process = processList.get(currentProcess);

                compTime++;
            }

            out.close();
        } catch (IOException e) { /* Handle exceptions */ }

        result.computationTime = compTime;
        return result;
    }
}
