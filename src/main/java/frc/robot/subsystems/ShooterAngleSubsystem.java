package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Servo;
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
    public double GetAngle()
    {
        return _left_servo.getAngle();
    }
}
