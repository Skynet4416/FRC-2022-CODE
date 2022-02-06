// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static class Shooter{
        public static class Physics{
            public static final int MAX_RPM = 6000;
            public static double diamater = 101.6; //in mm
            public static double gravitational_acceleration_near_earth = 9.83; //in m/s^2
            public static double ball_mass =  0.267619498; //in kg 
            public static double density_of_air = 1.293;//in kg/m^3
            public static double drag_coefficient = 0.47; // no units
            public static double RPM_presentange_loss = 0.0;
            public static double circumference = (diamater/1000) * Math.PI;
            public static double cross_area = Math.PI * Math.pow((diamater/2)/1000,2); //m^2
            public static double drag_thing = (density_of_air * drag_coefficient * cross_area)/2; // kg/m
            public static double resolution = 0.1;//in s 
            public static double shooter_height = 0; // in meter
            public static double threashold_x = 0;//in meter
            public static double threashold_y = 0;//in meter
            public static double hub_distance = 0; // in meter
            public static double hub_diameter = 0; // in meter
            public static double hub_height = 0; // in meter
            public static double threashold = 0; // in meter
            public static double optimisation_RPM_Resolution = 100; // in RPM
        }
    }
}
