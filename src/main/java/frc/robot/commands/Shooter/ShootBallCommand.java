// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Shooter;

import frc.robot.Constants.Shooter.Physics;
import frc.robot.subsystems.IndexingSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class ShootBallCommand extends CommandBase {
  @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
  private ShooterSubsystem _shooter;
  private double top_rpm;
  private double bottom_rpm;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public ShootBallCommand(ShooterSubsystem shooter) {
    _shooter = shooter;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // _shooter.SetRPM(SmartDashboard.getNumber("Shooter Precentage", 0));
    _shooter.SetRPM(top_rpm, bottom_rpm);
    _shooter._leftLEDS.set(false);
    _shooter._rightLEDS.set(false);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (Math.abs(_shooter.Gettop().getSelectedSensorVelocity()*600/2048 - top_rpm)<= Physics.ShooterThreshold && Math.abs(_shooter.Getbottom().getSelectedSensorVelocity()*600/2048 - bottom_rpm)<= Physics.ShooterThreshold)
    {
      _shooter._leftLEDS.set(true);
      _shooter._rightLEDS.set(true);
    
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    _shooter.setPercentage(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
