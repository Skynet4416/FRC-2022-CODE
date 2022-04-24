package frc.robot.commands.Chassis;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.Globals;
import frc.robot.subsystems.ChassisSubsystem;

public class CurvetureDrive extends CommandBase{
    private ChassisSubsystem m_chassis;
    private DoubleSupplier m_left;
    private DoubleSupplier m_right;
    private BooleanSupplier m_straight;
    private DoubleSupplier m_limit;

    /**
     * Creates a new DriveByJoy command.
     * 
     * @param chassis Chassis subsystem.
     * @param left    Supplier for left motor output.
     * @param right   Supplier for right motor output.
     */
    public CurvetureDrive(ChassisSubsystem chassis, DoubleSupplier left, DoubleSupplier right, BooleanSupplier straight, DoubleSupplier limit) {
        this.addRequirements(chassis);
        this.m_chassis = chassis;
        this.m_left = left;
        this.m_right = right;
        this.m_straight = straight;
        this.m_limit = limit;
    }

    public CurvetureDrive(ChassisSubsystem chassis, DoubleSupplier power) {
        this.addRequirements(chassis);
        this.m_chassis = chassis;
        this.m_left = power;
        this.m_right = power;
        this.m_straight = () -> false;
        this.m_limit = () -> 0.5;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        if (Globals.joyControlEnbaled){
            double left = -this.m_left.getAsDouble();
            left = Math.abs(left) > Constants.Inputs.joysticks.MIN_POWER ? left : 0;
            
            double right = -this.m_right.getAsDouble();
            right = Math.abs(right) > Constants.Inputs.joysticks.MIN_POWER ? right : 0;



            if(m_straight.getAsBoolean()){
                double avg = (left + right) / 2;
                left = avg;
                right = avg;
            }

            this.m_chassis.setCervetureDrive(left, right);

        }

    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        this.m_chassis.set(0, 0);
    }
}