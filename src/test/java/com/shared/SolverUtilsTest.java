package com.shared;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

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
    public void testGetRotatedKey() {
        Map<Color, String> colorStringMap = SolverUtils.colorMapFromBinaryValues(Lists.newArrayList("00", "01", "10", "11"));
        List<String> rotatedKey;
        rotatedKey = SolverUtils.getRotatedKey(colorStringMap, true, 0);
        Assert.assertEquals(rotatedKey.get(0), "00");
        Assert.assertEquals(rotatedKey.get(1), "01");
        Assert.assertEquals(rotatedKey.get(2), "10");
        Assert.assertEquals(rotatedKey.get(3), "11");

        rotatedKey = SolverUtils.getRotatedKey(colorStringMap, true, 1);
        Assert.assertEquals(rotatedKey.get(0), "10");
        Assert.assertEquals(rotatedKey.get(1), "00");
        Assert.assertEquals(rotatedKey.get(2), "11");
        Assert.assertEquals(rotatedKey.get(3), "01");

        rotatedKey = SolverUtils.getRotatedKey(colorStringMap, true, 2);
        Assert.assertEquals(rotatedKey.get(0), "11");
        Assert.assertEquals(rotatedKey.get(1), "00");
        Assert.assertEquals(rotatedKey.get(2), "01");
        Assert.assertEquals(rotatedKey.get(3), "10");

        // rotate the other way
        rotatedKey = SolverUtils.getRotatedKey(colorStringMap, false, 0);
        Assert.assertEquals(rotatedKey.get(0), "00");
        Assert.assertEquals(rotatedKey.get(1), "01");
        Assert.assertEquals(rotatedKey.get(2), "10");
        Assert.assertEquals(rotatedKey.get(3), "11");

        rotatedKey = SolverUtils.getRotatedKey(colorStringMap, false, 1);
        Assert.assertEquals(rotatedKey.get(0), "00");
        Assert.assertEquals(rotatedKey.get(1), "11");
        Assert.assertEquals(rotatedKey.get(2), "01");
        Assert.assertEquals(rotatedKey.get(3), "10");

        rotatedKey = SolverUtils.getRotatedKey(colorStringMap, false, 2);
        Assert.assertEquals(rotatedKey.get(0), "01");
        Assert.assertEquals(rotatedKey.get(1), "10");
        Assert.assertEquals(rotatedKey.get(2), "11");
        Assert.assertEquals(rotatedKey.get(3), "00");

        rotatedKey = SolverUtils.getRotatedKey(colorStringMap, false, 3);
        Assert.assertEquals(rotatedKey.get(0), "11");
        Assert.assertEquals(rotatedKey.get(1), "01");
        Assert.assertEquals(rotatedKey.get(2), "10");
        Assert.assertEquals(rotatedKey.get(3), "00");

    }

    @Test
    public void howDoesPublicKeyLook() {
        String pk = "da81b192dbf2edbe6cf5b2f8450ea355f3ae4ac708639de3fdd41a489499681c";
        String address = "fAc066D42793Ad062d2C7D91Fba0f488Ffe5e68e";
        Assert.assertEquals(SolverUtils.getPublicFromPrivate(pk).toLowerCase(), address.toLowerCase());
    }

}
