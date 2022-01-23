// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.Chassis.Kinematics;
import frc.robot.Constants.Chassis.Odometry;
import frc.robot.sensors.NavxGyro;

public class ChassisSubsystem extends SubsystemBase {
  // Initialize motor controllers.
  private CANSparkMax _rightMaster = new CANSparkMax(Constants.Chassis.Motors.kMasterRight,MotorType.kBrushless);
  private CANSparkMax _rightSlave = new CANSparkMax(Constants.Chassis.Motors.kSlaveRight,MotorType.kBrushless);
  private CANSparkMax _leftMaster = new CANSparkMax(Constants.Chassis.Motors.kMasterLeft,MotorType.kBrushless);
  private CANSparkMax _leftSlave = new CANSparkMax(Constants.Chassis.Motors.kSlaveLeft,MotorType.kBrushless);
  private MotorControllerGroup m_left = new MotorControllerGroup(_leftMaster, _leftSlave);
  private MotorControllerGroup m_right = new MotorControllerGroup(_rightMaster, _rightSlave);

  private RelativeEncoder m_rightEncoder = _rightMaster.getEncoder();
  private RelativeEncoder m_leftEncoder = _leftMaster.getEncoder();

  DifferentialDrive m_drive = new DifferentialDrive(m_left, m_right);

  NavxGyro m_gyro = new NavxGyro(Port.kMXP);
  Pose2d m_pose = new Pose2d();
  private DifferentialDriveOdometry m_odometry = new DifferentialDriveOdometry(m_gyro.getHeading());

  private double getLeftDistance(){
      return m_leftEncoder.getPosition() * Odometry.DISTANCE_OF_ENCODER_COUNT;
  }

  private double getRightDistance(){
    return m_rightEncoder.getPosition() * Odometry.DISTANCE_OF_ENCODER_COUNT;
  }

  /**
   * Creates the subsystem and configures motor controllers.
   */
  public ChassisSubsystem () {
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
      m_pose = m_odometry.update(m_gyro.getHeading(), getLeftDistance(), getRightDistance());    
  }
}
