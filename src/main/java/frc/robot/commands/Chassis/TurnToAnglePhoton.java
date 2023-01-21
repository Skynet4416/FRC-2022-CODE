package frc.robot.commands.Chassis;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Globals;
import frc.robot.Constants.Chassis;
import frc.robot.MethTools.VisionMeth;
import frc.robot.subsystems.ChassisSubsystem;

public class TurnToAnglePhoton extends CommandBase {
    private ChassisSubsystem chassis;
    PhotonCamera camera;

    public TurnToAnglePhoton(ChassisSubsystem chassis) {
        this.chassis = chassis;
    }

    @Override
    public void initialize() {
        if (Globals.front != null && Globals.front.getLatestResult().hasTargets())
        // if(false)
        {
            System.out.println("FRONT");

            camera = Globals.front;
        } else if (Globals.back != null && Globals.back.getLatestResult().hasTargets())
        // else if ( true)
        {
            System.out.println("BACK");
            camera = Globals.back;
        } else {
            System.out.println("BOTH NULL");
            end(true);
        }
        Globals.joyControlEnbaled = false;
        Globals.joysticksControlEnbaled = false;
    }

    double degrees = 0;

    @Override
    public void execute() {

        if (camera!=null) {
            PhotonPipelineResult pipelineResult = camera.getLatestResult();
            if (pipelineResult.hasTargets())
            {
                degrees = VisionMeth.angle_from_target(pipelineResult);
                double max_speed = 0.1;
                double min_speed = 0.08;
                chassis.setArcadeDrive(0,
                        Math.signum(chassis.getRotatPidController().calculate(-degrees, 0)) * Math.max(min_speed,
                                Math.min(max_speed, Math.abs(chassis.getRotatPidController().calculate(-degrees, 0)))));
            }
        }
    }

    public boolean isFinished() {
        if (camera!=null ) {
            PhotonPipelineResult pipelineResult = camera.getLatestResult();

            if (pipelineResult.hasTargets())
            {
                return Math.abs(degrees) <= Chassis.turn_to_angle_threashold;
            }
            else
            {
                return false;
            }

        } else {
            return true;
        }

    }

    public void end(boolean interrupted) {
        Globals.joyControlEnbaled = true;
        Globals.joysticksControlEnbaled = true;
        chassis.setArcadeDrive(0, 0);
        // System.out.println("Finished AngleTurn!");
    }
}
