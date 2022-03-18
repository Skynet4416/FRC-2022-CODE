package frc.robot.subsystems;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import org.photonvision.PhotonCamera;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;

import frc.robot.Globals;
import frc.robot.Constants.Shooter;
import frc.robot.Constants.Shooter.Physics;
import frc.robot.Constants.Shooter.Physics.Motors;
import frc.robot.MethTools.VisionMeth;

public class ShooterSubsystem extends SubsystemBase {
    private TalonFX _master = new TalonFX(Motors.Kmaster);
    private TalonFX _slave = new TalonFX(Motors.Kslave);

    public Solenoid _bottomLEDS = new Solenoid(PneumaticsModuleType.CTREPCM, 5);
    public Solenoid _rightLEDS = new Solenoid(PneumaticsModuleType.CTREPCM, 4);
    public Solenoid _leftLEDS = new Solenoid(PneumaticsModuleType.CTREPCM, 6);

    public ShooterSubsystem() {
        SmartDashboard.putNumber("Shooter Precentage", 0);
        _slave.configFactoryDefault();
        _master.configFactoryDefault();
        _master.config_kD(0, Shooter.Physics.PID.kD);
        _master.config_kP(0, Shooter.Physics.PID.kP);
        _master.config_kI(0, Shooter.Physics.PID.kI, 0);
        _master.config_kF(0, Shooter.Physics.PID.kF);
        _slave.config_kD(0, Shooter.Physics.PID.kD);
        _slave.config_kP(0, Shooter.Physics.PID.kP);
        _slave.config_kI(0, Shooter.Physics.PID.kI, 0);
        _slave.config_kF(0, Shooter.Physics.PID.kF);
        _slave.follow(_master);
        _slave.setInverted(InvertType.FollowMaster);
        _rightLEDS.set(true);
        _leftLEDS.set(true);
    }

    @Override
    public void periodic() {
        // _master.config_kP(0, SmartDashboard.getNumber(Shooter.Physics.SmartDashboard.ShooterKP, Shooter.Physics.PID.kP));
        // _master.config_kI(0, SmartDashboard.getNumber(Shooter.Physics.SmartDashboard.ShooterKI, Shooter.Physics.PID.kI));
        // _master.config_kD(0, SmartDashboard.getNumber(Shooter.Physics.SmartDashboard.ShooterKD, Shooter.Physics.PID.kD));
        // _master.config_kF(0, SmartDashboard.getNumber(Shooter.Physics.SmartDashboard.ShooterKF, Shooter.Physics.PID.kF));
        // // System.out.println(SmartDashboard.getNumber(Shooter.Physics.SmartDashboard.ShooterKP, Shooter.Physics.PID.kP));
        // // System.out.println(SmartDashboard.getNumber(Shooter.Physics.SmartDashboard.ShooterKD, Shooter.Physics.PID.kP));
        // // System.out.println(SmartDashboard.getNumber(Shooter.Physics.SmartDashboard.ShooterKI, Shooter.Physics.PID.kP));
        // // System.out.println(SmartDashboard.getNumber(Shooter.Physics.SmartDashboard.ShooterKF, Shooter.Physics.PID.kP));

        // _slave.config_kP(0, SmartDashboard.getNumber(Shooter.Physics.SmartDashboard.ShooterKP, Shooter.Physics.PID.kP));
        // _slave.config_kI(0, SmartDashboard.getNumber(Shooter.Physics.SmartDashboard.ShooterKI, Shooter.Physics.PID.kI));
        // _slave.config_kD(0, SmartDashboard.getNumber(Shooter.Physics.SmartDashboard.ShooterKD, Shooter.Physics.PID.kD));
        // _slave.config_kF(0, SmartDashboard.getNumber(Shooter.Physics.SmartDashboard.ShooterKF, Shooter.Physics.PID.kF));

        SmartDashboard.putNumber("Shooter Master Velocity (RPM)", _master.getSelectedSensorVelocity() * 600 / 2048);
        SmartDashboard.putNumber("Shooter Master Current", _master.getMotorOutputPercent());
        SmartDashboard.putNumber("Shooter Slave Current", _slave.getMotorOutputPercent());
        SmartDashboard.putNumber("Shooter Slave Velocity (RPM)", _slave.getSelectedSensorVelocity() * 600 / 2048);
        // SmartDashboard.putNumber("Distance", Math.sqrt(Math.pow(VisionMeth.distanceFromTarget(new PhotonCamera("Front")),2) - Math.pow(Physics.hub_height + Physics.shooter_height,2)));
        // SmartDashboard.putNumber("Distance", );
        _bottomLEDS.set(true);
        Physics.ShooterThreshold = SmartDashboard.getNumber("Shooter Threashold", 0);
        Physics.RPM_presentange_loss = SmartDashboard.getNumber("RPM Precentage Loss", 0);
        Physics.threashold_y = SmartDashboard.getNumber("Threashold Y", 0);
        

        // _rightLEDS.set(true);
        // _leftLEDS.set(true);
    }

    public void SetRPM(double RPM) {
        // System.out.println(RPM);
        _master.set(ControlMode.Velocity, RPM);
        _slave.set(ControlMode.Velocity, RPM);
    }

    public TalonFX GetMaster() {
        return _master;
    }

    public TalonFX GetSlave() {
        return _slave;
    }

    public void setPercentage(double precentege) {
        _master.set(ControlMode.PercentOutput, precentege);
        _slave.set(ControlMode.PercentOutput, precentege);
    }
}