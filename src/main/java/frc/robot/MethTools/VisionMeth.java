package frc.robot.MethTools;

import java.util.List;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import frc.robot.Constants.Vision;
import frc.robot.Constants.Shooter.Physics;

public class VisionMeth {
    public static double distanceFromTarget(PhotonCamera camera) throws NullPointerException
    {
        return PhotonUtils.calculateDistanceToTargetMeters(0.7, Physics.hub_height,Math.toRadians(20), Math.toRadians(camera.getLatestResult().getBestTarget().getPitch())) - (Physics.hub_diameter / 3);
    }
    public static double quarticDistance(PhotonCamera camera) throws NullPointerException
    {
        double pitch = camera.getLatestResult().getBestTarget().getPitch();
        return 0.00352092 * Math.pow(pitch, 2) -0.140462 * pitch + 4.32269;
    }
    public static double  angle_from_target(PhotonCamera camera) throws NullPointerException
    {
        return camera.getLatestResult().getBestTarget().getYaw() + 5;
    }
    public static double DistanceFromBall(PhotonCamera camera) throws NullPointerException
    {
        // return PhotonUtils.calculateDistanceToTargetMeters(cameraHeightMeters, Physics.hub_height-Physics.shooter_height, cameraPitchRadians, targetPitchRadians)
        return  PhotonUtils.calculateDistanceToTargetMeters(0.7, Physics.hub_height, 0, Math.toRadians(camera.getLatestResult().getBestTarget().getPitch() - 10));
    }
    public static double angle_from_ball(PhotonCamera camera) throws NullPointerException
    {
        return camera.getLatestResult().getBestTarget().getYaw();
    }
}
