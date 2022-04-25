package frc.robot.commands.ShooterAngle;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Globals;
import frc.robot.Constants.Shooter.Physics;
import frc.robot.Constants.Shooter.Physics.Motors;
import frc.robot.subsystems.ShooterAngleSubsystem;

public class ConstantAngleMove extends CommandBase{
    private ShooterAngleSubsystem shooterAngleSubsystem;
    public ConstantAngleMove(ShooterAngleSubsystem shooterAngleSubsystem)
    {
        this.shooterAngleSubsystem = shooterAngleSubsystem;
    }
    @Override
    public void execute()
    {
        if (!(Math.abs((shooterAngleSubsystem.getRightAbsuluteAngle() - Physics.right_home) - Globals.angle) <= Motors.AngleThreashold))
        {
        shooterAngleSubsystem.setPrecnetage(Motors.AnglePrecentage * (shooterAngleSubsystem.getRightAbsuluteAngle() - Physics.right_home > Globals.angle ?- 1 : 1));
        }
    }
    @Override
    public void end(boolean iterupted)
    {
        shooterAngleSubsystem.setPrecnetage(0);
    }
}
