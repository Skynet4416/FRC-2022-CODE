package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.Shooter;
import frc.robot.subsystems.ShooterAngleSubsystem;

public class ShooterAngleMoveTestCommand extends CommandBase {
    private ShooterAngleSubsystem angle_moving;
    public ShooterAngleMoveTestCommand(ShooterAngleSubsystem angle_moving)
    {
        this.angle_moving = angle_moving;
    }
    @Override
    public void initialize()
    {
        angle_moving.SetAbs();
        angle_moving.Set(SmartDashboard.getNumber("Precentage", 0));
    }

    @Override
    public void execute()
    {
    }
    @Override
    public void end(boolean inter)
    {
        angle_moving.Set(0);
    }
    @Override
    public boolean isFinished()
    {
return false;
    }
}
