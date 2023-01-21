package frc.robot.lib.base.MotorControllers;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.lib.base.MotorControllers.ControllersInteface.PIDValues;
import frc.robot.lib.base.MotorControllers.ControllersInteface.PositionContorl;
import frc.robot.lib.base.MotorControllers.ControllersInteface.SmartMotion;
import frc.robot.lib.base.MotorControllers.ControllersInteface.VelocityControl;

public class MySparkMax extends MotorControllerBase implements PIDValues, VelocityControl, PositionContorl {
    private final CANSparkMax spark_max_controller;

    public MySparkMax(int CANID, MotorType motor_type) {
        super.controller_name = MotorControllerName.SparkMax;
        this.spark_max_controller = new CANSparkMax(CANID, motor_type);
    }

    @Override
    public void set_voltage(double voltage) {
        this.spark_max_controller.setVoltage(voltage);

    }

    @Override
    public void set_precentage(double precentage) {
        this.spark_max_controller.set(precentage);
    }

    @Override
    public int get_CANID() {
        return this.spark_max_controller.getDeviceId();
    }

    public void follow(MySparkMax master_SparkMax) {
        this.spark_max_controller.follow(master_SparkMax.get_spark_max());
    }

    public CANSparkMax get_spark_max() {
        return this.spark_max_controller;
    }

    @Override
    public double getPosition() {
        return spark_max_controller.getEncoder().getPosition();
    }

    @Override
    public void set_Position(double position) {
        this.spark_max_controller.getPIDController().setReference(position, ControlType.kPosition);
    }

    @Override
    public double get_velocity() {
        return spark_max_controller.getEncoder().getVelocity();
    }

    @Override
    public void set_velocity(double velocity) {
        this.spark_max_controller.getPIDController().setReference(velocity, ControlType.kVelocity);

    }

    @Override
    public void setKp(double Kp) {
        this.spark_max_controller.getPIDController().setP(Kp);

    }

    @Override
    public void setKi(double Ki) {
        this.spark_max_controller.getPIDController().setI(Ki);

    }

    @Override
    public void setKd(double Kd) {
        this.spark_max_controller.getPIDController().setD(Kd);

    }

    @Override
    public void setKff(double Kff) {
        this.spark_max_controller.getPIDController().setFF(Kff);
    }

    public double getKp() {
        return this.spark_max_controller.getPIDController().getP();
    }

    public double getKi() {
        return this.spark_max_controller.getPIDController().getI();
    }

    public double getKd() {
        return this.spark_max_controller.getPIDController().getD();
    }

    public double getKff() {
        return this.spark_max_controller.getPIDController().getFF();
    }

}
