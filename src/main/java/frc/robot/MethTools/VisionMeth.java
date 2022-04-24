package frc.robot.MethTools;

import java.util.List;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Constants.Vision;
import frc.robot.Constants.Shooter.Physics;

public class VisionMeth {
    public static double distanceFromTarget(PhotonCamera camera) throws NullPointerException
    {
        return PhotonUtils.calculateDistanceToTargetMeters(0.7, Physics.hub_height,Math.toRadians(20), Math.toRadians(camera.getLatestResult().getBestTarget().getPitch())) - (Physics.hub_diameter / 3);
    }
    public static double quarticDistance(PhotonCamera camera) throws NullPointerException
    {
        if (camera.getLatestResult().hasTargets())
        {
            double pitch = camera.getLatestResult().getBestTarget().getPitch();
            SmartDashboard.putNumber("Camera Pitch", pitch);
            // return 0.00400843 * Math.pow(pitch, 2) -0.159914 * pitch + 4.21172 + (Physics.hub_diameter / 5);
            return (2.64 - 0.72) / Math.tan(Math.toRadians(pitch + 20 + Constants.CAMERA_OFFSET)) - (Physics.hub_diameter / 2);
        }
        return 0;
    }
    public static double  angle_from_target(PhotonCamera camera) throws NullPointerException
    {
        double sum=0;
        double avg =0;
        if (camera.getLatestResult().hasTargets())
        {
            for(PhotonTrackedTarget target: camera.getLatestResult().targets)
            {
                sum += target.getYaw();
                avg +=Math.abs(target.getYaw());
            }
            System.out.print(avg/camera.getLatestResult().targets.size());
            return Math.signum(sum)*avg/camera.getLatestResult().targets.size() +15;
            // return camera.getLatestResult().getBestTarget().getYaw();
        }
        return 0;
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
