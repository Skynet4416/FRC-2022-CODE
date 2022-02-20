package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IndexingSubsystem;

import frc.robot.Constants;

public class MoveBallsUpToShooter extends CommandBase {
    private IndexingSubsystem m_indexing;

    @Override
    public void initialize() {
        this.m_indexing.setPercentage(Constants.Indexing.kIndexingPercent);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        this.m_indexing.setPercentage(0);
    }
}
