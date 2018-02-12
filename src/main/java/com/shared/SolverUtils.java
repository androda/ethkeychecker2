package com.shared;

import com.google.common.collect.Maps;
import org.ethereum.crypto.ECKey;
import org.spongycastle.util.encoders.Hex;

import java.io.File;
import java.io.FileWriter;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SolverUtils {

    static final String lineSeparator = System.lineSeparator();
    static String filePath = "C:\\%s\\ethkeychecker2\\allKeysTried.txt";

    private File logFile = null;
    private FileWriter logWriter;

    private static AtomicLong numPkChecked = new AtomicLong(0);
    private static long lastPkCountTimestamp;

    public SolverUtils(String logFileName) {
        if (logFileName != null && !logFileName.isEmpty()) {
            logFile = new File(logFileName);
            try {
                logFile.createNewFile();
                logWriter = new FileWriter(logFile);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    StringBuilder pkHolder = new StringBuilder();

    public void recordPk(String pk) {
        if (logFile != null) {
            if (pkHolder.length() < 1024) {
                pkHolder.append(pk).append(lineSeparator);
            } else {
                try {
                    logWriter.write(pkHolder.toString());
                    logWriter.flush();
                    pkHolder.delete(0, pkHolder.length());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public enum SegOrder {
        C_F,
        T_F,

    }

    private static String segmentToStringColorFirst(Map<Color, String> colorMap, Map<Thickness, String> thicknessMap, Segment seg) {
        return colorMap.get(seg.color) + thicknessMap.get(seg.thickness);
    }

    private static String segmentToStringThicknessFirst(Map<Color, String> colorMap, Map<Thickness, String> thicknessMap, Segment seg) {
        return thicknessMap.get(seg.thickness) + colorMap.get(seg.color);
    }

    public static String ringToString(Map<Color, String> colorMap, Map<Thickness, String> thicknessMap, List<Segment> ring, SegOrder type) {
        StringBuilder builder = new StringBuilder();

        switch (type) {
            case C_F:
                ring.forEach(seg -> builder.append(segmentToStringColorFirst(colorMap, thicknessMap, seg)));
                break;
            case T_F:
                ring.forEach(seg -> builder.append(segmentToStringThicknessFirst(colorMap, thicknessMap, seg)));
                break;
        }

        return builder.toString();
    }


    public static String leftRotate(String s, int shift) {
        int i = shift % s.length();
        return s.substring(i) + s.substring(0, i);
    }

    public static String rightRotate(String s, int shift) {
        int i = s.length() - (shift % s.length());
        return s.substring(i) + s.substring(0, i);
    }

    public static String getPublicFromPrivate(String senderPrivKey) {
        String publicKey;
        BigInteger pk = new BigInteger(senderPrivKey, 16);
        // System.out.println("Private key: " + pk.toString(16));

        ECKey key = ECKey.fromPrivate(pk);
        publicKey = Hex.toHexString(key.getAddress());
        return publicKey;
    }

    public static boolean validate_nolog(String privateKey) {
        String publicFromPrivate = getPublicFromPrivate(privateKey);
        return publicFromPrivate.equalsIgnoreCase("D64FDEfa8dbc540c2582a6FC44B8f88FfB6657Ce");
    }

    public void validateBinaryPk(String binaryKey, String metadata) {
        BigInteger integer = new BigInteger(binaryKey, 2);
        if (validateHexPk(integer.toString(16), metadata)) {
            throw new Error("YOU WIN!!!!!!!!!!! " + integer.toString(16));
        }
    }

    public boolean validateHexPk(String privateKey, String metadata) {
        numPkChecked.incrementAndGet();
        if (privateKey.contains("6060604052")) {
            System.out.println("Found 6060604052 :" + privateKey);
        }
        String publicFromPrivate = getPublicFromPrivate(privateKey);
        recordPk(metadata + privateKey + " : " + publicFromPrivate);
        return publicFromPrivate.toLowerCase().equalsIgnoreCase("D64FDEfa8dbc540c2582a6FC44B8f88FfB6657Ce".toLowerCase());
    }

    public static Map<Color, String> colorMapFromBinaryValues(List<String> binary) {
        Map enumListToBinaryMap = Maps.newHashMap();
        enumListToBinaryMap.put(Color.DG, binary.get(0));
        enumListToBinaryMap.put(Color.LG, binary.get(1));
        enumListToBinaryMap.put(Color.BLUE, binary.get(2));
        enumListToBinaryMap.put(Color.PINK, binary.get(3));
        return enumListToBinaryMap;
    }

    public static Map<Thickness, String> thicknessMapFromBinaryValues(List<String> binary) {
        Map enumListToBinaryMap = Maps.newHashMap();
        enumListToBinaryMap.put(Thickness.S, binary.get(0));
        enumListToBinaryMap.put(Thickness.M, binary.get(1));
        enumListToBinaryMap.put(Thickness.L, binary.get(2));
        enumListToBinaryMap.put(Thickness.XL, binary.get(3));
        return enumListToBinaryMap;
    }

}
