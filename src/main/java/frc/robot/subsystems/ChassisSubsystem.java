// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ChassisSubsystem extends SubsystemBase {
  // Initialize motor controllers.
  private PWMSparkMax _rightMaster = new PWMSparkMax(Constants.Chassis.Motors.kMasterRight);
  private PWMSparkMax _rightSlave = new PWMSparkMax(Constants.Chassis.Motors.kSlaveRight);
  private PWMSparkMax _leftMaster = new PWMSparkMax(Constants.Chassis.Motors.kMasterLeft);
  private PWMSparkMax _leftSlave = new PWMSparkMax(Constants.Chassis.Motors.kSlaveLeft);

  MotorControllerGroup m_left = new MotorControllerGroup(_leftMaster, _leftSlave);
  MotorControllerGroup m_right = new MotorControllerGroup(_rightMaster, _rightSlave);

  DifferentialDrive m_drive = new DifferentialDrive(m_left, m_right);
  
  /**
   * Creates the subsystem and configures motor controllers.
   */
  public ChassisSubsystem () {}

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
    // This method will be called once per scheduler run
  }
}
