package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class OI {
    public static Joystick leftJoy = new Joystick(Constants.Inputs.joysticks.LEFT_JOY_PORT);
    public static Joystick rightJoy = new Joystick(Constants.Inputs.joysticks.RIGHT_JOY_PORT);

    public static boolean joyTriggerOr(){
        return leftJoy.getTrigger() || rightJoy.getTrigger();
    }
}
