package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.Elevator;
import frc.robot.Constants.Elevator.Angle;

public class ElevatorAngleSubsystem extends SubsystemBase{
    private TalonSRX _anglemotor = new TalonSRX(Constants.Elevator.Motors.KangleMotor);
    private int kTimeoutMs = 30;
    public ElevatorAngleSubsystem()
    {
        _anglemotor.configFactoryDefault();
        _anglemotor.config_kD(0, Angle.PID.Kd);
        _anglemotor.config_kI(0,  Angle.PID.Ki);
        _anglemotor.config_kP(0,  Angle.PID.Kp);
        _anglemotor.config_kF(0,  Angle.PID.Kf);
        _anglemotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
        SmartDashboard.putNumber("Elevator Angle KP", Angle.PID.Kp);
        SmartDashboard.putNumber("Elevator Angle KD", Angle.PID.Kd);
        SmartDashboard.putNumber("Elevator Angle KI", Angle.PID.Ki);
        SmartDashboard.putNumber("Elevator Angle KF", Angle.PID.Kf);


    }
    public void setManual(double precnetege,boolean reversed){
        _anglemotor.set(ControlMode.PercentOutput, reversed?-1* precnetege:precnetege);
    }
    public void setPosition(double angle)
    {
        _anglemotor.set(ControlMode.Position, angle/360 * 4096);
    }

    public void initQuadrature() {
        /* get the absolute pulse width position */
        int pulseWidth = _anglemotor.getSensorCollection().getPulseWidthPosition();
        pulseWidth = pulseWidth & 0xFFF;

        /* Update Quadrature position */
        _anglemotor.getSensorCollection().setQuadraturePosition(pulseWidth, kTimeoutMs);
    }
    public double ToDeg(double units) {
        double deg = units * 360.0 / 4096.0;

        /* truncate to 0.1 res */
        deg *= 10;
        deg = (int) deg;
        deg /= 10;

        return deg;
    }
    public double get_angle()
    {
        initQuadrature();
        return ToDeg(_anglemotor.getSelectedSensorPosition());
    }
    @Override
    public void periodic()
    {
        SmartDashboard.putNumber(Elevator.SmartDashboard.CurrentAngle, get_angle());
        _anglemotor.config_kD(0,SmartDashboard.getNumber("Elevator Angle KD", 0));
        _anglemotor.config_kP(0,SmartDashboard.getNumber("Elevator Angle KP", 0));
        _anglemotor.config_kI(0,SmartDashboard.getNumber("Elevator Angle KI", 0));
        _anglemotor.config_kF(0,SmartDashboard.getNumber("Elevator Angle KF", 0));

    }


}
