package frc.robot.MethTools;

import frc.robot.Constants.Shooter.Physics;

public class ShooterMeth {
    public static double[] CalculatePosY(double current_y_pos, double current_velocity_y,
            double current_acceleration_y) {
        double force_y;
        if (current_velocity_y > 0) {
            force_y = -(Physics.ball_mass * Physics.gravitational_acceleration_near_earth
                    - Physics.drag_thing * Math.pow(current_velocity_y, 2));
        } else {
            force_y = -(Physics.ball_mass * Physics.gravitational_acceleration_near_earth
                    + Physics.drag_thing * Math.pow(current_velocity_y, 2));
        }
        double acceleration_y = force_y / Physics.ball_mass; // use Newton's second law to calculate acceleration
        double velocity_y = current_velocity_y + (acceleration_y + current_acceleration_y) * Physics.resolution;
        double y_pos = current_y_pos + velocity_y * Physics.resolution;
        return new double[] { y_pos, velocity_y, acceleration_y };
    }

    public static double[] CalculatePosX(double current_x_pos, double current_velocity_x,
            double current_acceleration_x) {
        double force_x = -Physics.drag_thing * Math.pow(current_velocity_x, 2);
        double acceleration_x = force_x / Physics.ball_mass;
        double velocity_x = current_velocity_x + (acceleration_x + current_acceleration_x) * Physics.resolution;
        double x_pos = current_x_pos + velocity_x * Physics.resolution;
        return new double[] { x_pos, velocity_x, acceleration_x };
    }

    public static double[][] ReturnArrayOfPos(double muzzle_velocity, double angle_rad) {
        double pos_y = Physics.shooter_height;
        double y_velocity = muzzle_velocity * Math.sin(angle_rad);
        double y_acceleration = 0;
        double pos_x = 0;
        double x_velocity = muzzle_velocity * Math.cos(angle_rad);
        double x_acceleration = 0;
        double sim_duration = ((2 * muzzle_velocity * Math.sin(angle_rad))
                / Physics.gravitational_acceleration_near_earth) + 1;
        int arr_size = (int) Math.ceil(sim_duration / Physics.resolution);
        double[] arrX = new double[arr_size], arrY = new double[arr_size];
        double[] returned_arr = new double[3];
        returned_arr = CalculatePosX(pos_x, x_velocity, x_acceleration);
        pos_x = returned_arr[0];
        x_velocity = returned_arr[1];
        x_acceleration = returned_arr[2];
        returned_arr = CalculatePosY(pos_y, y_velocity, y_acceleration);
        pos_y = returned_arr[0];
        y_velocity = returned_arr[1];
        y_acceleration = returned_arr[2];
        if (pos_y > 0) {
            arrX[0] = pos_x;
            arrY[0] = pos_y;
        }
        for (int i = 1; i < arr_size; i++) {
            returned_arr = CalculatePosX(pos_x, x_velocity, x_acceleration);
            pos_x = returned_arr[0];
            x_velocity = returned_arr[1];
            x_acceleration = returned_arr[2];
            returned_arr = CalculatePosY(pos_y, y_velocity, y_acceleration);
            pos_y = returned_arr[0];
            y_velocity = returned_arr[1];
            y_acceleration = returned_arr[2];
            if (pos_y > 0) {
                System.out.print(pos_y);
                System.out.print(pos_x);
                arrX[i] = pos_x;
                arrY[i] = pos_y;
            }
        }
        return new double[][] { arrX, arrY };
    }
    public static double RPMToVelcoity(double rpm)
    {
        double rps  = (rpm*(1-Physics.RPM_presentange_loss))/60;
        double circumference = (Physics.diamater/1000)*Math.PI;
        double velocity = rps * circumference;
        return velocity;
    }
    public static double[] FindMaxHight(double RPM, double angle_rad) // O(n)
    {
        double radius_in_memter = Physics.diamater / 2000;
        double velocity = radius_in_memter * 2 * Math.PI * RPM / 60;
        double[][] array_of_positions = ReturnArrayOfPos(velocity, angle_rad);
        double[] return_arr = new double[] { array_of_positions[0][0], array_of_positions[1][0] };
        for (int i = 0; i < array_of_positions[1].length; i++) {
            if (return_arr[1] > array_of_positions[1][i])
                return return_arr;
            if (return_arr[1] < array_of_positions[1][i]) {
                return_arr = new double[] { array_of_positions[0][i], array_of_positions[1][i] };
            }
        }
        return return_arr;
    }

    protected static double[] GetArrayOfRPM(int max_rpm)// O(n)
    {
        double[] arr_of_rpm = new double[max_rpm];
        for (int i = 0; i < max_rpm; i++) {
            arr_of_rpm[i] = i;
        }
        return arr_of_rpm;
    }
    
    public static boolean fits_in_hub(double[][] trajectory, double hub_distance)
    {
        Point lastPoint = new Point();
        Point currentPoint = new Point();
        boolean prevfit = false;
        for(int i = 0; i<trajectory[0].length; i++)
        {
            lastPoint.x = currentPoint.x;
            lastPoint.y = currentPoint.y;
            currentPoint.x = trajectory[i][0];
            currentPoint.y = trajectory[i][1];
            if (currentPoint.x > hub_distance + Physics.hub_diameter /2 && currentPoint.y>Physics.hub_height)
                return false;
            else if (currentPoint.x > Physics.hub_height + Physics.threashold + Physics.diamater && lastPoint.y > (Physics.hub_height + Physics.threashold + Physics.diamater) && currentPoint.x > (hub_distance - Physics.hub_diameter/2) && currentPoint.x < hub_distance + Physics.hub_diameter && lastPoint.x > hub_distance-Physics.hub_diameter/2)
            {
                return false;
            }
        }
        return false;        

    }   
    public static double[] optimize(double hub_distance)
    {
        boolean done = false;
        double rpm = Physics.MAX_RPM *(1-Physics.RPM_presentange_loss);
        double best_rpm = Physics.MAX_RPM*(1-Physics.RPM_presentange_loss);
        double best_angle = -1;
        double[][] line;
        for(int angle = 45; angle<90; angle++)
        {
            rpm = best_rpm;
            line = ReturnArrayOfPos(rpm, Math.toRadians(90-angle+45));
            boolean prevfit = false;
            while (rpm - Physics.optimisation_RPM_Resolution > 0)
            {
                if( fits_in_hub(line, hub_distance))
                {
                    prevfit = true;
                    best_rpm = rpm;
                    best_angle = (90-angle + 45);
                }
                else{
                    if(prevfit)
                    {
                        rpm = 0;
                    }
                }
                rpm -= Physics.optimisation_RPM_Resolution;
                line = ReturnArrayOfPos(rpm, Math.toRadians(90-angle+45));
            }
            line = ReturnArrayOfPos(best_rpm, Math.toRadians(best_angle));
        }
        line = ReturnArrayOfPos(best_rpm, Math.toRadians(best_angle));
        return new double[] {best_rpm,best_angle};
    }
}