package com.googlecode.fannj;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class TrainData {

    public static TrainData load(File trainingFile) throws IOException {
        InputStream is = new FileInputStream(trainingFile);
        TrainData data = null;
        try {
            data = new TrainData(is);
        } finally {
            is.close();
        }
        return data;
    }

    private final int numDataSets;
    private final int numInputs;
    private final int numOutputs;
    private final float[][] inputValues;
    private final float[][] outputValues;

    /**
     * load training data from {@link InputStream}.
     *
     * @param is
     * @throws java.io.IOException
     */
    public TrainData(InputStream is) throws IOException {
        Scanner scan = new Scanner(is);
        if (!scan.hasNextLine()) {
            throw new IOException("no header line");
        }
        final String headerLine = scan.nextLine();
        final String[] headerFields = headerLine.split("\\s+");
        numDataSets = Integer.parseInt(headerFields[0]);
        numInputs = Integer.parseInt(headerFields[1]);
        numOutputs = Integer.parseInt(headerFields[2]);
        inputValues = new float[numDataSets][numInputs];
        outputValues = new float[numDataSets][numOutputs];

        for (int i = 0; i < numDataSets; i++) {
            if (!scan.hasNextLine()) {
                throw new IOException("dataset #" + i + " not found, input too short");
            }
            String inputLine = scan.nextLine();
            int n = 0;
            for (String value : inputLine.split("\\s+")) {
                inputValues[i][n] = Float.valueOf(value);
                n++;
            }
            if (n != numInputs) {
                throw new IOException("too few input values for dataset #" + i);
            }

            if (!scan.hasNextLine()) {
                throw new IOException("dataset #" + i + " not found, input too short");
            }
            String outputLine = scan.nextLine();
            n = 0;
            for (String value : outputLine.split("\\s+")) {
                outputValues[i][n] = Float.valueOf(value);
                n++;
            }
            if (n != numOutputs) {
                throw new IOException("too few output values for dataset #" + i);
            }
        }
        scan.close();
    }

    /**
     * @return the numDataSets
     */
    public int getNumDataSets() {
        return numDataSets;
    }

    /**
     * @return the numInputs
     */
    public int getNumInputs() {
        return numInputs;
    }

    /**
     * @return the numOutputs
     */
    public int getNumOutputs() {
        return numOutputs;
    }

    /**
     * @return the inputValues
     */
    public float[][] getInputValues() {
        return inputValues;
    }

    /**
     * @return the outputValues
     */
    public float[][] getOutputValues() {
        return outputValues;
    }
}
