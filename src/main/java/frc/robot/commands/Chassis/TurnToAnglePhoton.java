package frc.robot.commands.Chassis;

import org.photonvision.PhotonCamera;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Globals;
import frc.robot.Constants.Chassis;
import frc.robot.MethTools.VisionMeth;
import frc.robot.subsystems.ChassisSubsystem;

public class TurnToAnglePhoton extends CommandBase{
    private ChassisSubsystem chassis;
    public TurnToAnglePhoton(ChassisSubsystem chassis)
    {
        this.chassis = chassis;
    }
    @Override
    public void initialize()
    {
       
    }
    @Override
    public void execute()
    {   
        double degrees = VisionMeth.angle_from_target(new PhotonCamera("Front"));
        chassis.setArcadeDrive(0,Math.signum(chassis.getRotatPidController().calculate(-degrees,0))*Math.min(0.225,Math.abs(chassis.getRotatPidController().calculate(-degrees,0))));
    }
    public boolean isFinished(){
        return Math.abs(VisionMeth.angle_from_target(new PhotonCamera("Front"))) <=  Chassis.turn_to_angle_threashold;
    }
    public void end(boolean interrupted)
    {
        chassis.setArcadeDrive(0,0);
    }
}
