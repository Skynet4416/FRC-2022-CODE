package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Shooter.Motors;;

public class ShooterAngleSubsystem extends SubsystemBase{
    private Servo _left_servo = new Servo(Motors.left_servo);
    private Servo _right_servo = new Servo(Motors.right_servo);

    public void SetAngle(double angle)
    {
        _left_servo.setAngle(angle);
        _right_servo.setAngle(180-angle);
    }
    public double GetLeftAngle()
    {
        return _left_servo.getAngle();
    }
    public double GetRightAngle()
    {
        return _right_servo.getAngle();
    }
    @Override
    public void periodic()
    {
        SmartDashboard.putNumber(frc.robot.Constants.Shooter.SmartDashboard.LeftServoAngle, _left_servo.getAngle());
        SmartDashboard.putNumber(frc.robot.Constants.Shooter.SmartDashboard.LeftServoAngle, _right_servo.getAngle());
    }
}
