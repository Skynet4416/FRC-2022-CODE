// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Globals;
import frc.robot.Constants.Shooter.Motors;
import frc.robot.subsystems.ShooterAngleSubsystem;

import java.time.Instant;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class ShooterMoveToAngleCommand extends CommandBase {
  @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
  private ShooterAngleSubsystem _angle_moving;
  private double angle;
  private double increasement = 0.25;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public ShooterMoveToAngleCommand(ShooterAngleSubsystem angle_moving) {
    _angle_moving = angle_moving;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(angle_moving);

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    angle = (SmartDashboard.getNumber(frc.robot.Constants.Shooter.SmartDashboard.AngleToSet, 0));
    // _angle_moving.SetAngle(0);
    // System.out.println(SmartsDashboard.getNumber(frc.robot.Constants.Shooter.SmartDashboard.AngleToSet,
    // 0));
    angle = Math.max(Math.min(angle, Motors.Max), Motors.Min);
    if (angle <= Motors.Min || angle >= Motors.Max) {
      end(true);
      return;
    }
    _angle_moving.Set(angle < _angle_moving.GetAngle() ? -1 * Motors.AnglePrecentage : Motors.AnglePrecentage);
  }

  // Called every time the scheduler runs while the command is scheduled.`x
  @Override
  public void execute() {

  }

  // System.out.println(_angle_moving.encoder.get());
  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    System.out.println("Command Ended");
    _angle_moving.Set(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return  (_angle_moving.GetAngle()== angle) || Math.abs((_angle_moving.GetAngle() - angle)) <= Motors.AngleThreashold;
  }
}
