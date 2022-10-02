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
    PhotonCamera camera;
    public TurnToAnglePhoton(ChassisSubsystem chassis)
    {
        this.chassis = chassis;
    }
    @Override
    public void initialize()
    {
        if (Globals.front != null && Globals.front.getLatestResult().hasTargets())
        {
             camera = Globals.front;
        } 
        else if (Globals.back != null && Globals.back.getLatestResult().hasTargets())
        {
            camera = Globals.back;
        } 
        else{
            end(true);
        }
       Globals.joyControlEnbaled =false;
       Globals.joysticksControlEnbaled = false;
    }
    double degrees = 0;

    @Override
    public void execute()
    {

        if(camera.getLatestResult().hasTargets()){
            degrees = VisionMeth.angle_from_target(camera);
            chassis.setArcadeDrive(0,Math.signum(chassis.getRotatPidController().calculate(-degrees,0))*Math.min(0.3,Math.abs(chassis.getRotatPidController().calculate(-degrees,0))));
        }
    }
    public boolean isFinished(){
        if(camera.getLatestResult().hasTargets()){
            chassis.setArcadeDrive(0,Math.signum(chassis.getRotatPidController().calculate(-degrees,0))*Math.min(0.3,Math.abs(chassis.getRotatPidController().calculate(-degrees,0))));
            return Math.abs(degrees) <=  Chassis.turn_to_angle_threashold;
        }
        else{
            return true;
        }
        
    }
    public void end(boolean interrupted)
    {
        Globals.joyControlEnbaled =true;
        Globals.joysticksControlEnbaled = true;
        chassis.setVoltage(0,0);
        System.out.println("Finished AngleTurn!");
    }
}
