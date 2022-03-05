package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.Elevator.Motors;
import frc.robot.subsystems.HookUpAndDownSubsystem;

public class HookDownCommand extends CommandBase{
    private HookUpAndDownSubsystem _hook;
    public HookDownCommand(HookUpAndDownSubsystem hook)
    {
        this._hook = hook;
        addRequirements(hook);
    }
      // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    _hook.set(Motors.hook_precentage,true);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    _hook.set(0,false);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return _hook.IsClosed();
  }
}
