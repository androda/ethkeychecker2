package com.lostphoenix;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringUtilsTest {

    @Test
    public void hexToAscii() {
        String result = StringUtils.hexToAscii("77334c63304d655f744f2d3768332e46754e633731306e633072452d476c4866");
        assertEquals("w3Lc0Me_tO-7h3.FuNc710nc0rE-GlHf", result);
    }

    @Test
    public void leftRotate() {
        String test = "2fe7";
        assertEquals("fe72",StringUtils.leftRotate(test,1));
        assertEquals("e72f",StringUtils.leftRotate(test,2));
        assertEquals("72fe",StringUtils.leftRotate(test,3));

    }

    @Test
    public void rightRotate() {

        String test = "2fe7";
        assertEquals("fe72",StringUtils.rightRotate(test,3));
        assertEquals("e72f",StringUtils.rightRotate(test,2));
        assertEquals("72fe",StringUtils.rightRotate(test,1));
    }

    @Test
    public void convertBinaryStringToHex(){
        String result = StringUtils.convertBinaryStringToHex("0111001101100101011000110111001001100101011101000110101101100101011110010111010101110011011001010110010001110100011011110111001001100101011000010110001101101000011101000110100001100101011011000110000101110011011101000110110001100101011101100110010101101100");
        assertEquals("7365637265746b657975736564746f72656163687468656c6173746c6576656c", result);

    }
}