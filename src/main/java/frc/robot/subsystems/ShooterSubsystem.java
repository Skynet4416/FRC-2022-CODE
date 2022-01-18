package frc.robot.subsystems;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import java.util.Set;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.TalonFXSensorCollection;
import com.ctre.phoenix.motorcontrol.TalonFXSimCollection;

import frc.robot.Constants;
import frc.robot.Constants.Shooter.Motors;

public class ShooterSubsystem extends PIDSubsystem {
    private TalonFX _master = new TalonFX(Motors.Kmaster); // not sure we will use this engine
    private TalonFX _slave = new TalonFX(Motors.Kslave); // not sure we will use this engine
    private PIDController PIDcontroller = new PIDController(0, 0, 0);

    public ShooterSubsystem() {
        super(new PIDController(Constants.Shooter.PID.kP, Constants.Shooter.PID.kI, Constants.Shooter.PID.kD));
        _slave.configFactoryDefault();
        _master.configFactoryDefault();
        _slave.follow(_master);
        _slave.setInverted(InvertType.InvertMotorOutput);
    }

    @Override
    public void periodic() {

    }

    @Override
    protected void useOutput(double output, double setpoint) {
        // TODO Auto-generated method stub
        _master.set(ControlMode.PercentOutput, output);
        _slave.set(ControlMode.PercentOutput, output); // might not work (TEST) check wtice

    }

    @Override
    protected double getMeasurement() {
        // TODO Auto-generated method stub
        return 0;
    }
}