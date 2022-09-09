package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Globals;
import frc.robot.Constants.Shooter.Physics;
import frc.robot.MethTools.ShooterMeth;
import frc.robot.subsystems.IndexingSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class ShootAndIndexWhenRPMIsRead extends CommandBase{
    private ShooterSubsystem _shooter;
    private IndexingSubsystem _indexing;
    public ShootAndIndexWhenRPMIsRead(ShooterSubsystem shooterSubsystem, IndexingSubsystem indexing){

        this._indexing = indexing;
        this._shooter = shooterSubsystem;
    }
    
    @Override
    public void initialize() {
      // _shooter.SetRPM(SmartDashboard.getNumber("Shooter Precentage", 0));
      _shooter.SetRPM(Globals.top_rpm,Globals.bottom_rpm);
      System.out.println("Shooter top_rpm: " + Globals.top_rpm);
      System.out.println("Shooter bottom_rpm: " + Globals.bottom_rpm);
      _shooter._leftLEDS.set(false);
      _shooter._rightLEDS.set(false);
    }
  
    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
      if (Math.abs(Math.abs(_shooter.GetMaster().getSelectedSensorVelocity()*600/2048) - Globals.top_rpm)<= Physics.ShooterThreshold && Math.abs(Math.abs(_shooter.GetMaster().getSelectedSensorVelocity()*600/2048) - Globals.bottom_rpm)<= Physics.ShooterThreshold)
      {
        _shooter._leftLEDS.set(true);
        _shooter._rightLEDS.set(true);
        _indexing.setPercentage(0.5);
      
      }else{
        // _shooter._leftLEDS.set(false);
        // _shooter._rightLEDS.set(false);
        _indexing.setPercentage(-0.1);
      
      }

    }
  
    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
      _shooter.setPercentage(0);
      _indexing.setPercentage(0);
      _shooter._leftLEDS.set(true);
      _shooter._rightLEDS.set(true);
    }
  
    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
      return false;
    }
    
}
