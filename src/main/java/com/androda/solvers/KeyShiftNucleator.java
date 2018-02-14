package com.androda.solvers;

import com.shared.Color;
import com.shared.Segment;
import com.shared.SolverUtils;
import com.shared.Thickness;

import java.util.List;
import java.util.Map;

import static com.shared.SolverUtils.getRotatedKey;

public class KeyShiftNucleator extends NucleatorBase implements Nucleator {

    boolean rightOrLeftAppend;
    boolean rightOrLeftShift;
    KeyShiftMode keyShiftMode;

    public KeyShiftNucleator(SolverUtils utils, List<Segment> segments) {
        super(utils, segments);
    }

    public void setRightOrLeftAppend(boolean rightOrLeftAppend) {
        this.rightOrLeftAppend = rightOrLeftAppend;
    }

    public void setRightOrLeftShift(boolean rightOrLeftShift) {
        this.rightOrLeftShift = rightOrLeftShift;
    }

    public void setKeyShiftMode(KeyShiftMode mode) {
        this.keyShiftMode = mode;
    }

    public enum KeyShiftMode {
        CenterNone_OnePerRing,  // shifting by a bit
        CenterNone_TwoPerRing,  // shifting by a half-ascii
        CenterNone_FourPerRing, // shifting by full-ascii
        CenterNone_RingNumber  // shift by ring number
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
        existing.append(ringSoFar);
    }

    @Override
    public void nucleate(Map<Color, String> colorMap, Map<Thickness, String> thicknessMap) {
        List<List<Segment>> forwardRings = getForwardSegmentsByRing();
        List<List<Segment>> reverseRings = getReverseSegmentsByRing();

        StringBuilder metadataBuilder = new StringBuilder();

        Map<Color, String> keyRotationColorStringMap;
        Map<Thickness, String> keyRotationThicknessStringMap;

        StringBuilder forwardKeyRotate = new StringBuilder(64);
        int rotateBy = -1;
        for (int f = 0; f < forwardRings.size(); f++) {
            switch (keyShiftMode) {
                case CenterNone_FourPerRing:
                    rotateBy = f == 0 ? 0 : 4;
                    break;
                case CenterNone_RingNumber:
                    rotateBy = f;
                    break;
                case CenterNone_TwoPerRing:
                    rotateBy = f == 0 ? 0 : 2;
                    break;
                case CenterNone_OnePerRing:
                    rotateBy = f == 0 ? 0 : 1;
                    break;
                default:
                    rotateBy = f;
            }
            keyRotationColorStringMap =
                    SolverUtils.colorMapFromBinaryValues(getRotatedKey(colorMap, rightOrLeftShift, rotateBy));
            keyRotationThicknessStringMap =
                    SolverUtils.thicknessMapFromBinaryValues(getRotatedKey(thicknessMap, rightOrLeftShift, rotateBy));
            appendThenRotate(forwardRings.get(f), forwardKeyRotate, keyRotationColorStringMap, keyRotationThicknessStringMap);


        }

        metadataBuilder.append("rk_CW|")
                .append(rotateBy).append('|')
                .append(segOrder.name()).append('|')
                .append(rightOrLeftAppend ? '1' : '0').append('|')
                .append(rightOrLeftShift ? '1' : '0').append('|');
        utils.validateBinaryPk(forwardKeyRotate.toString(), metadataBuilder.toString());

        StringBuilder reverseKeyRotate = new StringBuilder(64);
        for (int r = 0; r < reverseRings.size(); r++) {
            switch (keyShiftMode) {
                case CenterNone_FourPerRing:
                    rotateBy = r == 0 ? 0 : 4;
                    break;
                case CenterNone_RingNumber:
                    rotateBy = r;
                    break;
                case CenterNone_TwoPerRing:
                    rotateBy = r == 0 ? 0 : 2;
                    break;
                case CenterNone_OnePerRing:
                    rotateBy = r == 0 ? 0 : 1;
                    break;
                default:
                    rotateBy = r;
            }
            keyRotationColorStringMap =
                    SolverUtils.colorMapFromBinaryValues(getRotatedKey(colorMap, !rightOrLeftShift, rotateBy));
            keyRotationThicknessStringMap =
                    SolverUtils.thicknessMapFromBinaryValues(getRotatedKey(thicknessMap, !rightOrLeftShift, rotateBy));
            appendThenRotate(forwardRings.get(r), reverseKeyRotate, keyRotationColorStringMap, keyRotationThicknessStringMap);
        }
        metadataBuilder.delete(0, metadataBuilder.length())
                .append("rkCCW|")
                .append(rotateBy).append('|')
                .append(segOrder.name()).append('|')
                .append(rightOrLeftAppend ? '1' : '0').append('|')
                .append(rightOrLeftShift ? '1' : '0').append('|');
        utils.validateBinaryPk(reverseKeyRotate.toString(), metadataBuilder.toString());

    }


}
