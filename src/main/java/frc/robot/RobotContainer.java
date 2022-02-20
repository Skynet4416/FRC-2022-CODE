// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.commands.Chassis.DriveByJoy;
import frc.robot.subsystems.ChassisSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.ShootBallOnPrecentageCommand;
import frc.robot.commands.ShooterAngleMoveTestCommand;
import frc.robot.commands.ShooterMoveToAngleCommand;
import frc.robot.subsystems.ShooterAngleSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and button mappings)
 * should be declared here.
 */
public class RobotContainer {

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  // The robot's subsystems and commands are defined here...
  PowerDistribution pdp = new PowerDistribution(0, ModuleType.kCTRE);
  private final XboxController system_controller = new XboxController(0);
  private final JoystickButton A = new JoystickButton(system_controller, XboxController.Button.kA.value);
  private final JoystickButton B = new JoystickButton(system_controller, XboxController.Button.kB.value);
  private final ShooterSubsystem shooter = new ShooterSubsystem();
  private final ShooterAngleSubsystem shooter_angle_subsystem = new ShooterAngleSubsystem();
  
  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
    final ChassisSubsystem chassisSubsystem = new ChassisSubsystem();

    // Configure the button bindings
    configureButtonBindings();
    
    chassisSubsystem.setDefaultCommand(new DriveByJoy(chassisSubsystem, OI.leftJoy::getY, OI.rightJoy::getY, OI::joyTriggerOr));
  }

  

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing
   * it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    SmartDashboard.putNumber("Precentage", 0);
    // this.A.whenPressed(new ShooterMoveToAngleCommand(shooter_angle_subsystem));
    this.B.whileHeld(new ShootBallOnPrecentageCommand(shooter));
    this.A.whileHeld(new ShooterAngleMoveTestCommand(shooter_angle_subsystem)); 
   }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() { 
    // An ExampleCommand will run in autonomous
    return null;
  }
}
