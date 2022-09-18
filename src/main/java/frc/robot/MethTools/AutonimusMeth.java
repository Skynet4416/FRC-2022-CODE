package frc.robot.MethTools;

public class AutonimusMeth {
    public static void main(String[] args) {
        Circle C1 = new Circle(33.767, -149.227,51.3941);
        Circle C2 = new Circle(-25.91, -150.79, 67.797);

        double[] res = C1.circle_intersect(C2);
        // System.out.println(res[0]);
        // System.out.println(res[1]);

    }
}