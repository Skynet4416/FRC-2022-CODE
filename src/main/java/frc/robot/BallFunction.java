package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.Shooter.Physics;
import frc.robot.com.github.iprodigy.physics.util.vector.Vector;
import frc.robot.lib.Meth.Target;
import frc.robot.lib.Meth.shooter_optimiztion;
import frc.robot.lib.Physics.lib.Ball;

public class BallFunction extends Thread {
    private static Ball ball = new Ball(Physics.ball_mass, Physics.ball_radius, Physics.drag_coefficient,
            Physics.resolution);
    private static Target target = new Target(new Vector(4.0, Physics.hub_height - Physics.shooter_height, 0.0),
            new Vector(1.22 -
                    ball.get_target_threshold(), 0.05,
                    1.0),
            90, 45, 999, -999);

    @Override
    public void run() {
        while (true) {
            System.out.println("START");
            System.out.println(Globals.hub_distance);
            if (Globals.hub_distance > 0) {
                System.out.println(Globals.hub_distance);
                System.out.println("Bigger then 0");

                target.y_pos = Globals.hub_distance;
                Vector results = shooter_optimiztion.binary_smart_optimize_runge_kutta(ball, target, Physics.MAX_ANGLE,
                        Physics.MIN_ANGLE, Physics.MAX_RPM, Physics.MIN_RPM, 12.0);
                Globals.top_rpm = results.getComponent(0);
                Globals.bottom_rpm = results.getComponent(1);
                Globals.angle = results.getComponent(2) + 45;
                SmartDashboard.putNumber("Wanted TOPRPM", Globals.top_rpm);
                SmartDashboard.putNumber("Wanted BOTTOMRPM", Globals.bottom_rpm);
                SmartDashboard.putNumber("Wanted ANGLE", Globals.angle);
            } else {
                System.out.println(Globals.hub_distance);
                System.out.println("Less then 0");

                target.y_pos = 0.8382 + 0.97 / 2;
                System.out.println("Less then 0");
                Vector results = shooter_optimiztion.binary_smart_optimize_runge_kutta(ball, target, Physics.MAX_ANGLE,
                        Physics.MIN_ANGLE, Physics.MAX_RPM, Physics.MIN_RPM, 12.0);
                Globals.top_rpm = results.getComponent(0);
                Globals.bottom_rpm = results.getComponent(1);
                Globals.angle = results.getComponent(2) + 45;
                System.out.println("Smart");
                SmartDashboard.putNumber("Wanted TOPRPM", Globals.top_rpm);
                SmartDashboard.putNumber("Wanted BOTTOMRPM", Globals.bottom_rpm);
                SmartDashboard.putNumber("Wanted ANGLE", Globals.angle);
                System.out.println("Finished Smart");

            }
        }
    }
}
