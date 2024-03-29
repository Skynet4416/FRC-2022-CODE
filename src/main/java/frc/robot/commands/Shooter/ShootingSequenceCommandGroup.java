package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.ShootAndIndexWhenRPMIsRead;
import frc.robot.commands.Chassis.TurnToAngle;
import frc.robot.commands.Chassis.TurnToAnglePhoton;
import frc.robot.commands.Indexing.IndexCommand;
import frc.robot.commands.ShooterAngle.ShooterMoveToAngleCommand;
import frc.robot.commands.ShooterAngle.ShooterMoveToGivenAngleCommand;
import frc.robot.lib.Physics.lib.Ball;
import frc.robot.subsystems.ChassisSubsystem;
import frc.robot.subsystems.IndexingSubsystem;
import frc.robot.subsystems.ShooterAngleSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class ShootingSequenceCommandGroup extends SequentialCommandGroup{
    public ShootingSequenceCommandGroup(ChassisSubsystem chassis, IndexingSubsystem indexing, ShooterAngleSubsystem shooter_angle, ShooterSubsystem shooter,Ball ball)
    {
        // addCommands(new TurnToAngle(chassis, chassis.ahrs),new ShooterMoveToAngleCommand(shooter_angle),new ParallelCommandGroup(new IndexCommand(indexing, false),new ShootBallCommand(shooter)));
        // addCommands(new ParallelCommandGroup(new IndexCommand(indexing,false),new ShootBallCommand(shooter,indexing,false)));
        addCommands(new TurnToAnglePhoton(chassis), new ShooterMoveToGivenAngleCommand(shooter_angle),new ShootAndIndexWhenRPMIsRead(shooter, indexing));
    }
}
