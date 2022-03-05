package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Elevator.Motors;

public class HookUpAndDownSubsystem  extends SubsystemBase{
    private VictorSPX _hook = new VictorSPX(Motors.Khook);
    private DigitalInput closed_switch = new DigitalInput(0);
    private DigitalInput open_switch = new DigitalInput(1);
    public HookUpAndDownSubsystem() {
        _hook.configFactoryDefault();
        for(int i =2;i < 21; i++)
        {
            _hook.setStatusFramePeriod(i, 1000);
        }
    }
    public void set(double precentage,boolean reversed)
    {
        _hook.set(ControlMode.PercentOutput,reversed? -1*precentage: precentage);
    }
    @Override
    public void periodic() {
        SmartDashboard.putBoolean("Hook Closed", IsClosed());
        SmartDashboard.putBoolean("Hook Opend", IsOpen()); 
        // This method will be called once per scheduler run
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
    }
    public boolean IsOpen()
    {
        return open_switch.get();
    }
    public boolean IsClosed()
    {
        return closed_switch.get();
    }
    
}
