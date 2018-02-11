package com.shared;

import org.junit.Assert;
import org.junit.Test;

public class SolverUtilsTest {




    @Test
    public void testLeftShift() {
        String binary = "0001";

        Assert.assertEquals(SolverUtils.leftRotate(binary, 1), "0010");
        Assert.assertEquals(SolverUtils.leftRotate(binary, 2), "0100");
        Assert.assertEquals(SolverUtils.leftRotate(binary, 3), "1000");
        Assert.assertEquals(SolverUtils.leftRotate(binary, 4), "0001");
        Assert.assertEquals(SolverUtils.leftRotate(binary, 5), "0010");
    }

    @Test
    public void testRightShift() {
        String binary = "1000";

        Assert.assertEquals(SolverUtils.rightRotate(binary, 1), "0100");
        Assert.assertEquals(SolverUtils.rightRotate(binary, 2), "0010");
        Assert.assertEquals(SolverUtils.rightRotate(binary, 3), "0001");
        Assert.assertEquals(SolverUtils.rightRotate(binary, 4), "1000");
        Assert.assertEquals(SolverUtils.rightRotate(binary, 5), "0100");
    }


    @Test
    public void howDoesPublicKeyLook() {
        String pk = "da81b192dbf2edbe6cf5b2f8450ea355f3ae4ac708639de3fdd41a489499681c";
        String address = "fAc066D42793Ad062d2C7D91Fba0f488Ffe5e68e";
        Assert.assertEquals(SolverUtils.getPublicFromPrivate(pk).toLowerCase(), address.toLowerCase());
    }

}
