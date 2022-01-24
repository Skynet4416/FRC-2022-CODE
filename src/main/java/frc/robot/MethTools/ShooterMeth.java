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
    public static double[][] ReturnArrayOfPos(double muzzle_velocity, double angle_rad)
    {
        double pos_y = Physics.shooter_height;
        double y_velocity = muzzle_velocity*Math.sin(angle_rad);
        double y_acceleration = 0;
        double pos_x = 0;
        double x_velocity = muzzle_velocity * Math.cos(angle_rad);
        double x_acceleration = 0;
        double sim_duration = ((2*muzzle_velocity*Math.sin(angle_rad))/Physics.gravitational_acceleration_near_earth) + 1;
        int arr_size = (int)Math.ceil(sim_duration/Physics.resolution);
        double[] arrX = new double[arr_size],arrY = new double[arr_size];
        double[] returned_arr = new double[3];
        returned_arr = CalculatePosX(pos_x, x_velocity, x_acceleration);
        pos_x = returned_arr[0];x_velocity = returned_arr[1];x_acceleration=returned_arr[2];
        returned_arr = CalculatePosY(pos_y, y_velocity, y_acceleration);
        pos_y = returned_arr[0];y_velocity = returned_arr[1];y_acceleration=returned_arr[2];
        if(pos_y>0)
        {
            arrX[0] = pos_x;
            arrY[0] = pos_y;
        }
        for(int i=1; i<arr_size;i++)
        {
            returned_arr = CalculatePosX(pos_x, x_velocity, x_acceleration);
            pos_x = returned_arr[0];x_velocity = returned_arr[1];x_acceleration=returned_arr[2];
            returned_arr = CalculatePosY(pos_y, y_velocity, y_acceleration);
            pos_y = returned_arr[0];y_velocity = returned_arr[1];y_acceleration=returned_arr[2];
            if(pos_y>0)
            {
                System.out.print(pos_y);
                System.out.print(pos_x);
                arrX[i] = pos_x;
                arrY[i] = pos_y;
            }
        }
        return new double[][] {arrX,arrY};
    }
    public static double[] FindMaxHight(double RPM, double angle_rad)
    {
        double radius_in_memter = Physics.diamater /2000;
        double velocity = radius_in_memter*2*Math.PI*RPM/60;
        double[][] array_of_positions  = ReturnArrayOfPos(velocity, angle_rad);
        double [] return_arr = new double[] {array_of_positions[0][0],array_of_positions[1][0]};
        for(int i = 0; i<array_of_positions[1].length; i++ )
        {
            if(return_arr[1] > array_of_positions[1][i])
                return return_arr;
            if (return_arr[1] < array_of_positions[1][i])
            {
                return_arr = new double[] {array_of_positions[0][i],array_of_positions[1][i]};
            }
        }
        return return_arr;
    }
    protected static double[] GetArrayOfRPM(int max_rpm)
    {
        double [] arr_of_rpm = new double[max_rpm];
        for(int i = 0; i< max_rpm; i++)
        {
            arr_of_rpm[i] = i;
        }
        return arr_of_rpm;
    }
    public static double binarySearch(double arr[], int first, int last, double key_x,double key_y,double angle) throws Exception{  
        int mid = (first + last)/2;  
        while( first <= last ){  
            double[] max_point = FindMaxHight(arr[mid],angle);

           if (max_point[0] + Physics.threashold_x < key_x && max_point[1] + Physics.threashold_y < key_y ){  
             first = mid + 1;     
           }else if ( Math.abs(max_point[0]-key_x) < Physics.threashold_x && Math.abs(max_point[1]-key_y)< Physics.threashold_y) { 
               return arr[mid];
           }
           else{  
              last = mid - 1;  
           }  
           mid = (first + last)/2;  
        }  
        if ( first > last ){  
            throw new Exception("element not found");
        }
        return -1;
      } 
    public static double FindVelocityForDistance(double x,double y,double angle) throws Exception
    {
        double[] arr = GetArrayOfRPM(Physics.MAX_RPM);
        double result = binarySearch(arr, 0,arr.length-1, x,y, angle);
        return result;

    }
}