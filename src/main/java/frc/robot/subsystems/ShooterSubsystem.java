package frc.robot.subsystems;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import frc.robot.Constants;
import frc.robot.Globals;
import frc.robot.Constants.Shooter;
import frc.robot.Constants.Shooter.Physics;
import frc.robot.Constants.Shooter.Physics.Motors;
import frc.robot.MethTools.VisionMeth;

public class ShooterSubsystem extends SubsystemBase {
    private TalonFX _bottom = new TalonFX(Motors.Kbottom);
    private TalonFX _top = new TalonFX(Motors.Ktop);
    public Solenoid _bottomLEDS = new Solenoid(PneumaticsModuleType.CTREPCM, 5);
    public Solenoid _rightLEDS = new Solenoid(PneumaticsModuleType.CTREPCM, 4);
    public Solenoid _leftLEDS = new Solenoid(PneumaticsModuleType.CTREPCM, 6);

    public ShooterSubsystem() {
        SmartDashboard.putNumber("Shooter Precentage", 0);
        _top.configFactoryDefault();
        _bottom.configFactoryDefault();
        _bottom.config_kD(0, Shooter.Physics.PID.kD);
        _bottom.config_kP(0, Shooter.Physics.PID.kP);
        _bottom.config_kI(0, Shooter.Physics.PID.kI, 0);
        _bottom.config_kF(0, Shooter.Physics.PID.kF);
        _top.config_kD(0, Shooter.Physics.PID.kD);
        _top.config_kP(0, Shooter.Physics.PID.kP);
        _top.config_kI(0, Shooter.Physics.PID.kI, 0);
        _top.config_kF(0, Shooter.Physics.PID.kF);
        // _top.setNeutralMode(NeutralMode.Brake);
        // _bottom.setNeutralMode(NeutralMode.Brake);
        // _top.setInverted(InvertType.InvertMotorOutput);
        _rightLEDS.set(false);
        _leftLEDS.set(false);
        _bottomLEDS.set(true);
    }

    @Override
    public void periodic() {
        PhotonCamera camera;
        // _bottom.config_kP(0,
        // SmartDashboard.getNumber(Shooter.Physics.SmartDashboard.ShooterKP,
        // Shooter.Physics.PID.kP));
        // _bottom.config_kI(0,
        // SmartDashboard.getNumber(Shooter.Physics.SmartDashboard.ShooterKI,
        // Shooter.Physics.PID.kI));
        // _bottom.config_kD(0,
        // SmartDashboard.getNumber(Shooter.Physics.SmartDashboard.ShooterKD,
        // Shooter.Physics.PID.kD));
        // _bottom.config_kF(0,
        // SmartDashboard.getNumber(Shooter.Physics.SmartDashboard.ShooterKF,
        // Shooter.Physics.PID.kF));
        // //
        // System.out.println(SmartDashboard.getNumber(Shooter.Physics.SmartDashboard.ShooterKP,
        // Shooter.Physics.PID.kP));
        // //
        // System.out.println(SmartDashboard.getNumber(Shooter.Physics.SmartDashboard.ShooterKD,
        // Shooter.Physics.PID.kP));
        // //
        // System.out.println(SmartDashboard.getNumber(Shooter.Physics.SmartDashboard.ShooterKI,
        // Shooter.Physics.PID.kP));
        // //
        // System.out.println(SmartDashboard.getNumber(Shooter.Physics.SmartDashboard.ShooterKF,
        // Shooter.Physics.PID.kP));

        // _top.config_kP(0,
        // SmartDashboard.getNumber(Shooter.Physics.SmartDashboard.ShooterKP,
        // Shooter.Physics.PID.kP));
        // _top.config_kI(0,
        // SmartDashboard.getNumber(Shooter.Physics.SmartDashboard.ShooterKI,
        // Shooter.Physics.PID.kI));
        // _top.config_kD(0,
        // SmartDashboard.getNumber(Shooter.Physics.SmartDashboard.ShooterKD,
        // Shooter.Physics.PID.kD));
        // _top.config_kF(0,
        // SmartDashboard.getNumber(Shooter.Physics.SmartDashboard.ShooterKF,
        // Shooter.Physics.PID.kF));
        // Constants.CAMERA_OFFSET = SmartDashboard.getNumber("Camera Offset",
        // Constants.CAMERA_OFFSET);

        // SmartDashboard.putNumber("Distance",
        // Math.sqrt(Math.pow(VisionMeth.distanceFromTarget(new
        // PhotonCamera("Front")),2) - Math.pow(Physics.hub_height +
        // Physics.shooter_height,2)));
        // SmartDashboard.putNumber("Distance", );
        // Physics.ShooterThreshold = SmartDashboard.getNumber("Shooter Threashold", 0);
        // Physics.RPM_presentange_loss = SmartDashboard.getNumber("RPM Precentage
        // Loss", 0);
        // Physics.threashold_y = SmartDashboard.getNumber("Threashold Y", 0);

        if (Globals.front != null) {
            PhotonPipelineResult fornt_pipeline = Globals.front.getLatestResult();
            if (fornt_pipeline.hasTargets()) {
                camera = Globals.front;
                Double angle = VisionMeth.angle_from_target(fornt_pipeline);
                Double distance = VisionMeth.quarticDistance(fornt_pipeline);
                _rightLEDS.set(true);
                _leftLEDS.set(true);
                SmartDashboard.putNumber("Angle From Target", angle);
                SmartDashboard.putNumber("Distance From Target", distance);
                Globals.hub_distance = distance;
            }

        } else if (Globals.back != null) {
            PhotonPipelineResult pipelineResult = Globals.back.getLatestResult();
            if (pipelineResult.hasTargets()) {
                Double angle = VisionMeth.angle_from_target(pipelineResult);
                Double distance = VisionMeth.quarticDistance(pipelineResult);
                Globals.hub_distance = distance;
                _rightLEDS.set(true);
                _leftLEDS.set(true);
                SmartDashboard.putNumber("Angle From Target", angle);
                SmartDashboard.putNumber("Distance From Target", Globals.hub_distance);
            }
        } else {
            _rightLEDS.set(false);
            _leftLEDS.set(false);
        }
        SmartDashboard.putNumber("Shooter bottom Velocity (RPM)", _bottom.getSelectedSensorVelocity() * 600 / 2048);
        SmartDashboard.putNumber("Shooter bottom Current", _bottom.getMotorOutputPercent());
        SmartDashboard.putNumber("Shooter Top Current", _top.getMotorOutputPercent());
        SmartDashboard.putNumber("Shooter Top Velocity (RPM)", _top.getSelectedSensorVelocity() * 600 / 2048);

    }

    public void SetRPM(double top_rpm, double botom_rpm) {
        // System.out.println(RPM);
        _bottom.set(ControlMode.Velocity, botom_rpm);
        _top.set(ControlMode.Velocity, top_rpm);
    }

    public TalonFX Getbottom() {
        return _bottom;
    }

    public TalonFX Gettop() {
        return _top;
    }

    public void setPercentage(double precentege) {
        _bottom.set(ControlMode.PercentOutput, precentege);
        _top.set(ControlMode.PercentOutput, precentege);
    }
}