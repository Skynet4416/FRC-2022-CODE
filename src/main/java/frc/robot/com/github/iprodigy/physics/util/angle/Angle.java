package frc.robot.com.github.iprodigy.physics.util.angle;

import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Wither;
import frc.robot.com.github.iprodigy.physics.util.MathUtil;
import frc.robot.com.github.iprodigy.physics.util.Quadrant;
import frc.robot.com.github.iprodigy.physics.util.abstraction.Computational;
import frc.robot.com.github.iprodigy.physics.util.abstraction.Quantifiable;

import static frc.robot.com.github.iprodigy.physics.util.angle.AngleUnit.*;

@Value
public class Angle implements Quantifiable<Double>, Comparable<Angle>, Computational<Angle> {
	public static final Angle ZERO = new Angle(0.0, RADIANS), RIGHT = new Angle(90, DEGREES);

	@Wither
	private final double value;
	private final AngleUnit unit;
	public Angle(double value, AngleUnit unit)
	{
		this.value = value;
		this.unit = unit;
	}
	public Angle convert(@NonNull final AngleUnit to) {
		return new Angle(this.value * this.unit.convFactor(to), to);
	}

	public Angle simplified() {
		double deg = getDegrees() % 360;

		if (deg < 0)
			deg += 360;

		return new Angle(deg * AngleUnit.DEGREES.convFactor(this.unit), this.unit);
	}

	@Override
	public Angle add(final Angle other) {
		return this.withValue(this.value + other.convert(this.unit).getValue(this.unit));
	}

	@Override // override to be more efficient by not creating unnecessary objects
	public Angle subtract(final Angle other) {
		return this.withValue(this.value - other.convert(this.unit).getValue(this.unit));
	}

	public Angle withValue(double d) {
		return new Angle(d, this.unit);
	}
	@Override
	public Angle multiply(final double scalar) {
		return this.withValue(this.value * scalar);
	}

	public Quadrant getQuadrant() {
		return Quadrant.values()[MathUtil.getQuadrant(getDegrees()) - 1];
	}

	public double getDegrees() {
		return this.getValue(DEGREES);
	}

	public double getRadians() {
		return this.getValue(RADIANS);
	}

	public double getGradians() {
		return this.getValue(GRADIANS);
	}

	public double getTurns() {
		return this.getValue(TURNS);
	}

	public double getValue(final AngleUnit au) {
		return this.convert(au).value;
	}

	public double sin() {
		return Math.sin(getRadians());
	}

	public double cos() {
		return Math.cos(getRadians());
	}

	public double tan() {
		return Math.tan(getRadians());
	}

	public double csc() {
		return 1 / sin();
	}

	public double sec() {
		return 1 / cos();
	}

	public double cot() {
		return 1 / tan();
	}

	@Override
	public Double getMagnitude() {
		return this.getValue(unit);
	}

	@Override
	public int compareTo(@NonNull Angle o) {
		final Angle simpl = this.simplified();
		return Double.compare(simpl.value, o.simplified().convert(simpl.unit).value);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final Angle simpl = this.simplified(),
			other = ((Angle) o).simplified().convert(simpl.unit);
		return Double.compare(simpl.value, other.value) == 0;
	}

	@Override
	public int hashCode() {
		return Double.hashCode(this.simplified().getDegrees());
	}

}
