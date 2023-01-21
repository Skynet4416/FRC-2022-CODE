package frc.robot.subsystems;

import java.time.Instant;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
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
        SmartDashboard.putNumber(Constants.Shooter.Physics.SmartDashboard.AngleToSet, 0);
        left_master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        right_slave.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);

    }
    public void SetAbs()
    {
        initQuadrature(left_master);

        initQuadrature(right_slave);
    }
    public double ToDeg(double units)
    {
        return (double)units*360/4096;
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
    public void setLeft(double precentage)
    {
        left_master.set(ControlMode.PercentOutput, precentage);
    }
    public void setRight(double precentage)
    {
        right_slave.set(ControlMode.PercentOutput, precentage);
    }
   public void setPrecnetage(double left)
    {
        left_master.set(ControlMode.PercentOutput, left);
        // right_slave.set(ControlMode.PercentOutput, -left);
    }

    @Override
    public void periodic() {
        SetAbs();
        SmartDashboard.putNumber(frc.robot.Constants.Shooter.Physics.SmartDashboard.LeftAbsuluteAngle, getLeftAbsuluteAngle());
        SmartDashboard.putNumber(frc.robot.Constants.Shooter.Physics.SmartDashboard.RighAbsulureAngle, getRightAbsuluteAngle());
        SmartDashboard.putNumber(Physics.SmartDashboard.LeftAngle, getLeftAngle());
        SmartDashboard.putNumber(Physics.SmartDashboard.RightAngle, getRightAngle());
    }
    private void initQuadrature(TalonSRX talonSRX)
    {
        int pulseWidth = talonSRX.getSensorCollection().getPulseWidthPosition();
        pulseWidth = pulseWidth & 0xFFF;

		/* Update Quadrature position */
		talonSRX.getSensorCollection().setQuadraturePosition(pulseWidth,30);
    }

}
