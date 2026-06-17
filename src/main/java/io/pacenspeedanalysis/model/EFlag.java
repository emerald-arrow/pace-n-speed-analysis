package io.pacenspeedanalysis.model;

public enum EFlag {
    GF, // GREEN FLAG
    FF, // FINISH FLAG
    FCY, // FULL COURSE YELLOW
    SF, // SAFETY CAR
    RF, // RED FLAG
    UNKNOWN;

    public static EFlag of(String s) {
        for (EFlag flag : EFlag.values()) {
            if (flag.toString().equalsIgnoreCase(s.trim())) {
                return flag;
            }
        }
        return UNKNOWN;
    }
}
