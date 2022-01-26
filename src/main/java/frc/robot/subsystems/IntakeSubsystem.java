package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Intake.Motors;

/**
 * Chassis subsystem.
 */
public class IntakeSubsystem extends SubsystemBase {
    // Initialize motor controllers.
    private VictorSPX _intake;

    /**
     * Creates the subsystem and configures motor controllers.
     */
    public IntakeSubsystem() {
      _intake = new VictorSPX(Motors.kIntake);
    }

    /**
     * Set intake motor output.
     * 
     * @param power setpoint (percentage).
     */
    public void setIntake(double power) {
      if (Math.abs(power) <= 1) this._intake.set(ControlMode.PercentOutput, power);
      return;
    }

    @Override
    public void periodic() { }
}
