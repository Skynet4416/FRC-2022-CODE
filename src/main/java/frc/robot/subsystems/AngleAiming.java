package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Encoder;
import frc.robot.Constants;
import frc.robot.Constants.Elevator.Encoders;

public class AngleAiming {
    private TalonSRX _anglemotor = new TalonSRX(Constants.Elevator.Motors.kangleMotor);
    private Encoder _encoder = new Encoder(Encoders.ChannelA,Encoders.ChannelB);
    public void ResetEncoder(){
        _encoder.reset();
    }  
    public AngleAiming()
    {
        _encoder.reset();
        _anglemotor.configFactoryDefault();
    }
    public void Set(double precnetege){
        _anglemotor.set(ControlMode.PercentOutput, precnetege);;
    }

}
