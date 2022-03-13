package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.ChassisSubsystem;
import frc.robot.subsystems.IndexingSubsystem;
import frc.robot.subsystems.ShooterAngleSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class ShootingSequenceCommandGroup extends SequentialCommandGroup{
    public ShootingSequenceCommandGroup(ChassisSubsystem chassis, IndexingSubsystem indexing, ShooterAngleSubsystem shooter_angle, ShooterSubsystem shooter)
    {
        // addCommands(new TurnToAngle(chassis, chassis.ahrs),new ShooterMoveToAngleCommand(shooter_angle),new ParallelCommandGroup(new IndexCommand(indexing),new ShootBallCommand(shooter)));
        // addCommands(new ParallelCommandGroup(new IndexCommand(indexing,false),new ShootBallCommand(shooter,indexing,false)));
    }
}
