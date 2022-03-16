package frc.robot.MethTools;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;

public class Circle{
    // An OOP implementation of a circle as an object
    
    final static int PRECISION = 5;  // Decimal point precision

    double xpos, ypos, radius;
    public Circle(Point center, double radius)
    {
        this.xpos = center.x;
        this.ypos = center.y;
        this.radius =radius;
    }
    public Circle(double xposition, double yposition, double radius){
        this.xpos = xposition;
        this.ypos = yposition;
        this.radius = radius;
    }
    public Circle(Pose2d pose,double radius)
    {
        this.radius = radius;
        this.xpos = pose.getX();
        this.ypos = pose.getY();
    }

    private double round_perc(double value){
        return Math.round(value * Math.pow(10, PRECISION)) / Math.pow(10, PRECISION);
    }

    public double[] circle_intersect(Circle circle2){
        /*
        Intersection points of two circles using the construction of triangles
        as proposed by Paul Bourke, 1997.
        http://paulbourke.net/geometry/circlesphere/
        */

        double X1 = xpos;
        double Y1 = ypos;
        double X2 = circle2.xpos;
        double Y2 = circle2.ypos;
        double R1 = radius;
        double R2 = circle2.radius;

        double Dx = X2-X1;
        double Dy = Y2-Y1;
        double D = round_perc(Math.sqrt(Math.pow(Dx, 2) + Math.sqrt(Math.pow(Dy, 2))));
        // Distance between circle centres
        if (D > R1 + R2) // The circles do not intersect
            return null;
        else if (D < Math.abs(R2 - R1)) // No Intersect - One circle is contained within the other
            return null;
        else if (D == 0 && R1 == R2) // No Intersect - The circles are equal and coincident
            return null;
        else {
            double chorddistance = (Math.pow(R1,2) - Math.pow(R2,2) + Math.pow(D, 2))/(2*D);
            // distance from 1st circle's centre to the chord between intersects
            double halfchordlength = Math.sqrt(Math.pow(R1,2) - Math.pow(chorddistance,2));
            double chordmidpointx = X1 + (chorddistance*Dx)/D;
            double chordmidpointy = Y1 + (chorddistance*Dy)/D;
            double[]I1 = {round_perc(chordmidpointx + (halfchordlength*Dy)/D), round_perc(chordmidpointy - (halfchordlength*Dx)/D)};
            double dist1_sq = round_perc(Math.pow(I1[0], 2) + Math.pow(I1[1], 2));
            double[]I2 = {round_perc(chordmidpointx - (halfchordlength*Dy)/D), round_perc(chordmidpointy + (halfchordlength*Dx)/D)};
            double dist2_sq = round_perc(Math.pow(I2[0], 2) + Math.pow(I2[1], 2));

            if (dist1_sq < dist2_sq) return I1;
            else return I2;
        }
    }
    public static Pose2d pos_from_distance_and_angle(double distance,double angle,Pose2d seen_ball,Pose2d not_seen_pos)
    {
        double distance_from_balls = Math.sqrt(Math.pow(seen_ball.getX() - not_seen_pos.getX(), 2) + Math.pow(seen_ball.getY()-not_seen_pos.getY(),2));
        double distance_from_not_seen_ball = Math.sqrt(Math.pow(distance, 2)+Math.pow(distance_from_balls, 2) - 2 *distance * distance_from_balls * Math.sin(Math.toRadians(angle)));
        double[] cords = new Circle(seen_ball, distance).circle_intersect(new Circle(not_seen_pos, distance_from_not_seen_ball));
        return new Pose2d(cords[0],cords[1],new Rotation2d(0));
    }
}