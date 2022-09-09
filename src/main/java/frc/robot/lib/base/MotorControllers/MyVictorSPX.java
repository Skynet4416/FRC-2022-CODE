package frc.robot.lib.base.MotorControllers;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import frc.robot.lib.base.MotorControllers.ControllersInteface.PIDValues;
import frc.robot.lib.base.MotorControllers.ControllersInteface.PositionContorl;
import frc.robot.lib.base.MotorControllers.ControllersInteface.SmartMotion;
import frc.robot.lib.base.MotorControllers.ControllersInteface.VelocityControl;

public class MyVictorSPX extends MotorControllerBase
        implements VelocityControl, PositionContorl, PIDValues, SmartMotion {
    private final WPI_VictorSPX victor_controller;

    public MyVictorSPX(int CANID) {
        super.controller_name = MotorControllerName.VictorSPX;

        this.victor_controller = new WPI_VictorSPX(CANID);
    }

    public WPI_VictorSPX get_talon_controller() {
        return this.victor_controller;
    }

    @Override
    public void set_voltage(double voltage) {
        this.victor_controller.setVoltage(voltage);
    }

    @Override
    public void set_velocity(double velocity) {
        this.victor_controller.set(ControlMode.Velocity, velocity);
    }

    @Override
    public void set_precentage(double precentage) {
        this.victor_controller.set(ControlMode.PercentOutput, precentage);

    }

    public void follow(MyTalonSRX motor_contorller) {
        this.victor_controller.follow(motor_contorller.get_talon_controller());
    }

    @Override
    public int get_CANID() {
        return this.victor_controller.getDeviceID();
    }

    @Override
    public double get_velocity() {
        return this.victor_controller.getSelectedSensorVelocity();
    }

    @Override
    public double getPosition() {
        return this.victor_controller.getSelectedSensorPosition();
    }

    @Override
    public void set_Position(double position) {
        this.victor_controller.set(ControlMode.Position, position);
    }

    @Override
    public void setKp(double Kp) {
        this.victor_controller.config_kP(0, Kp);
    }

    @Override
    public void setKi(double Ki) {
        this.victor_controller.config_kI(0, Ki);

    }

    @Override
    public void setKd(double Kd) {
        this.victor_controller.config_kD(0, Kd);

    }

    @Override
    public void setKff(double Kff) {
        this.victor_controller.config_kF(0, Kff);
    }

    @Override
    public void set_motion_magic_setpoint(double setpoint) {
        this.victor_controller.set(ControlMode.MotionMagic, setpoint);
    }

    @Override
    public void set_motion_magic_desired_velocity(double velocity) {
        this.victor_controller.configMotionAcceleration(velocity);

    }

    @Override
    public void set_motion_magic_desired_acceleration(double acceleration) {
        this.victor_controller.configMotionAcceleration(acceleration);
    }

}
