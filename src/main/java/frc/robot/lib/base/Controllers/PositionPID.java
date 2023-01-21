package frc.robot.lib.base.Controllers;

import edu.wpi.first.math.controller.PIDController;

public interface PositionPID {
    public PIDController get_pid_config();

    public void set_p();

}
