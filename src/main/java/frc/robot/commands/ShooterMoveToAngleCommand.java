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
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private  ShooterAngleSubsystem _angle_moving;
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
    increasement = 0.25;
    angle = (SmartDashboard.getNumber(frc.robot.Constants.Shooter.SmartDashboard.AngleToSet, 0));
    if (Globals.angle > angle)
    {
      increasement *=-1;
    }
    _angle_moving.start_time = Instant.now();
      // _angle_moving.SetAngle(0);
      // System.out.println(SmartsDashboard.getNumber(frc.robot.Constants.Shooter.SmartDashboard.AngleToSet, 0));
      angle = Math.max(Math.min(angle,Motors.Max),Motors.Min);
      if(angle <=Motors.Min || angle>=Motors.Max)
      {
        Globals.WRONGVALUES = true;
        _angle_moving.setRightAngle(180 - Globals.angle);
        _angle_moving.setLeftAngle(Globals.angle);
        end(true);
      }
  }
  // Called every time the scheduler runs while the command is scheduled.`x
  @Override
  public void execute() {
    
    
    _angle_moving.setLeftAngle(Globals.angle + increasement);
    System.out.println("LEFT ANGLE : " + Globals.angle + increasement);
    _angle_moving.setRightAngle(180- (Globals.angle + increasement));
    System.out.println("RIGHT ANGLE: "  + (180 - (Globals.angle +increasement)));
    Globals.angle += increasement;
    System.out.println(angle);
   }
  //  System.out.println(_angle_moving.encoder.get());
  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    System.out.println("Command Ended");
    if(!Globals.WRONGVALUES)
      Globals.angle = angle;
  }

  // Returns true when the command should end.
    @Override
    public boolean isFinished() {
      return (Globals.angle == angle) || Math.abs((Globals.angle - angle)) <= Motors.AngleThreashold;
    }
}
