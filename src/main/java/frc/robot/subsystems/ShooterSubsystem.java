package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;

import frc.robot.Constants.Shooter;
import frc.robot.Constants.Shooter.Motors;

public class ShooterSubsystem extends SubsystemBase {
    private TalonFX _master = new TalonFX(Motors.Kmaster);
    private TalonFX _slave = new TalonFX(Motors.Kslave);

    public ShooterSubsystem() {
        SmartDashboard.putNumber("Shooter Precentage", 0);
        _slave.configFactoryDefault();
        _master.configFactoryDefault();
        _master.config_kD(0, Shooter.PID.kD);
        _master.config_kP(0, Shooter.PID.kP);
        _master.config_kI(0, Shooter.PID.kI, 0);
        _master.config_kF(0, Shooter.PID.kF);
        _slave.config_kD(0, Shooter.PID.kD);
        _slave.config_kP(0, Shooter.PID.kP);
        _slave.config_kI(0, Shooter.PID.kI, 0);
        _slave.config_kF(0, Shooter.PID.kF);
        _slave.follow(_master);
        _slave.setInverted(InvertType.FollowMaster);
    }

    @Override
    public void periodic() {
        _master.config_kP(0, SmartDashboard.getNumber(Shooter.SmartDashboard.ShooterKP, Shooter.PID.kP));
        _master.config_kI(0, SmartDashboard.getNumber(Shooter.SmartDashboard.ShooterKI, Shooter.PID.kI));
        _master.config_kD(0, SmartDashboard.getNumber(Shooter.SmartDashboard.ShooterKD, Shooter.PID.kD));
        _master.config_kF(0, SmartDashboard.getNumber(Shooter.SmartDashboard.ShooterKF, Shooter.PID.kF));

        _slave.config_kP(0, SmartDashboard.getNumber(Shooter.SmartDashboard.ShooterKP, Shooter.PID.kP));
        _slave.config_kI(0, SmartDashboard.getNumber(Shooter.SmartDashboard.ShooterKI, Shooter.PID.kI));
        _slave.config_kD(0, SmartDashboard.getNumber(Shooter.SmartDashboard.ShooterKD, Shooter.PID.kD));
        _slave.config_kF(0, SmartDashboard.getNumber(Shooter.SmartDashboard.ShooterKF, Shooter.PID.kF));

        SmartDashboard.putNumber("Shooter Master Velocity (RPM)", _master.getSelectedSensorVelocity() * 600 / 2048);
        SmartDashboard.putNumber("Shooter Master Current", _master.getMotorOutputPercent());
        SmartDashboard.putNumber("Shooter Slave Current", _slave.getMotorOutputPercent());
        SmartDashboard.putNumber("Shooter Slave Velocity (RPM)", _slave.getSelectedSensorVelocity() * 600 / 2048);
    }

    public void SetRPM(double RPM) {
        _master.set(ControlMode.Velocity, RPM);
        _slave.set(ControlMode.Velocity, RPM);
    }

    public TalonFX GetMaster() {
        return _master;
    }

    public TalonFX GetSlave() {
        return _slave;
    }

    public void setPercentage(double precentege) {

        _master.set(ControlMode.PercentOutput, precentege);
        _slave.set(ControlMode.PercentOutput, precentege);
    }
}