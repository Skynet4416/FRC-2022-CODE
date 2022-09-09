package frc.robot.lib.Flywheel.Subsystem;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.wpi.first.math.system.plant.DCMotor;
import frc.robot.lib.base.MotorControllers.MotorControllerBase;
import frc.robot.lib.base.Subsytems.MySubsytemBase;

public class DoubleFlywheel extends MySubsytemBase {
    private Dictionary<MotorControllerBase, DCMotor> left_side_motors, right_side_motors;

    public DoubleFlywheel(Hashtable<MotorControllerBase, DCMotor> left_side_motors,
            Hashtable<MotorControllerBase, DCMotor> right_side_motors) {
        this.left_side_motors = left_side_motors;
        this.right_side_motors = right_side_motors;

    }

}
