package frc.robot.MethTools;

import java.time.Instant    ;
import java.util.ArrayList;

import frc.robot.Globals;
import frc.robot.Constants.Shooter.Physics;

public class ShooterMeth {
    public static double[] CalculatePosY(double current_y_pos, double current_velocity_y,
            double current_acceleration_y) {
        double force_y;
        if (current_velocity_y > 0) {
            force_y = -(Physics.ball_mass * Physics.gravitational_acceleration_near_earth
                    + Physics.drag_thing * Math.pow(current_velocity_y, 2));
        } else {
            force_y = -(Physics.ball_mass * Physics.gravitational_acceleration_near_earth
                    - Physics.drag_thing * Math.pow(current_velocity_y, 2));
        }
        double acceleration_y = force_y / Physics.ball_mass; // use Newton's second law to calculate acceleration
        double velocity_y = current_velocity_y + (acceleration_y + current_acceleration_y) * Physics.resolution;
        double y_pos = Math.max(current_y_pos + velocity_y * Physics.resolution, 0);
        return new double[] { y_pos, velocity_y, acceleration_y };
    }

    public static double[] CalculatePosX(double current_x_pos, double current_velocity_x,
            double current_acceleration_x) {
        double force_x = -(Physics.drag_thing * Math.pow(current_velocity_x, 2));
        double acceleration_x = force_x / Physics.ball_mass;
        double velocity_x = current_velocity_x + (acceleration_x + current_acceleration_x) * Physics.resolution;
        double x_pos = current_x_pos + velocity_x * Physics.resolution;
        return new double[] { x_pos, velocity_x, acceleration_x };
    }

    public static ArrayList<ArrayList<Double>> ReturnArrayOfPos(double muzzle_velocity, double angle_rad) {
        // System.out.println(muzzle_velocity + " > " + angle_rad);
        double pos_y = Physics.shooter_height;
        double y_velocity = muzzle_velocity * Math.sin(angle_rad);
        double y_acceleration = 0;
        double pos_x = 0;
        double x_velocity = muzzle_velocity * Math.cos(angle_rad);
        double x_acceleration = 0;

        ArrayList<Double> arrX = new ArrayList<Double>(), arrY = new ArrayList<Double>();
        arrX.add(pos_x);
        arrY.add(pos_y);

        double[] returned_arr;

        returned_arr = CalculatePosY(pos_y, y_velocity, y_acceleration);
        pos_y = returned_arr[0];
        y_velocity = returned_arr[1];
        y_acceleration = returned_arr[2];

        returned_arr = CalculatePosX(pos_x, x_velocity, x_acceleration);
        pos_x = returned_arr[0];
        x_velocity = returned_arr[1];
        x_acceleration = returned_arr[2];

        arrX.add(pos_x);
        arrY.add(pos_y);

        while (pos_y > 0) {
            returned_arr = CalculatePosY(pos_y, y_velocity, y_acceleration);
            pos_y = returned_arr[0];
            y_velocity = returned_arr[1];
            y_acceleration = returned_arr[2];

            returned_arr = CalculatePosX(pos_x, x_velocity, x_acceleration);
            pos_x = returned_arr[0];
            x_velocity = returned_arr[1];
            x_acceleration = returned_arr[2];

            arrX.add(pos_x);
            arrY.add(pos_y);
        }

        ArrayList<ArrayList<Double>> temp = new ArrayList<ArrayList<Double>>();
        temp.add(arrX);
        temp.add(arrY);

        return temp;
    }

    public static double RPMToVelcoity(double rpm) {
        double rps = (rpm) / 60;
        double circumference = (Physics.diamater / 1000) * Math.PI;
        double velocity = rps * circumference;
        return velocity;
    }

    public static boolean fits_in_hub(ArrayList<ArrayList<Double>> trajectory) {
        boolean over_thresh = false;
        Point lastPoint = new Point(0, 0);
        Point currePoint = new Point(0, 0);
        for (int i = 0; i < trajectory.get(0).size(); i++) {
            lastPoint.y = currePoint.y;
            lastPoint.x = currePoint.x;

            currePoint.x = trajectory.get(0).get(i);
            currePoint.y = trajectory.get(1).get(i);

            if (currePoint.x > Globals.hub_distance + Physics.hub_diameter / 2)
                return false;
            else if (currePoint.x >= Globals.hub_distance - Physics.hub_diameter / 2 + Physics.ball_diamater
                    && currePoint.x <= Globals.hub_distance + Physics.hub_diameter / 2 - Physics.ball_diamater
                    && currePoint.y <= Physics.hub_height && lastPoint.y >= Physics.hub_height && over_thresh) { // TODO: "currePoint.y <= Physics.hub_height && lastPoint.y >= Physics.hub_height" was "lastPoint.y <= Physics.hub_height"
                return true;
            } else if (!over_thresh
                    && (currePoint.y <= Physics.hub_height + Physics.ball_diamater + Physics.threashold_y)
                    && (lastPoint.y >= Physics.hub_height + Physics.ball_diamater + Physics.threashold_y)
                    && currePoint.x >= Globals.hub_distance - Physics.hub_diameter / 2) {
                over_thresh = true;
            }
        }
        return false;

    }

    public static double[] optimize() {
        double rpm = Physics.MAX_RPM * Physics.precentage;
        double best_rpm = rpm;
        double best_angle = 45;
        ArrayList<ArrayList<Double>> line = ReturnArrayOfPos(RPMToVelcoity(best_rpm),
                Math.toRadians((90 - best_angle) + 45));
        for (int angle = 45; angle <= 90; angle++) {
            if (rpm <= 0) {
                rpm = best_rpm;
            }
            boolean fits = false;
            while (!fits && rpm > 0) {
                rpm -= Physics.optimisation_RPM_Resolution;
                line = ReturnArrayOfPos(RPMToVelcoity(rpm), Math.toRadians((90 - angle) + 45));
                fits = fits_in_hub(line);
            }
            if (fits_in_hub(line)) {
                best_rpm = rpm;
                best_angle = (90 - angle) + 45;
            }
        }
        
        return new double[] { best_rpm, best_angle };
    }

    public static void main(String[] args) {
        Globals.hub_distance = 0.87; // TODO: WAS 7
        long startTime = System.currentTimeMillis();
        // Run some code
        double[] a = optimize();
        long stopTime = System.currentTimeMillis();
        System.out.println("Elapsed time was " + (stopTime - startTime) + " miliseconds.");
        System.out.println(">>>>= " + a[0] + " " + a[1]);

    }
}