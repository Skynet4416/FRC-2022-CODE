package frc.robot.subsystems;

import java.time.Instant;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Globals;
import frc.robot.Constants.Shooter.Motors;

public class ShooterAngleSubsystem extends SubsystemBase {
    private TalonSRX left_master = new TalonSRX(Motors.left_master);
    private TalonSRX right_slave = new TalonSRX(Motors.right_slave);
    public Instant start_time;
    private final boolean kDiscontinuityPresent = false;
    private final int kBookEnd_0 = ToUnits(Motors.kStartDeg);
    private final int kBookEnd_1 = ToUnits(Motors.kEndDeg);
    // private double time_to_finish = 1000; // in milliseconds
    public double start_angle;
    private int kTimeoutMs = 30;

    public ShooterAngleSubsystem() {
        left_master.configFactoryDefault();
        right_slave.configFactoryDefault();
        right_slave.follow(left_master);
        SmartDashboard.putNumber(Constants.Shooter.SmartDashboard.AngleToSet, 0);
        right_slave.setInverted(InvertType.FollowMaster);
        initQuadrature(left_master);
    }

    public void initQuadrature(TalonSRX talon) {
        /* get the absolute pulse width position */
        int pulseWidth = talon.getSensorCollection().getPulseWidthPosition();

        /**
         * If there is a discontinuity in our measured range, subtract one half
         * rotation to remove it
         */
        if (kDiscontinuityPresent) {

            /* Calculate the center */
            int newCenter;
            newCenter = (kBookEnd_0 + kBookEnd_1) / 2;
            newCenter &= 0xFFF;

            /**
             * Apply the offset so the discontinuity is in the unused portion of
             * the sensor
             */
            pulseWidth -= newCenter;
        }

        /**
         * Mask out the bottom 12 bits to normalize to [0,4095],
         * or in other words, to stay within [0,360) degrees
         */
        pulseWidth = pulseWidth & 0xFFF;

        /* Update Quadrature position */
        talon.getSensorCollection().setQuadraturePosition(pulseWidth, kTimeoutMs);
    }

    /**
     * @param units CTRE mag encoder sensor units
     * @return degrees rounded to tenths.
     */
    public double ToDeg(double units) {
        double deg = units * 360.0 / 4096.0;

        /* truncate to 0.1 res */
        deg *= 10;
        deg = (int) deg;
        deg /= 10;

        return deg;
    }

    public int ToUnits(double angle) {
        return (int) (angle * 4096 / 360);
    }

    public double GetAngle() {
        System.out.println(ToDeg(left_master.getSelectedSensorPosition()));
        return ToDeg(left_master.getSelectedSensorPosition());
    }

    public void Set(double precentage) {
        left_master.set(ControlMode.PercentOutput, precentage);
    }

    // public double map(double x, double in_min, double in_max, double out_min,
    // double out_max)
    // {
    // return (x*in_min) * (out_max-out_min) / (in_max-in_min) +out_min;
    // }
    @Override
    public void periodic() {
        SmartDashboard.putNumber(frc.robot.Constants.Shooter.SmartDashboard.LeftAngle, GetAngle());
        SmartDashboard.putNumber(frc.robot.Constants.Shooter.SmartDashboard.RightAngle, 180 - GetAngle());
    }
    public void SetAbs()
    {
        initQuadrature(left_master);
        initQuadrature(right_slave);
    }
}
