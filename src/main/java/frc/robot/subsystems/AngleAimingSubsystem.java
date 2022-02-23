package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.Elevator;
import frc.robot.Constants.Elevator.Encoders;

public class AngleAimingSubsystem extends SubsystemBase{
    private TalonSRX _anglemotor = new TalonSRX(Constants.Elevator.Motors.KangleMotor);
    private int kTimeoutMs = 30;
    public AngleAimingSubsystem()
    {
        _anglemotor.configFactoryDefault();
    }
    public void set(double precnetege,boolean reversed){
        _anglemotor.set(ControlMode.PercentOutput, reversed?-1* precnetege:precnetege);
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
    }


}
