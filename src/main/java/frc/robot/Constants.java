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
    public final class Shooter
    {
        public final class Motors{
            public static final int  Kmaster = 50;
            public static final int  Kslave = 51;
            public static final int left_Motor = 1;
            public static final int right_Motor = 0;
            public static final double Min = 20;
            public static final double Max = 70;
            public static final double AngleThreashold = 0.25;
        }
        public final class PID
        {
            //needs to be set to real values
            public static final double kP = 0;
            public static final double kI = 0;
            public static final double kD = 0;
            public static final double kF = 0;
            public static final double MAX_RPM = 0;
        }
        public final class SmartDashboard
        {
            public static final String RightServoAngle = "Right Servo Angle";
            public static final String LeftServoAngle = "Left Servo Angle";
            public static final String AngleToSet = "Angle To Set";

            public static final String ShooterKP = "Shooter kP";
            public static final String ShooterKI = "Shooter kI";
            public static final String ShooterKD = "Shooter kD";
            public static final String ShooterKF = "Shooter kF";
        }
    }
    public final class Indexing
    {
        public final class Motors
        {
            public static final int Kmaster = -1;
            public static final int  Kslave = -1;
        }
        public static final double kIndexingPercent = 0; //needs to be set to real power percentage
    }
}
