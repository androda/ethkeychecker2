import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import ch.qos.logback.core.joran.conditional.ElseAction;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.ethereum.crypto.ECKey;
import org.spongycastle.util.encoders.Hex;

public class Application {
   static  char hexDigit[] = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

//    static  char hexDigit[] = {
//            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
//    };

    static public String byteToHex(byte b) {
        // Returns hex String representation of byte b

        char[] array = {hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f]};
        return new String(array);
    }

    static public String charToHexWithShift(char c, int bits) {
        // Returns hex String representation of char c
        if(bits<=8) {
            byte hi = (byte) ((c >>> (8 + bits)) | (c << (8 - bits)));
            byte lo = (byte) ((c >>> bits) & 0xff);
            return byteToHex(hi) + byteToHex(lo);
        }
        else{
            byte hi = (byte) ((c >>> (bits)) | (c << (16-bits)));
            byte lo = (byte) ((c >>> (bits-8)) & 0xff);
            return byteToHex(lo) + byteToHex(hi);
        }
    }
    public static void main(String[] args) {
        int[] hexOrder = new int[]{0,5,4,5,14,9,12,13,7,8,3,6,10,10,4,10,5,0,6,11,12,3,13,13,3,8,14,7,10,15,6,1,13,14,11,9,11,3,5,4,3,0,4,2,5,8,1,15,15,9,8,1,13,1,15,15,6,7,10,5,8,11,2,8};

//        int[] charLookup = new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
//            StringBuilder sb = new StringBuilder();
//            for (int ring = 0; ring < 16; ring++) {
//                char ringChar=0x0000;
//                ringChar += hexOrder[ring*4]<<12;
//                ringChar += hexOrder[ring*4+1]<<8;
//                ringChar += hexOrder[ring*4+2]<<4;
//                ringChar += hexOrder[ring*4+3];
//                sb.append(charToHexWithShift(ringChar,ring));
//            }

//            System.out.println(sb.toString());
        String publicKey="";
        String senderPrivKey="";

        int count=0;
        List<Integer> charsList = Lists.newArrayList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);
        Iterator<List<Integer>> charListIterator = Collections2.permutations(charsList).iterator();
        boolean found = false;

        do {
            if(!charListIterator.hasNext())
                break;
            List<Integer> charList = charListIterator.next();

            StringBuilder normal = new StringBuilder();
            StringBuilder leftShiftBuilder = new StringBuilder();
            StringBuilder rightShiftBuilder = new StringBuilder();

            for (int ring = 0; ring < 16; ring++) {
                char ringChar = getRingString(hexOrder, ring, charList);


                String rightShift = charToHexWithShift(ringChar, ring);
                String leftShift = charToHexWithShift(ringChar, 16-ring);
                normal.append(charToHexWithShift(ringChar,0));
                rightShiftBuilder.append(rightShift);
                leftShiftBuilder.append(leftShift);

            }



            senderPrivKey = normal.toString();
            publicKey = getPublicFromPrivate(senderPrivKey);
            if(publicKey.equalsIgnoreCase("D64FDEfa8dbc540c2582a6FC44B8f88FfB6657Ce"))
            {
                found=true;
                break;
            }

            senderPrivKey = leftShiftBuilder.toString();
            publicKey = getPublicFromPrivate(senderPrivKey);
            if(publicKey.equalsIgnoreCase("D64FDEfa8dbc540c2582a6FC44B8f88FfB6657Ce"))
            {
                found=true;
                break;
            }
            senderPrivKey = rightShiftBuilder.toString();
            publicKey = getPublicFromPrivate(senderPrivKey);
            if(publicKey.equalsIgnoreCase("D64FDEfa8dbc540c2582a6FC44B8f88FfB6657Ce"))
            {
                found=true;
                break;
            }


            if(count%1000==0||count<100){

                System.out.println(count);
                System.out.println(normal.toString());
                System.out.println(rightShiftBuilder.toString());
                System.out.println(leftShiftBuilder.toString());
            }
            count++;


        }while(!found);
        System.out.println(senderPrivKey);
        System.out.println("Public key: " + publicKey);
    }

    private static char getRingString(int[] hexOrder, int ring, List<Integer> charLookup) {
        char ringChar = 0x0000;
        ringChar += charLookup.get(hexOrder[ring * 4]) << 12;
        ringChar += charLookup.get(hexOrder[ring * 4 + 1]) << 8;
        ringChar += charLookup.get(hexOrder[ring * 4 + 2]) << 4;
        ringChar += charLookup.get(hexOrder[ring * 4 + 3]);
        return ringChar;
    }


    private static String getPublicFromPrivate(String senderPrivKey) {
        String publicKey;BigInteger pk = new BigInteger(senderPrivKey, 16);
        // System.out.println("Private key: " + pk.toString(16));

        ECKey key = ECKey.fromPrivate(pk);
        publicKey = Hex.toHexString(key.getAddress());
        return publicKey;
    }

    private static String bitShiftCylcic(String orig, int bits){
        return null;
    }
}