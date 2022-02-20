package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class OI {
    public static Joystick leftJoy = new Joystick(Constants.Inputs.joysticks.LEFT_JOY_PORT);
    public static Joystick rightJoy = new Joystick(Constants.Inputs.joysticks.RIGHT_JOY_PORT);

    public static boolean joyTriggerOr(){
        return leftJoy.getTrigger() || rightJoy.getTrigger();
    }

    public static XboxController system_controller = new XboxController(0);
    public static JoystickButton A = new JoystickButton(system_controller, XboxController.Button.kA.value);
    public static JoystickButton B = new JoystickButton(system_controller, XboxController.Button.kB.value);
    public static JoystickButton X = new JoystickButton(system_controller, XboxController.Button.kX.value);
    public static JoystickButton Y = new JoystickButton(system_controller, XboxController.Button.kY.value);
}
