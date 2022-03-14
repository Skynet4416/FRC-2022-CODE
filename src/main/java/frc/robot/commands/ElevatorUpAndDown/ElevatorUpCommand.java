package frc.robot.commands.ElevatorUpAndDown;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.Elevator.Motors;
import frc.robot.subsystems.ElevatorUpAndDownSubsystem;

public class ElevatorUpCommand extends CommandBase {
  private ElevatorUpAndDownSubsystem _elevator;

  public ElevatorUpCommand(ElevatorUpAndDownSubsystem elevator) {
    this._elevator = elevator;
    addRequirements(elevator);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    _elevator.setPreccentage(Motors.elevator_precentage);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // System.out.println("Up end.");
    _elevator.setPreccentage(0);
    // System.out.println("Up ended.");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

}
