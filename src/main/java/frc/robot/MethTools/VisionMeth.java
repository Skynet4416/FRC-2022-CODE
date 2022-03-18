package frc.robot.MethTools;

import java.util.List;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonTrackedTarget;

import frc.robot.Constants.Vision;
import frc.robot.Constants.Shooter.Physics;

public class VisionMeth {
    public static double distanceFromTarget(PhotonCamera camera)
    {
        return Vision.Target.target_size_at_one_meter/camera.getLatestResult().getBestTarget().getArea();
    }
    public static double  angle_from_target(PhotonCamera camera)
    {
        return camera.getLatestResult().getBestTarget().getYaw();
    }
    public static double DistanceFromBall(PhotonCamera camera)
    {
        // return PhotonUtils.calculateDistanceToTargetMeters(cameraHeightMeters, Physics.hub_height-Physics.shooter_height, cameraPitchRadians, targetPitchRadians)
        return  camera.getLatestResult().getBestTarget().getArea() / Vision.Ball.ball_size_at_one_meter;
    }
    public static double angle_from_ball(PhotonCamera camera)
    {
        return camera.getLatestResult().getBestTarget().getYaw();
    }
}
