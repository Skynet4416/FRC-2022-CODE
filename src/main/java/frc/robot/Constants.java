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
    public class Inputs{
        public class joysticks{
            public static final double MIN_POWER = 0.15;
        }

    }
    public class Chassis{
        public class Motors{
            public static final int kMasterRight = -1;
            public static final int kSlaveRight = -1;
            public static final int kMasterLeft = -1;
            public static final int kSlaveLeft = -1;
        }
        public class FeedForward
        {
            public static final double kS = 0;
            public static final double kv = 0;
            public static final double ka = 0;

        }
        public class PID
        {
            public static final double kP = 0;
            public static final double kI = 0;
            public static final double kD = 0;
        }
        public class Odometry{
            public static final double START_X = 0;
            public static final double START_Y = 0;
            public static final double START_ANGLE = 0;

            public static final double DISTANCE_OF_ENCODER_COUNT = 0.1524 / 42; // wheel rad / number of encoder counts in a rev (https://www.revrobotics.com/rev-21-1650/)
        }

        public class Physical{
            public static final double Robot_Width = 27.14; // in Inches
            public static final double ratio = 10.75;
            public static final double wheel_size = 6; // inches
        }
    } 
}
