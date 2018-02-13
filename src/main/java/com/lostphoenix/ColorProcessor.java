package com.lostphoenix;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Map.Entry;

interface ColorProcessor {
    public String get(Coordinate coordinate, SegmentType segmentType);

    public static class SimpleColorProcessor implements ColorProcessor{

        public SimpleColorProcessor(Map<Color, String> colorMap) {
            this.colorMap = colorMap;
        }

        final Map<Color, String> colorMap;
        @Override
        public String get(Coordinate coordinate, SegmentType segmentType) {
            return colorMap.get(segmentType.color);
        }
    }

    public static class LegendShiftingColorProcessor implements ColorProcessor{

        private final String[] strings;
        private final Map<Color, Integer> startingIndex;

        public LegendShiftingColorProcessor(Map<Color, String> startingColorMap) {
            startingIndex = ImmutableMap.of(Color.PINK, 0, Color.DG, 1, Color.BLUE, 2, Color.LG, 3);

            strings = new String[4];
            for (Entry<Color, String> colorStringEntry : startingColorMap.entrySet()) {
                Integer index = startingIndex.get(colorStringEntry.getKey());
                strings[index] = colorStringEntry.getValue();
            }
        }

        @Override
        public String get(Coordinate coordinate, SegmentType segmentType) {
            return strings[(startingIndex.get(segmentType.color)-coordinate.ring+16)%4];
        }
    }
}
