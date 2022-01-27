package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import frc.robot.Constants;

public class AngleAiming {
    private TalonSRX _anglemotor = new TalonSRX(Constants.Elevator.Motors.kangleMotor);

    public void Encoder(){}

    public void ResetEncoder(){}

    public void Set(double speed){
        _anglemotor.set(ControlMode.PercentOutput, speed);;
    }

}
