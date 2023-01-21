package frc.robot.commands.ShooterAngle;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.Elevator.Angle;
import frc.robot.Constants.Shooter.Physics;
import frc.robot.Constants.Shooter.Physics.Motors;
import frc.robot.subsystems.ShooterAngleSubsystem;

public class ShooterMoveToAngleProportional extends CommandBase {
    private static final double kP = 0.025;

    private ShooterAngleSubsystem _subsystem;
    private Supplier<Double> _angleSupplier;

    public ShooterMoveToAngleProportional(ShooterAngleSubsystem subsystem, Supplier<Double> angleSupplier) {
        this._subsystem = subsystem;
        this._angleSupplier = angleSupplier;
        addRequirements(this._subsystem);
    }

    @Override
    public void initialize() {
        return;
    }

    @Override
    public void execute() {
        double target = this._angleSupplier.get();
        if (target <= Motors.Min || target >= Motors.Max) {
            cancel();
        }

        double current = _subsystem.getRightAbsuluteAngle() - Physics.right_home;
        double percentage = Math.max(-1, Math.min(1, (target - current) * kP));
        this._subsystem.setPrecnetage(percentage);
    }

    @Override
    public boolean isFinished() {
        // Note that this never ends, because it takes a supplier (where the value can change) and not a constant angle.
        // This should be interrupted by whileHold or something similar.
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        this._subsystem.setPrecnetage(0);
    }
}
