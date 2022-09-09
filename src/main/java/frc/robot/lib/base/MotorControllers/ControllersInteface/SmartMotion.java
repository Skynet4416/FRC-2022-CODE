package frc.robot.lib.base.MotorControllers.ControllersInteface;

public interface SmartMotion {
    public void set_motion_magic_setpoint(double setpoint);

    public void set_motion_magic_desired_velocity(double velocity);

    public void set_motion_magic_desired_acceleration(double acceleration);
}
