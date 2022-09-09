package frc.robot.com.github.iprodigy.physics.util.vector;

import frc.robot.com.github.iprodigy.physics.util.MathUtil;
import frc.robot.com.github.iprodigy.physics.util.abstraction.Computational;
import frc.robot.com.github.iprodigy.physics.util.abstraction.Quantifiable;
import frc.robot.com.github.iprodigy.physics.util.angle.Angle;
import frc.robot.com.github.iprodigy.physics.util.angle.AngleUnit;

import edu.wpi.first.math.util.Units;
import lombok.NonNull;
import lombok.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import static frc.robot.com.github.iprodigy.physics.util.MathUtil.floatsEqual;

@Value
public class Vector implements Quantifiable<Double>, Computational<Vector>, Comparable<Vector> {
	private final List<Double> components;
	private final Double magnitude;
	private final Angle angle;
	private final boolean isUnit;
	public String name;

	public Vector(@NonNull final List<Double> comps) {
		this.components = Collections.unmodifiableList(new ArrayList<>(comps));
		this.magnitude = getMagnitude();
		this.angle = new Angle(getDegree(), AngleUnit.DEGREES);
		this.isUnit = floatsEqual(this.magnitude, 1.0);
		name = null;
	}


	public Vector(@NonNull final Double... comps) {
		this(Arrays.asList(comps));
	}

	public Vector(@NonNull final double[] comps) {
		this(DoubleStream.of(comps).boxed().collect(Collectors.toList()));
	}

	public Vector(final double magnitude, final double angle, @NonNull final AngleUnit angleUnit) {
		this(magnitude, new Angle(angle, angleUnit));
	}

	public Vector(final double magnitude, @NonNull final Angle angle) {
		name = null;
		this.angle = angle.simplified();
		final Angle temp;
		final double x, y;

		switch (this.angle.getQuadrant()) {
			case I:
				x = magnitude * this.angle.cos();
				y = magnitude * this.angle.sin();
				break;

			case II:
				temp = this.angle.withValue(180 - this.angle.getValue(AngleUnit.DEGREES));
				x = -magnitude * temp.cos();
				y = magnitude * temp.sin();
				break;

			case III:
				temp = this.angle.withValue(this.angle.getValue(AngleUnit.DEGREES) - 180);
				x = -magnitude * temp.cos();
				y = -magnitude * temp.sin();
				break;

			case IV:
				temp = this.angle.withValue(this.angle.getValue(AngleUnit.DEGREES) - 360);
				x = magnitude * temp.cos();
				y = magnitude * temp.sin();
				break;

			default:
				x = y = 0;
				break;
		}

		this.components = Collections.unmodifiableList(Arrays.asList(x, y));
		this.magnitude = magnitude;
		this.isUnit = floatsEqual(this.magnitude, 1.0);
	}

	public Vector(final double[] from, final double[] to) {
		this(MathUtil.diff(to, from));
	}

	@Override
	public Vector add(@NonNull final Vector other) {
		final List<Double> comps = new ArrayList<>(components);

		for (int i = 0; i < other.numComponents(); i++) {
			if (comps.size() > i) {
				comps.set(i, comps.get(i) + other.getComponents().get(i));
			} else {
				comps.add(other.getComponents().get(i));
			}
		}

		return new Vector(comps);
	}

	@Override
	public Vector multiply(final double scalar) {
		return new Vector(this.components.stream().map(comp -> comp * scalar).collect(Collectors.toList()));
	}

	public double dotProduct(@NonNull final Vector other) {
		final int n = numComponents();
		if (n != other.numComponents())
			throw new IllegalArgumentException("Dot Product cannot be computed with a vector of differing dimension");

		double sum = 0.0;

		for (int i = 0; i < n; i++)
			sum += components.get(i) * other.getComponents().get(i);

		return sum;
	}

	public Vector crossProduct3D(@NonNull final Vector other) {
		return Vectors.crossProduct3D(this, other);
	}

	@Override
	public Double getMagnitude() {
		if (this.magnitude != null)
			return this.magnitude;

		return Math.sqrt(this.dotProduct(this));
	}

	public Angle angularDiff(@NonNull final Vector other) {
		return Vectors.angularDifference(this, other);
	}

	public double degreeDiff(@NonNull final Vector other) {
		return this.angularDiff(other).getDegrees();
	}

	public Double getDegree() {
		if (this.angle != null)
			return this.angle.getDegrees();

		if (numComponents() == 0)
			return 0.0;

		return Math.toDegrees(Math.acos(components.get(0) / this.getMagnitude()));
	}

	public Vector normalized() {
		return this.multiply(1 / getMagnitude());
	}

	public Vector orthOn(@NonNull final Vector axis) {
		return this.subtract(this.projectOnto(axis));
	}

	public Vector projectOnto(@NonNull final Vector axis) {
		return axis.multiply(this.componentOf(axis) / axis.getMagnitude());
	}

	public double componentOf(@NonNull final Vector axis) {
		return this.dotProduct(axis) / axis.getMagnitude();
	}

	public boolean isOrthogonal(@NonNull final Vector other) {
		return floatsEqual(this.dotProduct(other), 0);
	}

	public Vector interpolate(@NonNull final Vector other, final double percent) {
		return Vectors.lerp(this, other, percent);
	}

	public Vector nlerp(@NonNull final Vector other, final double percent) {
		return Vectors.nlerp(this, other, percent);
	}

	public Double getComponent(final int index) {
		return this.components.get(index);
	}

	public int numComponents() {
		return this.components.size();
	}

	public double[] getMatrix() {
		return this.components.stream().mapToDouble(Double::doubleValue).toArray();
	}

	@Override
	public int compareTo(@NonNull final Vector o) {
		return MathUtil.fuzzyCompare(this.magnitude, o.magnitude);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final Vector other = (Vector) o;
		if (this.numComponents() != other.numComponents()) return false;
		for (int i = 0; i < this.numComponents(); i++) {
			if (!floatsEqual(this.getComponent(i), other.getComponent(i)))
				return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 17;
		for (final Double d : this.getComponents()) {
			hash = 31 * hash + Double.hashCode(MathUtil.round(d, 5)); // decimals = -log(epsilon) - 1
		}
		return hash;
	}
	public List<Double> getComponents()
	{
		return this.components;
	}
}
