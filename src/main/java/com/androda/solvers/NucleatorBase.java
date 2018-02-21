package com.androda.solvers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.shared.Color;
import com.shared.Segment;
import com.shared.SolverUtils;
import com.shared.Thickness;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class NucleatorBase {

    protected List<Segment> segments_originalOrder;
    protected List<Segment> segments_reverseOrder;
    protected List<List<Segment>> segmentsInOrderByRing;
    protected List<List<Segment>> segmentsInOrderByRing_reversed;

    protected List<Boolean> trueFalse = Lists.newArrayList(Boolean.TRUE, Boolean.FALSE);

    protected SolverUtils.SegOrder segOrder;
    protected SolverUtils utils;

    protected final int SEG_PER_RING = 4;
    protected final int NUM_RINGS = 16;

    public NucleatorBase(SolverUtils utils, List<Segment> segments, List<List<Segment>> segmentsByRing) {
        segments_originalOrder = ImmutableList.copyOf(segments);
        segments_reverseOrder = Lists.newArrayList(segments);
        Collections.reverse(segments_reverseOrder);

        segmentsInOrderByRing = ImmutableList.copyOf(segmentsByRing);
        segmentsInOrderByRing_reversed = Lists.newArrayList(segmentsByRing);
        for (int i = 0; i < segmentsInOrderByRing_reversed.size(); i++) {
            Collections.reverse(segmentsInOrderByRing_reversed.get(i));
        }
        Collections.reverse(segmentsInOrderByRing_reversed);
        this.utils = utils;
    }

    protected List<StringBuilder> makeReturnBuilderList(int numBuilders) {
        List<StringBuilder> builders = Lists.newArrayList();
        for (int i = 0; i < numBuilders; i++) {
            builders.add(new StringBuilder());
        }
        return builders;
    }

    protected List<String> convertToResponseStrings(List<StringBuilder> builders) {
        List<String> responses = Lists.newArrayListWithExpectedSize(builders.size());
        builders.forEach(builder -> responses.add(builder.toString()));
        return responses;
    }

    protected List<List<Segment>> getForwardSegmentsByRing() {
        return Lists.partition(segments_originalOrder, SEG_PER_RING);
    }

    protected List<List<Segment>> getReverseSegmentsByRing() {
        return Lists.partition(segments_reverseOrder, SEG_PER_RING);
    }

    public void setSegOrder(SolverUtils.SegOrder segOrder) {
        this.segOrder = segOrder;
    }

//    protected String getRingString(Map<Color, String> colorMap, Map<Thickness, String> thicknessMap, List<Segment> ring) {
//        return SolverUtils.ringToString(colorMap, thicknessMap, ring, segOrder);
//    }

}
