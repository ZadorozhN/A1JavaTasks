import java.util.Arrays;

public class IpParser {
    private static final int AMOUNT_OF_OCTETS = 4;

    public static int ipToIntParser(String ip) {
        String[] octets = ip.split("[.]");
        int encodedIp = 0;

        if(octets.length != AMOUNT_OF_OCTETS){
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < AMOUNT_OF_OCTETS; i++) {
            encodedIp = (encodedIp << 8) + Integer.parseInt(octets[i]);
        }

        return encodedIp;
    }

    public static String intToIpParser(long encodedIp) {
        String[] octets = new String[AMOUNT_OF_OCTETS];
        int mask = 0b11111111;

        for (int i = AMOUNT_OF_OCTETS - 1; i >= 0; i--) {
            octets[i] = Long.toString((encodedIp & mask) >> 8 * (AMOUNT_OF_OCTETS - 1 - i));
            mask = (mask << 8);
        }

        return String.join(".", octets);
    }
}
