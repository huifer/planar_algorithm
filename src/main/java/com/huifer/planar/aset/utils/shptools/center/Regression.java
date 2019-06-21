package com.huifer.planar.aset.utils.shptools.center;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Title : Regression </p>
 * <p>Description : 最小二乘法</p>
 *
 * @author huifer
 * @date 2018/10/22
 */
public class Regression {

    List<Double> x;
    List<Double> y;
    double beta1;
    double beta0;

    public Regression(List<Double> x, List<Double> y) {
        this.x = x;
        this.y = y;
        beta0 = getBeta0();
        beta1 = getBeta1();
    }

    public static void main(String[] args) {
//        Double[] tm = new Double[]{300.0, 600.0, 900.0, 1200.0, 1500.0, 1800.0, 2100.0, 2400.0, 2700.0, 3000.0};
//        Double[] x = new Double[]{87207.572, 87208.201, 87208.536, 87208.763, 87208.776, 87208.64, 87208.102, 87207.509, 87206.994, 87206.444};
//        Double[] y = new Double[]{74227.619, 74227.177, 74226.671, 74226.1, 74225.675, 74225.222, 74224.605, 74224.202, 74224.084, 74224.061};
//        Double[] z = new Double[]{5.973, 5.973, 5.968, 5.961, 5.963, 5.956, 5.963, 5.964, 5.96, 5.967};
//
//        System.out.println("==========");
//        Regression rs = new Regression(Arrays.stream(tm).collect(Collectors.toList()), Arrays.stream(x).collect(Collectors.toList()));
//        System.out.println(rs.beta0);
//        System.out.println(rs.beta1);
//        System.out.println("==========");
//        Regression rs1 = new Regression(Arrays.stream(tm).collect(Collectors.toList()), Arrays.stream(y).collect(Collectors.toList()));
//        System.out.println(rs1.beta0);
//        System.out.println(rs1.beta1);
//        System.out.println("==========");
//        Regression rs2 = new Regression(Arrays.stream(tm).collect(Collectors.toList()), Arrays.stream(z).collect(Collectors.toList()));
//        System.out.println(rs2.beta0);
//        System.out.println(rs2.beta1);

        Double[] xxxx = new Double[]{1.0, 2.0, 3.0};
//        Double[] xxxx = new Double[]{329.6532078, 314.9685383, 291.8290615, 263.7946911, 235.0928364, 203.0535564, 187.0339184, 178.8016033, 173.2392292, 171.0142765, 166.3418827, 164.7844181, 165.4519024, 168.3443394, 172.7942371, 175.2416821, 178.8016033, 199.9386272, 203.2760525, 220.4081669, 227.3055096, 241.1002026, 245.3276043, 250.8899822, 253.1149311, 257.7873249, 257.5648289, 256.0073643, 254.6723957, 254.2274075, 258.8998013, 263.7946911, 305.8462429, 316.9709949, 326.3157825, 329.4307156, 334.1031094, 337.4405346, 340.3329678, 342.3354244, 347.2303143, 349.0102749, 349.9002552, 350.1227474, 350.3452435, 354.350153, 354.1276569, 350.1227474, 344.7828693, 338.7755032, 329.6532078,};

        Double[] yyyy = new Double[]{1.0, 2.0, 3.0};
//        Double[] yyyy = new Double[]{97.41548347, 88.73818016, 88.07069206, 87.40320778, 82.50831795, 72.94103432, 65.37620354, 52.91648293, 40.2342701, 31.11197472, 31.55696297, 38.6768055, 40.90175438, 47.13161278, 56.92139244, 60.48131371, 68.71362877, 78.9484005, 81.39584541, 85.1782589, 87.18071175, 88.96067238, 91.63061333, 93.41057396, 93.85556221, 94.07805824, 97.41548347, 102.0878773, 106.7602749, 112.100153, 110.7651844, 93.41057396, 94.52304649, 95.63552284, 101.642889, 103.8678379, 106.5377789, 111.2101727, 116.9950428, 124.3373775, 141.691988, 150.5917873, 155.4866772, 163.9414883, 167.7239017, 168.6138821, 155.0416889, 132.1247044, 115.8825665, 106.7602749, 97.41548347,};

//        System.out.println(xxxx.length);
//        System.out.println(yyyy.length);

        Regression rs3 = new Regression(Arrays.stream(xxxx).collect(Collectors.toList()),
                Arrays.stream(yyyy).collect(Collectors.toList()));
//        System.out.println(rs3.beta0);
//        System.out.println(rs3.beta1);

        double v = Math.toDegrees(Math.atan(Math.PI));
//        System.out.println(v);
    }

    private double xBar() {
        Double sum = 0.0;
        for (int i = 0; i < x.size(); i++) {
            sum += x.get(i);
        }
        return sum / x.size();
    }

    private double yBar() {
        Double sum = 0.0;
        for (int i = 0; i < y.size(); i++) {
            sum += y.get(i);
        }
        return sum / y.size();
    }


    private Double sse() {
        Double sum = 0.0;
        double xa = xBar();
        double ya = yBar();
        for (int i = 0; i < x.size(); i++) {
            sum += (x.get(i) - xa) * (y.get(i) - ya);
        }
        return sum;
    }

    private Double ssx() {
        Double sum = 0.0;
        double xa = xBar();
        for (int i = 0; i < x.size(); i++) {
            sum += (x.get(i) - xa) * (x.get(i) - xa);
        }

        return sum;
    }

    private double getBeta1() {
        double beta1 = sse() / ssx();
        return beta1;
    }

    private double getBeta0() {
        double beta0 = yBar() - getBeta1() * xBar();
        return beta0;
    }


}
