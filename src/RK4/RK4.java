package RK4;

import java.util.List;

public interface RK4 {
    void setInitialConditions(double h, double x0, double y0, double t0,
                              double tMax, double eGr, double nMax, double lokPogr,
                              double c, double k, double K, double m);
    List<List<Dot>> getSolution(String expression1, String expression2, boolean isControl, List<SolutionData> solutionDataList);
}
