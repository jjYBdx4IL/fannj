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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import static org.junit.Assert.*;
import org.junit.Test;

public class FannTest {

    public static File build(String resourceId) throws IOException {

        File temp = File.createTempFile("fannj_", ".net");
        temp.deleteOnExit();
        IOUtils.copy(FannTest.class.getResourceAsStream(resourceId), new FileOutputStream(temp));
        List<Layer> layers = new ArrayList<Layer>();
        layers.add(Layer.create(2));
        layers.add(Layer.create(3, ActivationFunction.FANN_SIGMOID_SYMMETRIC));
        layers.add(Layer.create(1, ActivationFunction.FANN_SIGMOID_SYMMETRIC));
        Fann fann = new Fann(layers);
        Trainer trainer = new Trainer(fann);
        float desiredError = .001f;
        float mse = trainer.train(temp.getPath(), 500000, 1000, desiredError);
        fann.save(temp.toString());
        assertEquals(0, fann.getErrno());
        return temp;
    }

    @Test
    public void testTrainAndRun() throws IOException {
        File temp = build("xor.data");
        Fann fann = new Fann(temp.getPath());
        assertEquals(2, fann.getNumInputNeurons());
        assertEquals(1, fann.getNumOutputNeurons());
        assertEquals(-1f, fann.run(new float[]{-1, -1})[0], .2f);
        assertEquals(1f, fann.run(new float[]{-1, 1})[0], .2f);
        assertEquals(1f, fann.run(new float[]{1, -1})[0], .2f);
        assertEquals(-1f, fann.run(new float[]{1, 1})[0], .2f);
        fann.close();
    }

    @Test
    public void testFannGetErrNo() throws IOException, URISyntaxException, InterruptedException {

        File temp = build("xor.data");
        Fann fann = new Fann(temp.getPath());

        Trainer trainer = new Trainer(fann);
        float desiredError = .001f;
        assertEquals(0, fann.getErrno());

        float mse = trainer.train(new File(FannTest.class.getResource("badTrainingData10.data").toURI()).getPath(), 500000, 1000, desiredError);
        // TODO not good, filed an issue: https://github.com/libfann/fann/issues/69
        assertEquals(0, fann.getErrno());
    }

    public static void main(String[] args) {
        try {
            new FannTest().testFannGetErrNo();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }
}
