// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static class Inputs {
        public class joysticks {
            public static final double MIN_POWER = 0.15;
            public static final int LEFT_JOY_PORT = 0;
            public static final int RIGHT_JOY_PORT = 1;
        }

    }

    public static class Field {
        public static class Waypoints {
            public static class BlueAllince {
                public static class Balls {
                    public static final Pose2d red_hangar = new Pose2d(Units.inchesToMeters(-88.303),
                            Units.inchesToMeters(124.946), new Rotation2d(0));
                    public static final Pose2d blue_hangar = new Pose2d(Units.inchesToMeters(-121.396),
                            Units.inchesToMeters(81.643), new Rotation2d(0));
                    public static final Pose2d red_terminal = new Pose2d(Units.inchesToMeters(-146.227),
                            Units.inchesToMeters(-33.767), new Rotation2d(0));
                    public static final Pose2d blue_terminal = new Pose2d(Units.inchesToMeters(-124.946),
                            Units.inchesToMeters(-88.303), new Rotation2d(0));
                    public static final Pose2d blue_wall = new Pose2d(Units.inchesToMeters(-25.91),
                            Units.inchesToMeters(-150.79), new Rotation2d(0));
                    public static final Pose2d red_wall = new Pose2d(Units.inchesToMeters(33.767),
                            Units.inchesToMeters(-149.767), new Rotation2d(0));
                    public static final Pose2d blue_on_terminal = new Pose2d(Units.inchesToMeters(-282.08),
                            Units.inchesToMeters(-117.725), new Rotation2d(0));
                }

                public static final Pose2d safe_zone = new Pose2d(Units.inchesToMeters(-191.01), Units.inchesToMeters(53.74),
                        new Rotation2d(0));
                public static final Pose2d safe_zone_wall = new Pose2d(Units.inchesToMeters(-197.26),
                        Units.inchesToMeters(150.26), new Rotation2d(0));
            }

            public static class RedAllince {
                public static class Balls {
                    public static final Pose2d blue_hangar = new Pose2d(Units.inchesToMeters(-88.303),
                            Units.inchesToMeters(-124.946), new Rotation2d(0));
                    public static  final Pose2d red_hangar = new Pose2d(Units.inchesToMeters(-129.396),
                            Units.inchesToMeters(-81.643), new Rotation2d(0));
                    public static final Pose2d blue_terminal = new Pose2d(Units.inchesToMeters(149.227),
                            Units.inchesToMeters(33.767), new Rotation2d(0));
                    public static final Pose2d red_terminal = new Pose2d(Units.inchesToMeters(124.946),
                            Units.inchesToMeters(88.303), new Rotation2d(0));
                    public static final Pose2d red_wall = new Pose2d(Units.inchesToMeters(25.91), Units.inchesToMeters(150.79),
                            new Rotation2d(0));
                    public static final Pose2d blue_wall = new Pose2d(Units.inchesToMeters(-33.767),
                            Units.inchesToMeters(149.767), new Rotation2d(0));
                    public static  final Pose2d red_on_terminal = new Pose2d(Units.inchesToMeters(282.08),
                            Units.inchesToMeters(117.725), new Rotation2d(0));
                }

                public final Pose2d safe_zone = new Pose2d(Units.inchesToMeters(191.01), Units.inchesToMeters(-53.74),
                        new Rotation2d(0));
                public final Pose2d safe_zone_wall = new Pose2d(Units.inchesToMeters(197.26),
                        Units.inchesToMeters(-150.24), new Rotation2d(0));
            }
        }
    }

    public static class Chassis {
        public static final class SmartDashboard{

            public final static String TurnAnglePointAx = "Turn Angle Point Ax";
            public  final static String TurnAnglePointAy = "Turn Angle Point Ay";
            public  final static String TurnAnglePointBx = "Turn Angle Point Bx";
            public  final static String TurnAnglePointBy = "Turn Angle Point By";
            public  final static String TurnAnglePointCx = "Turn Angle Point Cx";
            public  final static String TurnAnglePointCy = "Turn Angle Point Cy";
            public  final static String TurnAnglePointDx = "Turn Angle Point Dx";
            public  final static String TurnAnglePointDy = "Turn Angle Point Dy";
            public  final static String TurnAngleThreshold = "Turn Angle Theshold";
            public final static String kP = "Turn Angle KP";            
        }
        public static class TurnToAngleConstants{

            public static final double kP =0;
            public static final  double kPmin = 0;
            public static final double kPmax = 0;
        }
        public class Motors {
            public static final int kMasterRight = 10;
            public static final int kSlaveRight = 11;
            public static final int kMasterLeft = 12;
            public static final int kSlaveLeft = 13;
        }

        public class FeedForward {
            public static final double kS = 0;
            public static final double kv = 0;
            public static final double ka = 0;

        }

        public class PID {
            public static final double kP = 0;
            public static final double kI = 0;
            public static final double kD = 0;
        }

        public class Odometry {
            public static final double START_X = 0;
            public static final double START_Y = 0;
            public static final double START_ANGLE = 0;
            public static final double max_velocity = 0; // m/s
            public static final double max_acceleration = 0; // m/s^2
            public static final double DISTANCE_OF_ENCODER_COUNT = 0.1524 / 42; // wheel rad / number of encoder counts
                                                                                // in a rev
                                                                                // (https://www.revrobotics.com/rev-21-1650/)
        }

        public class Physical {
            public static final double Robot_Width = 27.14; // in Inches
            public static final double ratio = 10.75;
            public static final double wheel_radius = 3; // inches
        }
    }

    public class Intake {
        public class Motors {
            public static final int kIntake = 20;
            public static final double PowerPercentage = 1;
        }
    }

    public static class Shooter {
        public static class Physics {
            public static final int MAX_RPM = 6000;
            public static double diamater = 101.6; // in mm
            public static double gravitational_acceleration_near_earth = 9.83; // in m/s^2
            public static double ball_mass = 0.267619498; // in kg
            public static double temperture = 25; // in celsius
            public static double density_of_air = -0.0046 * temperture + 1.2978;// in kg/m^3
            public static double drag_coefficient = 0.47; // no units
            public static double RPM_presentange_loss = 0.0;
            public static double optimisation_RPM_Resolution = 100; // in RPM
            public static double circumference = (diamater / 1000) * Math.PI;
            public static double cross_area = Math.PI * Math.pow((diamater / 2) / 1000, 2); // m^2
            public static double drag_thing = (density_of_air * drag_coefficient * cross_area) / 2; // kg/m
            public static double resolution = 0.1;// in s
            public static double shooter_height = 0; // in meter
            public static double threashold_x = 0;// in meter
            public static double threashold_y = 0;// in meter
            public static double hub_diameter = Units.inchesToMeters(25); // in meter
            public static double hub_height = Units.inchesToMeters(104.02); // in meter            
            public static final double precentage = 1;

            public final class Motors {
                public static final int Kmaster = 50;
                public static final int Kslave = 51;
                public static final int left_master =  41;
                public static final int right_slave = 40;
                public static final double Min = 20;
                public static final double Max = 70;
                public static final double AngleThreashold = 0.25;
                public static final double kEndDeg = 180;
                public static final double kStartDeg = 0;
                public static final double AnglePrecentage = 0;

                
            }
            public static final double left_home = 326;
            public static final double right_home = 166.8;
    
            public final class PID {
                // needs to be set to real values
                public static final double kP = 0;
                public static final double kI = 0;
                public static final double kD = 0;
                public static final double kF = 0;
                public static final double MAX_RPM = 0;
            }
    
            public final class SmartDashboard {
                public static final String RightAngle = "Right Angle";
                public static final String LeftAngle = "Left Angle";
                public static final String AngleToSet = "Angle To Set";
    
                public static final String ShooterKP = "Shooter kP";
                public static final String ShooterKI = "Shooter kI";
                public static final String ShooterKD = "Shooter kD";
                public static final String ShooterKF = "Shooter kF";
            }
        }
    }


    public static final class Indexing {
        public static final class Motors {
            public static final int Kmaster = 30;
            public static final int Kslave = 31;
        }

        public static final double kIndexingPercent = 1; // needs to be set to real power percentage
    }
    
    public class Elevator{
        public class Motors{

            public static final int Kmaster = 4;
            public static final int Kslave = 5;
            public static final double elevator_precentage = 0.4;
            public static final int KangleMotor = 6;
            public static final double angle_move_precentage = 1;
            public static final int Khook = 7;
            public static final double hook_precentage = 0.2;
            
        }
        public class Angle{
            public class PID{
                public static final double Kf = 0;
                public static final double Kd = 0;
                public static final double Kp = 0;
                public static final double Ki = 0;
            }
        }
        public class UpAndDown{
            public class PID{
                public static final double Kf = 0;
                public static final double Kd = 0;
                public static final double Kp = 0;
                public static final double Ki = 0;
            }
        }
        public class Encoders
        {
            public static final int ChannelA  = -1;
            public static final int ChannelB = -1;
        }
        public class SmartDashboard
        {
            public class UpAndDown{
                public static final String Kd = "Elevator Kd";
                public static final String Kf = "Elevator Kf";
                public static final String Ki = "Elevator Ki";
                public static final String Kp = "Elevator Kp";
            }
            public static final String CurrentAngle = "Climbing Current Angle";
        }
    }
}
