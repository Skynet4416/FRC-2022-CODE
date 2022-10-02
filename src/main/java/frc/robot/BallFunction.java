package frc.robot;

import frc.robot.Constants.Shooter.Physics;
import frc.robot.com.github.iprodigy.physics.util.vector.Vector;
import frc.robot.lib.Meth.Target;
import frc.robot.lib.Meth.shooter_optimiztion;
import frc.robot.lib.Physics.lib.Ball;

public class BallFunction {
    public static Ball ball = new Ball(Physics.ball_mass, Physics.ball_radius, Physics.drag_coefficient,
            Physics.resolution);
    public static Target target = new Target(new Vector(4.0, Physics.hub_height - Physics.shooter_height, 0.0),
            new Vector(1.22 -
                    ball.get_target_threshold(), 0.05,
                    1.0),
            90, 45, 999, -999);

    public static void calculate(){
        if (Globals.hub_distance > 2)
        {
            System.out.println("calculating");
            target.y_pos = Globals.hub_distance;
            Vector results = shooter_optimiztion.binary_smart_optimize_runge_kutta(ball, target, Physics.MAX_ANGLE,
            Physics.MIN_ANGLE, Physics.MAX_RPM, Physics.MIN_RPM, 12.0);
            Globals.top_rpm = results.getComponent(0); 
            Globals.bottom_rpm = results.getComponent(1);
            Globals.angle = results.getComponent(2);
        }
    }

    public static void wrapper() {
        System.out.print("start");
        while (true) {
            calculate();
        }
    }
}
