package com.androda.solvers;

import com.shared.Color;
import com.shared.Thickness;

import java.util.Map;

public interface Nucleator {
    /**
     * Performs several permutations (or just one) against the specified color and thickness maps
     */
    void nucleate(Map<Color, String> colorMap, Map<Thickness, String> thicknessMap);
}
