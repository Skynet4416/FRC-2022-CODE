package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Elevator.Motors;

public class HookUpAndDownSubsystem  extends SubsystemBase{
    private VictorSPX _hook = new VictorSPX(Motors.Khook);
    public HookUpAndDownSubsystem() {
        _hook.configFactoryDefault();
    }
    public void set(double precentage,boolean reversed)
    {
        _hook.set(ControlMode.PercentOutput,reversed? -1*precentage: precentage);
    }
    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
    }
    
}
