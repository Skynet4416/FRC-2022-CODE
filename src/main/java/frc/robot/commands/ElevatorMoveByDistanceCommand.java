package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ElevatorUpAndDownSubsystem;

public class ElevatorMoveByDistanceCommand extends CommandBase {
    private ElevatorUpAndDownSubsystem _elevator;
    private double _distance; //the intended distance for the climb arms
    private static double p = 36.2204*Math.PI; //the perimeter of the engine
    public ElevatorMoveByDistanceCommand(ElevatorUpAndDownSubsystem elevator, double distance)
    {
        this._elevator = elevator;
        addRequirements(elevator);
        this._distance = distance;
    }
    public void initialize()
    {
        double percent = this._distance/p;
        this._elevator.set(percent);
    }
}
