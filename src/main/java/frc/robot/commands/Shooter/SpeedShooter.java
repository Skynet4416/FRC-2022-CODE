package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Globals;
import frc.robot.subsystems.ShooterSubsystem;

public class SpeedShooter extends CommandBase{
    private ShooterSubsystem shooter;
    public SpeedShooter(ShooterSubsystem shooter)
    {
        this.shooter = shooter;
    }
    @Override
    public void execute()
    {
        this.shooter.SetRPM(Globals.RPM/4);
    }
    public void end(boolean interapted)
    {
        this.shooter.setPercentage(0);
    }
}
