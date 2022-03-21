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
       Globals.joyControlEnbaled =false;
       Globals.joysticksControlEnbaled = false;
    }
    @Override
    public void execute()
    {   double degrees = 0;
        try{
         degrees = VisionMeth.angle_from_target(new PhotonCamera("Front"));
        }
        catch(Exception e){
            try{
                degrees = VisionMeth.angle_from_target(new PhotonCamera("Back"));
            }
            catch(Exception i_dont_give_a_shit)
            {
                degrees = 0;
            }
        }
        chassis.setArcadeDrive(0,Math.signum(chassis.getRotatPidController().calculate(-degrees,0))*Math.min(0.3,Math.abs(chassis.getRotatPidController().calculate(-degrees,0))));
    }
    public boolean isFinished(){
        return Math.abs(VisionMeth.angle_from_target(new PhotonCamera("Front"))) <=  Chassis.turn_to_angle_threashold;
    }
    public void end(boolean interrupted)
    {
        Globals.joyControlEnbaled =true;
        Globals.joysticksControlEnbaled = true;
        chassis.setArcadeDrive(0,0);
    }
}
