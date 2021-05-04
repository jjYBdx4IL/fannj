/* FannJ
 * Copyright (C) 2009 Kyle Renfro
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA. The text of license can be also found
 * at http://www.gnu.org/copyleft/lgpl.html
 */
package com.googlecode.fannj;

import static com.googlecode.fannj.FannTest.createTemp;
import static com.googlecode.fannj.FannTest.glob1;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FannTrainerTest {

    @Test
    public void testTrainingDefault() throws IOException { 
        File temp = createTemp(glob1("**/xor.data"));

        List<Layer> layers = new ArrayList<Layer>();
        layers.add(Layer.create(2));
        layers.add(Layer.create(3, ActivationFunction.FANN_SIGMOID_SYMMETRIC));
        layers.add(Layer.create(1, ActivationFunction.FANN_SIGMOID_SYMMETRIC));
        Fann fann = new Fann(layers);
        Trainer trainer = new Trainer(fann);
        float desiredError = .001f;
        float mse = trainer.train(temp.getPath(), 500000, 1000, desiredError);
        assertTrue("" + mse, mse <= desiredError);
    }

    @Test
    public void testTrainingQuickprop() throws IOException {
        File temp = createTemp(glob1("**/xor.data"));        
        
        List<Layer> layers = new ArrayList<Layer>();
        layers.add(Layer.create(2));
        layers.add(Layer.create(3, ActivationFunction.FANN_SIGMOID_SYMMETRIC));
        layers.add(Layer.create(1, ActivationFunction.FANN_SIGMOID_SYMMETRIC));
        Fann fann = new Fann(layers);
        Trainer trainer = new Trainer(fann);
        trainer.setTrainingAlgorithm(TrainingAlgorithm.FANN_TRAIN_QUICKPROP);
        float desiredError = .001f;
        float mse = trainer.train(temp.getPath(), 500000, 1000,
                desiredError);
        assertTrue("" + mse, mse <= desiredError);
    }

    @Test
    public void testTrainingBackprop() throws IOException {
        File temp = createTemp(glob1("**/xor.data"));
        
        List<Layer> layers = new ArrayList<Layer>();
        layers.add(Layer.create(2));
        layers.add(Layer.create(3, ActivationFunction.FANN_SIGMOID_SYMMETRIC));
        layers.add(Layer.create(2, ActivationFunction.FANN_SIGMOID_SYMMETRIC));
        layers.add(Layer.create(1, ActivationFunction.FANN_SIGMOID_SYMMETRIC));
        Fann fann = new Fann(layers);
        Trainer trainer = new Trainer(fann);
        trainer.setTrainingAlgorithm(TrainingAlgorithm.FANN_TRAIN_INCREMENTAL);
        float desiredError = .001f;
        float mse = trainer.train(temp.getPath(), 500000, 1000, desiredError);
        assertTrue("" + mse, mse <= desiredError);
    }

    @Test
    public void testTrainingBackpropUsingJavaLoop() throws IOException {
        File temp = createTemp(glob1("**/xor.data"));
        
        List<Layer> layers = new ArrayList<Layer>();
        layers.add(Layer.create(2));
        layers.add(Layer.create(3, ActivationFunction.FANN_SIGMOID_SYMMETRIC));
        layers.add(Layer.create(2, ActivationFunction.FANN_SIGMOID_SYMMETRIC));
        layers.add(Layer.create(1, ActivationFunction.FANN_SIGMOID_SYMMETRIC));
        Fann fann = new Fann(layers);
        Trainer trainer = new Trainer(fann);
        trainer.setTrainingAlgorithm(TrainingAlgorithm.FANN_TRAIN_INCREMENTAL);
        float desiredError = .001f;
        float mse = trainer.trainJavaLoop(temp, 500000, desiredError);
        assertTrue("" + mse, mse <= desiredError);
    }

    @Test
    public void testTrainingBackpropUsingJavaLoopJNI() throws IOException {
        File temp = createTemp(glob1("**/xor.data"));
        
        List<Layer> layers = new ArrayList<Layer>();
        layers.add(Layer.create(2));
        layers.add(Layer.create(3, ActivationFunction.FANN_SIGMOID_SYMMETRIC));
        layers.add(Layer.create(2, ActivationFunction.FANN_SIGMOID_SYMMETRIC));
        layers.add(Layer.create(1, ActivationFunction.FANN_SIGMOID_SYMMETRIC));
        Fann fann = new Fann(layers);
        Trainer trainer = new Trainer(fann);
        trainer.setTrainingAlgorithm(TrainingAlgorithm.FANN_TRAIN_INCREMENTAL);
        float desiredError = .001f;
        float mse = trainer.trainJavaLoopJNI(temp, 500000, desiredError);
        assertTrue("" + mse, mse <= desiredError);
    }

    @Test
    public void testCascadeTraining() throws IOException {
        File temp = createTemp(glob1("**/parity8.train"));
        
        Fann fann = new FannShortcut(8, 1);
        Trainer trainer = new Trainer(fann);
        float desiredError = .00f;
        float mse = trainer.cascadeTrain(temp.getPath(), 30, 1, desiredError);
        assertTrue("" + mse, mse <= desiredError);
    }

}
