package com.googlecode.fannj;

import org.junit.Test;

public class FannJniFirstTest {

    @Test
    public void testLibLoading() {
        System.out.println(FannJNI.class.getName());
    }
}
