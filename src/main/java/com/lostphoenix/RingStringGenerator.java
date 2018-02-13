package com.lostphoenix;

import java.util.List;

abstract class RingStringGenerator {
    final ColorProcessor colorProcessor;
    final ThicknessProcessor thicknessProcessor;

    public RingStringGenerator(ColorProcessor colorProcessor, ThicknessProcessor thicknessProcessor) {
        this.colorProcessor = colorProcessor;
        this.thicknessProcessor = thicknessProcessor;
    }

    abstract String generate(List<Coordinate> segments, SegmentType[] segmentOrder);


    public static class CTCTRingStringGenerator extends RingStringGenerator {
        public CTCTRingStringGenerator(ColorProcessor colorProcessor, ThicknessProcessor thicknessProcessor) {
            super(colorProcessor, thicknessProcessor);
        }

        public String generate(List<Coordinate> segments, SegmentType[] segmentOrder) {
            StringBuilder binaryString = new StringBuilder();
            for (Coordinate segment : segments) {
                binaryString.append(colorProcessor.get(segment, segmentOrder[segment.ring * 4 + segment.segmentInRing]));
                binaryString.append(thicknessProcessor.get(segment, segmentOrder[segment.ring * 4 + segment.segmentInRing]));
            }
            return binaryString.toString();
        }
    }

    public static class TCTCRingStringGenerator extends RingStringGenerator {
        public TCTCRingStringGenerator(ColorProcessor colorProcessor, ThicknessProcessor thicknessProcessor) {
            super(colorProcessor, thicknessProcessor);
        }

        public String generate(List<Coordinate> segments, SegmentType[] segmentOrder) {
            StringBuilder binaryString = new StringBuilder();
            for (Coordinate segment : segments) {
                binaryString.append(thicknessProcessor.get(segment, segmentOrder[segment.ring * 4 + segment.segmentInRing]));
                binaryString.append(colorProcessor.get(segment, segmentOrder[segment.ring * 4 + segment.segmentInRing]));
            }
            return binaryString.toString();
        }
    }

    public static class CCTTRingStringGenerator extends RingStringGenerator {
        public CCTTRingStringGenerator(ColorProcessor colorProcessor, ThicknessProcessor thicknessProcessor) {
            super(colorProcessor, thicknessProcessor);
        }

        public String generate(List<Coordinate> segments, SegmentType[] segmentOrder) {
            StringBuilder binaryString = new StringBuilder();
            for (Coordinate segment : segments) {
                binaryString.append(colorProcessor.get(segment, segmentOrder[segment.ring * 4 + segment.segmentInRing]));
            }
            for (Coordinate segment : segments) {
                binaryString.append(thicknessProcessor.get(segment, segmentOrder[segment.ring * 4 + segment.segmentInRing]));
            }
            return binaryString.toString();
        }
    }


    public static class TTCCRingStringGenerator extends RingStringGenerator {
        public TTCCRingStringGenerator(ColorProcessor colorProcessor, ThicknessProcessor thicknessProcessor) {
            super(colorProcessor, thicknessProcessor);
        }

        public String generate(List<Coordinate> segments, SegmentType[] segmentOrder) {
            StringBuilder binaryString = new StringBuilder();
            for (Coordinate segment : segments) {
                binaryString.append(thicknessProcessor.get(segment, segmentOrder[segment.ring * 4 + segment.segmentInRing]));
            }
            for (Coordinate segment : segments) {
                binaryString.append(colorProcessor.get(segment, segmentOrder[segment.ring * 4 + segment.segmentInRing]));
            }
            return binaryString.toString();
        }
    }

    public static class CTCTIntertwinedRingStringGenerator extends RingStringGenerator {
        public CTCTIntertwinedRingStringGenerator(ColorProcessor colorProcessor, ThicknessProcessor thicknessProcessor) {
            super(colorProcessor, thicknessProcessor);
        }

        public String generate(List<Coordinate> segments, SegmentType[] segmentOrder) {
            StringBuilder colorOrder = new StringBuilder();
            StringBuilder thicknessOrder = new StringBuilder();

            for (Coordinate segment : segments) {
                thicknessOrder.append(thicknessProcessor.get(segment, segmentOrder[segment.ring * 4 + segment.segmentInRing]));
                colorOrder.append(colorProcessor.get(segment, segmentOrder[segment.ring * 4 + segment.segmentInRing]));
            }
            StringBuilder binaryString = new StringBuilder();
            for (int i = 0; i < colorOrder.length(); i++) {
                binaryString.append(colorOrder.charAt(i));
                binaryString.append(thicknessOrder.charAt(i));
            }

            return binaryString.toString();
        }
    }

    public static class TCTCIntertwinedRingStringGenerator extends RingStringGenerator {
        public TCTCIntertwinedRingStringGenerator(ColorProcessor colorProcessor, ThicknessProcessor thicknessProcessor) {
            super(colorProcessor, thicknessProcessor);
        }

        public String generate(List<Coordinate> segments, SegmentType[] segmentOrder) {
            StringBuilder colorOrder = new StringBuilder();
            StringBuilder thicknessOrder = new StringBuilder();

            for (Coordinate segment : segments) {
                thicknessOrder.append(thicknessProcessor.get(segment, segmentOrder[segment.ring * 4 + segment.segmentInRing]));
                colorOrder.append(colorProcessor.get(segment,segmentOrder[segment.ring * 4 + segment.segmentInRing]));
            }
            StringBuilder binaryString = new StringBuilder();
            for (int i = 0; i < colorOrder.length(); i++) {
                binaryString.append(thicknessOrder.charAt(i));
                binaryString.append(colorOrder.charAt(i));

            }

            return binaryString.toString();
        }
    }
}
