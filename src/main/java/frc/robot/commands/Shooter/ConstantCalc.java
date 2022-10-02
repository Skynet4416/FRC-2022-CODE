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

    public ConstantCalc(Ball ball, ShooterSubsystem shooterSubsystem, boolean end) {
        this.addRequirements(shooterSubsystem);
        this.ball = ball;
        this.target = new Target(new Vector(4.0, 2.3 - 0.72, 0.0), new Vector(1.22 -
                ball.get_target_threshold(), 0.05,
                1.0), 90, 45, 999, -999);
        this.end = end;
    }

    @Override
    public void initialize() {
        boolean front = false;
        if (Globals.front != null && Globals.front.getLatestResult().hasTargets()) {
            front = true;

        } 
        else if (Globals.back != null && Globals.back.getLatestResult().hasTargets()) {
            front = false;
        }
        if (Globals.hub_distance > 0 && Globals.hub_distance < 10) {
            target.x_pos = Globals.hub_distance;
            Vector results = shooter_optimiztion.binary_smart_optimize_runge_kutta(ball, target, 45.0,
                    0.0, 5000.0, 1500.0, 12.0);
            Globals.top_rpm = results.getComponent(0);
            Globals.bottom_rpm = results.getComponent(1);
            Globals.angle = !front ? results.getComponent(2) - 45 : results.getComponent(2);
            System.out.println("calc finished");

        } else {
            Globals.top_rpm = 1704;
            Globals.bottom_rpm = 1704;
            Globals.angle = 60;
        }
        System.out.println("TOP RPM " + Globals.top_rpm);
        System.out.println("Bottom RPM " + Globals.bottom_rpm);
    }

    @Override
    public void execute() {
        boolean front = false;
        if (Globals.front != null && Globals.front.getLatestResult().hasTargets()) {
            front = true;

        } 
        else if (Globals.back != null && Globals.back.getLatestResult().hasTargets()) {
            front = false;
        }
        if (Globals.hub_distance > 0 && Globals.hub_distance < 10) {
            System.out.println(true);
            target.x_pos = Globals.hub_distance;
            Vector results = shooter_optimiztion.binary_smart_optimize_runge_kutta(ball, target, 45.0,
                    0.0, 5000.0, 1500.0, 12.0);
            Globals.top_rpm = results.getComponent(0);
            Globals.bottom_rpm = results.getComponent(1);
            Globals.angle = !front ? results.getComponent(2) - 45 : results.getComponent(2);
            System.out.println("calc finished");

        } else {
            Globals.top_rpm = 1704;
            Globals.bottom_rpm = 1704;
            Globals.angle = 60;

        }

    }

    @Override
    public boolean isFinished() {
        return end;
    }

}
