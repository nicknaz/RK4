package RK4;

public class Dot {
    private double xCor, yCor;

    public Dot(double xCor, double yCor) {
        this.xCor = xCor;
        this.yCor = yCor;
    }

    public double getXCor() {
        return xCor;
    }

    public double getYCor() {
        return yCor;
    }

    @Override
    public String toString() {
        return "Dot{" +
                "xCor=" + xCor +
                ", yCor=" + yCor +
                '}';
    }
}
