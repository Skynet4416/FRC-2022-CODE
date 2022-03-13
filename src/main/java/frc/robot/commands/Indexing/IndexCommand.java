// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Indexing;

import frc.robot.Constants.Indexing;
import frc.robot.subsystems.IndexingSubsystem;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class IndexCommand extends CommandBase {
  private final IndexingSubsystem m_subsystem;
  private boolean reversed;
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public IndexCommand(IndexingSubsystem subsystem,boolean inversed){
    m_subsystem = subsystem;
    this.reversed = inversed;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
      m_subsystem.setPercentage(reversed? -Indexing.kIndexingPercent: Indexing.kIndexingPercent);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_subsystem.setPercentage(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
