package frc.robot.lib.Physics.lib;

import java.util.ArrayList;
import java.util.Arrays;
import frc.robot.com.github.iprodigy.physics.util.abstraction.Scalar;
import frc.robot.com.github.iprodigy.physics.util.vector.Vector;

import edu.wpi.first.math.util.Units;

import java.io.FileWriter; // Import the FileWriter class
import java.io.IOException; // Import the IOException class to handle errors
import java.time.Duration;
import java.time.Instant;

import frc.robot.lib.Physics.lib.base.PhysicalObjectBase;
import frc.robot.lib.Physics.lib.base.State;
import pabeles.concurrency.IntOperatorTask.Min;
import frc.robot.lib.Meth.Target;
import frc.robot.lib.Meth.shooter_optimiztion;
import frc.robot.lib.Meth.shooter_optimiztion.OptimizationType;
import frc.robot.lib.Physics.Constants;

public class Ball extends PhysicalObjectBase {
    protected Scalar radius;
    protected Scalar drag_constant;
    protected Scalar magnus_constant;
    protected Scalar cross_section_area_of_ball;
    protected Scalar lift_coeficent;

    public Ball(double mass, double radius, double drag_constant, double estemation_resulotion) {
        this.estemation_resulotion = () -> estemation_resulotion;
        this.mass = () -> mass;
        state = new State();
        this.drag_constant = () -> drag_constant;
        this.radius = () -> radius;
        cross_section_area_of_ball = this.radius.multiply(this.radius).multiply(Math.PI);
        this.moment_of_inertia = () -> mass * radius * radius;
    }

    public State get_state() {
        return this.state;
    }

    public void set_state(State state) {
        this.state = state;
    }

    public double get_radius() {
        return radius.getMagnitude();
    }

    public double get_target_threshold() {
        Double tolerance = 0.1; // IN METERS
        return radius.getMagnitude() * 4 + tolerance;
    }

    public void set_position(Vector position) {
        state.position = position;
    }

    public void set_started_velocity(Vector velocity) {
        state.velocity = velocity;
    }

    public void set_rotational_velocity(Vector rotational_velocity) {
        state.rotational_velocity = rotational_velocity;
    }

    public Vector get_position() {
        return state.position;
    }

    public void calc_magnus_forces() {
        state.sigma_forces = new Vector(0.0, 0.0, 0.0);
        Vector velocity_direction = state.velocity.divide(state.velocity.getMagnitude());
        add_force(Constants.gravitational_acceleration.multiply(mass)); // gravity
        add_force(velocity_direction.multiply(-0.5).multiply(drag_constant).multiply(Constants.density_of_air)
                .multiply(cross_section_area_of_ball).multiply(Math.pow(state.velocity.getMagnitude(), 2))); // drag =
                                                                                                             // -1/2 *
                                                                                                             // Cd * p *
                                                                                                             // A * v^2
                                                                                                             // *v(vector)/abs(v)
        // System.out.println(velocity_direction.multiply(-0.5).multiply(drag_constant).multiply(Constants.density_of_air)
        // .multiply(cross_section_area_of_ball).multiply(Math.pow(state.velocity.getMagnitude(),
        // 2)));
        if (Math.abs(state.rotational_velocity.getMagnitude()) > 0) {
            Vector axis_of_rotation = state.rotational_velocity.divide(state.rotational_velocity.getMagnitude());



            add_force(velocity_direction.crossProduct3D(axis_of_rotation).multiply(0.5).multiply(lift_coeficent)
                    .multiply(Constants.density_of_air).multiply(cross_section_area_of_ball)
                    .multiply(Math.pow(state.velocity.getMagnitude(), 2))); 
                    // magnus = 1/2 cl * p * A * v^2 *
                                                                            // (v(vector)/abd(v) X
                                                                            // rotational_velocity(vector)/abs(rotational_velocity))
            // System.out.println(velocity_direction.crossProduct3D(axis_of_rotation).multiply(0.5).multiply(lift_coeficent)
            // .multiply(Constants.density_of_air).multiply(cross_section_area_of_ball)
            // .multiply(Math.pow(state.velocity.getMagnitude(), 2)));
        }

    }

    public void calc_magnus_forces_nasa() {
        state.sigma_forces = new Vector(0.0, 0.0, 0.0);
        Vector velocity_direction = state.velocity.divide(state.velocity.getMagnitude());
        add_force(Constants.gravitational_acceleration.multiply(mass)); // gravity
        add_force(velocity_direction.multiply(-0.5).multiply(drag_constant).multiply(Constants.density_of_air)
                .multiply(cross_section_area_of_ball).multiply(Math.pow(state.velocity.getMagnitude(), 2))); // drag =
                                                                                                             // -1/2 *
                                                                                                             // Cd * p *
                                                                                                             // A * v^2
                                                                                                             // *v(vector)/abs(v)
        // System.out.println(velocity_direction.multiply(-0.5).multiply(drag_constant).multiply(Constants.density_of_air)
        // .multiply(cross_section_area_of_ball).multiply(Math.pow(state.velocity.getMagnitude(),
        // 2)));
        Vector axis_of_rotation = state.rotational_velocity.divide(state.rotational_velocity.getMagnitude());
        // System.out.println(velocity_direction.crossProduct3D(axis_of_rotation)
        //         .multiply(5.33333333 * Math.PI * Math.PI * Math.pow(radius.getMagnitude(), 3)
        //                 * state.rotational_velocity.getMagnitude() * Constants.density_of_air.getMagnitude()
        //                 * state.velocity.getMagnitude()));

        add_force(velocity_direction.crossProduct3D(axis_of_rotation)
                .multiply(5.33333333 * Math.PI * Math.PI * Math.pow(radius.getMagnitude(), 3)
                        * state.rotational_velocity.getMagnitude() * Constants.density_of_air.getMagnitude()
                        * state.velocity.getMagnitude())); // magnus = 1/2 cl * p * A * v^2 *
        // (v(vector)/abd(v) X
        // rotational_velocity(vector)/abs(rotational_velocity))
        // System.out.println(velocity_direction.crossProduct3D(axis_of_rotation).multiply(0.5).multiply(lift_coeficent)
        // .multiply(Constants.density_of_air).multiply(cross_section_area_of_ball)
        // .multiply(Math.pow(state.velocity.getMagnitude(), 2)));
    }

    public void calc_no_magnusforces() {
        state.sigma_forces = new Vector(0.0, 0.0, 0.0);
        Vector velocity_direction = state.velocity.divide(state.velocity.getMagnitude());
        add_force(Constants.gravitational_acceleration.multiply(mass)); // gravity
        add_force(velocity_direction.multiply(-0.5).multiply(drag_constant).multiply(Constants.density_of_air)
                .multiply(cross_section_area_of_ball).multiply(Math.pow(state.velocity.getMagnitude(), 2))); // drag =
                                                                                                             // -1/2 *
                                                                                                             // Cd * p *
                                                                                                             // A * v^2
                                                                                                             // *v(vector)/abs(v)
        // System.out.println(velocity_direction.multiply(-0.5).multiply(drag_constant).multiply(Constants.density_of_air)
        // .multiply(cross_section_area_of_ball).multiply(Math.pow(state.velocity.getMagnitude(),
        // 2)));
        Vector axis_of_rotation = state.rotational_velocity.divide(state.rotational_velocity.getMagnitude());

        // add_force(velocity_direction.crossProduct3D(axis_of_rotation).multiply(0.5).multiply(lift_coeficent)
        // .multiply(Constants.density_of_air).multiply(cross_section_area_of_ball)
        // .multiply(Math.pow(state.velocity.getMagnitude(), 2))); // magnus = 1/2 cl *
        // p * A * v^2 *
        // (v(vector)/abd(v) X
        // rotational_velocity(vector)/abs(rotational_velocity))
        // System.out.println(velocity_direction.crossProduct3D(axis_of_rotation).multiply(0.5).multiply(lift_coeficent)
        // .multiply(Constants.density_of_air).multiply(cross_section_area_of_ball)
        // .multiply(Math.pow(state.velocity.getMagnitude(), 2)));
    }

    public ArrayList<State> simulate_ball(Boolean print) {
        this.before_before_state = new State(this.state);
        // System.out.println(ball.cross_section_area_of_ball.getMagnitude());
        lift_coeficent = radius.multiply(state.rotational_velocity.getMagnitude())
                .divide(state.velocity.getMagnitude());
        ArrayList<ArrayList<Double>> pos_array = new ArrayList<ArrayList<Double>>();
        pos_array.add(0, new ArrayList<Double>());
        pos_array.add(1, new ArrayList<Double>());
        pos_array.add(2, new ArrayList<Double>());
        pos_array.add(3, new ArrayList<Double>());
        ArrayList<State> states_array = new ArrayList<State>();
        states_array.add(new State(this.state));
        int i = 0;
        while (this.state.position.getComponent(1) > 0) {
            // System.out.println(i + " iteration");
            this.calc_magnus_forces();
            this.update_position(i);
            pos_array.get(0).add(this.state.position.getComponent(0)); // x
            pos_array.get(1).add(this.state.position.getComponent(1)); // y
            pos_array.get(2).add(this.state.position.getComponent(2)); // z
            pos_array.get(3).add(this.state.velocity.getDegree()); // alpha
            states_array.add(new State(this.state));
            i++;
        }

        if (print) {
            // System.out.print("\n\n\nx_array = ");
            // System.out.print(Arrays.deepToString(pos_array.get(0).toArray()));
            // System.out.print("\n\n\ny_array = ");
            // System.out.println(Arrays.deepToString(pos_array.get(1).toArray()));
        }

        return states_array;
    }
    public ArrayList<State> simulate_ball_runge_kutta(Boolean print) {
        ArrayList<ArrayList<Double>> pos_array = new ArrayList<ArrayList<Double>>();
        pos_array.add(0, new ArrayList<Double>());
        pos_array.add(1, new ArrayList<Double>());

        ArrayList<State> states_array = new ArrayList<State>();
        states_array.add(new State(this.state));

        while (this.state.position.getComponent(1) > 0) {
            State before_state = new State(this.state);
            lift_coeficent = radius.multiply(state.rotational_velocity.getMagnitude())
            .divide(state.velocity.getMagnitude());
            this.calc_magnus_forces();
            this.calc();
            this.runge_kutta_aproxemation(before_state);
            pos_array.get(0).add(this.state.position.getComponent(0));
            pos_array.get(1).add(this.state.position.getComponent(1));
            this.before_before_state = new State(before_state);

            states_array.add(new State(this.state));
        }
        if (print) {
            // System.out.print("\n\n\nx_array_runge_kutta = ");
            // System.out.print(Arrays.deepToString(pos_array.get(0).toArray()));
            // System.out.print("\n\n\ny_array_runge_kutta  = ");
            // System.out.println(Arrays.deepToString(pos_array.get(1).toArray()));
        }
        return states_array;
    }

    public static void test_magnus_recursion() {
        Ball ball = new Ball(0.26932047, 0.12065, 0.47, 0.1);
        ball.set_started_velocity(new Vector(19.9366967045, 19.9366967045, 0.0));
        ball.set_rotational_velocity(new Vector(0.0, 0.0, 0.0));
        ball.lift_coeficent = ball.radius.multiply(ball.state.rotational_velocity.getMagnitude())
                .divide(ball.state.velocity.getMagnitude()); // if cd is close to 0.5 then cl = R*rotational_velocity/v
                                                             // which/ is S
        ball.set_position(new Vector(0.0, 0.0000001, 0.0));
        ball.state.save_to_list();
        ball.state.kinematics_varuibales.add(new Vector(0.0, 0.0, 0.0));

        ball.before_before_state = new State(ball.state);
        // System.out.println(ball.cross_section_area_of_ball.getMagnitude());
        ArrayList<ArrayList<Double>> pos_array = new ArrayList<ArrayList<Double>>();
        pos_array.add(0, new ArrayList<Double>());
        pos_array.add(1, new ArrayList<Double>());
        pos_array.add(2, new ArrayList<Double>());
        pos_array.add(3, new ArrayList<Double>());
        ArrayList<State> states_array = new ArrayList<State>();
        states_array.add(new State(ball.state));
        int i = 0;
        while (ball.state.position.getComponent(1) > 0) {
            // System.out.println(i + " iteration");
            ball.calc_magnus_forces();
            ball.update_position(i);
            pos_array.get(0).add(ball.state.position.getComponent(0)); // x
            pos_array.get(1).add(ball.state.position.getComponent(1)); // y
            pos_array.get(2).add(ball.state.position.getComponent(2)); // z
            pos_array.get(3).add(ball.state.velocity.getDegree()); // alpha
            states_array.add(new State(ball.state));
            i++;
        }
        // System.out.print("\n\n\nx_array = ");
        // System.out.print(Arrays.deepToString(pos_array.get(0).toArray()));
        // System.out.print("\n\n\ny_array = ");
        // System.out.println(Arrays.deepToString(pos_array.get(1).toArray()));

    }

    public static void test_drag_recursion() {
        Ball ball = new Ball(0.26932047, 0.12065, 0.47, 0.1);
        ball.set_started_velocity(new Vector(19.9366967045, 19.9366967045, 0.0));
        ball.set_rotational_velocity(new Vector(0.0, 0.0, -58.422600157894736842105263157895));
        ball.lift_coeficent = ball.radius.multiply(ball.state.rotational_velocity.getMagnitude())
                .divide(ball.state.velocity.getMagnitude()); // if cd is close to 0.5 then cl = R*rotational_velocity/v
                                                             // which/ is S
        ball.set_position(new Vector(0.0, 0.0000001, 0.0));
        ball.state.save_to_list();
        ball.state.kinematics_varuibales.add(new Vector(0.0, 0.0, 0.0));

        ball.before_before_state = new State(ball.state);
        // System.out.println(ball.cross_section_area_of_ball.getMagnitude());
        ArrayList<ArrayList<Double>> pos_array = new ArrayList<ArrayList<Double>>();
        pos_array.add(0, new ArrayList<Double>());
        pos_array.add(1, new ArrayList<Double>());
        int i = 0;
        while (ball.state.position.getComponent(1) > 0) {
            // System.out.println(i + " iteration");
            ball.calc_no_magnusforces();
            ball.update_position(i);
            pos_array.get(0).add(ball.state.position.getComponent(0));
            pos_array.get(1).add(ball.state.position.getComponent(1));
            i++;
        }
        System.out.print("\n\n\nx_array_drag_recursion = ");
        System.out.print(Arrays.deepToString(pos_array.get(0).toArray()));
        System.out.print("\n\n\ny_array_drag_recursion = ");
        System.out.print(Arrays.deepToString(pos_array.get(1).toArray()));
    }

    public static void test_drag_no_recursion() {
        Ball ball = new Ball(0.26932047, 0.12065, 0.47, 0.1);
        ball.set_started_velocity(new Vector(19.9366967045, 19.9366967045, 0.0));
        ball.set_rotational_velocity(new Vector(0.0, 0.0, -58.422600157894736842105263157895));
        ball.lift_coeficent = ball.radius.multiply(ball.state.rotational_velocity.getMagnitude())
                .divide(ball.state.velocity.getMagnitude()); // if cd is close to 0.5 then cl = R*rotational_velocity/v
                                                             // which/ is S
        ball.set_position(new Vector(0.0, 0.0000001, 0.0));
        ball.state.save_to_list();
        ball.state.kinematics_varuibales.add(new Vector(0.0, 0.0, 0.0));

        ball.before_before_state = new State(ball.state);
        // System.out.println(ball.cross_section_area_of_ball.getMagnitude());
        ArrayList<ArrayList<Double>> pos_array = new ArrayList<ArrayList<Double>>();
        pos_array.add(0, new ArrayList<Double>());
        pos_array.add(1, new ArrayList<Double>());
        int i = 0;
        while (ball.state.position.getComponent(1) > 0) {
            // System.out.println(i + " iteration");
            ball.calc_no_magnusforces();
            ball.update_position_no_recursion();
            pos_array.get(0).add(ball.state.position.getComponent(0));
            pos_array.get(1).add(ball.state.position.getComponent(1));
            i++;
        }
        // System.out.print("\n\n\nx_array_drag_no_recursion = ");
        // System.out.print(Arrays.deepToString(pos_array.get(0).toArray()));
        // System.out.print("\n\n\ny_array_drag_no_recursion = ");
        // System.out.print(Arrays.deepToString(pos_array.get(1).toArray()));
    }

    public static void test_lift_no_recursion() {
        Ball ball = new Ball(0.26932047, 0.12065, 0.47, 0.1);
        ball.set_started_velocity(new Vector(19.9366967045, 19.9366967045, 0.0));
        ball.set_rotational_velocity(new Vector(0.0, 0.0, -58.422600157894736842105263157895));
        ball.lift_coeficent = ball.radius.multiply(ball.state.rotational_velocity.getMagnitude())
                .divide(ball.state.velocity.getMagnitude()); // if cd is close to 0.5 then cl = R*rotational_velocity/v
                                                             // which/ is S
        ball.set_position(new Vector(0.0, 0.0000001, 0.0));
        ball.state.save_to_list();
        ball.state.kinematics_varuibales.add(new Vector(0.0, 0.0, 0.0));
        ball.before_before_state = new State(ball.state);
        // System.out.println(ball.cross_section_area_of_ball.getMagnitude());
        ArrayList<ArrayList<Double>> pos_array = new ArrayList<ArrayList<Double>>();
        pos_array.add(0, new ArrayList<Double>());
        pos_array.add(1, new ArrayList<Double>());
        int i = 0;
        while (ball.state.position.getComponent(1) > 0) {
            // System.out.println(i + " iteration");
            ball.calc_magnus_forces();
            ball.update_position_no_recursion();
            pos_array.get(0).add(ball.state.position.getComponent(0));
            pos_array.get(1).add(ball.state.position.getComponent(1));
            i++;
        }
        // System.out.print("\n\n\nx_array_lift_no_recursion = ");
        // System.out.print(Arrays.deepToString(pos_array.get(0).toArray()));
        // System.out.print("\n\n\ny_array_lift_no_recursion = ");
        // System.out.print(Arrays.deepToString(pos_array.get(1).toArray()));
    }

    public static void test_more_parameters() {
        Ball ball = new Ball(0.26932047, 0.12065, 0.47, 0.1);
        ball.set_started_velocity(new Vector(19.9366967045, 19.9366967045, 0.0));
        ball.set_rotational_velocity(new Vector(0.0, 0.0, -58.422600157894736842105263157895));
        ball.lift_coeficent = ball.radius.multiply(ball.state.rotational_velocity.getMagnitude())
                .divide(ball.state.velocity.getMagnitude()); // if cd is close to 0.5 then cl = R*rotational_velocity/v
                                                             // which/ is S
        ball.set_position(new Vector(0.0, 0.0000001, 0.0));
        ball.state.save_to_list();
        ball.state.kinematics_varuibales.add(new Vector(0.0, 0.0, 0.0));

        ball.before_before_state = new State(ball.state);
        // System.out.println(ball.cross_section_area_of_ball.getMagnitude());
        ArrayList<ArrayList<Double>> pos_array = new ArrayList<ArrayList<Double>>();
        pos_array.add(0, new ArrayList<Double>());
        pos_array.add(1, new ArrayList<Double>());
        int i = 0;
        for (i = 0; i < 2; i++) {
            ball.calc_magnus_forces();
            ball.update_position(i);
            pos_array.get(0).add(ball.state.position.getComponent(0));
            pos_array.get(1).add(ball.state.position.getComponent(1));
        }
        // System.out.print("\n\n\nx_array_lift_recursion = ");
        // System.out.print(Arrays.deepToString(pos_array.get(0).toArray()));
        // System.out.print("\n\n\ny_array_lift_recursion = ");
        // System.out.print(Arrays.deepToString(pos_array.get(1).toArray()));
    }

    public static void test_magnus_adjusted_lift_recursion() {
        Ball ball = new Ball(0.26932047, 0.12065, 0.47, 0.1);
        ball.set_position(new Vector(0.0, 0.0000001, 0.0));
        ball.state.save_to_list();
        ball.state.kinematics_varuibales.add(new Vector(0.0, 0.0, 0.0));

        ball.before_before_state = new State(ball.state);
        // System.out.println(ball.cross_section_area_of_ball.getMagnitude());
        ArrayList<ArrayList<Double>> pos_array = new ArrayList<ArrayList<Double>>();
        pos_array.add(0, new ArrayList<Double>());
        pos_array.add(1, new ArrayList<Double>());
        int i = 0;
        while (ball.state.position.getComponent(1) > 0) {
            // System.out.println(i + " iteration");
            ball.lift_coeficent = ball.radius.multiply(ball.state.rotational_velocity.getMagnitude())
                    .divide(ball.state.velocity.getMagnitude()); // if cd is close to 0.5 then cl =
                                                                 // R*rotational_velocity/v
                                                                 // which/ is S
            ball.calc_magnus_forces();
            ball.update_position(i);
            pos_array.get(0).add(ball.state.position.getComponent(0));
            pos_array.get(1).add(ball.state.position.getComponent(1));
            i++;
        }
        System.out.print("\n\n\nx_array_changing_lift_recursion = ");
        System.out.print(Arrays.deepToString(pos_array.get(0).toArray()));
        System.out.print("\n\n\ny_array_changing_lift_recursion = ");
        System.out.print(Arrays.deepToString(pos_array.get(1).toArray()));
    }

    public static void test_magnus_recursion_nasa() {
        Ball ball = new Ball(0.26932047, 0.12065, 0.47, 0.1);
        ball.set_started_velocity(new Vector(19.9366967045, 19.9366967045, 0.0));
        ball.set_rotational_velocity(new Vector(0.0, 0.0, -58.422600157894736842105263157895));
        ball.lift_coeficent = ball.radius.multiply(ball.state.rotational_velocity.getMagnitude())
                .divide(ball.state.velocity.getMagnitude()); // if cd is close to 0.5 then cl = R*rotational_velocity/v
                                                             // which/ is S
        ball.set_position(new Vector(0.0, 0.0000001, 0.0));
        ball.state.save_to_list();
        ball.state.kinematics_varuibales.add(new Vector(0.0, 0.0, 0.0));

        ball.before_before_state = new State(ball.state);
        // System.out.println(ball.cross_section_area_of_ball.getMagnitude());
        ArrayList<ArrayList<Double>> pos_array = new ArrayList<ArrayList<Double>>();
        pos_array.add(0, new ArrayList<Double>());
        pos_array.add(1, new ArrayList<Double>());
        int i = 0;
        while (ball.state.position.getComponent(1) > 0) {
            // System.out.println(i + " iteration");
            ball.calc_magnus_forces_nasa();
            ball.update_position(i);
            pos_array.get(0).add(ball.state.position.getComponent(0));
            pos_array.get(1).add(ball.state.position.getComponent(1));
            i++;
        }
        // System.out.print("\n\n\nx_array_nasa_lift_recursion = ");
        // System.out.print(Arrays.deepToString(pos_array.get(0).toArray()));
        // System.out.print("\n\n\ny_array_nasa_lift_recursion = ");
        // System.out.println(Arrays.deepToString(pos_array.get(1).toArray()));
    }

    public static void write_csv(Ball projectile, Target target, Double MaxAngle, Double MinAngle, Double MaxRPM,
            Double MinRPM, Boolean Magnus, Double Resolution) {

        // System.out.println("STARTED!");
        try {
            double TopinDiameter = 4;
            double TopmDiameter = Units.inchesToMeters(TopinDiameter);
            double TopCircumference = (TopmDiameter) * Math.PI;

            double BottominDiameter = 4;
            double BottommDiameter = Units.inchesToMeters(BottominDiameter);
            double BottomCircumference = (BottommDiameter) * Math.PI;

            FileWriter writer = new FileWriter("optimization_output.csv");
            boolean found = false;

            Double distance_inc = 0.01;
            Double angle_inc = 1.0;
            Double rpm_inc = 10.0;
            Double ratio_inc = 0.1;

            for (Double distance = 3.0; distance < 12.0;) {
                found = false;
                Instant start = Instant.now();
                for (Double angle = MinAngle; angle < MaxAngle && !found; angle += angle_inc) {
                    for (Double rpm = MinRPM; rpm < MaxRPM; rpm += rpm_inc) {
                        for (Double ratio = -1.0; ratio < 1.0; ratio += ratio_inc) {
                            double TopRPM = ratio > 0.0 ? rpm * (ratio - 1) : rpm;
                            double TopRPS = TopRPM / 60.0;
                            double BottomRPM = ratio < 0.0 ? rpm * (1 - ratio) : rpm;
                            double BottomRPS = BottomRPM / 60.0;

                            double muzzle_velocity = (TopRPS * TopCircumference + BottomRPS * BottomCircumference)
                                    / 2.0; // SURFACE
                                           // SPEED
                            double angle_rads = Math.toRadians(angle); // IN RADIANS

                            double TopRads = 0.104719755 * TopRPM;
                            double BottompRads = 0.104719755 * BottomRPM;

                            Vector started_velocity = new Vector(muzzle_velocity * Math.sin(angle_rads),
                                    muzzle_velocity * Math.cos(angle_rads), 0.0); // m/s
                            Vector started_rotational_velocity = new Vector(0.0, 0.0,
                                    (TopRads * TopmDiameter / 2 - BottompRads * BottommDiameter / 2)
                                            / (2 * projectile.get_radius())); // radians

                            projectile.set_position(new Vector(0.0, 0.1, 0.0));
                            projectile.set_started_velocity(started_velocity);
                            projectile.set_rotational_velocity(started_rotational_velocity);

                            ArrayList<State> states = projectile.simulate_ball(false);
                            target.set_distance(distance);
                            Boolean result = target.check(states);

                            if (result && TopRPM > MinRPM && BottomRPM > MinRPM) {
                                System.out.println(TopRPM + " " + BottomRPM + " " + (90.0 - angle) + " " +
                                        distance + " " + Duration.between(start, Instant.now()).getSeconds());
                                writer.write(String.valueOf(shooter_optimiztion.formatter.format(distance)));
                                writer.write(",");
                                writer.write(String.valueOf(shooter_optimiztion.formatter.format(90.0 -
                                        angle)));
                                writer.write(",");
                                writer.write(String.valueOf(shooter_optimiztion.formatter.format(TopRPM)));
                                writer.write(",");
                                writer.write(String.valueOf(shooter_optimiztion.formatter.format(BottomRPM)));
                                writer.write("\n");
                                found = true;
                                distance += distance_inc;
                            }

                        }
                    }
                }
            }
            writer.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void simulte_from_rpm_and_angle(Ball projectile, double TopRPM, double BottomRPM, double angle) {
        double TopinDiameter = 4;
        double TopmDiameter = Units.inchesToMeters(TopinDiameter);
        double TopCircumference = (TopmDiameter) * Math.PI;

        double BottominDiameter = 4;
        double BottommDiameter = Units.inchesToMeters(BottominDiameter);
        double BottomCircumference = (BottommDiameter) * Math.PI;
        double TopRPS = TopRPM / 60.0;
        double BottomRPS = BottomRPM / 60.0;
        double muzzle_velocity = (TopRPS * TopCircumference + BottomRPS * BottomCircumference) / 2.0;
        double angle_rads = Math.toRadians(angle); // IN RADIANS

        double TopRads = 0.104719755 * TopRPM;
        double BottompRads = 0.104719755 * BottomRPM;

        Vector started_velocity = new Vector(muzzle_velocity * Math.sin(angle_rads),
                muzzle_velocity * Math.cos(angle_rads), 0.0); // m/s
        Vector started_rotational_velocity = new Vector(0.0, 0.0,
                (TopRads * TopmDiameter / 2 - BottompRads * BottommDiameter / 2)
                        / (2 * projectile.get_radius())); // radians

        projectile.set_position(new Vector(0.0, 0.1, 0.0));
        projectile.set_started_velocity(started_velocity);
        projectile.set_rotational_velocity(started_rotational_velocity);
        projectile.simulate_ball(true);

    }

    public static void simulte_from_rpm_and_angle_runge_kutta(Ball projectile, double TopRPM, double BottomRPM,
            double angle) {
        double TopinDiameter = 4;
        double TopmDiameter = Units.inchesToMeters(TopinDiameter);
        double TopCircumference = (TopmDiameter) * Math.PI;

        double BottominDiameter = 4;
        double BottommDiameter = Units.inchesToMeters(BottominDiameter);
        double BottomCircumference = (BottommDiameter) * Math.PI;
        double TopRPS = TopRPM / 60.0;
        double BottomRPS = BottomRPM / 60.0;
        double muzzle_velocity = (TopRPS * TopCircumference + BottomRPS * BottomCircumference) / 2.0;
        double angle_rads = Math.toRadians(angle); // IN RADIANS

        double TopRads = 0.104719755 * TopRPM;
        double BottompRads = 0.104719755 * BottomRPM;

        Vector started_velocity = new Vector(muzzle_velocity * Math.sin(angle_rads),
                muzzle_velocity * Math.cos(angle_rads), 0.0); // m/s
        Vector started_rotational_velocity = new Vector(0.0, 0.0,
                (TopRads * TopmDiameter / 2 - BottompRads * BottommDiameter / 2)
                        / (2 * projectile.get_radius())); // radians

        projectile.set_position(new Vector(0.0, 0.1, 0.0));
        projectile.set_started_velocity(started_velocity);
        projectile.set_rotational_velocity(started_rotational_velocity);

        // ball.lift_coeficent =
        // ball.radius.multiply(ball.state.rotational_velocity.getMagnitude())
        // .divide(ball.state.velocity.getMagnitude()); // if cd is close to 0.5 then cl
        // = R*rotational_velocity/v
        // which/ is S
        projectile.simulate_ball_runge_kutta(true);

    }

    public static void main(String[] args) {
        Ball ball = new Ball(0.26932047, 0.12065, 0.47, 0.1);
        ball.state.kinematics_varuibales.add(new Vector(0.0, 0.0, 0.0));

        Target hub = new Target(new Vector(4.0, 2.7178, 0.0), new Vector(1.22 -
        ball.get_target_threshold(), 0.05,
        1.0), 90, 45, 999, -999);

        // // ANGLE FROM Y AXIS ^

        Vector results = shooter_optimiztion.binary_smart_optimize(ball, hub, 45.0,
        0.0, 5000.0, 1500.0, 12.0);

        simulte_from_rpm_and_angle(ball, results.getComponent(0),
        results.getComponent(1), results.getComponent(2));

        results = shooter_optimiztion.binary_smart_optimize_runge_kutta(ball, hub, 45.0,
        0.0, 5000.0, 1500.0, 12.0);

        simulte_from_rpm_and_angle_runge_kutta(ball, results.getComponent(0),
        results.getComponent(1), results.getComponent(2));

        // simulte_from_rpm_and_angle(ball, 3000, 3000, 45);

        // simulte_from_rpm_and_angle_runge_kutta(ball, 3000, 3000, 45);
    }
}
