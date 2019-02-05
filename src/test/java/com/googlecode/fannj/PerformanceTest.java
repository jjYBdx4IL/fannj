package com.googlecode.fannj;

import static com.googlecode.fannj.FannTest.glob1;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class PerformanceTest {

    @Benchmark
    public void benchmarkSingleStepTrainingJNA1000x(BenchmarkState state, Blackhole bh) {
        state.trainer.resetMSE();
        for (int epoch = 0; epoch < state.list.length; epoch++) {
            for (int i = 0; i < state.numDataSets; i++) {
                state.trainer.trainSingleStep(state.inputValues[i], state.outputValues[i]);
                bh.consume(state.list[epoch]);
            }
        }
    }

    @Benchmark
    public void benchmarkSingleStepTrainingJNI1000x(BenchmarkState state, Blackhole bh) {
        state.trainer.resetMSEJNI();
        for (int epoch = 0; epoch < state.list.length; epoch++) {
            for (int i = 0; i < state.numDataSets; i++) {
                state.trainer.trainSingleStepJNI(state.inputValues[i], state.outputValues[i]);
                bh.consume(state.list[epoch]);
            }
        }
    }

    @Test
    public void testRunner() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(this.getClass().getName() + ".*")
                .mode(Mode.AverageTime)
                .timeUnit(TimeUnit.MICROSECONDS)
                .warmupTime(TimeValue.milliseconds(100))
                .warmupIterations(2)
                .measurementTime(TimeValue.milliseconds(100))
                .measurementIterations(10)
                .threads(2)
                .forks(1)
                .shouldFailOnError(true)
                .shouldDoGC(true)
                .build();

        Runner r = new Runner(opt);
        Collection<RunResult> result = r.run();
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @State(Scope.Thread)
    public static class BenchmarkState {

        int[] list = new int[1000];

        TrainData data;
        Fann fann;
        Trainer trainer;
        float[][] inputValues;
        float[][] outputValues;
        int numDataSets;

        @Setup(Level.Trial)
        public void initialize() throws IOException {
            data = new TrainData(new FileInputStream(glob1("**/xor.data")));
            inputValues = data.getInputValues();
            outputValues = data.getInputValues();
            numDataSets = data.getNumDataSets();

            List<Layer> layers = new ArrayList<Layer>();
            layers.add(Layer.create(2));
            layers.add(Layer.create(3, ActivationFunction.FANN_SIGMOID_SYMMETRIC));
            layers.add(Layer.create(2, ActivationFunction.FANN_SIGMOID_SYMMETRIC));
            layers.add(Layer.create(1, ActivationFunction.FANN_SIGMOID_SYMMETRIC));
            fann = new Fann(layers);
            trainer = new Trainer(fann);
            trainer.setTrainingAlgorithm(TrainingAlgorithm.FANN_TRAIN_INCREMENTAL);

            Random rand = new Random();

            for (int i = 0; i < list.length; i++) {
                list[i] = rand.nextInt();
            }
        }
    }

}
