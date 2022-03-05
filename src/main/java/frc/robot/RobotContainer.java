// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.Arrays;
import java.util.function.BooleanSupplier;

import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.Field.Waypoints;
import frc.robot.commands.IntakeAndIndexCommandGroup;
import frc.robot.commands.ShootingSequenceCommandGroup;
import frc.robot.commands.Chassis.DriveByJoy;
import frc.robot.subsystems.ChassisSubsystem;
import frc.robot.subsystems.ElevatorAngleSubsystem;
import frc.robot.subsystems.ElevatorUpAndDownSubsystem;
import frc.robot.subsystems.HookUpAndDownSubsystem;
import frc.robot.subsystems.IndexingSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterAngleSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
  private ChassisSubsystem chassisSubsystem = new ChassisSubsystem();
  private IndexingSubsystem indexingSubsystem = new IndexingSubsystem();
  private ShooterAngleSubsystem shooterAngleSubsystem = new ShooterAngleSubsystem();
  private ShooterSubsystem shooterSubsystem = new ShooterSubsystem();
  private HookUpAndDownSubsystem hookUpSubsystem = new HookUpAndDownSubsystem();
  private ElevatorUpAndDownSubsystem elevatorUpAndDownSubsystem = new ElevatorUpAndDownSubsystem();
  private ElevatorAngleSubsystem elevatorAngleSubsystem = new ElevatorAngleSubsystem();
  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {

    chassisSubsystem.resetEncoders();
    configureSmartDashboard();
    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    BooleanSupplier triggeror = () -> OI.leftJoy.getTrigger() || OI.rightJoy.getTrigger();
    chassisSubsystem.setDefaultCommand(new DriveByJoy(chassisSubsystem, OI.leftJoy::getY, OI.rightJoy::getY, triggeror));
    // configure OI
    OI.A.whileHeld(new IntakeAndIndexCommandGroup(intakeSubsystem, indexingSubsystem));
    OI.B.whenActive(new ShootingSequenceCommandGroup(chassisSubsystem,indexingSubsystem,shooterAngleSubsystem,shooterSubsystem));


  }
  private void configureSmartDashboard()
  {
    SmartDashboard.putNumber(Constants.Chassis.SmartDashboard.TurnAnglePointAx, 0);
    SmartDashboard.putNumber(Constants.Chassis.SmartDashboard.TurnAnglePointBx, 0.3333333);
    SmartDashboard.putNumber(Constants.Chassis.SmartDashboard.TurnAnglePointCx,2* 0.33333333);
    SmartDashboard.putNumber(Constants.Chassis.SmartDashboard.TurnAnglePointDx, 1);
    SmartDashboard.putNumber(Constants.Chassis.SmartDashboard.TurnAnglePointAy, 1);
    SmartDashboard.putNumber(Constants.Chassis.SmartDashboard.TurnAnglePointCy, 1);
    SmartDashboard.putNumber(Constants.Chassis.SmartDashboard.TurnAnglePointBy, 0.5);
    SmartDashboard.putNumber(Constants.Chassis.SmartDashboard.TurnAnglePointDy, 0);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    SequentialCommandGroup autocommand = new SequentialCommandGroup();
    Command wait1sec = new WaitCommand(1);
    IntakeAndIndexCommandGroup intake_index = new IntakeAndIndexCommandGroup(intakeSubsystem,indexingSubsystem);
    ParallelDeadlineGroup intake_with_deadline = new ParallelDeadlineGroup(new WaitCommand(2),new SequentialCommandGroup(wait1sec,intake_index));
    ParallelDeadlineGroup shoot = new ParallelDeadlineGroup(wait1sec,new ShootingSequenceCommandGroup(chassisSubsystem,indexingSubsystem , shooterAngleSubsystem, shooterSubsystem));
    ParallelDeadlineGroup shoot2 = new ParallelDeadlineGroup(new WaitCommand(2),new ShootingSequenceCommandGroup(chassisSubsystem,indexingSubsystem , shooterAngleSubsystem, shooterSubsystem));

    autocommand.addCommands(shoot);
    if (DriverStation.Alliance.Blue == DriverStation.getAlliance())
    {
      Trajectory trajectory = TrajectoryGenerator.generateTrajectory(Arrays.asList(Globals.startPos,Waypoints.BlueAllince.Balls.blue_wall), chassisSubsystem.GetConfig());
      RamseteCommand start_to_wall = new RamseteCommand(trajectory, chassisSubsystem::getPosition, new RamseteController(2.0,0.7),  chassisSubsystem.getFeedforward(), chassisSubsystem.getDifferentialDriveKinematics(),chassisSubsystem::getSpeeds, chassisSubsystem.getLeftController(), chassisSubsystem.getRightController(), chassisSubsystem::setVoltage, chassisSubsystem);
      autocommand.addCommands(new ParallelCommandGroup(start_to_wall,new SequentialCommandGroup(wait1sec,intake_index)));
      trajectory = TrajectoryGenerator.generateTrajectory(Arrays.asList(chassisSubsystem.getPosition(),Waypoints.BlueAllince.Balls.blue_terminal), chassisSubsystem.GetConfig());
      RamseteCommand wall_to_facing_terminal = new RamseteCommand(trajectory, chassisSubsystem::getPosition, new RamseteController(2.0,0.7),  chassisSubsystem.getFeedforward(), chassisSubsystem.getDifferentialDriveKinematics(),chassisSubsystem::getSpeeds, chassisSubsystem.getLeftController(), chassisSubsystem.getRightController(), chassisSubsystem::setVoltage, chassisSubsystem);
      autocommand.addCommands(new ParallelCommandGroup(wall_to_facing_terminal,intake_with_deadline));
      autocommand.addCommands(shoot2);
      trajectory = TrajectoryGenerator.generateTrajectory(Arrays.asList(chassisSubsystem.getPosition(),Waypoints.BlueAllince.Balls.blue_on_terminal), chassisSubsystem.GetConfig());
      RamseteCommand facing_terminal_to_on_terminal = new RamseteCommand(trajectory, chassisSubsystem::getPosition, new RamseteController(2.0,0.7),  chassisSubsystem.getFeedforward(), chassisSubsystem.getDifferentialDriveKinematics(),chassisSubsystem::getSpeeds, chassisSubsystem.getLeftController(), chassisSubsystem.getRightController(), chassisSubsystem::setVoltage, chassisSubsystem);
      autocommand.addCommands(new ParallelCommandGroup(facing_terminal_to_on_terminal,intake_with_deadline));
      autocommand.addCommands(shoot2);
    }
    if (DriverStation.Alliance.Red == DriverStation.getAlliance())
    {
      Trajectory trajectory = TrajectoryGenerator.generateTrajectory(Arrays.asList(Globals.startPos,Waypoints.RedAllince.Balls.red_wall), chassisSubsystem.GetConfig());
      RamseteCommand start_to_wall = new RamseteCommand(trajectory, chassisSubsystem::getPosition, new RamseteController(2.0,0.7),  chassisSubsystem.getFeedforward(), chassisSubsystem.getDifferentialDriveKinematics(),chassisSubsystem::getSpeeds, chassisSubsystem.getLeftController(), chassisSubsystem.getRightController(), chassisSubsystem::setVoltage, chassisSubsystem);
      autocommand.addCommands(new ParallelCommandGroup(start_to_wall,new SequentialCommandGroup(wait1sec,intake_index)));
      trajectory = TrajectoryGenerator.generateTrajectory(Arrays.asList(chassisSubsystem.getPosition(),Waypoints.RedAllince.Balls.red_terminal), chassisSubsystem.GetConfig());
      RamseteCommand wall_to_facing_terminal = new RamseteCommand(trajectory, chassisSubsystem::getPosition, new RamseteController(2.0,0.7),  chassisSubsystem.getFeedforward(), chassisSubsystem.getDifferentialDriveKinematics(),chassisSubsystem::getSpeeds, chassisSubsystem.getLeftController(), chassisSubsystem.getRightController(), chassisSubsystem::setVoltage, chassisSubsystem);
      autocommand.addCommands(new ParallelCommandGroup(wall_to_facing_terminal,intake_with_deadline));
      autocommand.addCommands(shoot2);
      trajectory = TrajectoryGenerator.generateTrajectory(Arrays.asList(chassisSubsystem.getPosition(),Waypoints.RedAllince.Balls.red_on_terminal), chassisSubsystem.GetConfig());
      RamseteCommand facing_terminal_to_on_terminal = new RamseteCommand(trajectory, chassisSubsystem::getPosition, new RamseteController(2.0,0.7),  chassisSubsystem.getFeedforward(), chassisSubsystem.getDifferentialDriveKinematics(),chassisSubsystem::getSpeeds, chassisSubsystem.getLeftController(), chassisSubsystem.getRightController(), chassisSubsystem::setVoltage, chassisSubsystem);
      autocommand.addCommands(new ParallelCommandGroup(facing_terminal_to_on_terminal,intake_with_deadline));
      autocommand.addCommands(shoot2);
    }
    // An ExampleCommand will run in autonomous
    return autocommand;
  }
}