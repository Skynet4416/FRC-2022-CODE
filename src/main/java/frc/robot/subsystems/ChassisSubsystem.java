// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.constraint.MaxVelocityConstraint;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Globals;
import frc.robot.Constants.Chassis;
import frc.robot.Constants.Chassis.FeedForward;
import frc.robot.Constants.Chassis.Odometry;
import frc.robot.Constants.Chassis.PID;
import frc.robot.Constants.Chassis.Physical;
import frc.robot.Constants.Chassis.TurnToAngleConstants;
import frc.robot.commands.Chassis.TurnToAngle;
import frc.robot.sensors.NavxGyro;

public class ChassisSubsystem extends SubsystemBase {
  // Initialize motor controllers.
  private CANSparkMax _rightMaster = new CANSparkMax(Constants.Chassis.Motors.kMasterRight, MotorType.kBrushless);
  private CANSparkMax _rightSlave = new CANSparkMax(Constants.Chassis.Motors.kSlaveRight, MotorType.kBrushless);
  private CANSparkMax _leftMaster = new CANSparkMax(Constants.Chassis.Motors.kMasterLeft, MotorType.kBrushless);
  private CANSparkMax _leftSlave = new CANSparkMax(Constants.Chassis.Motors.kSlaveLeft, MotorType.kBrushless);

  private MotorControllerGroup m_left;

  private MotorControllerGroup m_right;
  private final Field2d m_field = new Field2d();
  private RelativeEncoder _rightEncoder = _rightMaster.getEncoder();
  private RelativeEncoder _leftEncoder = _leftMaster.getEncoder();
  private Pose2d _pose = Globals.startPos;
  private DifferentialDrive m_drive;

  private NavxGyro m_gyro = new NavxGyro(Port.kMXP);
  // public AHRS ahrs;
  private DifferentialDriveKinematics _kinematics = new DifferentialDriveKinematics(
      Units.inchesToMeters(Physical.Robot_Width));
  private DifferentialDriveOdometry m_odometry = new DifferentialDriveOdometry(m_gyro.getHeading(), _pose);
  private SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(FeedForward.kS, FeedForward.kv,
      FeedForward.ka);

  private PIDController _left_controller = new PIDController(PID.kP, PID.kI, PID.kD);
  private PIDController _right_controller = new PIDController(PID.kP, PID.kI, PID.kD);
  private TrajectoryConfig _config = new TrajectoryConfig(Odometry.max_velocity, Odometry.max_acceleration);
  private PIDController turn_Controller;

  public ChassisSubsystem() {
    SmartDashboard.putData("Field", m_field);
    _config.setKinematics(_kinematics);
    _leftMaster.restoreFactoryDefaults();
    _leftSlave.restoreFactoryDefaults();
    _rightMaster.restoreFactoryDefaults();
    _rightSlave.restoreFactoryDefaults();

    m_right = new MotorControllerGroup(_rightMaster, _rightSlave);
    m_right.setInverted(true);
    m_left = new MotorControllerGroup(_leftMaster, _leftSlave);
    m_drive = new DifferentialDrive(m_left, m_right);
    m_drive.setExpiration(0.3);
    // ahrs = m_gyro.ahrs;
    turn_Controller = new PIDController(TurnToAngleConstants.kP, TurnToAngleConstants.kI, TurnToAngleConstants.kD);

  }

  public void setCervetureDrive(double idk1, double idk2) {
    m_drive.curvatureDrive(idk1, idk2, true);
  }

  public PIDController getRotatPidController() {
    return turn_Controller;
  }

  public Rotation2d getHeading() {
    return m_gyro.getHeading();
  }

  public DifferentialDriveWheelSpeeds getSpeeds() {
    return new DifferentialDriveWheelSpeeds(
        _leftMaster.getEncoder().getVelocity() / Physical.ratio * 2 * Math.PI
            * Units.inchesToMeters(Physical.wheel_radius),
        _rightMaster.getEncoder().getVelocity() / Physical.ratio * 2 * Math.PI
            * Units.inchesToMeters(Physical.wheel_radius));
  }

  public void setBreak() {
    _leftMaster.setIdleMode(IdleMode.kBrake);
    _leftSlave.setIdleMode(IdleMode.kBrake);
    _rightMaster.setIdleMode(IdleMode.kBrake);
    _rightSlave.setIdleMode(IdleMode.kBrake);
  }

  public void setArcadeDrive(double forward_speed, double rotation_speed) {
    m_drive.arcadeDrive(forward_speed, rotation_speed);
  }

  private double getLeftDistance() {

    return _leftEncoder.getPosition() * Chassis.x;
  }

  private double getRightDistance() {
    return _rightEncoder.getPosition() / Physical.ratio * 2 * Units.inchesToMeters(Physical.wheel_radius) * Math.PI;
  }

  public void zeroHeading() {
    m_gyro.reset();
  }

  /**
   * Creates the subsystem and configures motor controllers.
   */

  public void resetEncoders() {
    _rightEncoder.setPosition(0);
    _leftEncoder.setPosition(0);
  }

  /**
   * Set chassis motor output.
   * 
   * @param left  Left setpoint (percentage).
   * @param right Right setpoint (percentage).
   */

  public void set(double left, double right) {
    m_drive.tankDrive(left, right);
  }

  @Override
  public void periodic() {
    _pose = m_odometry.update(m_gyro.getHeading(), getLeftDistance(), getRightDistance());
    SmartDashboard.putNumber("Left Velocity", getSpeeds().leftMetersPerSecond);
    SmartDashboard.putNumber("Right Velocity", getSpeeds().rightMetersPerSecond);
    SmartDashboard.putNumber("X Position", m_odometry.getPoseMeters().getX());
    SmartDashboard.putNumber("Y Position", m_odometry.getPoseMeters().getY());
    SmartDashboard.putNumber("Rotation 2D", getHeading().getDegrees());
    m_field.setRobotPose(m_odometry.getPoseMeters());
    SmartDashboard.putNumber("Left Encoder", getLeftDistance());
    SmartDashboard.putNumber("Right Encoder", getRightDistance());
    turn_Controller.setP(TurnToAngleConstants.kP);
    // System.out.println(this._left_controller.calculate(1000));
  }

  public Pose2d getPosition() {
    return m_odometry.getPoseMeters();
  }

  public SimpleMotorFeedforward getFeedforward() {
    return feedforward;
  }

  public PIDController getLeftController() {
    return _left_controller;
  }

  public PIDController getRightController() {
    return _right_controller;
  }

  public DifferentialDriveKinematics getDifferentialDriveKinematics() {
    return _kinematics;
  }

  public DifferentialDriveOdometry getOdometry() {
    return m_odometry;
  }

  public TrajectoryConfig GetConfig() {
    return _config;
  }

  public void setVoltage(double left, double right) {
    m_left.setVoltage(left);
    m_right.setVoltage(right);
  }
}
