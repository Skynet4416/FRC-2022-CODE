package frc.robot.lib.base.MotorControllers;

import frc.robot.lib.base.MotorControllers.ControllersInteface.VelocityControl;
import frc.robot.lib.base.MotorControllers.ControllersInteface.PositionContorl;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import frc.robot.lib.base.MotorControllers.ControllersInteface.PIDValues;
import frc.robot.lib.base.MotorControllers.ControllersInteface.SmartMotion;

public class MyTalonSRX extends MotorControllerBase
        implements VelocityControl, PositionContorl, PIDValues, SmartMotion {
    private final WPI_TalonSRX talon_controller;

    public MyTalonSRX(int CANID) {
        super.controller_name = MotorControllerName.TalonSRX;
        this.talon_controller = new WPI_TalonSRX(CANID);
    }

    public WPI_TalonSRX get_talon_controller() {
        return this.talon_controller;
    }

    @Override
    public void set_voltage(double voltage) {
        this.talon_controller.setVoltage(voltage);
    }

    @Override
    public void set_velocity(double velocity) {
        this.talon_controller.set(ControlMode.Velocity, velocity);
    }

    @Override
    public void set_precentage(double precentage) {
        this.talon_controller.set(ControlMode.PercentOutput, precentage);

    }

    public void follow(MyTalonSRX motor_contorller) {
        this.talon_controller.follow(motor_contorller.get_talon_controller());
    }

    @Override
    public int get_CANID() {
        return this.talon_controller.getDeviceID();
    }

    @Override
    public double get_velocity() {
        return this.talon_controller.getSelectedSensorVelocity();
    }

    @Override
    public double getPosition() {
        return this.talon_controller.getSelectedSensorPosition();
    }

    @Override
    public void set_Position(double position) {
        this.talon_controller.set(ControlMode.Position, position);
    }

    @Override
    public void setKp(double Kp) {
        this.talon_controller.config_kP(0, Kp);
    }

    @Override
    public void setKi(double Ki) {
        this.talon_controller.config_kI(0, Ki);

    }

    @Override
    public void setKd(double Kd) {
        this.talon_controller.config_kD(0, Kd);

    }

    @Override
    public void setKff(double Kff) {
        this.talon_controller.config_kF(0, Kff);
    }

    @Override
    public void set_motion_magic_setpoint(double setpoint) {
        this.talon_controller.set(ControlMode.MotionMagic, setpoint);
    }

    @Override
    public void set_motion_magic_desired_velocity(double velocity) {
        this.talon_controller.configMotionAcceleration(velocity);

    }

    @Override
    public void set_motion_magic_desired_acceleration(double acceleration) {
        this.talon_controller.configMotionAcceleration(acceleration);
    }

}
