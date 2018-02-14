package com.lostphoenix;

import com.google.common.collect.Lists;

import java.util.List;

public interface RingTraverser {
    List<List<RingCoordinate>> generatePaths();

    public static class StandardTraverser implements RingTraverser{

        @Override
        public List<List<RingCoordinate>> generatePaths() {
            List<List<RingCoordinate>> retVal = Lists.newArrayList();
            for(int ring = 0;ring<16;ring++){
                List<RingCoordinate> ringCoordinates = Lists.newArrayList();
                retVal.add(ringCoordinates);
                for(int slice=0;slice<16;slice++){
                    ringCoordinates.add(new RingCoordinate(ring, (slice+ring)%16));
                }
            }
            return retVal;
        }
    }
}
