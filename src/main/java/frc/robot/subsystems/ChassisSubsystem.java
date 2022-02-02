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
import edu.wpi.first.math.util.Units;

import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Globals;
import frc.robot.Constants.Chassis.FeedForward;
import frc.robot.Constants.Chassis.Odometry;
import frc.robot.Constants.Chassis.PID;
import frc.robot.Constants.Chassis.Physical;
import frc.robot.sensors.NavxGyro;

public class ChassisSubsystem extends SubsystemBase {
  // Initialize motor controllers.
  private CANSparkMax _rightMaster = new CANSparkMax(Constants.Chassis.Motors.kMasterRight,MotorType.kBrushless);
  private CANSparkMax _rightSlave = new CANSparkMax(Constants.Chassis.Motors.kSlaveRight,MotorType.kBrushless);
  private CANSparkMax _leftMaster = new CANSparkMax(Constants.Chassis.Motors.kMasterLeft,MotorType.kBrushless);
  private CANSparkMax _leftSlave = new CANSparkMax(Constants.Chassis.Motors.kSlaveLeft,MotorType.kBrushless);
  private MotorControllerGroup m_left; 
  private MotorControllerGroup m_right; 

  private RelativeEncoder _rightEncoder = _rightMaster.getEncoder();
  private RelativeEncoder _leftEncoder = _leftMaster.getEncoder();
  private Pose2d _pose = Globals.startPos;
  private DifferentialDrive m_drive; 

  private NavxGyro m_gyro = new NavxGyro(Port.kMXP);
  private DifferentialDriveKinematics _kinematics = new DifferentialDriveKinematics(Units.inchesToMeters(Physical.Robot_Width));
  private DifferentialDriveOdometry m_odometry = new DifferentialDriveOdometry(m_gyro.getHeading(),_pose);
  private SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(FeedForward.kS, FeedForward.kv,FeedForward.ka);

  private PIDController _left_controller = new PIDController(PID.kP, PID.kI, PID.kD);
  private PIDController _right_controller = new PIDController(PID.kP, PID.kI, PID.kD);


  public ChassisSubsystem () {

    _leftMaster.restoreFactoryDefaults();
    _leftSlave.restoreFactoryDefaults();
    _rightMaster.restoreFactoryDefaults();
    _rightSlave.restoreFactoryDefaults();

    _leftMaster.setIdleMode(IdleMode.kCoast);
    _leftSlave.setIdleMode(IdleMode.kCoast);
    _rightMaster.setIdleMode(IdleMode.kCoast);
    _rightSlave.setIdleMode(IdleMode.kCoast);

    m_right = new MotorControllerGroup(_rightMaster, _rightSlave);
    m_left = new MotorControllerGroup(_leftMaster, _leftSlave);
    
    m_drive = new DifferentialDrive(m_left, m_right);

  
  }


  public Rotation2d getHeading()
  {
    return m_gyro.getHeading();
  }


  public DifferentialDriveWheelSpeeds getSpeeds()
  {
    return new DifferentialDriveWheelSpeeds(_leftMaster.getEncoder().getVelocity()/ Physical.ratio * 2 * Math.PI * Units.inchesToMeters(Physical.wheel_radius)
    ,_rightMaster.getEncoder().getVelocity()/ Physical.ratio * 2 * Math.PI * Units.inchesToMeters(Physical.wheel_radius));
  }




  private double getLeftDistance(){
    return _leftEncoder.getPosition() * Odometry.DISTANCE_OF_ENCODER_COUNT;
  }





  private double getRightDistance(){
    return _rightEncoder.getPosition() * Odometry.DISTANCE_OF_ENCODER_COUNT;
  }

  public void zeroHeading() {
    m_gyro.reset();
  }



  /**
   * Creates the subsystem and configures motor controllers.
   */

  public void resetEncoders()
  {
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
    if (Math.abs(left) > 1 || Math.abs(right) > 1) return;
      
    m_drive.tankDrive(left, right);
  }

  

  @Override
  public void periodic() {
    _pose = m_odometry.update(m_gyro.getHeading(), getLeftDistance(), getRightDistance());    
  }
  
  
  
  public SimpleMotorFeedforward getFeedforward()
  {
    return feedforward;
  } 
  
  
  
  public PIDController getLeftController()
  {
    return _left_controller;
  }


  public PIDController getRightController()
  {
    return _right_controller;
  }

  public DifferentialDriveKinematics getDifferentialDriveKinematics()
  {
    return _kinematics;
  }

  public DifferentialDriveOdometry getOdometry()
  {
    return m_odometry;
  }
}
