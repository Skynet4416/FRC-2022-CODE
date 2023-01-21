package frc.robot.lib.base.MotorControllers;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import frc.robot.lib.base.MotorControllers.ControllersInteface.PIDValues;
import frc.robot.lib.base.MotorControllers.ControllersInteface.PositionContorl;
import frc.robot.lib.base.MotorControllers.ControllersInteface.SmartMotion;
import frc.robot.lib.base.MotorControllers.ControllersInteface.VelocityControl;

public class MyTalonFX extends MotorControllerBase implements VelocityControl, PositionContorl, PIDValues, SmartMotion {
    private final WPI_TalonFX falcon_controller;

    public MyTalonFX(int CANID) {
        super.controller_name = MotorControllerName.TalonFX;
        this.falcon_controller = new WPI_TalonFX(CANID);
    }

    public WPI_TalonFX get_falcon_controller() {
        return this.falcon_controller;
    }

    @Override
    public void set_voltage(double voltage) {
        this.falcon_controller.setVoltage(voltage);
    }

    @Override
    public void set_velocity(double velocity) {
        this.falcon_controller.set(ControlMode.Velocity, velocity);
    }

    @Override
    public void set_precentage(double precentage) {
        this.falcon_controller.set(ControlMode.PercentOutput, precentage);

    }

    public void follow(MyTalonFX motor_contorller) {
        this.falcon_controller.follow(motor_contorller.get_falcon_controller());
    }

    @Override
    public int get_CANID() {
        return this.falcon_controller.getDeviceID();
    }

    @Override
    public double get_velocity() {
        return this.falcon_controller.getSelectedSensorVelocity();
    }

    @Override
    public double getPosition() {
        return this.falcon_controller.getSelectedSensorPosition();
    }

    @Override
    public void set_Position(double position) {
        this.falcon_controller.set(ControlMode.Position, position);
    }

    @Override
    public void setKp(double Kp) {
        this.falcon_controller.config_kP(0, Kp);
    }

    @Override
    public void setKi(double Ki) {
        this.falcon_controller.config_kI(0, Ki);

    }

    @Override
    public void setKd(double Kd) {
        this.falcon_controller.config_kD(0, Kd);

    }

    @Override
    public void setKff(double Kff) {
        this.falcon_controller.config_kF(0, Kff);
    }

    @Override
    public void set_motion_magic_setpoint(double setpoint) {
        this.falcon_controller.set(ControlMode.MotionMagic, setpoint);
    }

    @Override
    public void set_motion_magic_desired_velocity(double velocity) {
        this.falcon_controller.configMotionAcceleration(velocity);

    }

    @Override
    public void set_motion_magic_desired_acceleration(double acceleration) {
        this.falcon_controller.configMotionAcceleration(acceleration);
    }

}
