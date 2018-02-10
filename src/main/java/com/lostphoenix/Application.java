package com.lostphoenix;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ch.qos.logback.core.joran.conditional.ElseAction;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.ethereum.crypto.ECKey;
import org.spongycastle.util.encoders.Hex;

public class Application {
    static char hexDigit[] = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    //    static  char hexDigit[] = {
//            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
//    };







    static int[] hexOrder = new int[]{0, 5, 4, 5, 14, 9, 12, 13, 7, 8, 3, 6, 10, 10, 4, 10, 5, 0, 6, 11, 12, 3, 13, 13, 3, 8, 14, 7, 10, 15, 6, 1, 13, 14, 11, 9, 11, 3, 5, 4, 3, 0, 4, 2, 5, 8, 1, 15, 15, 9, 8, 1, 13, 1, 15, 15, 6, 7, 10, 5, 8, 11, 2, 8};


    static public String byteToHex(byte b) {
        // Returns hex String representation of byte b

        char[] array = {hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f]};
        return new String(array);
    }

    static public String charToHexWithShift(char c, int bits) {
        // Returns hex String representation of char c
        if (bits <= 8) {
            byte hi = (byte) ((c >>> (8 + bits)) | (c << (8 - bits)));
            byte lo = (byte) ((c >>> bits) & 0xff);
            return byteToHex(hi) + byteToHex(lo);
        } else {
            byte hi = (byte) ((c >>> (bits)) | (c << (16 - bits)));
            byte lo = (byte) ((c >>> (bits - 8)) & 0xff);
            return byteToHex(lo) + byteToHex(hi);
        }
    }

    public static void main(String[] args) {

        List<Integer> charLookup = Lists.newArrayList(
                4,
                0x0c,
                0,
                8,
                5,
                0xd,
                1,
                9,
                6,
                0xe,
                2,
                0xa,
                7,
                0xf,
                3,
                0xb);
        List<String> keys = buildPossiblePrivateKeys(charLookup);
        for (String key : keys) {
            System.out.println(key);
            if (validate(key)) {
                System.out.println("FOUND!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println(key);
                System.out.println(key);
                System.out.println(key);
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!");
                break;
            }
        }

    }


    public static boolean validate(String privateKey) {
        String publicFromPrivate = getPublicFromPrivate(privateKey);
        return publicFromPrivate.equalsIgnoreCase("D64FDEfa8dbc540c2582a6FC44B8f88FfB6657Ce");
    }

    public static void brute() {

        String publicKey = "";
        String senderPrivKey = "";

        int count = 0;
        List<Integer> charsList = Lists.newArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        Iterator<List<Integer>> charListIterator = Collections2.permutations(charsList).iterator();
        boolean found = false;

        do {
            if (!charListIterator.hasNext())
                break;
            List<Integer> charList = charListIterator.next();
            List<String> privateKeys = buildPossiblePrivateKeys(charList);
            for (String privateKey : privateKeys) {
                senderPrivKey = privateKey;
                if (validate(privateKey)) {
                    System.out.println("FOUND!!!!!!!!!!!!!!!!!!!!!!");
                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    System.out.println(privateKey);
                    System.out.println(privateKey);
                    System.out.println(privateKey);
                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    found = true;
                    break;
                }


            }


            if (count % 1000 == 0 || count < 100) {

                System.out.println(count);
                for (String privateKey : privateKeys) {
                    System.out.println(privateKey);
                }
            }
            count++;


        } while (!found);
        System.out.println(senderPrivKey);
        System.out.println("Public key: " + publicKey);
    }

    private static char getRingString(int[] hexOrder, int ring, List<Integer> charLookup) {
        char ringChar = 0x0000;
        ringChar += charLookup.get(hexOrder[ring * 4]) << 12;
        ringChar += charLookup.get(hexOrder[ring * 4 + 1]) << 8;
        ringChar += charLookup.get(hexOrder[ring * 4 + 2]) << 4;
        ringChar += charLookup.get(hexOrder[ring * 4 + 3]);
        return ringChar;
    }





    private static String getPublicFromPrivate(String senderPrivKey) {
        String publicKey;
        BigInteger pk = new BigInteger(senderPrivKey, 16);
        // System.out.println("Private key: " + pk.toString(16));

        ECKey key = ECKey.fromPrivate(pk);
        publicKey = Hex.toHexString(key.getAddress());
        return publicKey;
    }

    private static List<String> buildPossiblePrivateKeys(List<Integer> charList) {
        List<String> retVal = Lists.newArrayList();
        StringBuilder normal = new StringBuilder();
        StringBuilder leftShiftBuilder = new StringBuilder();
        StringBuilder rightShiftBuilder = new StringBuilder();

        for (int ring = 0; ring < 16; ring++) {
            char ringChar = getRingString(hexOrder, ring, charList);


            String rightShift = charToHexWithShift(ringChar, ring);
            String leftShift = charToHexWithShift(ringChar, 16 - ring);
            normal.append(charToHexWithShift(ringChar, 0));
            rightShiftBuilder.append(rightShift);
            leftShiftBuilder.append(leftShift);

        }

        retVal.add(normal.toString());
        retVal.add(leftShiftBuilder.toString());
        retVal.add(rightShiftBuilder.toString());
        retVal.add(normal.reverse().toString());
        retVal.add(leftShiftBuilder.reverse().toString());
        retVal.add(rightShiftBuilder.reverse().toString());
        return retVal;


    }




}