package frc.robot;

import org.photonvision.PhotonCamera;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;

public class Globals {
    public static boolean joyControlEnbaled = true;
    public static Pose2d startPos = new Pose2d(7.819,2.180,new Rotation2d(0));
    public static boolean joysticksControlEnbaled = true;
    public static double hub_distance;
    public static double angle;
    public static double RPM;
    public static PhotonCamera front;
    public static PhotonCamera back;
}
