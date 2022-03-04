package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.Indexing.Motors;

public class IndexingSubsystem extends SubsystemBase {
    private VictorSPX _master = new VictorSPX(Motors.Kmaster);
    private VictorSPX _slave = new VictorSPX(Motors.Kslave);

    public IndexingSubsystem() {
        _master.configFactoryDefault();
        _slave.configFactoryDefault();
    }

    public void setPercentage(double percent) {
        // sets the percentage of power for the motors
        _master.set(ControlMode.PercentOutput, percent);
        _slave.set(ControlMode.PercentOutput, -percent);
    }

    public void setPercentageSame(double percent){
        _master.set(ControlMode.PercentOutput, percent);
        _master.set(ControlMode.PercentOutput, percent);
    }
}
