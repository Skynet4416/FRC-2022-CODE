package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Elevator;

public class ElevatorUpAndDownSubsystem extends SubsystemBase {
    private VictorSPX _master = new VictorSPX(Elevator.Motors.Kmaster);
    private VictorSPX _slave = new VictorSPX(Elevator.Motors.Kslave);
    private TalonSRX _angleMotor = new TalonSRX(Elevator.Motors.kangleMotor);

    public void Encoder(){}

    public void ResetEncoder(){}

    public void set(double speed){
        _angleMotor.set(ControlMode.PercentOutput, speed);
    }

    public ElevatorUpAndDownSubsystem()
    {
        _master.configFactoryDefault();
        _slave.configFactoryDefault();
        _slave.follow(_master);
        _slave.setInverted(true);
    }
    public void set()
    {
        _master.set(VictorSPXControlMode.PercentOutput, Elevator.Motors.speed);
    }
    
}