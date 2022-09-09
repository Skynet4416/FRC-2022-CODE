package frc.robot.lib.base.MotorControllers;

import javax.swing.AbstractAction;

public abstract class MotorControllerBase {
    protected MotorControllerName controller_name;

    public MotorControllerName get_contorller_name() {
        return this.controller_name;
    }

    public abstract void set_voltage(double voltage);

    public abstract void set_precentage(double precentage);

    public abstract int get_CANID();
}
