package RK4;

import javafx.collections.FXCollections;
import mathParser.MathParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RK4_with_control implements RK4{
    double h;
    double x0;
    double y0;
    double t0;
    double tMax;
    double eGr;
    double nMax;
    double lokPogr;
    MathParser mathParser;
    HashMap<String, Double> variables;

    public RK4_with_control() {
        this.variables = new HashMap<>();
        this.mathParser = new MathParser(variables);
    }

    @Override
    public void setInitialConditions(double h, double x0, double y0, double t0,
                                     double tMax, double eGr, double nMax, double lokPogr,
                                     double c, double k, double K, double m) {
        this.h = h;
        this.x0 = x0;
        this.y0 = y0;
        this.t0 = t0;
        this.tMax = tMax;
        this.eGr = eGr;
        this.nMax = nMax;
        this.lokPogr = lokPogr;
        variables.put("c", c);
        variables.put("k", k);
        variables.put("K", K);
        variables.put("m", m);
    }

    @Override
    public List<List<Dot>> getSolution(String expression1, String expression2, boolean isControl, List<SolutionData>solutionDataList) {
        List<Dot> list1 = FXCollections.observableArrayList();
        List<Dot> list2 = FXCollections.observableArrayList();
        List<Dot> list3 = FXCollections.observableArrayList();
        double xN = x0;
        double yN = y0;
        double tN = t0;

        double maxOLP = 0;
        double countDiv = 0;
        double countMult = 0;
        double maxH = h;
        double minH = h;

        int n = 0;
        list1.add(new Dot(t0, y0));
        list2.add(new Dot(t0, x0));
        list3.add(new Dot(y0, x0));
        solutionDataList.add(new SolutionData(n, tN, xN, xN, xN, yN, yN, yN, 0.0,h,0,0,
                maxOLP, countDiv, countMult, maxH, minH));

        for(double i = t0; i <= (tMax - eGr); i+=h){
            n++;
            if(n > nMax){
                break;
            }

            double[] pairSolution = calculate(expression1, expression2, h, i, yN, xN);
            if(isControl) {
                double[] testSolution1 = calculate(expression1, expression2, h / 2, i, yN, xN);
                double[] testSolution2 = calculate(expression1, expression2, h / 2, i + h / 2,
                        testSolution1[1], testSolution1[0]);
                //xN = xN + (h) * (k1);
                //yN = yN + (h) * (l1);
                double Sy = Math.abs((testSolution2[1] - pairSolution[1]) / 15);
                if(Sy > maxOLP){
                    maxOLP = Sy;
                }
                double Sx = Math.abs((testSolution2[0] - pairSolution[0]) / 15);
                //System.out.println(testSolution2[1]);
                //System.out.println(pairSolution[1]);
                //System.out.println(h);
                //System.out.println("*");
                if (Sy > lokPogr && Sx > lokPogr) {
                    countDiv++;
                    h = h / 2;
                    if(h < minH){
                        minH = h;
                    }
                    xN = testSolution1[0];
                    yN = testSolution1[1];
                    list1.add(new Dot(i + h, yN));
                    list2.add(new Dot(i + h, xN));
                    list3.add(new Dot(yN, xN));
                    solutionDataList.add(new SolutionData(n,i, xN, testSolution2[0],
                            testSolution2[0] - pairSolution[0], yN, testSolution2[1],
                            testSolution2[1] - pairSolution[1], Sy,h,1,0,
                            maxOLP, countDiv, countMult, maxH, minH));
                } else if (Sy < lokPogr / 32 && Sx < lokPogr / 32) {
                    countMult++;
                    xN = pairSolution[0];
                    yN = pairSolution[1];
                    list1.add(new Dot(i + h, yN));
                    list2.add(new Dot(i + h, xN));
                    list3.add(new Dot(yN, xN));
                    h = h * 2;
                    if(h > maxH){
                        maxH = h;
                    }
                    solutionDataList.add(new SolutionData(n,i, xN, testSolution2[0],
                            testSolution2[0] - pairSolution[0], yN, testSolution2[1],
                            testSolution2[1] - pairSolution[1], Sy,h,0,1,
                            maxOLP, countDiv, countMult, maxH, minH));
                } else {
                    xN = pairSolution[0];
                    yN = pairSolution[1];
                    list1.add(new Dot(i + h, yN));
                    list2.add(new Dot(i + h, xN));
                    list3.add(new Dot(yN, xN));
                    solutionDataList.add(new SolutionData(n,i, xN, testSolution2[0],
                            testSolution2[0] - pairSolution[0], yN, testSolution2[1],
                            testSolution2[1] - pairSolution[1], Sy,h,0,0,
                            maxOLP, countDiv, countMult, maxH, minH));
                }
            }else{
                xN = pairSolution[0];
                yN = pairSolution[1];

                list1.add(new Dot(i+h, yN));
                list2.add(new Dot(i+h, xN));
                list3.add(new Dot(yN, xN));
                solutionDataList.add(new SolutionData(n,i, xN, xN, xN, yN, yN, yN, 0.0,h,0,0,
                        maxOLP, countDiv, countMult, maxH, minH));
            }
        }
        System.out.println(n);
        System.out.println(h);
        //System.out.println(list);
        List<List<Dot>> list = new ArrayList<>();
        list.add(list1);
        list.add(list2);
        list.add(list3);

        return list;
    }

    private double[] calculate(String expression1, String expression2, double h, double t, double yN, double xN){
        variables.put("t", t);
        variables.put("y", yN);
        variables.put("x", xN);
        double k1 = mathParser.parse(expression1, variables);
        double l1 = mathParser.parse(expression2, variables);
        variables.put("t", t + h/2);
        variables.put("y", yN + l1*h/2);
        variables.put("x", xN + k1*h/2);
        double k2 = mathParser.parse(expression1, variables);
        double l2 = mathParser.parse(expression2, variables);
        variables.put("t", t + h/2);
        variables.put("y", yN + l2*h/2);
        variables.put("x", xN + k2*h/2);
        double k3 = mathParser.parse(expression1, variables);
        double l3 = mathParser.parse(expression2, variables);
        variables.put("t", t + h);
        variables.put("y", yN + l3*h);
        variables.put("x", xN + k3*h);
        double k4 = mathParser.parse(expression1, variables);
        double l4 = mathParser.parse(expression2, variables);

        xN = xN + (h/6) * (k1 + 2*k2 + 2*k3 + k4);
        yN = yN + (h/6) * (l1 + 2*l2 + 2*l3 + l4);

        return new double[]{xN, yN};
    }
}
