package frc.robot.MethTools;

import java.util.List;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Constants.Vision;
import frc.robot.Constants.Shooter.Physics;

public class VisionMeth {
    public static double distanceFromTarget(PhotonPipelineResult pipelineResult) throws NullPointerException
    {
        return PhotonUtils.calculateDistanceToTargetMeters(0.7, Physics.hub_height,Math.toRadians(20), Math.toRadians(pipelineResult.getBestTarget().getYaw())) - (Physics.hub_diameter / 3);
    }
    public static double quarticDistance(PhotonPipelineResult pipelineResult) throws NullPointerException
    {
        if (pipelineResult.hasTargets())
        {
            double pitch = (pipelineResult.getBestTarget().getPitch());
            SmartDashboard.putNumber("Camera Pitch", pitch);
            // return 0.00400843 * Math.pow(pitch, 2) -0.159914 * pitch + 4.21172 + (Physics.hub_diameter / 5);

            double real_height = Physics.hub_height; double camera_height = 0.72;
            // double distance = (real_height - camera_height) / Math.tan(Math.toRadians(20-pitch)); 
            double distance = PhotonUtils.calculateDistanceToTargetMeters(camera_height, real_height, Math.toRadians(29.7546), Math.toRadians(pitch)); 
            SmartDashboard.putNumber("Target Distance", distance);
            return distance;
            // return (2.3 - 0.72) / Math.tan(Math.toRadians(pitch + 20 + Constants.CAMERA_OFFSET)) - (Physics.hub_diameter / 2);
        }
        return 0;
    }
    public static double  angle_from_target(PhotonPipelineResult pipelineResult) throws NullPointerException
    {
        double sum=0;
        double avg =0;
        double result = 0;
        if (pipelineResult.hasTargets())
        {
            for(PhotonTrackedTarget target: pipelineResult.targets)
            {
                sum += target.getYaw();
                avg +=Math.abs(target.getYaw());
                result = target.getYaw();
            }
            // System.out.println(avg/camera.getLatestResult().targets.size());
            // return Math.signum(sum)*avg/camera.getLatestResult().targets.size() +15;
            return result;
            // return camera.getLatestResult().getBestTarget().getPitch();
        }
        return 0;
    }
    public static double DistanceFromBall(PhotonCamera camera) throws NullPointerException
    {
        // return PhotonUtils.calculateDistanceToTargetMeters(cameraHeightMeters, Physics.hub_height-Physics.shooter_height, cameraPitchRadians, targetYawRadians)
        return  PhotonUtils.calculateDistanceToTargetMeters(0.7, Physics.hub_height, 0, Math.toRadians(camera.getLatestResult().getBestTarget().getYaw() - 10));
    }
    public static double angle_from_ball(PhotonCamera camera) throws NullPointerException
    {
        return camera.getLatestResult().getBestTarget().getPitch();
    }
}
