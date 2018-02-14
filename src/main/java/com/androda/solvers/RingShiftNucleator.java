package com.androda.solvers;

import com.google.common.collect.Lists;
import com.shared.Color;
import com.shared.Segment;
import com.shared.SolverUtils;
import com.shared.Thickness;

import java.util.List;
import java.util.Map;

public class RingShiftNucleator extends NucleatorBase implements Nucleator {

    boolean rightOrLeftAppend;
    boolean rightOrLeftShift;
    private int rotationDistance;

    final int ARTR = 0;  // append ring then rotate the whole string so far
    final int RRTA = 1;  // rotate ring then append
    final int RBDFCTA = 2;  // rotate based on distance from core then append
    final int ARTRBDFC = 3;  // append ring then rotate based on distance from core

    public RingShiftNucleator(SolverUtils utils, List<Segment> segments) {
        super(utils, segments);
    }

    public void setRotationDistance(int rotationDistance) {
        this.rotationDistance = rotationDistance;
    }

    public void setRightOrLeftAppend(boolean rightOrLeftAppend) {
        this.rightOrLeftAppend = rightOrLeftAppend;
    }

    public void setRightOrLeftShift(boolean rightOrLeftShift) {
        this.rightOrLeftShift = rightOrLeftShift;
    }

    void appendThenRotate(List<Segment> ring,
                          StringBuilder existing,
                          Map<Color, String> colorToBinaryMap,
                          Map<Thickness, String> thicknessToBinaryMap) {
        String ringString = SolverUtils.ringToString(colorToBinaryMap, thicknessToBinaryMap, ring, segOrder);
        String ringSoFar;
        if (rightOrLeftAppend) {
            existing.append(ringString);
            ringSoFar = existing.toString();
        } else {
            existing.insert(0, ringString);
            ringSoFar = existing.toString();
        }

        existing.delete(0, existing.length());
        if (rightOrLeftShift) {
            existing.append(SolverUtils.rightRotate(ringSoFar, rotationDistance));
        } else {
            existing.append(SolverUtils.leftRotate(ringSoFar, rotationDistance));
        }
    }

    List<String> getRotatedKey(Map<?, String> map, boolean rotateKeyRightOrLeft, int keyRotationDistance) {
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

    /*
    Append the ring, then rotate the *entire ring string so far*
    Rotate the ring, then append
    Rotate the ring based on the number it is from the core
    Rotate the ring just once each time
    Maybe the first ring is not rotated

     */

    @Override
    public void nucleate(Map<Color, String> colorMap, Map<Thickness, String> thicknessMap) {
        List<List<Segment>> forwardRings = getForwardSegmentsByRing();
        List<List<Segment>> reverseRings = getReverseSegmentsByRing();

        StringBuilder metadataBuilder = new StringBuilder();
        metadataBuilder.append(rotationDistance).append('|')
                .append(segOrder.name()).append('|')
                .append(rightOrLeftAppend ? '1' : '0').append('|')
                .append(rightOrLeftShift ? '1' : '0').append('|');

        Map<Color, String> keyRotationColorStringMap;
        Map<Thickness, String> keyRotationThicknessStringMap;

        StringBuilder forwardAtr = new StringBuilder(64);
        StringBuilder forwardKeyRotate = new StringBuilder(64);
        for (int f = 0; f < forwardRings.size(); f++) {
            keyRotationColorStringMap =
                    SolverUtils.colorMapFromBinaryValues(getRotatedKey(colorMap, rightOrLeftShift,
                            rightOrLeftAppend ? f : f == 0 ? 0 : 1));
            keyRotationThicknessStringMap =
                    SolverUtils.thicknessMapFromBinaryValues(getRotatedKey(thicknessMap, rightOrLeftShift,
                            rightOrLeftAppend ? f : f == 0 ? 0 : 1));
            appendThenRotate(forwardRings.get(f), forwardKeyRotate, keyRotationColorStringMap, keyRotationThicknessStringMap);

            appendThenRotate(forwardRings.get(f), forwardAtr, colorMap, thicknessMap);

        }
//                            utils.validateBinaryPk(forwardAtr.toString(), metadataBuilder.toString());
        utils.validateBinaryPk(forwardKeyRotate.toString(), "rkCW|" + metadataBuilder.toString());

        StringBuilder reverseAtr = new StringBuilder(64);
        StringBuilder reverseKeyRotate = new StringBuilder(64);
        for (int r = 0; r < reverseRings.size(); r++) {
            keyRotationColorStringMap =
                    SolverUtils.colorMapFromBinaryValues(getRotatedKey(colorMap, !rightOrLeftShift,
                            !rightOrLeftAppend ? r : r == 0 ? 0 : 1));
            keyRotationThicknessStringMap =
                    SolverUtils.thicknessMapFromBinaryValues(getRotatedKey(thicknessMap, !rightOrLeftShift,
                            !rightOrLeftAppend ? r : r == 0 ? 0 : 1));
            appendThenRotate(forwardRings.get(r), reverseKeyRotate, keyRotationColorStringMap, keyRotationThicknessStringMap);

            appendThenRotate(reverseRings.get(r), reverseAtr, colorMap, thicknessMap);


        }
//                            utils.validateBinaryPk(reverseAtr.toString(), metadataBuilder.toString());
        utils.validateBinaryPk(reverseKeyRotate.toString(), "rkCCW" + metadataBuilder.toString());

    }
}
