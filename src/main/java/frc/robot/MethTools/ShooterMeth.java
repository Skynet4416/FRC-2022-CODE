package frc.robot.MethTools;
import frc.robot.Constants.Shooter.Physics;

public class ShooterMeth {
    public static double[] CalculatePosY(double current_y_pos,double current_velocity_y,double current_acceleration_y)
    {
        double force_y;
        if(current_velocity_y>0)
        {
            force_y = -(Physics.ball_mass *Physics.gravitational_acceleration_near_earth- Physics.drag_thing * Math.pow(current_velocity_y,2));  
        }
        else
        {
            force_y = -(Physics.ball_mass *Physics.gravitational_acceleration_near_earth+ Physics.drag_thing * Math.pow(current_velocity_y,2));  
        } 
        double acceleration_y = force_y/Physics.ball_mass; //use Newton's second law to calculate acceleration
        double velocity_y = current_velocity_y +(acceleration_y+current_acceleration_y)*Physics.resolution;
        double y_pos = current_y_pos + velocity_y*Physics.resolution;
        return new double[] {y_pos,velocity_y,acceleration_y};
    }
    public static double[] CalculatePosX(double current_x_pos, double current_velocity_x, double current_acceleration_x)
    {
        double force_x = -Physics.drag_thing* Math.pow(current_velocity_x,2);
        double acceleration_x = force_x / Physics.ball_mass;
        double velocity_x = current_velocity_x + (acceleration_x + current_acceleration_x)*Physics.resolution;
        double x_pos = current_x_pos + velocity_x*Physics.resolution;
        return new double[] {x_pos,velocity_x,acceleration_x};
    }
    
}
