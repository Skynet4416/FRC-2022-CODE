package frc.robot.lib.Meth;

import java.util.ArrayList;
import frc.robot.com.github.iprodigy.physics.util.vector.Vector;

import frc.robot.lib.Physics.lib.base.State;

public class Target {
    public double x_pos;
    public double y_pos;
    public double z_pos;
    public double x_size;
    public double y_size;
    public double z_size;
    public double minimum_entry_angle;
    public double maximum_entry_angle;
    public double minimum_entry_velocity;
    public double maximum_entry_velocity;

    public Target(Vector position, Vector size, double maximum_entry_angle, double minimum_entry_angle,
            double maximum_entry_velocity, double minimum_entry_velocity) {
        this.x_pos = position.getComponent(0);
        this.y_pos = position.getComponent(1);
        this.z_pos = position.getComponent(2);
        this.x_size = size.getComponent(0);
        this.y_size = size.getComponent(1);
        this.z_size = size.getComponent(2);
        this.maximum_entry_angle = maximum_entry_angle;
        this.minimum_entry_angle = minimum_entry_angle;
        this.maximum_entry_velocity = maximum_entry_velocity;
        this.minimum_entry_velocity = minimum_entry_velocity;
    }

    // private double LineFunction(double x, double slope, double
    // startA.getComponent(0), double startA.getComponent(1)) {
    // return (slope * x) - (slope * startA.getComponent(0)) +
    // startA.getComponent(1);
    // }

    public void set_distance(Double distance) {
        this.x_pos = distance;
    }

    private static Boolean LineLine(Vector startA, Vector endA, Vector startB, Vector endB) {
        // Boolean Avertical = (endA.getComponent(0) - startA.getComponent(0)) == 0;
        // Boolean Bvertical = (endB.getComponent(0) - startB.getComponent(0)) == 0;

        // if (!Avertical && !Bvertical) {
        // double slopeA = (endA.getComponent(1) - startA.getComponent(1))
        // / ((endA.getComponent(0) - startA.getComponent(0)) + 0.1);
        // double slopeB = (endB.getComponent(1) - startB.getComponent(1))
        // / ((endB.getComponent(0) - startB.getComponent(0)) + 0.1);
        // double heightA = slopeA * (-startA.getComponent(0)) + startA.getComponent(1);
        // double heightB = slopeB * (-startB.getComponent(0)) + startB.getComponent(1);

        // if (slopeA == slopeB)
        // return false;

        // double interceptionX = (heightB - heightA) / ((slopeA - slopeB));
        // double interceptionY = slopeA * (interceptionX - startA.getComponent(0)) +
        // startA.getComponent(1);

        // return ((interceptionX >= startA.getComponent(0) && interceptionX <=
        // endA.getComponent(0))
        // && (interceptionY >= startA.getComponent(1) && interceptionY <=
        // endA.getComponent(1)));
        // } else if (Avertical) {
        // double slopeB = (endB.getComponent(1) - startB.getComponent(1))
        // / ((endB.getComponent(0) - startB.getComponent(0)) + 0.1);
        // Double heightB = slopeB * (-startB.getComponent(0)) + startB.getComponent(1);

        // Double interceptionX = startA.getComponent(0);
        // Double interceptionY = slopeB * startA.getComponent(0) + heightB;

        // return ((interceptionX >= startA.getComponent(0) && interceptionX <=
        // endA.getComponent(0))
        // && (interceptionY >= startA.getComponent(1) && interceptionY <=
        // endA.getComponent(1)));
        // } else if (Bvertical) {
        // double slopeA = (endA.getComponent(1) - startA.getComponent(1))
        // / ((endA.getComponent(0) - startA.getComponent(0)) + 0.1);
        // Double heightA = slopeA * (-startA.getComponent(0)) + startA.getComponent(1);

        // Double interceptionX = startB.getComponent(0);
        // Double interceptionY = slopeA * startB.getComponent(0) + heightA;

        // return ((interceptionX >= startA.getComponent(0) && interceptionX <=
        // endA.getComponent(0))
        // && (interceptionY >= startA.getComponent(1) && interceptionY <=
        // endA.getComponent(1)));
        // } else
        // return false;

        double x1 = startA.getComponent(0); // line controlled by mouse
        double y1 = startA.getComponent(1);
        double x2 = endA.getComponent(0); // fixed end
        double y2 = endA.getComponent(1);

        double x3 = startB.getComponent(0); // static line
        double y3 = startB.getComponent(1);
        double x4 = endB.getComponent(0);
        double y4 = endB.getComponent(1);

        double uA = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3))
                / ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1) + 0.001);

        double uB = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3))
                / ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1) + 0.001);

        if (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1) {
            return true;
        }
        return false;
    }

    public Boolean check(ArrayList<State> states) {
        if (states.size() < 5)
            return false;
        Segment segment = new Segment(0.0, 0.0, 0.0, 0.0); // LX LY CX CY
        double x_max = this.x_pos + (x_size / 2.0);
        double x_min = this.x_pos - (x_size / 2.0);
        double y_max = this.y_pos + (y_size / 2.0);
        double y_min = this.y_pos - (y_size / 2.0);

        for (int index = 0; index < states.size(); index++) {
            segment.LX = segment.CX;
            segment.LY = segment.CY;
            segment.CX = states.get(index).position.getComponent(0);
            segment.CY = states.get(index).position.getComponent(1);

            double angle_deg = -(Math.toDegrees(Math.atan2(segment.CY - segment.LY, segment.CX - segment.LX)));

            double velocity = new Vector(segment.CX - segment.LX, segment.CY - segment.LY).getMagnitude();

            if (LineLine(new Vector(segment.LX, segment.LY), new Vector(segment.CX, segment.CY),
                    new Vector(x_min, y_max), new Vector(x_max, y_max))
                    || LineLine(new Vector(segment.LX, segment.LY), new Vector(segment.CX, segment.CY),
                            new Vector(x_min, y_min), new Vector(x_max, y_min))
                    || LineLine(new Vector(segment.LX, segment.LY), new Vector(segment.CX, segment.CY),
                            new Vector(x_min, y_max), new Vector(x_min, y_min))
                    ||
                    LineLine(new Vector(segment.LX, segment.LY), new Vector(segment.CX, segment.CY),
                            new Vector(x_max, y_max), new Vector(x_max, y_min))) {
                if (true) { // Z AXIS TBD
                    if (angle_deg >= minimum_entry_angle && angle_deg <= maximum_entry_angle) {
                        if (velocity >= minimum_entry_velocity && velocity <= maximum_entry_velocity) {
                            return true;
                        }
                    } else {
                    }
                }
            }

        }
        return false;
    }

    public Double check_distance(ArrayList<State> states) {
        if (states.size() > 5) {

            Segment segment = new Segment(0.0, 0.0, 0.0, 0.0); // LX LY CX CY
            double x_max = this.x_pos + (x_size / 2.0);
            double x_min = this.x_pos - (x_size / 2.0);
            double y_max = this.y_pos + (y_size / 2.0);
            double y_min = this.y_pos - (y_size / 2.0);

            for (int index = 0; index < states.size(); index++) {
                segment.LX = segment.CX;
                segment.LY = segment.CY;
                segment.CX = states.get(index).position.getComponent(0);
                segment.CY = states.get(index).position.getComponent(1);

                // System.out.println(segment.CY);

                double angle_deg = -(Math.toDegrees(Math.atan2(segment.CY - segment.LY, segment.CX - segment.LX)));

                double velocity = new Vector(segment.CX - segment.LX, segment.CY - segment.LY).getMagnitude();

                if (true) { // Z AXIS TBD
                    if (angle_deg >= minimum_entry_angle && angle_deg <= maximum_entry_angle) {
                        if (velocity >= minimum_entry_velocity && velocity <= maximum_entry_velocity) {
                            return ((segment.LX + segment.CX) / 2.0) - this.x_pos;
                        }
                    }
                }
            }
        }

        return 9999.0;
    }
}
