// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Shooter;

import frc.robot.Constants.Shooter;
import frc.robot.subsystems.ShooterSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class ShootBallGivenRPMCommand extends CommandBase {
  @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
  private ShooterSubsystem _shooter;
  private double top_rpm;
  private double bottom_rpm;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public ShootBallGivenRPMCommand(ShooterSubsystem shooter, double top_rpm ,double bottom_rpm) {
    _shooter = shooter;
    this.top_rpm = top_rpm;
    this.bottom_rpm = bottom_rpm;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    _shooter.SetRPM(top_rpm,bottom_rpm);
    // System.out.println("Shooter RPM Initialized");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    _shooter.setPercentage(0);
    // System.out.println("Finished Shoot RPM");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
