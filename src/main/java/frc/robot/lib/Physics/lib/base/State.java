package frc.robot.lib.Physics.lib.base;

import java.util.ArrayList;
import java.util.List;

import frc.robot.com.github.iprodigy.physics.util.abstraction.Scalar;
import frc.robot.com.github.iprodigy.physics.util.vector.Vector;

public class State {
    public Vector position; // meters
    public Vector velocity; // m/s
    public Vector acceleration; // m/s^1
    public Vector sigma_forces; // N, kg*m/s^2
    public Vector rotational_velocity; // radians/s
    public Vector rotational_acceleration; // radians / s^2
    public Scalar energy; // j, kg*m^2/2
    public Scalar potential_energy; // j, kg*m^2/2
    public Vector liniar_moment; // kg*m/s, n*s
    public Vector angular_momentum; // kg*m^2/s
    public Vector jerk; // m/s^3
    public List<Vector> kinematics_varuibales;
    public Vector angular_velocity;
    public Vector torque;

    public State() {
        this.position = new Vector(0.0, 0.0, 0.0);
        this.velocity = new Vector(0.0, 0.0, 0.0);
        this.acceleration = new Vector(0.0, 0.0, 0.0);
        this.sigma_forces = new Vector(0.0, 0.0, 0.0);
        this.rotational_velocity = new Vector(0.0, 0.0, 0.0);
        this.rotational_acceleration = new Vector(0.0, 0.0, 0.0);
        this.liniar_moment = new Vector(0.0, 0.0, 0.0);
        this.angular_momentum = new Vector(0.0, 0.0, 0.0);
        this.energy = () -> 0.0;
        this.potential_energy = () -> 0.0;
        this.jerk = new Vector(0.0, 0.0, 0.0);
        this.kinematics_varuibales = new ArrayList<Vector>();
        this.angular_velocity = new Vector(0.0, 0.0, 0.0);
        this.torque = new Vector(0.0, 0.0, 0.0);

        kinematics_varuibales.add(position);
        kinematics_varuibales.add(velocity);
        kinematics_varuibales.add(acceleration);
        kinematics_varuibales.add(jerk);

    }

    public State(double TopMotorRPM, double BottomMotorRPM, double HoodAngle) {
        this.position = new Vector(0.0, 0.0, 0.0);
        this.velocity = new Vector(0.0, 0.0, 0.0);
        this.acceleration = new Vector(0.0, 0.0, 0.0);
        this.sigma_forces = new Vector(0.0, 0.0, 0.0);
        this.rotational_velocity = new Vector(0.0, 0.0, 0.0);
        this.rotational_acceleration = new Vector(0.0, 0.0, 0.0);
        this.liniar_moment = new Vector(0.0, 0.0, 0.0);
        this.angular_momentum = new Vector(0.0, 0.0, 0.0);
        this.energy = () -> 0.0;
        this.potential_energy = () -> 0.0;
        this.jerk = new Vector(0.0, 0.0, 0.0);
        this.kinematics_varuibales = new ArrayList<Vector>();
        this.angular_velocity = new Vector(0.0, 0.0, 0.0);
        this.torque = new Vector(0.0, 0.0, 0.0);

        kinematics_varuibales.add(position);
        kinematics_varuibales.add(velocity);
        kinematics_varuibales.add(acceleration);
        kinematics_varuibales.add(jerk);

    }

    public void save_to_list() {
        this.kinematics_varuibales.set(0, position);
        this.kinematics_varuibales.set(1, velocity);
        this.kinematics_varuibales.set(2, acceleration);
        this.kinematics_varuibales.set(3, jerk);

    }

    public void save_to_var() {
        this.position = kinematics_varuibales.get(0);
        this.velocity = kinematics_varuibales.get(1);
        this.acceleration = kinematics_varuibales.get(2);
        this.jerk = kinematics_varuibales.get(3);
    }

    public State(State state) {
        this.position = state.position;
        this.velocity = state.velocity;
        this.acceleration = state.acceleration;
        this.sigma_forces = state.sigma_forces;
        this.rotational_velocity = state.rotational_velocity;
        this.rotational_acceleration = state.rotational_acceleration;
        this.liniar_moment = state.liniar_moment;
        this.angular_momentum = state.angular_momentum;
        this.energy = state.energy;
        this.potential_energy = state.potential_energy;
        this.jerk = state.jerk;
        this.kinematics_varuibales = new ArrayList<>(state.kinematics_varuibales);

    }

    public State add_vector(List<Vector> vector) {
        State return_state = new State(this);
        return_state.position.add(vector.get(0));
        return_state.velocity.add(vector.get(1));
        return_state.acceleration.add(vector.get(2));
        return return_state;
    }

    @Override
    public String toString() {
        return "position: " + position + "\nvelocity: " + velocity + "\nacceleration: " + acceleration + "\njerk: "
                + jerk;
    }
}
