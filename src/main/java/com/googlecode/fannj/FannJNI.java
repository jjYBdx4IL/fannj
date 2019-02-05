/* FannJ
 * Copyright (C) 2016 jjYBdx4IL
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

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Platform;

import java.io.File;

/**
 * Native function declarations for JNI. Used to speed up lower level access to the native lib.
 *
 * @author jjYBdx4IL
 */
public class FannJNI {

    static {
        NativeLibrary.getInstance(Platform.isWindows() ? "libfann" : "fann");
        File libfile = null;
        try {
            libfile = Native.extractFromResourcePath(Platform.isWindows() ? "libjnifann" : "jnifann");
            System.load(libfile.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (libfile != null && libfile.getName().startsWith("jna")) {
                libfile.delete();
            }
        }
    }

    // no public access, main fann lib must be loaded first, which is done by accessing stuff via
    // Fann and Trainer classes.
    protected static native void train(long ann, float[] input, float[] output);

    /**
     * Resets the mean square error from the network.
     *
     * @param ann
     */
    protected static native void reset_MSE(long ann);

    /**
     * Reads the mean square error from the network.
     *
     * @param ann
     * @return the mean square error of the network
     */
    protected static native float get_MSE(long ann);
    
    private FannJNI() {
    }

}
