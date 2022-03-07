package frc.robot.subsystems;

import java.time.Instant;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.Chassis.Physical;
import frc.robot.Constants.Elevator.Angle.PID;
import frc.robot.Constants.Shooter.Physics;
import frc.robot.Constants.Shooter.Physics.Motors;
import frc.robot.Constants.Shooter.Physics.PIDAngle;

public class ShooterAngleSubsystem extends SubsystemBase {
    private TalonSRX left_master = new TalonSRX(Motors.left_master);
    private TalonSRX right_slave = new TalonSRX(Motors.right_slave);
    public Instant start_time;
    private final boolean kDiscontinuityPresent = false;
    // private double time_to_finish = 1000; // in milliseconds
    public double start_angle;

    public ShooterAngleSubsystem() {
        left_master.configFactoryDefault();
        right_slave.configFactoryDefault();
        right_slave.follow(left_master);
        right_slave.setInverted(InvertType.FollowMaster);
        left_master.setNeutralMode(NeutralMode.Brake);
        right_slave.setNeutralMode(NeutralMode.Brake);
        SmartDashboard.putNumber(Constants.Shooter.Physics.SmartDashboard.AngleToSet, 0);
        left_master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
        right_slave.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
        left_master.config_kD(0,PIDAngle.kD);
        left_master.config_kP(0,PIDAngle.kP);
        left_master.config_kF(0,PIDAngle.kF);
        left_master.config_kI(0,PIDAngle.kI);

        right_slave.config_kD(0,PIDAngle.kD);
        right_slave.config_kP(0,PIDAngle.kP);
        right_slave.config_kF(0,PIDAngle.kF);
        right_slave.config_kI(0,PIDAngle.kI);
        
        SmartDashboard.putNumber("Shooter Angle KP",PIDAngle.kP);
        SmartDashboard.putNumber("Shooter Angle KI",PIDAngle.kI);
        SmartDashboard.putNumber("Shooter Angle KD",PIDAngle.kD);
        SmartDashboard.putNumber("Shooter Angle KF",PIDAngle.kF);


    }
    public double ToDeg(double units)
    {
        return (double)units/4096;
    }
    
    public double ToUnits(double angle) {
        return (double) (angle * 4096 / 360);
    }
    public double getLeftAbsuluteAngle()
    {
        return ToDeg(left_master.getSelectedSensorPosition());
    }
    public double getRightAbsuluteAngle()
    {
        return ToDeg(right_slave.getSelectedSensorPosition());
    }
    public double getLeftAngle()
    {
        return Physics.left_home - getLeftAbsuluteAngle();
    }
    public double getRightAngle()
    {
        return getRightAbsuluteAngle()-Physics.right_home;
    }
    public void setLeftAngle(double angle_to_set)
    {
        left_master.set(ControlMode.Position, ToUnits(Physics.left_home + angle_to_set));
    }
    public void setRightAngle(double angle_to_set)
    {
        right_slave.set(ControlMode.Position, ToUnits(Physics.right_home + angle_to_set));
    }
    public void setPrecnetage(double left)
    {
        left_master.set(ControlMode.PercentOutput, left);
    }
    public void setAngle(double angle_to_set)
    {
        left_master.set(ControlMode.Position,ToUnits(angle_to_set + Physics.left_home));
    }
    @Override
    public void periodic() {
        SmartDashboard.putNumber(frc.robot.Constants.Shooter.Physics.SmartDashboard.LeftAbsuluteAngle, getLeftAbsuluteAngle());
        SmartDashboard.putNumber(frc.robot.Constants.Shooter.Physics.SmartDashboard.RighAbsulureAngle, getRightAbsuluteAngle());
        SmartDashboard.putNumber(Physics.SmartDashboard.LeftAngle, getLeftAngle());
        SmartDashboard.putNumber(Physics.SmartDashboard.RightAngle, getRightAngle());
        left_master.config_kD(0,SmartDashboard.getNumber("Shooter Angle KD", 0));
        left_master.config_kI(0,SmartDashboard.getNumber("Shooter Angle KI", 0));
        left_master.config_kP(0,SmartDashboard.getNumber("Shooter Angle KP", 0));
        left_master.config_kF(0,SmartDashboard.getNumber("Shooter Angle KF", 0));

        right_slave.config_kD(0,SmartDashboard.getNumber("Shooter Angle KD", 0));
        right_slave.config_kI(0,SmartDashboard.getNumber("Shooter Angle KI", 0));
        right_slave.config_kP(0,SmartDashboard.getNumber("Shooter Angle KP", 0));
        right_slave.config_kF(0,SmartDashboard.getNumber("Shooter Angle KF", 0));

    }

}
