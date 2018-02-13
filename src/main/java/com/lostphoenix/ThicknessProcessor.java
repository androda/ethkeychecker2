package com.lostphoenix;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Map.Entry;

interface ThicknessProcessor {
    public String get(Coordinate coordinate, SegmentType segmentType);

    public static class SimpleThicknessProcessor implements ThicknessProcessor{

        private final Map<Thickness, String> thicknessMap;

        public SimpleThicknessProcessor(Map<Thickness, String> thicknessMap) {
            this.thicknessMap = thicknessMap;
        }

        @Override
        public String get(Coordinate coordinate, SegmentType segmentType) {
            return thicknessMap.get(segmentType.thickness);
        }
    }

    public static class LegendShiftingThicknessProcessor implements ThicknessProcessor{

        private final String[] strings;
        private final Map<Thickness, Integer> startingIndex;

        public LegendShiftingThicknessProcessor(Map<Thickness, String> startingThicknessMap) {
            startingIndex = ImmutableMap.of(Thickness.S, 3,Thickness.XL, 2,Thickness.M, 1,Thickness.L, 0);

            strings = new String[4];
            for (Entry<Thickness, String> thicknessStringEntry : startingThicknessMap.entrySet()) {
                Integer index = startingIndex.get(thicknessStringEntry.getKey());
                strings[index] = thicknessStringEntry.getValue();
            }
        }

        @Override
        public String get(Coordinate coordinate, SegmentType segmentType) {
            return strings[(startingIndex.get(segmentType.thickness)-coordinate.ring+16)%4];
        }
    }
}
