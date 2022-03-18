package frc.robot.commands.Shooter;

import org.photonvision.PhotonCamera;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Globals;
import frc.robot.Constants.Shooter.Physics;
import frc.robot.MethTools.ShooterMeth;
import frc.robot.MethTools.VisionMeth;

public class CalcCommand extends CommandBase{
    @Override
    public void end(boolean i_dont_give_a_shit)
    {
        boolean front = true;
        try{
        Globals.hub_distance = VisionMeth.distanceFromTarget(new PhotonCamera("Front"));
        }
        catch(Exception e){
            Globals.hub_distance = VisionMeth.distanceFromTarget(new PhotonCamera("Back"));
            front = false;
        }
        double [] x = ShooterMeth.optimize();
        Globals.RPM = x[0];
        Globals.angle = front?x[1]-45:x[1];
        System.out.println("calc finished");
                // SmartDashboard.putNumber("Distance", Math.sqrt(Math.pow(VisionMeth.distanceFromTarget(new PhotonCamera("Front")),2) - Math.pow(Physics.hub_height - Physics.shooter_height,2)));

    }
    @Override
    public boolean isFinished()
    {
        return true;
    }

}
