package frc.robot.commands.ElevatorUpAndDown;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.Elevator;
import frc.robot.subsystems.ElevatorUpAndDownSubsystem;

/*
 * WARNING
 * 
 * This command is only meant for going up, and assumes the encoder values provided
 * also rise with time.
 */
public class ElevatorByDistance extends CommandBase {
  private ElevatorUpAndDownSubsystem _elevator;
  private double _rotationCount;
  private double _masterStartingPos;

  public ElevatorByDistance(ElevatorUpAndDownSubsystem elevator, double meterDistance) {
    this._rotationCount = meterDistance * 25 / Elevator.meter_circumference;
    this._elevator = elevator;
    addRequirements(elevator);
  }
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("Elevator By Distance Initialize");
    this._masterStartingPos = _elevator.getMasterRotations();
    _elevator.setPreccentage(_masterStartingPos < _rotationCount?0.5:-0.5);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    _elevator.setPreccentage(0);
    System.out.println("Elevator By Distance Finished");

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Math.abs(_elevator.getMasterRotations() - this._masterStartingPos) > this._rotationCount - Elevator.rotation_threshold;
  }

}
