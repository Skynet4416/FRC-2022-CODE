package frc.robot.commands.ShooterAngle;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ShooterAngleSubsystem;

public class ShooterAngleMoveRightCommand extends CommandBase {

    private ShooterAngleSubsystem angle_moving;
    public ShooterAngleMoveRightCommand(ShooterAngleSubsystem angle_moving)
    {
        this.angle_moving = angle_moving;
    }
    @Override
    public void initialize()
    {
        angle_moving.setRight(-SmartDashboard.getNumber("Precentage", 0));
    }

    @Override
    public void execute()
    {
    }
    @Override
    public void end(boolean inter)
    {
        angle_moving.setRight(0);
    }
    @Override
    public boolean isFinished()
    {
return false;
    }
}


