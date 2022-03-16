package frc.robot.MethTools;

import java.util.List;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;

import frc.robot.Constants.Vision;

public class VisionMeth {
    public static double distanceFromTarget(PhotonCamera camera)
    {
        return camera.getLatestResult().getBestTarget().getArea()/ Vision.Target.target_size_at_one_meter;
    }
    public static double  angle_from_target(PhotonCamera camera)
    {
        List<PhotonTrackedTarget> targets = camera.getLatestResult().targets;
        double angles_sum=0;
        double angles_absulute_sum = 0;
        for (PhotonTrackedTarget target : targets) {
            angles_sum += target.getYaw();
            angles_absulute_sum += Math.abs(target.getYaw());
        }
        return angles_sum > 0 ? angles_absulute_sum / targets.size() : -angles_absulute_sum/targets.size();
    }
    public static double DistanceFromBall(PhotonCamera camera)
    {
        return  camera.getLatestResult().getBestTarget().getArea() / Vision.Ball.ball_size_at_one_meter;
    }
    public static double angle_from_ball(PhotonCamera camera)
    {
        return camera.getLatestResult().getBestTarget().getYaw();
    }
}
