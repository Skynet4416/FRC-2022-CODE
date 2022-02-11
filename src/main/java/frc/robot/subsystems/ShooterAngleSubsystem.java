package frc.robot.subsystems;

import java.time.Instant;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Globals;
import frc.robot.Constants.Shooter.Motors;

public class ShooterAngleSubsystem extends SubsystemBase{
    private Servo left_servo = new Servo(Motors.left_Motor);
    private Servo right_servo = new Servo(Motors.right_Motor);
    public Instant start_time;
    // private double time_to_finish = 1000; // in milliseconds
    public double start_angle;

    public ShooterAngleSubsystem()
    {
        SmartDashboard.putNumber(Constants.Shooter.SmartDashboard.AngleToSet, 0);
    }
    // public double map(double x, double in_min, double in_max, double out_min, double out_max)
    // {
    //     return (x*in_min) * (out_max-out_min) / (in_max-in_min) +out_min;
    // }
    public void setLeftAngle(double angle)
    {
        left_servo.setAngle(angle);
    }
    public void setRightAngle(double angle)
    {
        right_servo.setAngle(angle);
    }
    public double getLeftAngle()
    {
        return left_servo.getAngle();
    }
    public double getRightAngle()
    {
        return right_servo.getAngle();
    }
    @Override
    public void periodic()
    {
        SmartDashboard.putNumber(frc.robot.Constants.Shooter.SmartDashboard.LeftServoAngle, Globals.angle);
        SmartDashboard.putNumber(frc.robot.Constants.Shooter.SmartDashboard.RightServoAngle,180- Globals.angle);
    }
}

