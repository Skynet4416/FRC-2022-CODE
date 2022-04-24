package frc.robot.commands.Shooter;

import org.photonvision.PhotonCamera;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Globals;
import frc.robot.Constants.Vision;
import frc.robot.Constants.Shooter.Physics;
import frc.robot.MethTools.ShooterMeth;
import frc.robot.MethTools.VisionMeth;
public class CalcCommand extends CommandBase{
    private boolean vision;


    @Override
    public void end(boolean i_dont_give_a_shit)
    {

        boolean front = true;
        try{
        // Globals.hub_distance = VisionMeth.distanceFromTarget(new PhotonCamera("Front"));
        Globals.hub_distance = VisionMeth.quarticDistance(new PhotonCamera("Front"));
        }
        catch(Exception e){
            // Globals.hub_distance = VisionMeth.distanceFromTarget(new PhotonCamera("Back"));
            Globals.hub_distance = VisionMeth.quarticDistance(new PhotonCamera("Back"));

            front = false;
        }
        if(Globals.hub_distance > 0 && Globals.hub_distance < 10){
            System.out.println(Globals.hub_distance);
            double [] x = ShooterMeth.optimize();
            Globals.RPM = x[0];
            Globals.angle = !front?x[1]-45:x[1];
            System.out.println("calc finished");
                    // SmartDashboard.putNumber("Distance", Math.sqrt(Math.pow(VisionMeth.distanceFromTarget(new PhotonCamera("Front")),2) - Math.pow(Physics.hub_height - Physics.shooter_height,2)));
        }
        else{
            System.out.print(1725);
            // Globals.RPM = 1704;
            // Globals.angle = 45+8;

            Globals.RPM = 4000;
            Globals.angle = 48;
        }


    }
    @Override
    public boolean isFinished()
    {
        return true;
    }

}
