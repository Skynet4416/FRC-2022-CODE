package frc.robot.commands.Autonomous;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.Shooter.ShootingSequenceCommandGroup;
import frc.robot.lib.Physics.lib.Ball;
import frc.robot.subsystems.ChassisSubsystem;
import frc.robot.subsystems.IndexingSubsystem;
import frc.robot.subsystems.ShooterAngleSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class ShootBall extends ParallelDeadlineGroup{
    public ShootBall(double time_to_shoot,ChassisSubsystem chassis,IndexingSubsystem indexing,ShooterAngleSubsystem shooterAngleSubsystem,ShooterSubsystem shooterSubsystem,boolean vision,Ball ball)
    {
        super(new WaitCommand(time_to_shoot), new ShootingSequenceCommandGroup(chassis, indexing, shooterAngleSubsystem, shooterSubsystem,ball));
    }
    
}
