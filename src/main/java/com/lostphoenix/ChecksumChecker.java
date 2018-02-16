package com.lostphoenix;

public class ChecksumChecker {

    public static void main(String[] args) {
        for (int slice = 0; slice < 16; slice++) {
            StringBuilder ringPositions = new StringBuilder();
            for (int ring = 0; ring < 16; ring++) {
                ringPositions.append((16+slice-ring)%16).append(",");

            }
            System.out.println(ringPositions.toString());

        }
    }
}
