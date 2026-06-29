package it.dnd.character_service.utils;

public final class BAB {
    private BAB() {}

    public static int[] good(int level) {
        return iterative(level);
    }

    public static int[] average(int level) {
        return iterative((int) Math.floor(0.75 * level));
    }

    public static int[] poor(int level) {
        return iterative((int) Math.floor(0.5 * level));
    }

    private static int[] iterative(int bab) {
        if (bab <= 0) return new int[0]; // sicurezza
        int n = (bab - 1) / 5 + 1;
        int[] res = new int[n];
        for (int i = 0; i < n; i++) res[i] = bab - (i * 5);
        return res;
    }

    public static String format(int[] attacks) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < attacks.length; i++) {
            if (i > 0) sb.append("/");
            sb.append(attacks[i] >= 0 ? "+" : "").append(attacks[i]);
        }
        return sb.toString();
    }
}