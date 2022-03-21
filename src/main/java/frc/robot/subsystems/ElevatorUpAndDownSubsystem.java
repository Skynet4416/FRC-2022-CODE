package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Elevator;
import frc.robot.Constants.Elevator.UpAndDown.PID;

public class ElevatorUpAndDownSubsystem extends SubsystemBase {
    private CANSparkMax _master = new CANSparkMax(Elevator.Motors.Kmaster,MotorType.kBrushless);
    private RelativeEncoder _master_encoder;
    private CANSparkMax _slave = new CANSparkMax(Elevator.Motors.Kslave,MotorType.kBrushless);
    private RelativeEncoder _slave_encoder;

    public ElevatorUpAndDownSubsystem()
    {
        _master.restoreFactoryDefaults();
        _slave.restoreFactoryDefaults();
        _slave.setIdleMode(IdleMode.kBrake);
        _master.setIdleMode(IdleMode.kBrake);
        // _slave.follow(_master, true);
        _master_encoder = _master.getEncoder();

        _slave_encoder = _slave.getEncoder();

        // _master.kd(0, PID.Kd);
        // _master.config_kF(0, PID.Kf);
        // _master.config_kI(0, PID.Ki);
        // _master.config_kP(0, PID.Kp);  

        // _slave.config_kD(0, PID.Kd);
        // _slave.config_kF(0, PID.Kf);
        // _slave.config_kI(0, PID.Ki);
        // _slave.config_kP(0, PID.Kp);
        // SmartDashboard.putNumber(Elevator.SmartDashboard.UpAndDown.Kd, PID.Kd);
        // SmartDashboard.putNumber(Elevator.SmartDashboard.UpAndDown.Kp, PID.Kp);
        // SmartDashboard.putNumber(Elevator.SmartDashboard.UpAndDown.Ki, PID.Ki);
        // SmartDashboard.putNumber(Elevator.SmartDashboard.UpAndDown.Kf, PID.Kf);

        // _master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        // _slave.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
    }

    public void setPreccentage(double precentage)
    {
        _master.set(precentage);
        _slave.set(precentage);
    }

    @Override
    public void periodic()
    {
        // SmartDashboard.putNumber("Elevator Master Position", _master.getSelectedSensorPosition()/4096 / Units.inchesToMeters(1.426) * Math.PI);
        // SmartDashboard.putNumber("Elevator Slave Position", _slave.getSelectedSensorPosition()/4096);

        // _master.config_kD(0, SmartDashboard.getNumber(Elevator.SmartDashboard.UpAndDown.Kd, PID.Kd));
        // _master.config_kF(0, SmartDashboard.getNumber(Elevator.SmartDashboard.UpAndDown.Kf, PID.Kf));
        // _master.config_kI(0, SmartDashboard.getNumber(Elevator.SmartDashboard.UpAndDown.Ki, PID.Ki));
        // _master.config_kP(0, SmartDashboard.getNumber(Elevator.SmartDashboard.UpAndDown.Kp, PID.Kp));      

        // _slave.config_kD(0, SmartDashboard.getNumber(Elevator.SmartDashboard.UpAndDown.Kd, PID.Kd));
        // _slave.config_kF(0, SmartDashboard.getNumber(Elevator.SmartDashboard.UpAndDown.Kf, PID.Kf));
        // _slave.config_kI(0, SmartDashboard.getNumber(Elevator.SmartDashboard.UpAndDown.Ki, PID.Ki));
        // _slave.config_kP(0, SmartDashboard.getNumber(Elevator.SmartDashboard.UpAndDown.Kp, PID.Kp));     
    }

    public double getMasterRotations() {
        return -1*_master_encoder.getPosition();
    }

    public double getSlaveRotations() {
        return _slave_encoder.getPosition();
    }

    // public void SetPoistion(double position_in_meters)
    // {
    //     _master.set(ControlMode.Position,position_in_meters * Units.inchesToMeters(1.426) * Math.PI * 4096);
    // }

}

