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
    System.out.println(
    "WAAAAAAAAAAAAAAAAAAAa");

       Globals.joyControlEnbaled =false;
       Globals.joysticksControlEnbaled = false;
    }
    @Override
    public void execute()
    {
        System.out.println("Finished AngleTurn!");
        double degrees = 0;
        PhotonCamera camera = new PhotonCamera("Front");
        if(camera.getLatestResult().hasTargets()){
            degrees = VisionMeth.angle_from_target(new PhotonCamera("Front"));
            chassis.setArcadeDrive(0,Math.signum(chassis.getRotatPidController().calculate(-degrees,0))*Math.min(0.3,Math.abs(chassis.getRotatPidController().calculate(-degrees,0))));
        }
    }
    public boolean isFinished(){
        double degrees = 0;
        System.out.println("WWWWWWWWWWWWWW");
        PhotonCamera camera = new PhotonCamera("Front");
        if(camera.getLatestResult().hasTargets()){
            System.out.println("AAAAAAAA");
            degrees = VisionMeth.angle_from_target(new PhotonCamera("Front"));
            System.out.println("BBBBBBBBBBBB");
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
        chassis.setArcadeDrive(0,0);
        System.out.println("Finished AngleTurn!");
    }
}
