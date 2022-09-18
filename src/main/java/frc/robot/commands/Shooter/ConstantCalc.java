package frc.robot.commands.Shooter;

import org.photonvision.PhotonCamera;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Globals;
import frc.robot.Constants.Shooter.Physics;
import frc.robot.MethTools.ShooterMeth;
import frc.robot.MethTools.VisionMeth;
import frc.robot.com.github.iprodigy.physics.util.vector.Vector;
import frc.robot.lib.Meth.Target;
import frc.robot.lib.Meth.shooter_optimiztion;
import frc.robot.lib.Physics.lib.Ball;
import frc.robot.subsystems.ShooterSubsystem;

public class ConstantCalc extends CommandBase {
    private Ball ball;
    private Target target;
    private Boolean end;

    public ConstantCalc(Ball ball,ShooterSubsystem shooterSubsystem,boolean end)
    {
        this.addRequirements(shooterSubsystem);    
        this.ball = ball;
        this.target = new Target(new Vector(4.0, 2.3 - 0.72
        , 0.0), new Vector(1.22 -
        ball.get_target_threshold(), 0.05,
        1.0), 90, 45, 999, -999);
        this.end = end;
    }

    @Override public void initialize() {
        boolean front = true;
        try {
            // Globals.hub_distance = VisionMeth.distanceFromTarget(new
            // PhotonCamera("Front"));
            Globals.hub_distance = VisionMeth.quarticDistance(new PhotonCamera("Front"));
        } catch (Exception e) {
            // Globals.hub_distance = VisionMeth.distanceFromTarget(new
            // PhotonCamera("Back"));
            Globals.hub_distance = VisionMeth.quarticDistance(new PhotonCamera("Back"));

            front = false;
        }
        if (Globals.hub_distance > 0 && Globals.hub_distance < 10) {
            target.x_pos = Globals.hub_distance;
            Vector results = shooter_optimiztion.binary_smart_optimize_runge_kutta(ball, target, 45.0,
            0.0, 5000.0, 1500.0, 12.0);
            Globals.top_rpm = results.getComponent(0); 
            Globals.bottom_rpm = results.getComponent(1);
            Globals.angle = !front ? results.getComponent(2) - 45 :results.getComponent(2);
            System.out.println("calc finished");
            // SmartDashboard.putNumber("Distance",
            // Math.sqrt(Math.pow(VisionMeth.distanceFromTarget(new
            // PhotonCamera("Front")),2) - Math.pow(Physics.hub_height -
            // Physics.shooter_height,2)));
        } else {
            Globals.top_rpm = 1704;
            Globals.bottom_rpm = 1704;
            Globals.angle = 60;

            // Globals.top_rpm = 5000;
            // Globals.bottom_rpm = 5000;
            // Globals.angle = 90;
        }
    System.out.println("TOP RPM " + Globals.top_rpm);
    System.out.println("Bottom RPM " + Globals.bottom_rpm);
    }
    @Override public void execute() {
        if (!end)
        {
            boolean front = true;
        try {
            // Globals.hub_distance = VisionMeth.distanceFromTarget(new
            // PhotonCamera("Front"));
            Globals.hub_distance = VisionMeth.quarticDistance(new PhotonCamera("Front"));
        } catch (Exception e) {
            // Globals.hub_distance = VisionMeth.distanceFromTarget(new
            // PhotonCamera("Back"));
            Globals.hub_distance = VisionMeth.quarticDistance(new PhotonCamera("Back"));

            front = false;
        }
        if (Globals.hub_distance > 0 && Globals.hub_distance < 10) {
            target.x_pos = Globals.hub_distance;
            Vector results = shooter_optimiztion.binary_smart_optimize_runge_kutta(ball, target, 45.0,
            0.0, 5000.0, 1500.0, 12.0);
            Globals.top_rpm = results.getComponent(0); 
            Globals.bottom_rpm = results.getComponent(1);
            Globals.angle = !front ? results.getComponent(2) - 45 :results.getComponent(2);
            System.out.println("calc finished");
            // SmartDashboard.putNumber("Distance",
            // Math.sqrt(Math.pow(VisionMeth.distanceFromTarget(new
            // PhotonCamera("Front")),2) - Math.pow(Physics.hub_height -
            // Physics.shooter_height,2)));
        } else {
            Globals.top_rpm = 1704;
            Globals.bottom_rpm = 1704;
            Globals.angle = 60;

            // Globals.top_rpm = 5000;
            // Globals.bottom_rpm = 5000;
            // Globals.angle = 90;
        }
        }
        

    }
    @Override
    public boolean isFinished()
    {
        return end;
    }
    

}