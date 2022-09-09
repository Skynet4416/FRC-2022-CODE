package frc.robot.lib.Flywheel.Subsystem;

import java.util.ArrayList;

import edu.wpi.first.math.Pair;
import edu.wpi.first.math.Vector;
import edu.wpi.first.math.controller.LinearQuadraticRegulator;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.system.LinearSystemLoop;
import edu.wpi.first.math.system.plant.DCMotor;
import frc.robot.lib.base.Controllers.IntegratedPID;
import frc.robot.lib.base.Controllers.LQR;
import frc.robot.lib.base.Controllers.ProfiledPID;
import frc.robot.lib.base.Controllers.VelocityPID;
import frc.robot.lib.base.MotorControllers.MotorControllerBase;
import frc.robot.lib.base.Subsytems.MySubsytemBase;
import frc.robot.lib.Physics.Constants;

public class SingleFlywheel extends MySubsytemBase implements LQR, ProfiledPID, IntegratedPID, VelocityPID {
    protected ArrayList<Pair<MotorControllerBase, DCMotor>> left_side;
    protected ArrayList<Pair<MotorControllerBase, DCMotor>> right_side;
    protected double wheel_radius_meters;

    {
        this.left_side = left_side;
        this.right_side = right_side;
        this.wheel_radius_meters = wheel_radius_meters;
    }

    @Override
    public LinearQuadraticRegulator getLQRController() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setQ(double Q) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setR(double R) {
        // TODO Auto-generated method stub

    }

    @Override
    public Vector calculate(Vector state) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LinearSystemLoop<N1, N1, N1> get_system_loop() {
        // TODO Auto-generated method stub
        return null;
    }

}
