package RK4;

public class SolutionData {
    private int nCol;
    private double t;
    private double x;
    private double x2;
    private double x2_x;
    private double y;
    private double y2;
    private double y2_y;
    private double olp;
    private double h;
    private int div = 0;
    private int multiply = 0;
    private double maxOLP = 0;
    private double countDiv = 0;
    private double countMult = 0;
    private double maxH = h;
    private double minH = h;

    public SolutionData(int nCol, double t, double x, double x2, double x2_x, double y, double y2,
                        double y2_y, double olp, double h, int div, int multiply,
                        double maxOLP, double countDiv, double countMult, double maxH, double minH) {
        this.nCol = nCol;
        this.t = t;
        this.x = x;
        this.x2 = x2;
        this.x2_x = x2_x;
        this.y = y;
        this.y2 = y2;
        this.y2_y = y2_y;
        this.olp = olp;
        this.h = h;
        this.div = div;
        this.multiply = multiply;
        this.maxOLP = maxOLP;
        this.countDiv = countDiv;
        this.countMult = countMult;
        this.maxH = maxH;
        this.minH = minH;
    }

    public void setN(int nCol) {
        this.nCol = nCol;
    }

    public void setT(double t) {
        this.t = t;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setX2(double x2) {
        this.x2 = x2;
    }

    public void setX2_x(double x2_x) {
        this.x2_x = x2_x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setY2(double y2) {
        this.y2 = y2;
    }

    public void sety2_y(double y2_y) {
        this.y2_y = y2_y;
    }

    public void setOlp(double olp) {
        this.olp = olp;
    }

    public void setH(double h) {
        this.h = h;
    }

    public void setDiv(int div) {
        this.div = div;
    }

    public void setMultiply(int multiply) {
        this.multiply = multiply;
    }

    public int getN() {
        return nCol;
    }

    public double getT() {
        return t;
    }

    public double getX() {
        return x;
    }

    public double getX2() {
        return x2;
    }

    public double getX2_x() {
        return x2_x;
    }

    public double getY() {
        return y;
    }

    public double getY2() {
        return y2;
    }

    public double getY2_y() {
        return y2_y;
    }

    public double getOlp() {
        return olp;
    }

    public double getH() {
        return h;
    }

    public int getDiv() {
        return div;
    }

    public int getMultiply() {
        return multiply;
    }

    public double getMaxOLP() {
        return maxOLP;
    }

    public double getCountDiv() {
        return countDiv;
    }

    public double getCountMult() {
        return countMult;
    }

    public double getMaxH() {
        return maxH;
    }

    public double getMinH() {
        return minH;
    }

    @Override
    public String toString() {
        return "SolutionData{" +
                "n=" + nCol +
                ", t=" + t +
                ", x=" + x +
                ", x2=" + x2 +
                ", x2_x=" + x2_x +
                ", y=" + y +
                ", y2=" + y2 +
                ", y2_y=" + y2_y +
                ", olp=" + olp +
                ", h=" + h +
                ", div=" + div +
                ", multiply=" + multiply +
                '}';
    }
}
