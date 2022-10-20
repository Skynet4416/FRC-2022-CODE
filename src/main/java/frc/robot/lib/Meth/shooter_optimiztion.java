package frc.robot.lib.Meth;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.spi.CurrencyNameProvider;

import frc.robot.com.github.iprodigy.physics.util.vector.Vector;

import edu.wpi.first.math.util.Units;
import frc.robot.lib.Physics.lib.Ball;
import frc.robot.lib.Physics.lib.base.State;

public final class shooter_optimiztion {
    public enum OptimizationType {
        MAXIMIZE,
        MINIMIZE,
        IGNORE
    }

    public static NumberFormat formatter = new DecimalFormat("#0.00");

    public static void states_to_pos(ArrayList<State> states) {
        ArrayList<Double> x = new ArrayList<Double>();
        ArrayList<Double> y = new ArrayList<Double>();
        for (State state : states) {
            x.add(state.position.getComponent(0));
            y.add(state.position.getComponent(1));

        }
        // System.out.print("\n\n\nx_array = ");
        // System.out.print(Arrays.deepToString(x.toArray()));
        // System.out.print("\n\n\ny_array = ");
        // System.out.println(Arrays.deepToString(y.toArray()));

    }

    private static final Integer failure_tolerance = 200;
    private static final Integer max_attempts = 9999;
    // Target hub = new Target(new Vector(16.0, 2.7178, 1.0), new Vector(1.22, 0.5,
    // 1.0), 999, -999, 999, -999);

    interface BooleanInterface {
        Boolean run(Double angle, Double rpm, Double ratio);
    }

    interface DoubleInterface {
        Double run(Double angle, Double rpm, Double ratio);
    }

    public static Double Lerp(Double A, Double B, Double Percent) {
        return A + (B - A) * Percent;
    }

    // private static Double full_distance_check(Double current_Angle, Double
    // current_RPM, Double current_Rotation_Ratio) {

    // return null;
    // }

    public static Vector optimize(Ball projectile, Target target, // PHYSICALS
            OptimizationType shooting_angle, OptimizationType shooting_RPM, OptimizationType spin_dierction, // INITIALS
            OptimizationType entrance_velocity, OptimizationType entrance_angle, OptimizationType trajectory_length, // GUIDES
            Double Max_Angle, Double Min_Angle, Double Max_RPM, Double Min_RPM) { // CONSTRAINTS

        Min_Angle = 90 - Min_Angle;
        Max_Angle = 90 - Max_Angle;
        Instant starts = Instant.now();

        double TopinDiameter = 4;
        double TopmDiameter = Units.inchesToMeters(TopinDiameter);
        double TopCircumference = (TopmDiameter) * Math.PI;

        double BottominDiameter = 4;
        double BottommDiameter = Units.inchesToMeters(BottominDiameter);
        double BottomCircumference = (BottommDiameter) * Math.PI;

        ///////////////////////////
        Integer Failures = 0;

        Double Angle_Resolution = 1.0; // DEGREES
        Double Rotation_Ratio_Resolution = 0.1; // PERCENTAGE
        Double RPM_Resolution = 50.0; // RPM

        Double Initial_Angle = (shooting_angle != OptimizationType.MAXIMIZE) ? Min_Angle : Max_Angle;
        Double Angle_Increment = (shooting_angle != OptimizationType.MAXIMIZE) ? -Angle_Resolution : Angle_Resolution;
        Double Current_Angle = Initial_Angle;

        Double Initial_Rotation_Ratio = (spin_dierction != OptimizationType.MAXIMIZE) ? -1.0 : 1.0;
        Double Rotation_Ratio_Increment = (spin_dierction != OptimizationType.MAXIMIZE) ? Rotation_Ratio_Resolution
                : -Rotation_Ratio_Resolution;
        Double Current_Rotation_Ratio = (spin_dierction == OptimizationType.IGNORE) ? Initial_Rotation_Ratio
                : Initial_Rotation_Ratio;

        Double Initial_RPM = (shooting_RPM != OptimizationType.MAXIMIZE) ? Max_RPM : Min_RPM;
        Double RPM_Increment = (shooting_RPM != OptimizationType.MAXIMIZE) ? -RPM_Resolution : RPM_Resolution;
        Double Current_RPM = Initial_RPM;

        BooleanInterface full_check = new BooleanInterface() {
            public Boolean run(Double angle, Double rpm, Double ratio) {
                double TopRPM = (spin_dierction != OptimizationType.IGNORE)
                        ? (ratio > 0.0 ? rpm * (ratio - 1) : rpm)
                        : rpm;
                double TopRPS = TopRPM / 60.0;
                double BottomRPM = (spin_dierction != OptimizationType.IGNORE)
                        ? (ratio < 0.0 ? rpm * (1 - ratio) : rpm)
                        : rpm;
                double BottomRPS = BottomRPM / 60.0;

                double muzzle_velocity = (TopRPS * TopCircumference + BottomRPS * BottomCircumference) / 2.0; // SURFACE
                                                                                                              // SPEED
                double angle_rads = Math.toRadians(angle); // IN RADIANS

                double TopRads = 0.104719755 * TopRPM;
                double BottompRads = 0.104719755 * BottomRPM;

                Vector started_velocity = new Vector(muzzle_velocity * Math.sin(angle_rads),
                        muzzle_velocity * Math.cos(angle_rads), 0.0); // m/s
                Vector started_rotational_velocity = new Vector(0.0, 0.0,
                        (TopRads * TopmDiameter / 2 - BottompRads * BottommDiameter / 2)
                                / (2 * projectile.get_radius())); // radians

                projectile.set_started_velocity(started_velocity);
                projectile.set_rotational_velocity(started_rotational_velocity);

                ArrayList<State> states = projectile.simulate_ball(false);

                Boolean result = target.check(states);

                if (result) {

                    // System.out.println(TopRPM);
                    // System.out.println(BottomRPM);
                    // System.out.println(" ");

                    // states_to_pos(states);

                    // System.out.println("\n\n" + started_velocity);
                    // System.out.println(started_rotational_velocity);
                }

                projectile.set_position(new Vector(0.0, 0.1, 0.0));
                return result;
            };
        };

        // System.out.println("STARTED!");
        Integer results = 0;
        // for (Integer Attempts = 0; Attempts < max_attempts; Attempts++) {
        // CurrentFound = false;
        // System.out.println(Attempts + " " + Current_Angle + " " + Current_RPM + " " +
        // Current_Rotation_Ratio);

        Integer Attempts = 0;

        Double BestTopRPM = -1.0;
        Double BestBottomRPM = -1.0;
        Double BestAngle = -1.0;

        Current_Angle = Initial_Angle;
        while (shooting_angle == OptimizationType.MAXIMIZE ? Current_Angle <= Min_Angle : Current_Angle >= Max_Angle) {
            Current_RPM = Initial_RPM;
            while (shooting_RPM == OptimizationType.MAXIMIZE ? Current_RPM <= Max_RPM : Current_RPM >= Min_RPM) {
                Current_Rotation_Ratio = Initial_Rotation_Ratio;
                do {
                    Attempts++;
                    if (full_check.run(Current_Angle, Current_RPM, Current_Rotation_Ratio)) {
                        Failures = 0;
                        results++;
                        BestTopRPM = (spin_dierction != OptimizationType.IGNORE)
                                ? (Current_Rotation_Ratio > 0.0 ? Current_RPM * (Current_Rotation_Ratio - 1)
                                        : Current_RPM)
                                : Current_RPM;

                        BestBottomRPM = (spin_dierction != OptimizationType.IGNORE)
                                ? (Current_Rotation_Ratio < 0.0 ? Current_RPM * (1 - Current_Rotation_Ratio)
                                        : Current_RPM)
                                : Current_RPM;
                        BestAngle = Current_Angle;

                        // System.out.println("HIT> " + Current_RPM + " " + Current_Angle);
                    }
                    Current_Rotation_Ratio += Rotation_Ratio_Increment;
                } while (Current_Rotation_Ratio <= 1.0 && Current_Rotation_Ratio >= -1.0
                        && spin_dierction != OptimizationType.IGNORE);
                Current_RPM += RPM_Increment;
            }
            // System.out.println(90 - Current_Angle);
            Current_Angle += Angle_Increment;
            Failures++;
        }

        // System.out.println("TRPM " + BestTopRPM + "\nBRPM " + BestBottomRPM +
        //         "\nANGLE " + (90.0 - BestAngle));

        return new Vector(BestTopRPM, BestBottomRPM, (90.0 - BestAngle));
    }

    public static Vector smart_optimize(Ball projectile, Target target, // PHYSICALS
            double Max_Angle, double Min_Angle, double Max_RPM, double Min_RPM, Double max_hub_distance) { // CONSTRAINTS
        Instant starts = Instant.now();

        double TopinDiameter = 4;
        double TopmDiameter = Units.inchesToMeters(TopinDiameter);
        double TopCircumference = (TopmDiameter) * Math.PI;

        double BottominDiameter = 4;
        double BottommDiameter = Units.inchesToMeters(BottominDiameter);
        double BottomCircumference = (BottommDiameter) * Math.PI;

        ///////////////////////////
        Integer Failures = 0;

        Double Angle_Resolution = 1.0; // DEGREES
        Double Rotation_Ratio_Resolution = 0.1; // PERCENTAGE
        Double RPM_Resolution = 100.0; // RPM

        Double Initial_Angle = Max_Angle;
        Double Angle_Increment = -Angle_Resolution;
        Double Current_Angle = Initial_Angle;

        // Double Initial_Rotation_Ratio = 1.0;
        Double Initial_Rotation_Ratio = 0.0;
        Double Rotation_Ratio_Increment = -Rotation_Ratio_Resolution;
        Double Current_Rotation_Ratio = Initial_Rotation_Ratio;

        Double Initial_RPM = Max_RPM;
        Double RPM_Increment = -RPM_Resolution;
        Double Current_RPM = Initial_RPM;

        BooleanInterface full_check = new BooleanInterface() {
            public Boolean run(Double angle, Double rpm, Double ratio) {
                double TopRPM = (ratio > 0.0 ? rpm * (ratio - 1) : rpm);
                double TopRPS = TopRPM / 60.0;
                double BottomRPM = ratio < 0.0 ? rpm * (1 - ratio) : rpm;
                double BottomRPS = BottomRPM / 60.0;

                double muzzle_velocity = (TopRPS * TopCircumference + BottomRPS * BottomCircumference) / 2.0; // SURFACE
                                                                                                              // SPEED
                double angle_rads = Math.toRadians(angle); // IN RADIANS

                double TopRads = 0.104719755 * TopRPM;
                double BottompRads = 0.104719755 * BottomRPM;

                Vector started_velocity = new Vector(muzzle_velocity * Math.sin(angle_rads),
                        muzzle_velocity * Math.cos(angle_rads), 0.0); // m/s
                Vector started_rotational_velocity = new Vector(0.0, 0.0,
                        (TopRads * TopmDiameter / 2 - BottompRads * BottommDiameter / 2)
                                / (2 * projectile.get_radius())); // radians

                projectile.set_position(new Vector(0.0, 0.1, 0.0));
                projectile.set_started_velocity(started_velocity);
                projectile.set_rotational_velocity(started_rotational_velocity);

                ArrayList<State> states = projectile.simulate_ball(false);
                Boolean result = target.check(states);

                if (result) {

                    // System.out.println(TopRPM);
                    // System.out.println(BottomRPM);
                    // System.out.println(" ");

                    // states_to_pos(states);

                    // System.out.println("\n\n" + started_velocity);
                    // System.out.println(started_rotational_velocity);
                }
                return result;
            };
        };

        DoubleInterface full_distance_check = new DoubleInterface() {
            public Double run(Double angle, Double rpm, Double ratio) {
                double TopRPM = (ratio > 0.0 ? rpm * (ratio - 1) : rpm);
                double TopRPS = TopRPM / 60.0;
                double BottomRPM = ratio < 0.0 ? rpm * (1 - ratio) : rpm;
                double BottomRPS = BottomRPM / 60.0;

                double muzzle_velocity = (TopRPS * TopCircumference + BottomRPS * BottomCircumference) / 2.0; // SURFACE
                                                                                                              // SPEED
                double angle_rads = Math.toRadians(angle); // IN RADIANS

                double TopRads = 0.104719755 * TopRPM;
                double BottompRads = 0.104719755 * BottomRPM;

                Vector started_velocity = new Vector(muzzle_velocity * Math.sin(angle_rads),
                        muzzle_velocity * Math.cos(angle_rads), 0.0); // m/s
                Vector started_rotational_velocity = new Vector(0.0, 0.0,
                        (TopRads * TopmDiameter / 2 - BottompRads * BottommDiameter / 2)
                                / (2 * projectile.get_radius())); // radians

                projectile.set_position(new Vector(0.0, 0.1, 0.0));
                projectile.set_started_velocity(started_velocity);
                projectile.set_rotational_velocity(started_rotational_velocity);

                ArrayList<State> states = projectile.simulate_ball(false);
                Double result = target.check_distance(states);

                if (result != 9999.0) {

                    // System.out.println(TopRPM);
                    // System.out.println(BottomRPM);
                    // System.out.println(" ");

                    // states_to_pos(states);

                    // System.out.println("\n\n" + started_velocity);
                    // System.out.println(started_rotational_velocity);
                }
                return result;
            };
        };

        if (max_hub_distance > 0)
            Current_Rotation_Ratio = Math.min(target.x_pos / max_hub_distance, 1.0) * -1.0;

        Integer results = 0;
        Integer Attempts = 0;

        Double BestTopRPM = -1.0;
        Double BestBottomRPM = -1.0;
        Double BestAngle = -1.0;
        Double BestRPM = -1.0;

        Current_Angle = Initial_Angle;

        BestRPM = Current_RPM;
        BestAngle = Current_Angle;

        while (Current_Angle > Min_Angle) {
            if (Current_RPM <= Min_RPM)
                Current_RPM = BestRPM;

            boolean fitsinhub = false;
            while (!fitsinhub && Current_RPM > Min_RPM) {
                Current_RPM += RPM_Increment;
                fitsinhub = full_check.run(Current_Angle, Current_RPM, Current_Rotation_Ratio);
            }
            if (full_check.run(Current_Angle, Current_RPM, Current_Rotation_Ratio)) {
                BestRPM = Current_RPM;
                BestAngle = Current_Angle;

                BestTopRPM = (Current_Rotation_Ratio > 0.0 ? BestRPM * (Current_Rotation_Ratio - 1) : BestRPM);
                BestBottomRPM = Current_Rotation_Ratio < 0.0 ? BestRPM * (1 - Current_Rotation_Ratio) : BestRPM;
            } else {
                // System.out.println(90 - Current_Angle + " " + Current_RPM);
                // System.out.println(full_distance_check.run(Current_Angle, Current_RPM,
                // Current_Rotation_Ratio));
                // System.out.println();
            }

            Current_Angle += Angle_Increment;
        }

        // System.out.println("TRPM " + BestTopRPM + "\nBRPM " + BestBottomRPM +
        //         "\nANGLE " + (90.0 - BestAngle));
        // System.out.println(Duration.between(starts, Instant.now()).getNano() / 1000000000.0);

        return new Vector(BestTopRPM, BestBottomRPM, (BestAngle));
    }

    public static Vector binary_smart_optimize(Ball projectile, Target target, // PHYSICALS
            double Max_Angle, double Min_Angle, double Max_RPM, double Min_RPM, Double max_hub_distance) { // CONSTRAINTS
        Instant starts = Instant.now();

        double TopinDiameter = 4;
        double TopmDiameter = Units.inchesToMeters(TopinDiameter);
        double TopCircumference = (TopmDiameter) * Math.PI;

        double BottominDiameter = 4;
        double BottommDiameter = Units.inchesToMeters(BottominDiameter);
        double BottomCircumference = (BottommDiameter) * Math.PI;

        ///////////////////////////
        Double Angle_Resolution = 1.0; // DEGREES
        Double RPM_Resolution = 50.0; // RPM

        Double Initial_Angle = Max_Angle;
        Double Angle_Increment = -Angle_Resolution;
        Double Current_Angle = Initial_Angle;

        // Double Initial_Rotation_Ratio = 1.0;
        Double Initial_Rotation_Ratio = 0.0;
        Double Current_Rotation_Ratio = Initial_Rotation_Ratio;

        Double Initial_RPM = Max_RPM;
        Double RPM_Increment = -RPM_Resolution;
        Double Current_RPM = Initial_RPM;

        BooleanInterface full_check = new BooleanInterface() {
            public Boolean run(Double angle, Double rpm, Double ratio) {
                double TopRPM = (ratio > 0.0 ? rpm * (ratio - 1) : rpm);
                double TopRPS = TopRPM / 60.0;
                double BottomRPM = ratio < 0.0 ? rpm * (1 - ratio) : rpm;
                double BottomRPS = BottomRPM / 60.0;

                double muzzle_velocity = (TopRPS * TopCircumference + BottomRPS * BottomCircumference) / 2.0; // SURFACE
                                                                                                              // SPEED
                double angle_rads = Math.toRadians(angle); // IN RADIANS

                double TopRads = 0.104719755 * TopRPM;
                double BottompRads = 0.104719755 * BottomRPM;

                Vector started_velocity = new Vector(muzzle_velocity * Math.sin(angle_rads),
                        muzzle_velocity * Math.cos(angle_rads), 0.0); // m/s
                Vector started_rotational_velocity = new Vector(0.0, 0.0,
                        (TopRads * TopmDiameter / 2 - BottompRads * BottommDiameter / 2)
                                / (2 * projectile.get_radius())); // radians

                projectile.set_position(new Vector(0.0, 0.1, 0.0));
                projectile.set_started_velocity(started_velocity);
                projectile.set_rotational_velocity(started_rotational_velocity);

                ArrayList<State> states = projectile.simulate_ball(false);
                Boolean result = target.check(states);

                if (result) {

                    // System.out.println(TopRPM);
                    // System.out.println(BottomRPM);
                    // System.out.println(" ");

                    // states_to_pos(states);

                    // System.out.println("\n\n" + started_velocity);
                    // System.out.println(started_rotational_velocity);
                }
                return result;
            };
        };

        DoubleInterface full_distance_check = new DoubleInterface() {
            public Double run(Double angle, Double rpm, Double ratio) {
                double TopRPM = (ratio > 0.0 ? rpm * (ratio - 1) : rpm);
                double TopRPS = TopRPM / 60.0;
                double BottomRPM = ratio < 0.0 ? rpm * (1 - ratio) : rpm;
                double BottomRPS = BottomRPM / 60.0;

                double muzzle_velocity = (TopRPS * TopCircumference + BottomRPS * BottomCircumference) / 2.0; // SURFACE
                                                                                                              // SPEED
                double angle_rads = Math.toRadians(angle); // IN RADIANS

                double TopRads = 0.104719755 * TopRPM;
                double BottompRads = 0.104719755 * BottomRPM;

                Vector started_velocity = new Vector(muzzle_velocity * Math.sin(angle_rads),
                        muzzle_velocity * Math.cos(angle_rads), 0.0); // m/s
                Vector started_rotational_velocity = new Vector(0.0, 0.0,
                        (TopRads * TopmDiameter / 2 - BottompRads * BottommDiameter / 2)
                                / (2 * projectile.get_radius())); // radians

                projectile.set_position(new Vector(0.0, 0.1, 0.0));
                projectile.set_started_velocity(started_velocity);
                projectile.set_rotational_velocity(started_rotational_velocity);

                ArrayList<State> states = projectile.simulate_ball(false);
                Double result = target.check_distance(states);

                if (result != 9999.0) {

                    // System.out.println(TopRPM);
                    // System.out.println(BottomRPM);
                    // System.out.println(" ");

                    // states_to_pos(states);

                    // System.out.println("\n\n" + started_velocity);
                    // System.out.println(started_rotational_velocity);
                }
                return result;
            };
        };

        if (max_hub_distance > 0)
            Current_Rotation_Ratio = Math.min(target.x_pos / max_hub_distance, 1.0) * -1.0;

        Integer results = 0;
        Integer Attempts = 0;

        Double BestTopRPM = -1.0;
        Double BestBottomRPM = -1.0;
        Double BestAngle = -1.0;
        Double BestRPM = -1.0;

        Current_Angle = Initial_Angle;

        BestRPM = Current_RPM;
        BestAngle = Current_Angle;
        double[] angles = new double[(int) ((Max_Angle - Min_Angle) / Math.abs(Angle_Increment))];
        int counter = 0;
        for (double i = Max_Angle; i > Min_Angle; i += Angle_Increment) {
            angles[counter] = i;
            counter++;
        }
        while (angles.length > 1) {

            if (Current_RPM <= Min_RPM)
                Current_RPM = BestRPM;

            boolean fitsinhub = false;
            while (!fitsinhub && Current_RPM > Min_RPM) {
                Current_RPM += RPM_Increment;
                fitsinhub = full_check.run(Current_Angle, Current_RPM, Current_Rotation_Ratio);
            }
            if (full_check.run(Current_Angle, Current_RPM, Current_Rotation_Ratio)) {
                BestRPM = Current_RPM;
                BestAngle = Current_Angle;

                BestTopRPM = (Current_Rotation_Ratio > 0.0 ? BestRPM * (Current_Rotation_Ratio - 1) : BestRPM);
                BestBottomRPM = Current_Rotation_Ratio < 0.0 ? BestRPM * (1 - Current_Rotation_Ratio) : BestRPM;
            } else {
                if (full_distance_check.run(Current_Angle, Current_RPM, Current_Rotation_Ratio) > 0.0) {
                    angles = Arrays.copyOfRange(angles, angles.length / 2, angles.length);

                } else {
                    angles = Arrays.copyOfRange(angles, 0, angles.length / 2);

                }
                Current_Angle = angles[angles.length / 2];
            }

        }

        // System.out.println("\nTRPM " + BestTopRPM + "\nBRPM " + BestBottomRPM + "\nANGLE " + (90.0 - BestAngle));
        // System.out.println(Duration.between(starts, Instant.now()).getNano() / 1000000000.0);

        return new Vector(BestTopRPM, BestBottomRPM, (BestAngle));
    }

    public static Vector binary_smart_optimize_runge_kutta(Ball projectile, Target target, // PHYSICALS
            double Max_Angle, double Min_Angle, double Max_RPM, double Min_RPM, Double max_hub_distance) { // CONSTRAINTS
        Instant starts = Instant.now();

        double TopinDiameter = 4;
        double TopmDiameter = Units.inchesToMeters(TopinDiameter);
        double TopCircumference = (TopmDiameter) * Math.PI;

        double BottominDiameter = 4;
        double BottommDiameter = Units.inchesToMeters(BottominDiameter);
        double BottomCircumference = (BottommDiameter) * Math.PI;

        ///////////////////////////
        Double Angle_Resolution = 1.0; // DEGREES
        Double RPM_Resolution = 50.0; // RPM

        Double Initial_Angle = Max_Angle;
        Double Angle_Increment = -Angle_Resolution;
        Double Current_Angle = Initial_Angle;

        // Double Initial_Rotation_Ratio = 1.0;
        Double Initial_Rotation_Ratio = 0.0;
        Double Current_Rotation_Ratio = Initial_Rotation_Ratio;

        Double Initial_RPM = Max_RPM;
        Double RPM_Increment = -RPM_Resolution;
        Double Current_RPM = Initial_RPM;

        BooleanInterface full_check = new BooleanInterface() {
            public Boolean run(Double angle, Double rpm, Double ratio) {
                double TopRPM = (ratio > 0.0 ? rpm * (ratio - 1) : rpm);
                double TopRPS = TopRPM / 60.0;
                double BottomRPM = ratio < 0.0 ? rpm * (1 - ratio) : rpm;
                double BottomRPS = BottomRPM / 60.0;

                double muzzle_velocity = (TopRPS * TopCircumference + BottomRPS * BottomCircumference) / 2.0; // SURFACE
                                                                                                              // SPEED
                double angle_rads = Math.toRadians(angle); // IN RADIANS

                double TopRads = 0.104719755 * TopRPM;
                double BottompRads = 0.104719755 * BottomRPM;

                Vector started_velocity = new Vector(muzzle_velocity * Math.sin(angle_rads),
                        muzzle_velocity * Math.cos(angle_rads), 0.0); // m/s
                Vector started_rotational_velocity = new Vector(0.0, 0.0,
                        (TopRads * TopmDiameter / 2 - BottompRads * BottommDiameter / 2)
                                / (2 * projectile.get_radius())); // radians

                projectile.set_position(new Vector(0.0, 0.1, 0.0));
                projectile.set_started_velocity(started_velocity);
                projectile.set_rotational_velocity(started_rotational_velocity);

                ArrayList<State> states = projectile.simulate_ball_runge_kutta(false);
                Boolean result = target.check(states);

                if (result) {

                    // System.out.println(TopRPM);
                    // System.out.println(BottomRPM);
                    // System.out.println(" ");

                    // states_to_pos(states);

                    // System.out.println("\n\n" + started_velocity);
                    // System.out.println(started_rotational_velocity);
                }
                return result;
            };
        };

        DoubleInterface full_distance_check = new DoubleInterface() {
            public Double run(Double angle, Double rpm, Double ratio) {
                double TopRPM = (ratio > 0.0 ? rpm * (ratio - 1) : rpm);
                double TopRPS = TopRPM / 60.0;
                double BottomRPM = ratio < 0.0 ? rpm * (1 - ratio) : rpm;
                double BottomRPS = BottomRPM / 60.0;

                double muzzle_velocity = (TopRPS * TopCircumference + BottomRPS * BottomCircumference) / 2.0; // SURFACE
                                                                                                              // SPEED
                double angle_rads = Math.toRadians(angle); // IN RADIANS

                double TopRads = 0.104719755 * TopRPM;
                double BottompRads = 0.104719755 * BottomRPM;

                Vector started_velocity = new Vector(muzzle_velocity * Math.sin(angle_rads),
                        muzzle_velocity * Math.cos(angle_rads), 0.0); // m/s
                Vector started_rotational_velocity = new Vector(0.0, 0.0,
                        (TopRads * TopmDiameter / 2 - BottompRads * BottommDiameter / 2)
                                / (2 * projectile.get_radius())); // radians

                projectile.set_position(new Vector(0.0, 0.1, 0.0));
                projectile.set_started_velocity(started_velocity);
                projectile.set_rotational_velocity(started_rotational_velocity);

                ArrayList<State> states = projectile.simulate_ball_runge_kutta(false);
                Double result = target.check_distance(states);

                if (result != 9999.0) {

                    // System.out.println(TopRPM);
                    // System.out.println(BottomRPM);
                    // System.out.println(" ");

                    // states_to_pos(states);

                    // System.out.println("\n\n" + started_velocity);
                    // System.out.println(started_rotational_velocity);
                }
                return result;
            };
        };

        

        if (max_hub_distance > 0)
            Current_Rotation_Ratio = Math.min(target.x_pos / max_hub_distance, 1.0) * -1.0;

        Integer results = 0;
        Integer Attempts = 0;

        Double BestTopRPM = -1.0;
        Double BestBottomRPM = -1.0;
        Double BestAngle = -1.0;
        Double BestRPM = -1.0;

        Current_Angle = Initial_Angle;

        BestRPM = Current_RPM;
        BestAngle = Current_Angle;
        double[] angles = new double[(int) ((Max_Angle - Min_Angle) / Math.abs(Angle_Increment))];
        int counter = 0;
        for (double i = Max_Angle; i > Min_Angle; i += Angle_Increment) {
            angles[counter] = i;
            counter++;
        }

        
        while (angles.length > 1) {

            if (Current_RPM <= Min_RPM)
                Current_RPM = BestRPM;

            boolean fitsinhub = false;
            while (!fitsinhub && Current_RPM > Min_RPM) {
                Current_RPM += RPM_Increment;
                fitsinhub = full_check.run(Current_Angle, Current_RPM, Current_Rotation_Ratio);
            }
            if (full_check.run(Current_Angle, Current_RPM, Current_Rotation_Ratio)) {
                BestRPM = Current_RPM;
                BestAngle = Current_Angle;

                BestTopRPM = (Current_Rotation_Ratio > 0.0 ? BestRPM * (Current_Rotation_Ratio - 1) : BestRPM);
                BestBottomRPM = Current_Rotation_Ratio < 0.0 ? BestRPM * (1 - Current_Rotation_Ratio) : BestRPM;
            }
            if (full_distance_check.run(Current_Angle, Current_RPM, Current_Rotation_Ratio) > 0.0) {
                angles = Arrays.copyOfRange(angles, angles.length / 2, angles.length);

            } else {
                angles = Arrays.copyOfRange(angles, 0, angles.length / 2);

            }
            Current_Angle = angles[angles.length / 2];

        }
        // System.out.println("\nTRPM " + BestTopRPM + "\nBRPM " + BestBottomRPM + "\nANGLE " + (90.0 - BestAngle));
        // System.out.println(Duration.between(starts, Instant.now()).getNano() / 1000000000.0);

        return new Vector(BestTopRPM, BestBottomRPM, (BestAngle));
    }
}
