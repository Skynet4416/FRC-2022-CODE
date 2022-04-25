// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.Arrays;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import org.photonvision.PhotonCamera;

import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
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
import frc.robot.Constants.Shooter;
import frc.robot.Constants.Chassis.TurnToAngleConstants;
import frc.robot.Constants.Field.Waypoints;
import frc.robot.commands.ShootAndIndexWhenRPMIsRead;
import frc.robot.commands.Autonomous.IntakeWithDeadLine;
import frc.robot.commands.Autonomous.ShootBall;
import frc.robot.commands.Chassis.CurvetureDrive;
import frc.robot.commands.Chassis.DriveByJoy;
import frc.robot.commands.Chassis.IntakeAndIndexCommandGroup;

import frc.robot.commands.ElevatorUpAndDown.ElevatorDownCommand;
import frc.robot.commands.ElevatorUpAndDown.ElevatorUpCommand;
import frc.robot.commands.Indexing.IndexCommand;
import frc.robot.commands.Intake.IntakeSpinUp;
import frc.robot.commands.Shooter.CalcCommand;
import frc.robot.commands.Shooter.ConstantCalc;
import frc.robot.commands.Shooter.ShootingSequenceCommandGroup;
import frc.robot.commands.Shooter.SpeedShooter;
import frc.robot.commands.ShooterAngle.ConstantAngleMove;
import frc.robot.commands.ShooterAngle.ShooterAngleMoveLeftCommand;
import frc.robot.commands.ShooterAngle.ShooterAngleMoveRightCommand;
import frc.robot.commands.ShooterAngle.ShooterMoveToConstantAngle;
import frc.robot.commands.ShooterAngle.ShooterMoveToGivenAngleCommand;
import frc.robot.subsystems.ChassisSubsystem;
import frc.robot.subsystems.ElevatorUpAndDownSubsystem;
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
  public ChassisSubsystem chassisSubsystem = new ChassisSubsystem();
  private IndexingSubsystem indexingSubsystem = new IndexingSubsystem();
  private ShooterAngleSubsystem shooterAngleSubsystem = new ShooterAngleSubsystem();
  private ShooterSubsystem shooterSubsystem = new ShooterSubsystem();
  // private HookUpAndDownSubsystem hookUpSubsystem = new HookUpAndDownSubsystem();
  private ElevatorUpAndDownSubsystem elevatorUpAndDownSubsystem = new ElevatorUpAndDownSubsystem();
  // private ElevatorAngleSubsystem elevatorAngleSubsystem = new ElevatorAngleSubsystem();
  private PhotonCamera front_camera = new PhotonCamera("front");
  private PhotonCamera back_camera = new PhotonCamera("back");
    /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    front_camera = new PhotonCamera("front");
    Globals.front =front_camera;
    back_camera = new PhotonCamera("back");
    Globals.back = back_camera;
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
    DoubleSupplier throttle = () -> 0.8;
    chassisSubsystem.setDefaultCommand(new DriveByJoy(chassisSubsystem, OI.leftJoy::getY, OI.rightJoy::getY, triggeror, throttle));
    shooterAngleSubsystem.setDefaultCommand(new ParallelCommandGroup(new ConstantCalc(),new ConstantAngleMove(shooterAngleSubsystem)));
    shooterSubsystem.setDefaultCommand(new SpeedShooter(shooterSubsystem));
    // configure OI
    OI.A.whileHeld(new IntakeAndIndexCommandGroup(intakeSubsystem, indexingSubsystem));
    // // OI.left_trigger.whileHeld(new IndexCommand(indexingSubsystem, false));
    // OI.B.whileHeld(new IndexCommand(indexingSubsystem, false));
    // OI.X.whileHeld(new ShootBallOnPrecentageCommand(shooterSubsystem));
    // OI.Y.whileHeld(new IndexCommand(indexingSubsystem,true));
// OI.B.whenHeld(new ElevatorByDistance(elevatorUpAndDownSubsystem, 0.5));
    // OI.X.whileHeld(new ParallelCommandGroup(new IndexCommand(indexingSubsystem, true), new IntakeSpinUp(intakeSubsystem,true)));
    // OI.right_trigger.whileHeld(new ElevatorUpCommand(elevatorUpAndDownSubsystem));
    // OI.right_bumper.whenPresqdsed(new ElevatorByDistance(elevatorUpAndDownSubsystem, 0.5));
    // OI.left_bumper.whenPressed(new ElevatorDownCommand(elevatorUpAndDownSubsystem));
    // OI.Y.whileHeld(new IntakeSpinUp(intakeSubsystem, true));
    // OI.X.whileHeld(new ShootAndIndexWhenRPMIsRead(shooterSubsystem,indexingSubsystem));
    // OI.X.whenReleased(new SequentialCommandGroup(new ShooterMoveToConstantAngle(shooterAngleSubsystem,10),new ShooterMoveToConstantAngle(shooterAngleSubsystem, 37)));
    // OI.left_bumper.whileHeld(new ShooterAngleMoveLeftCommand(shooterAngleSubsystem));
    // OI.right_bumper.whileHeld(new ShooterAngleMoveRightCommand(shooterAngleSubsystem));
    // OI.B.whenPressed(new ShooterMoveToConstantAngle(shooterAngleSubsystem, 62-45));
    OI.X.whileHeld(new ShootingSequenceCommandGroup(chassisSubsystem, indexingSubsystem, shooterAngleSubsystem, shooterSubsystem));
      OI.right_bumper.whileHeld(new ElevatorUpCommand(elevatorUpAndDownSubsystem));
      OI.left_bumper.whileHeld(new ElevatorDownCommand(elevatorUpAndDownSubsystem));
    // OI.Y.whenHeld(new HookUpCommand(hookUpSubsystem))`;
    // OI.X.whenPressed(new ShooterMoveToConstantAngle(shooterAngleSubsystem, 45));
    // OI.right_bumper.whenPressed(new ElevatorByDistance(elevatorUpAndDownSubsystem, 0.7));
    // OI.left_bumper.whenPressed(new ElevatorByDistance(elevatorUpAndDownSubsystem, -0.30));

    // ));

    OI.B.whileHeld(new IntakeSpinUp(intakeSubsystem, true));
    // OI.A.whileHeld(new IntakeSpinUp(intakeSubsystem, false));
  }
  private void configureSmartDashboard()
  {
    SmartDashboard.putNumber("Camera Offset", Constants.CAMERA_OFFSET);
    SmartDashboard.putNumber("Precentage", 0);
    SmartDashboard.putNumber(Constants.Chassis.SmartDashboard.TurnAnglePointAx, 0);
    SmartDashboard.putNumber(Constants.Chassis.SmartDashboard.TurnAnglePointBx, 0.3333333);
    SmartDashboard.putNumber(Constants.Chassis.SmartDashboard.TurnAnglePointCx,2* 0.33333333);
    SmartDashboard.putNumber(Constants.Chassis.SmartDashboard.TurnAnglePointDx, 1);
    SmartDashboard.putNumber(Constants.Chassis.SmartDashboard.TurnAnglePointAy, 1);
    SmartDashboard.putNumber(Constants.Chassis.SmartDashboard.TurnAnglePointCy, 1);
    SmartDashboard.putNumber(Constants.Chassis.SmartDashboard.TurnAnglePointBy, 0.5);
    SmartDashboard.putNumber(Constants.Chassis.SmartDashboard.TurnAnglePointDy, 0);
    SmartDashboard.putNumber("Right Set Angle", 0);
    SmartDashboard.putNumber(Shooter.Physics.SmartDashboard.ShooterKP, Shooter.Physics.PID.kP);
    SmartDashboard.putNumber(Shooter.Physics.SmartDashboard.ShooterKI, Shooter.Physics.PID.kI);
    SmartDashboard.putNumber(Shooter.Physics.SmartDashboard.ShooterKD, Shooter.Physics.PID.kD);
    SmartDashboard.putNumber(Shooter.Physics.SmartDashboard.ShooterKF, Shooter.Physics.PID.kF);  
    SmartDashboard.putNumber("Shooter Threashold",0);
    SmartDashboard.putNumber("RPM Precentage Loss",0);
    SmartDashboard.putNumber("Threashold Y",0);
    SmartDashboard.putNumber("Turn To Angle KP", TurnToAngleConstants.kP);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // SequentialCommandGroup autocommand = new SequentialCommandGroup();

    
    // autocommand.addCommands(new ShootBall(1,chassisSubsystem,indexingSubsystem,shooterAngleSubsystem,shooterSubsystem));
    // if (DriverStation.Alliance.Blue == DriverStation.getAlliance())
    // {
    //   Trajectory trajectory = TrajectoryGenerator.generateTrajectory(Arrays.asList(Globals.startPos,Waypoints.BlueAllince.Balls.blue_wall), chassisSubsystem.GetConfig());
    //   RamseteCommand start_to_wall = new RamseteCommand(trajectory, chassisSubsystem::getPosition, new RamseteController(2.0,0.7),  chassisSubsystem.getFeedforward(), chassisSubsystem.getDifferentialDriveKinematics(),chassisSubsystem::getSpeeds, chassisSubsystem.getLeftController(), chassisSubsystem.getRightController(), chassisSubsystem::setVoltage, chassisSubsystem);
    //   autocommand.addCommands(new ParallelCommandGroup(start_to_wall,new IntakeWithDeadLine(1, 1, intakeSubsystem, indexingSubsystem)));
    //   trajectory = TrajectoryGenerator.generateTrajectory(Arrays.asList(Waypoints.BlueAllince.Balls.blue_wall,Waypoints.BlueAllince.Balls.blue_terminal), chassisSubsystem.GetConfig());
    //   RamseteCommand wall_to_facing_terminal = new RamseteCommand(trajectory, chassisSubsystem::getPosition, new RamseteController(2.0,0.7),  chassisSubsystem.getFeedforward(), chassisSubsystem.getDifferentialDriveKinematics(),chassisSubsystem::getSpeeds, chassisSubsystem.getLeftController(), chassisSubsystem.getRightController(), chassisSubsystem::setVoltage, chassisSubsystem);
    //   autocommand.addCommands(new ParallelCommandGroup(wall_to_facing_terminal,new IntakeWithDeadLine(2,1, intakeSubsystem, indexingSubsystem)));
    //   autocommand.addCommands(new ShootBall(1.5,chassisSubsystem,indexingSubsystem,shooterAngleSubsystem,shooterSubsystem));
    //   trajectory = TrajectoryGenerator.generateTrajectory(Arrays.asList(Waypoints.BlueAllince.Balls.blue_terminal,Waypoints.BlueAllince.Balls.blue_on_terminal), chassisSubsystem.GetConfig());
    //   RamseteCommand facing_terminal_to_on_terminal = new RamseteCommand(trajectory, chassisSubsystem::getPosition, new RamseteController(2.0,0.7),  chassisSubsystem.getFeedforward(), chassisSubsystem.getDifferentialDriveKinematics(),chassisSubsystem::getSpeeds, chassisSubsystem.getLeftController(), chassisSubsystem.getRightController(), chassisSubsystem::setVoltage, chassisSubsystem);
    //   autocommand.addCommands(new ParallelCommandGroup(facing_terminal_to_on_terminal,new IntakeWithDeadLine(1,1,intakeSubsystem,indexingSubsystem)));
    //   autocommand.addCommands(new ShootBall(1.5,chassisSubsystem,indexingSubsystem,shooterAngleSubsystem,shooterSubsystem));
    // }
    // if (DriverStation.Alliance.Red == DriverStation.getAlliance())
    // {
    //   Trajectory trajectory = TrajectoryGenerator.generateTrajectory(Arrays.asList(Globals.startPos,Waypoints.RedAllince.Balls.red_wall), chassisSubsystem.GetConfig());
    //   RamseteCommand start_to_wall = new RamseteCommand(trajectory, chassisSubsystem::getPosition, new RamseteController(2.0,0.7),  chassisSubsystem.getFeedforward(), chassisSubsystem.getDifferentialDriveKinematics(),chassisSubsystem::getSpeeds, chassisSubsystem.getLeftController(), chassisSubsystem.getRightController(), chassisSubsystem::setVoltage, chassisSubsystem);
    //   autocommand.addCommands(new ParallelCommandGroup(start_to_wall,new IntakeWithDeadLine(1,1, intakeSubsystem, indexingSubsystem)));
    //   trajectory = TrajectoryGenerator.generateTrajectory(Arrays.asList(chassisSubsystem.getPosition(),Waypoints.RedAllince.Balls.red_terminal), chassisSubsystem.GetConfig());
    //   RamseteCommand wall_to_facing_terminal = new RamseteCommand(trajectory, chassisSubsystem::getPosition, new RamseteController(2.0,0.7),  chassisSubsystem.getFeedforward(), chassisSubsystem.getDifferentialDriveKinematics(),chassisSubsystem::getSpeeds, chassisSubsystem.getLeftController(), chassisSubsystem.getRightController(), chassisSubsystem::setVoltage, chassisSubsystem);
    //   autocommand.addCommands(new ParallelCommandGroup(wall_to_facing_terminal,new IntakeWithDeadLine(1, 1, intakeSubsystem, indexingSubsystem)));
    //   autocommand.addCommands(new ShootBall(1.5,chassisSubsystem,indexingSubsystem,shooterAngleSubsystem,shooterSubsystem));
    //   trajectory = TrajectoryGenerator.generateTrajectory(Arrays.asList(chassisSubsystem.getPosition(),Waypoints.RedAllince.Balls.red_on_terminal), chassisSubsystem.GetConfig());
    //   RamseteCommand facing_terminal_to_on_terminal = new RamseteCommand(trajectory, chassisSubsystem::getPosition, new RamseteController(2.0,0.7),  chassisSubsystem.getFeedforward(), chassisSubsystem.getDifferentialDriveKinematics(),chassisSubsystem::getSpeeds, chassisSubsystem.getLeftController(), chassisSubsystem.getRightController(), chassisSubsystem::setVoltage, chassisSubsystem);
    //   autocommand.addCommands(new ParallelCommandGroup(facing_terminal_to_on_terminal,new IntakeWithDeadLine(1, 1, intakeSubsystem, indexingSubsystem)));
    //   autocommand.addCommands(new ShootBall(1.5,chassisSubsystem,indexingSubsystem,shooterAngleSubsystem,shooterSubsystem));
    // }
    // // An ExampleCommand will run in autonomous
    // return autocommand;
      // return null;
  // Trajectory trajectory = TrajectoryGenerator.generateTrajectory(Arrays.asList(Globals.startPos,new Pose2d(0,0,new Rotation2d(0))), chassisSubsystem.GetConfig());
  // return new RamseteCommand(trajectory,chassisSubsystem::getPosition, new RamseteController(2.0,0.7),  chassisSubsystem.getFeedforward(), chassisSubsystem.getDifferentialDriveKinematics(),chassisSubsystem::getSpeeds, chassisSubsystem.getLeftController(), chassisSubsystem.getRightController(), chassisSubsystem::setVoltage, chassisSubsystem);
  // SequentialCommandGroup shooterMoveToAngleSequence = new SequentialCommandGroup(new ShooterMoveToConstantAngle(shooterAngleSubsystem,10),new ShooterMoveToConstantAngle(shooterAngleSubsystem, 37));
  // SequentialCommandGroup autocommand = new SequentialCommandGroup(shooterMoveToAngleSequence,new ParallelCommandGroup(new SequentialCommandGroup(new WaitCommand(1),new IndexCommand(indexingSubsystem, false)),new ShootBallCommand(shooterSubsystem)));
  // return new SequentialCommandGroup(new ParallelDeadlineGroup(new WaitCommand(5),new ShootingSequenceCommandGroup(chassisSubsystem, indexingSubsystem, shooterAngleSubsystem, shooterSubsystem,false)),new ParallelDeadlineGroup(new WaitCommand(1.75),new DriveByJoy(chassisSubsystem, ()->-0.7),new IntakeAndIndexCommandGroup(intakeSubsystem, indexingSubsystem)),new ParallelDeadlineGroup(new WaitCommand(1.75),new DriveByJoy(chassisSubsystem, ()->0.7)), new ParallelDeadlineGroup(new WaitCommand(5), new ShootingSequenceCommandGroup(chassisSubsystem, indexingSubsystem, shooterAngleSubsystem, shooterSubsystem,false)));
  // return new ParallelDeadlineGroup(autocommand,new WaitCommand(15),new SequentialCommandGroup(new WaitCommand(10),new DriveByJoy(chassisSubsystem, ()->0.6)));
  return new SequentialCommandGroup(new ParallelDeadlineGroup(new WaitCommand(2),new DriveByJoy(chassisSubsystem, ()->-0.7),new IntakeAndIndexCommandGroup(intakeSubsystem, indexingSubsystem)),new ParallelDeadlineGroup(new WaitCommand(7), new ShootingSequenceCommandGroup(chassisSubsystem, indexingSubsystem, shooterAngleSubsystem,shooterSubsystem)));
  }


}

