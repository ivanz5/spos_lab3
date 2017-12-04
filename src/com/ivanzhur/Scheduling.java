package com.ivanzhur;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Scheduling {

    private static final String resultsFile = "Summary-Results";

    private static int runtime = 1000;
    private static List<Process> processVector = new ArrayList<>();
    private static Results result = new Results("null", "null", 0);
    private static float aging;

    private static void init(String filename) {
        File file = new File(filename);
        String line;
        int totalTime;
        int burstTime;
        int deviation;

        try {
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            while ((line = in.readLine()) != null) {
                if (line.startsWith("aging")) {
                    StringTokenizer st = new StringTokenizer(line);
                    st.nextToken();
                    aging = Utils.stringToFloat(st.nextToken());
                }
                else if (line.startsWith("process")) {
                    StringTokenizer st = new StringTokenizer(line);
                    st.nextToken();
                    totalTime = Utils.stringToInt(st.nextToken());
                    burstTime = Utils.stringToInt(st.nextToken());
                    deviation = Utils.stringToInt(st.nextToken());
                    processVector.add(new Process(totalTime, burstTime, deviation));
                }
                else if (line.startsWith("runtime")) {
                    StringTokenizer st = new StringTokenizer(line);
                    st.nextToken();
                    runtime = Utils.stringToInt(st.nextToken());
                }
            }
            in.close();
        } catch (IOException e) { /* Handle exceptions */ }
    }

    public static void main(String[] args) {
        String filename = "scheduling.conf";

        File file = new File(filename);
        if (!(file.exists())) {
            System.out.println("Scheduling: error, file '" + file.getName() + "' does not exist.");
            System.exit(-1);
        }
        if (!(file.canRead())) {
            System.out.println("Scheduling: error, read of " + file.getName() + " failed.");
            System.exit(-1);
        }
        System.out.println("Working...");
        init(filename);

        result = SchedulingAlgorithm.run(runtime, aging, processVector, result);

        try {
            PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
            out.println("Scheduling Type: " + result.schedulingType);
            out.println("Scheduling Name: " + result.schedulingName);
            out.println("Simulation Run Time: " + result.computationTime);
            out.println("Process #\tCPU Time\tCPU Completed\tBursts count");
            for (int i = 0; i < processVector.size(); i++) {
                Process process = processVector.get(i);
                out.print(Integer.toString(i));
                if (i < 100) {
                    out.print("\t\t");
                } else {
                    out.print("\t");
                }
                out.print(Integer.toString(process.totalTime));
                if (process.totalTime < 100) {
                    out.print(" (ms)\t\t");
                } else {
                    out.print(" (ms)\t");
                }
                out.print(Integer.toString(process.timeDone));
                if (process.timeDone < 100) {
                    out.print(" (ms)\t\t");
                } else {
                    out.print(" (ms)\t");
                }
                out.println(process.burstsCount);
            }
            out.close();
        } catch (IOException e) { /* Handle exceptions */ }
        System.out.println("Completed.");
    }
}

