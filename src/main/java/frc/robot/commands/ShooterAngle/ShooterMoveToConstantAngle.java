// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.ShooterAngle;


import frc.robot.Constants.Shooter.Physics;
import frc.robot.Constants.Shooter.Physics.Motors;
import frc.robot.subsystems.ShooterAngleSubsystem;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class ShooterMoveToConstantAngle extends CommandBase {
  @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
  private ShooterAngleSubsystem _angle_moving;
  private double angle;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public ShooterMoveToConstantAngle(ShooterAngleSubsystem angle_moving) {
    _angle_moving = angle_moving;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(angle_moving);

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    this.angle  = SmartDashboard.getNumber("Shooter Angle To Set", 0);
    // _angle_moving.SetAngle(0);
    // System.out.println(SmartsDashboard.getNumber(frc.robot.Constants.Shooter.SmartDashboard.AngleToSet,
    // 0));
    if (angle <= Motors.Min || angle >= Motors.Max) {
      cancel();
      return;
    }
    System.out.println("Initialized Shooter Constant Angle");
  }

  // Called every time the scheduler runs while the command is scheduled.`x
  @Override
  public void execute() {
    _angle_moving.setPrecnetage(Motors.AnglePrecentage * (_angle_moving.getRightAbsuluteAngle() - Physics.right_home > angle ?- 1 : 1));
    // double percentage = ((_angle_moving.getRightAbsuluteAngle() - Physics.right_home) - angle) / 90;
    // System.out.println(percentage);
    // _angle_moving.setPrecnetage(percentage);
  }

  // System.out.println(_angle_moving.encoder.get());
  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // System.out.println("Command Ended");
    _angle_moving.setPrecnetage(0);
    System.out.println("Finished Shooter Constant Angle");

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Math.abs((_angle_moving.getRightAbsuluteAngle() - Physics.right_home) - angle) <= Motors.AngleThreashold;
    // return (_angle_moving.getRightAbsuluteAngle() == angle + Physics.right_home)
    //     || Math.abs((_angle_moving.getRightAbsuluteAngle() - angle - Physics.right_home)) <= Motors.AngleThreashold;
  }
}
