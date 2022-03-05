package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.Elevator.Motors;
import frc.robot.subsystems.ElevatorAngleSubsystem;

public class ElevatorAngleBackwardCommand extends CommandBase{
    private ElevatorAngleSubsystem _elevator;
    public ElevatorAngleBackwardCommand(ElevatorAngleSubsystem elevator)
    {
        this._elevator = elevator;
        addRequirements(elevator);
    }
      // Called when the command is initially scheduled.
  @Override
  public void initialize() {
      _elevator.setManual(Motors.angle_move_precentage,true);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
      _elevator.setManual(0,true);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

    
}
