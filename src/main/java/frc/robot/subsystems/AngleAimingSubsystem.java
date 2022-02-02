package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.Elevator.Encoders;

public class AngleAimingSubsystem extends SubsystemBase{
    private TalonSRX _anglemotor = new TalonSRX(Constants.Elevator.Motors.KangleMotor);
    private Encoder _encoder = new Encoder(Encoders.ChannelA,Encoders.ChannelB);
    public void ResetEncoder(){
        _encoder.reset();
    }
    public AngleAimingSubsystem()
    {
        _encoder.reset();
        _anglemotor.configFactoryDefault();
    }
    public void set(double precnetege,boolean reversed){
        _anglemotor.set(ControlMode.PercentOutput, reversed?-1* precnetege:precnetege);
    }

}
