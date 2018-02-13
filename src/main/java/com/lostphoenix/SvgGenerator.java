package com.lostphoenix;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class SvgGenerator {

    public static void main(String[] args) {
        for(int ring = 0; ring<16; ring++){
            for(int segment =0; segment<4;segment++)
            {
                SegmentType segmentType = Application2.segmentOrder[ring*4+segment];
                System.out.println("<path d=\"M "+segment*200+" "+(8+(ring*48))+" l 200 0 \" style=\"stroke: "+colorString.get(segmentType.color)+"; stroke-width: "+thicknessString.get(segmentType.thickness)+"; fill:none;\"></path>"   );

            }
//            for(int segment =1; segment<2;segment++)
//            {
//                SegmentType segmentType = Application2.segmentOrder[ring*4+segment];
//                System.out.println("<path d=\"M 0 "+(4+((ring+16)*24))+" l 97 0 \" style=\"stroke: rgb(0,0,0)"/*+colorString.get(segmentType.color)*/+"; stroke-width: "+thicknessString.get(segmentType.thickness)+"; fill:none;\"></path>"   );
//
//            }
        }
    }

    static Map<Color, String> colorString = ImmutableMap.of(Color.DG,"rgb(51, 85, 51)",Color.LG,"rgb(102, 136, 68)",Color.BLUE,"rgb(68, 102, 136)", Color.PINK,"rgb(136, 68, 102)");
    static Map<Thickness, String> thicknessString = ImmutableMap.of(Thickness.S, "10", Thickness.M,"20", Thickness.L, "30",Thickness.XL,"40");


    /*
    <path d="M 420 400 A 20 20 0 0 0 400 380" style="stroke: rgb(51, 85, 51); stroke-width: 5; fill: none;"></path>
	<path d="M 400 420 A 20 20 0 0 0 420 400" style="stroke: rgb(102, 136, 68); stroke-width: 10; fill: none;"></path>
	<path d="M 380 400 A 20 20 0 0 0 400 420" style="stroke: rgb(51, 85, 51); stroke-width: 10; fill: none;"></path>

4
20
4
20
4
     */
}
