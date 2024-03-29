package frc.robot.commands.Chassis;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Globals;
import frc.robot.subsystems.ChassisSubsystem;


import com.kauailabs.navx.frc.AHRS;
import frc.robot.MethTools.TurnToAngleMeth;
// DISCLAIMER: this doesn't have PID Controller, tho it needs it
import frc.robot.MethTools.Point;
import frc.robot.Constants.Chassis;

public class TurnToAngle extends CommandBase{
    private double targetAngle;
    private ChassisSubsystem chassis;
    private AHRS ahrs;
    private double angle=0;
    private Point[] kB;
    private double kP;
    private NetworkTableEntry angleFromTargetEntry;
    private double ahrs_angle;
    private double power;
    /**
        @param angle the target angle relative to the robot intial position(90 forward, 0 right etc)
    */

    public TurnToAngle(ChassisSubsystem chassis, AHRS ahrs){
        this.ahrs = ahrs;
        this.targetAngle = this.angle - 90 + this.ahrs.getAngle(); // the target angle based on the real world( ahrs.getAngle() )
        this.chassis = chassis;
        
        this.ahrs_angle = ahrs.getAngle();
    }

    @Override
    public void initialize(){
        // System.out.print("TurnToAngle Initiliazed");
        // this.ahrs_angle = Math.abs(ahrs.getAngle()%360);
        this.ahrs_angle = ahrs.getAngle();
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable visionTable = inst.getTable("Vision");
        this.angle = visionTable.getEntry("Angle From Target").getDouble(0);
        // System.out.println("TABLE TARGET: " + angle + ", CONST TARGET: " + Constants.Chassis.testAngle);
        // A1 B0.75 C0.5 D0
        this.kB = new Point[] {new Point(SmartDashboard.getNumber(Chassis.SmartDashboard.TurnAnglePointAx, 0),SmartDashboard.getNumber(Chassis.SmartDashboard.TurnAnglePointAy, 0)),new Point(SmartDashboard.getNumber(Chassis.SmartDashboard.TurnAnglePointBx, 0), SmartDashboard.getNumber(Chassis.SmartDashboard.TurnAnglePointBy, 0)), new Point(SmartDashboard.getNumber(Chassis.SmartDashboard.TurnAnglePointCx, 0), SmartDashboard.getNumber(Chassis.SmartDashboard.TurnAnglePointCy, 0)), new Point(SmartDashboard.getNumber(Chassis.SmartDashboard.TurnAnglePointDx, 0), SmartDashboard.getNumber(Chassis.SmartDashboard.TurnAnglePointDy, 0))};
        this.targetAngle = angle - 90 + ahrs_angle; // the target angle based on the real world( ahrs.getAngle() )
        // this.targetAngle = Math.abs(this.targetAngle%360);
        this.kP = SmartDashboard.getNumber(Chassis.SmartDashboard.kP, Chassis.TurnToAngleConstants.kP);
        Globals.joysticksControlEnbaled = false;
        power = TurnToAngleMeth.PController(this.targetAngle,ahrs_angle,this.kP,Math.abs((angle - 90)),Chassis.TurnToAngleConstants.kPmin,Chassis.TurnToAngleConstants.kPmax,this.kB);
    }

    @Override
    public boolean isFinished(){
        // this.ahrs_angle = Math.abs(ahrs.getAngle()%360);
        this.ahrs_angle = ahrs.getAngle();
        // System.out.println("ahrs angle: " + ahrs_angle + " angle got: " + angle + " target angle: " + this.targetAngle);
        power = TurnToAngleMeth.PController(this.targetAngle,ahrs_angle,this.kP,Math.abs((angle - 90)),Chassis.TurnToAngleConstants.kPmin,Chassis.TurnToAngleConstants.kPmax,this.kB);
           this.chassis.set(-power, power);
        //    System.out.println("LEFT - "+(0-power)+" RIGHT - "+(power));
        
        return Math.abs(this.targetAngle - this.ahrs.getAngle()) <= SmartDashboard.getNumber(Chassis.SmartDashboard.TurnAngleThreshold, 5);
    }

    @Override
    public void end(boolean interrupted){
        // System.out.print("TurnToAngle Finished");
        this.chassis.set(0, 0);
    }
    
}