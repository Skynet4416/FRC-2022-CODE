package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.Elevator.Motors;
import frc.robot.subsystems.AngleAimingSubsystem;

public class ElevatorAngleForwordCommand extends CommandBase{
    private AngleAimingSubsystem _elevator;
    public ElevatorAngleForwordCommand(AngleAimingSubsystem elevator)
    {
        this._elevator = elevator;
        addRequirements(elevator);
    }
      // Called when the command is initially scheduled.
  @Override
  public void initialize() {
      _elevator.set(Motors.angle_move_precentage,false);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
      _elevator.set(0,false);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

    
}
