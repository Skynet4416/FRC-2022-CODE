package frc.robot.commands.Chassis;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.Indexing.IndexStuck;
import frc.robot.commands.Intake.IntakeSpinUp;
import frc.robot.subsystems.IndexingSubsystem;
import frc.robot.subsystems.IntakeSubsystem;

public class IntakeAndIndexCommandGroup extends ParallelCommandGroup{
    public IntakeAndIndexCommandGroup(IntakeSubsystem intake, IndexingSubsystem indexing)
    {
        addCommands(new IntakeSpinUp(intake,false),new IndexStuck(indexing));
    }
}
