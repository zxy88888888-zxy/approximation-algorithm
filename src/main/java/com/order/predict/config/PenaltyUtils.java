package com.order.predict.config;


import java.util.*;

public class PenaltyUtils {

    public static double[] updateAlpha(double[] z, double[] Pi) {
        int n = z.length;
        double[] alpha = new double[n];
        Integer[] idx = new Integer[n];
        for (int i = 0; i < n; i++) idx[i] = i;
        Arrays.sort(idx, (a, b) -> Double.compare(z[b], z[a]));

        Set<Integer> S = new HashSet<>();
        for (int j = 0; j < n; j++) {
            Set<Integer> Sj = new HashSet<>(S);
            Sj.add(idx[j]);
            alpha[j] = penalty(Sj, Pi) - penalty(S, Pi);
            S = Sj;
        }
        return alpha;
    }

    public static double penalty(Set<Integer> S, double[] Pi) {
        if (S.isEmpty()) return 0.0;
        double sum = 0;
        for (int j : S) {
            sum += Pi[j];
        }
        int len = S.size();
        return sum - 0.2* (len * len - len);
    }
}

