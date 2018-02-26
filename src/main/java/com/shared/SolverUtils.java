package com.shared;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.ethereum.crypto.ECKey;
import org.spongycastle.util.encoders.Hex;

import java.io.File;
import java.io.FileWriter;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class SolverUtils {

    static final String lineSeparator = System.lineSeparator();
    static String filePath = "C:\\%s\\ethkeychecker2\\allKeysTried.txt";

    private File logFile = null;
    private FileWriter logWriter;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private static AtomicLong lastNumPkChecked = new AtomicLong(0);

    public static AtomicLong numPkChecked = new AtomicLong(0);

    private static final int counter_divisor = 5;

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
        scheduler.scheduleAtFixedRate(this::printKeyCheckingSpeedAndLogKeys, 0, counter_divisor, TimeUnit.SECONDS);
    }

    interface ThrowingRunnable {
        void run() throws Exception;
    }

    void runAndRethrow(ThrowingRunnable r) {
        try {
            r.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void printKeyCheckingSpeedAndLogKeys() {
        if (lastNumPkChecked.get() == 0) {
            lastNumPkChecked.set(numPkChecked.get());
        } else {
            long lastTime = lastNumPkChecked.get();
            long present = numPkChecked.get();

            System.out.println(((present - lastTime) / counter_divisor) + " Private Keys Per Second");
            lastNumPkChecked.set(present);
        }
        if (logFile != null && logWriter != null && pkInformationHolder.size() > 1024) {
            StringBuilder pkLogFileBuffer = new StringBuilder(8192);
            pkInformationHolder.forEach(pkInfo -> {
                pkLogFileBuffer.append(pkInfo).append(lineSeparator);
                pkInformationHolder.remove(pkInfo);
            });
            runAndRethrow(() -> {
                logWriter.write(pkLogFileBuffer.toString());
                logWriter.flush();
            });
        }
    }

    public void shutdownLoggers() {
        if (logWriter != null) {
            runAndRethrow(() -> logWriter.flush());
        }
        scheduler.shutdownNow();
    }

    ConcurrentLinkedQueue<String> pkInformationHolder = new ConcurrentLinkedQueue<>();
    public void recordPkMetadataBlob(String metadata, String privateKey, String publicKey) {
        if (logFile != null && logWriter != null) {
            pkInformationHolder.offer(metadata == null ? "" + privateKey + "," + publicKey : metadata + "," + privateKey + "," + publicKey);
        }
    }

    public enum SegOrder {
        /**
         * Color then thickness (each segment)
         */
        COLOR_FIRST,
        /**
         * Thickness then color (each segment)
         */
        THICK_FIRST,
        /**
         * Color first, color inverted
         */
        Co_F_INVERT,
        /**
         * Thickness first, thickness inverted
         */
        Th_F_INVERT,
        /**
         * Color first, all inverted
         */
        C_F_INV_ALL,
        /**
         * Thickness first, all inverted
         */
        T_F_INV_ALL,
        /**
         * All color, then all thickness (ring)
         */
        ALL_COLOR_T,
        /**
         * All thickness, then all color (ring)
         */
        ALL_THICK_C
    }

    public static String differenceToString(List<Segment> ring) {

        // Some sort of map of left color, right color to binary
        // Some sort of map of left thickness, right thickness to binary
        return null;
    }

    private static String segmentToStringColorFirst(Map<Color, String> colorMap,
                                                    Map<Color, String> invertedColorMap,
                                                    Map<Thickness, String> thicknessMap,
                                                    Map<Thickness, String> invertedThicknessMap,
                                                    Segment seg,
                                                    boolean invertColor,
                                                    boolean invertThickness) {
        return (invertColor ? invertedColorMap.get(seg.color) : colorMap.get(seg.color))
                +
                (invertThickness ? invertedThicknessMap.get(seg.thickness): thicknessMap.get(seg.thickness));
    }

    private static String segmentToStringThicknessFirst(Map<Color, String> colorMap,
                                                        Map<Color, String> invertedColorMap,
                                                        Map<Thickness, String> thicknessMap,
                                                        Map<Thickness, String> invertedThicknessMap,
                                                        Segment seg,
                                                        boolean invertColor,
                                                        boolean invertThickness) {
        return (invertThickness ? invertedThicknessMap.get(seg.thickness): thicknessMap.get(seg.thickness))
                +
                (invertColor ? invertedColorMap.get(seg.color) : colorMap.get(seg.color));
    }

    public static String ringToString(Map<Color, String> colorMap,
                                      Map<Color, String> invertedColorMap,
                                      Map<Thickness, String> thicknessMap,
                                      Map<Thickness, String> invertedThicknessMap,
                                      List<Segment> ring,
                                      SegOrder segOrder) {
        StringBuilder builder = new StringBuilder();

        switch (segOrder) {
            case COLOR_FIRST:
                ring.forEach(seg -> builder.append(segmentToStringColorFirst(colorMap, invertedColorMap, thicknessMap, invertedThicknessMap, seg, false, false)));
                break;
            case Co_F_INVERT:
                ring.forEach(seg -> builder.append(segmentToStringColorFirst(colorMap, invertedColorMap, thicknessMap, invertedThicknessMap, seg, true, false)));
                break;
            case C_F_INV_ALL:
                ring.forEach(seg -> builder.append(segmentToStringColorFirst(colorMap, invertedColorMap, thicknessMap, invertedThicknessMap, seg, true, true)));
                break;
            case THICK_FIRST:
                ring.forEach(seg -> builder.append(segmentToStringThicknessFirst(colorMap, invertedColorMap, thicknessMap, invertedThicknessMap, seg, false, false)));
                break;
            case Th_F_INVERT:
                ring.forEach(seg -> builder.append(segmentToStringThicknessFirst(colorMap, invertedColorMap, thicknessMap, invertedThicknessMap, seg, false, true)));
                break;
            case T_F_INV_ALL:
                ring.forEach(seg -> builder.append(segmentToStringThicknessFirst(colorMap, invertedColorMap, thicknessMap, invertedThicknessMap, seg, true, true)));
                break;
            case ALL_COLOR_T:
                ring.forEach(seg -> builder.append(colorMap.get(seg.color)));
                ring.forEach(seg -> builder.append(thicknessMap.get(seg.thickness)));
                break;
            case ALL_THICK_C:
                ring.forEach(seg -> builder.append(thicknessMap.get(seg.thickness)));
                ring.forEach(seg -> builder.append(colorMap.get(seg.color)));
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

    private static String getPublicFromPrivate(BigInteger privateKey) {
        ECKey key = ECKey.fromPrivate(privateKey);

        return Hex.toHexString(key.getAddress());
    }

    public static String getPublicFromPrivate(String senderPrivKey) {
        String publicKey;
        BigInteger pk = new BigInteger(senderPrivKey, 16);
        // System.out.println("Private key: " + pk.toString(16));

        ECKey key = ECKey.fromPrivate(pk);
        publicKey = Hex.toHexString(key.getAddress()).toLowerCase();
        return publicKey;
    }

    public static boolean validate_nolog(String privateKey) {
        String publicFromPrivate = getPublicFromPrivate(privateKey);
        return publicFromPrivate.equalsIgnoreCase("D64FDEfa8dbc540c2582a6FC44B8f88FfB6657Ce");
    }

    // Found here: https://stackoverflow.com/questions/4211705/binary-to-text-in-java
    public static String bytesToAlphabeticString(byte[] bytes) {
        CharBuffer cb = ByteBuffer.wrap(bytes).asCharBuffer();
        return cb.toString();
    }

    public void validateBinaryPk(String binaryKey, String metadata) {
        numPkChecked.incrementAndGet();

        BigInteger privateKey = new BigInteger(binaryKey, 2);
        String privateKeyHex = privateKey.toString(16);

        if (privateKeyHex.contains("6060604052")) {
            System.out.println("Found 6060604052 :" + privateKey);
        } else if (privateKeyHex.contains("6e656f6e")) {
            System.out.println("Found 'neon' :" + privateKey);
        } else if (privateKeyHex.contains("6469737472696374")) {
            System.out.println("Found 'district' :" + privateKey);
        }

        String publicKey = getPublicFromPrivate(privateKey).toLowerCase();

        if (publicKeyMatchesContest(publicKey)) {
            throw new Error("YOU WIN!!!!!!!!!!! " + privateKey.toString(16));
        }
        recordPkMetadataBlob(metadata, privateKey.toString(16), publicKey); //+ "," + bytesToAlphabeticString(privateKey.toByteArray()));
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
        recordPkMetadataBlob(metadata, privateKey, publicFromPrivate);
        return publicKeyMatchesContest(publicFromPrivate);
    }

    public boolean publicKeyMatchesContest(String publicKey) {
        return publicKey.contains("D64FDEfa8dbc540c2582a6FC44B8f88FfB6657Ce".toLowerCase());
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

    public static Map<Color, String> invertColorToBinaryStringMap(Map<Color, String> colorMap) {
        Map<Color, String> newColorMap = Maps.newHashMap();
        char[] inverted = new char[2];
        for (Map.Entry<Color, String> colorEntry : colorMap.entrySet()) {
            inverted[0] = colorEntry.getValue().charAt(1);
            inverted[1] = colorEntry.getValue().charAt(0);
            newColorMap.put(colorEntry.getKey(), new String(inverted));
        }
        return newColorMap;
    }

    public static Map<Thickness, String> invertThicknessToBinaryStringMap(Map<Thickness, String> colorMap) {
        Map<Thickness, String> newThicknessMap = Maps.newHashMap();
        char[] inverted = new char[2];
        for (Map.Entry<Thickness, String> colorEntry : colorMap.entrySet()) {
            inverted[0] = colorEntry.getValue().charAt(1);
            inverted[1] = colorEntry.getValue().charAt(0);
            newThicknessMap.put(colorEntry.getKey(), new String(inverted));
        }
        return newThicknessMap;
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
