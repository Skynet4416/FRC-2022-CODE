package frc.robot.lib.base.Subsytems;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import edu.wpi.first.math.Pair;
import edu.wpi.first.math.Vector;
import edu.wpi.first.math.controller.LinearQuadraticRegulator;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.system.LinearSystemLoop;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.lib.base.Controllers.LQR;
import frc.robot.lib.base.MotorControllers.ControlType;
import frc.robot.lib.base.MotorControllers.MotorControllerBase;
import frc.robot.lib.base.MotorControllers.MyTalonSRX;

public abstract class MySubsytemBase extends SubsystemBase {
    protected List<Pair<MotorControllerBase, DCMotor>> motor_controllers = new ArrayList<Pair<MotorControllerBase, DCMotor>>();

    public void add_motor_controller(MotorControllerBase motor_controller, DCMotor motor) {
        this.motor_controllers.add(new Pair<MotorControllerBase, DCMotor>(motor_controller, motor));
    }

}
