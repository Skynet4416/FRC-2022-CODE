package frc.robot.sensors;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.SPI.Port;

public class NavxGyro {
    public AHRS ahrs;

    public NavxGyro(Port port){
        ahrs = new AHRS(port);
    }

    public void reset(){
        ahrs.reset();
    }

    public double getYawRadians(){
        return Math.toRadians(ahrs.getYaw() + 180);
    }

    public Rotation2d getHeading(){
        return new Rotation2d(-ahrs.getAngle());
    }
    
}
