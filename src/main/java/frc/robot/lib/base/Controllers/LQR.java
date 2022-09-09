package frc.robot.lib.base.Controllers;

import edu.wpi.first.math.MatBuilder;
import edu.wpi.first.math.Num;
import edu.wpi.first.math.Vector;
import edu.wpi.first.math.controller.LinearQuadraticRegulator;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.system.LinearSystemLoop;

public interface LQR {
    public LinearQuadraticRegulator getLQRController();

    public void setQ(double Q);

    public void setR(double R);

    public Vector calculate(Vector state);

    public LinearSystemLoop<N1, N1, N1> get_system_loop();

}
