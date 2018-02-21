package com.androda.interfaces;

import com.shared.Color;
import com.shared.Thickness;

import java.util.Map;

public interface Nucleator {
    /**
     * Performs several permutations (or just one) against the specified color and thickness maps
     */
    void nucleate(Map<Color, String> colorMap, Map<Color, String> invColorMap, Map<Thickness, String> thicknessMap, Map<Thickness, String> invThicknessMap);
}
