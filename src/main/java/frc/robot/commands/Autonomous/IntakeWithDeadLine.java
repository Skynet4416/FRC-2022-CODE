package frc.robot.commands.Autonomous;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.Intake.IntakeAndIndexCommandGroup;
import frc.robot.subsystems.IndexingSubsystem;
import frc.robot.subsystems.IntakeSubsystem;

public class IntakeWithDeadLine extends ParallelDeadlineGroup{
    public IntakeWithDeadLine(double intake_time, double waiting_time, IntakeSubsystem intakeSubsystem, IndexingSubsystem indexing)
    {
        super(new WaitCommand(intake_time+waiting_time), new SequentialCommandGroup(new WaitCommand(waiting_time),new IntakeAndIndexCommandGroup(intakeSubsystem, indexing)));
    }
    
}
