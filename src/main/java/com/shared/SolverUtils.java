package com.shared;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.ethereum.crypto.ECKey;
import org.spongycastle.util.encoders.Hex;

import java.io.File;
import java.io.FileWriter;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
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

    public void flushKeyFile() {
        if (logWriter != null) {
            try {
                logWriter.flush();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

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
        // todo need entries for all color then all thickness and vice versa
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

    public static String xorBinaryStrings(final String s1, final String s2) {
        boolean tesselate = false;
        if (s1.length() != s2.length()) {
            if ((((double) s1.length()) / ((double) s2.length())) % 1 == 0
                    || (((double) s2.length()) / ((double) s1.length())) % 1 == 0) {
                tesselate = true;
            } else {
                throw new IllegalArgumentException("Sizes must match");
            }
        }
        StringBuilder retVal = new StringBuilder();

        String stringOne = null;
        String stringTwo = null;

        if (tesselate) {
            boolean s1IsShorter = s1.length() < s2.length();
            StringBuilder xorBuilder = new StringBuilder();

            Callable<Boolean> matchingProvider;
            Callable<String> stringProvider;
            if (s1IsShorter) {
                matchingProvider = () -> !(xorBuilder.length() == s2.length());
                stringProvider = () -> s1;
            } else {
                matchingProvider = () -> !(xorBuilder.length() == s1.length());
                stringProvider = () -> s2;
            }

            try {
                while (matchingProvider.call()) {
                    xorBuilder.append(stringProvider.call());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (s1IsShorter) {
                stringOne = xorBuilder.toString();
            } else {
                stringTwo = xorBuilder.toString();
            }
        }

        if (stringOne == null) {
            stringOne = s1;
        }
        if (stringTwo == null) {
            stringTwo = s2;
        }

        for (int i = 0; i < stringOne.length(); i++) {
            if (stringOne.charAt(i) == stringTwo.charAt(i)) {
                retVal.append("0");
            } else {
                retVal.append("1");
            }
        }
        return retVal.toString();
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
        } else if (privateKey.contains("6e656f6e")) {
            System.out.println("Found 'neon' :" + privateKey);
        } else if (privateKey.contains("6469737472696374")) {
            System.out.println("Found 'district' :" + privateKey);
        }

        String publicFromPrivate = getPublicFromPrivate(privateKey);
        recordPk(metadata + ","+ privateKey + "," + publicFromPrivate);
        return publicFromPrivate.toLowerCase().equalsIgnoreCase("D64FDEfa8dbc540c2582a6FC44B8f88FfB6657Ce".toLowerCase());
    }

    public static Map<Color, String> colorMapFromBinaryValues(List<String> binary) {
        Map enumListToBinaryMap = Maps.newHashMap();
        enumListToBinaryMap.put(Color.PINK, binary.get(0));
        enumListToBinaryMap.put(Color.DG, binary.get(1));
        enumListToBinaryMap.put(Color.BLUE, binary.get(2));
        enumListToBinaryMap.put(Color.LG, binary.get(3));
        return enumListToBinaryMap;
    }

    public static Map<Thickness, String> thicknessMapFromBinaryValues(List<String> binary) {
        Map enumListToBinaryMap = Maps.newHashMap();
        enumListToBinaryMap.put(Thickness.S, binary.get(0));
        enumListToBinaryMap.put(Thickness.XL, binary.get(1));
        enumListToBinaryMap.put(Thickness.M, binary.get(2));
        enumListToBinaryMap.put(Thickness.L, binary.get(3));
        return enumListToBinaryMap;
    }

    public static List<String> getRotatedKey(Map<?, String> map, boolean rotateKeyRightOrLeft, int keyRotationDistance) {
        StringBuilder builder = new StringBuilder(8);
        if (map.get(Color.PINK) == null) {
            builder.append(map.get(Thickness.S)).append(map.get(Thickness.M)).append(map.get(Thickness.L)).append(map.get(Thickness.XL));
        } else {
            builder.append(map.get(Color.PINK)).append(map.get(Color.DG)).append(map.get(Color.BLUE)).append(map.get(Color.LG));
        }
        String rotated;
        if (rotateKeyRightOrLeft) {
            rotated = SolverUtils.rightRotate(builder.toString(), keyRotationDistance);
        } else {
            rotated = SolverUtils.leftRotate(builder.toString(), keyRotationDistance);
        }

        return Lists.newArrayList(
                rotated.substring(0, 2),
                rotated.substring(2, 4),
                rotated.substring(4, 6),
                rotated.substring(6, 8));
    }

}
