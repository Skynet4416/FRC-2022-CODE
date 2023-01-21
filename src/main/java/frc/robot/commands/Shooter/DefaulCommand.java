package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.ShooterAngle.ShooterMoveToGivenAngleCommand;
import frc.robot.lib.Physics.lib.Ball;
import frc.robot.subsystems.ShooterAngleSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class DefaulCommand extends CommandBase{
    private Ball ball;
    private ShooterSubsystem shooterSubsystem;
    private ShooterAngleSubsystem shooterAngleSubsystem;
    private  SequentialCommandGroup sequential_command;
    public DefaulCommand(ShooterSubsystem shooterSubsystem,Ball ball,ShooterAngleSubsystem shooterAngleSubsystem) {
        this.addRequirements(shooterSubsystem,shooterAngleSubsystem);
        this.ball = ball;
        this.shooterAngleSubsystem = shooterAngleSubsystem;
        sequential_command = new SequentialCommandGroup(new ConstantCalc(ball,shooterSubsystem,true), new ParallelDeadlineGroup(new WaitCommand(2), new ShooterMoveToGivenAngleCommand(shooterAngleSubsystem),new SpeedShooter(shooterSubsystem)));

    }
    @Override
    public void initialize(){
        sequential_command.initialize();
    }
    @Override
    public boolean isFinished()
    {
        return false;
    }
    @Override
    public void end(boolean interrupted)
    {
        sequential_command.end(interrupted);
    }



}
