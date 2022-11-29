package sample;

import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import RK4.Dot;
import RK4.RK4;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import mathParser.MathParser;
import RK4.RungeKuttMethods;
import RK4.SolutionData;

public class Controller {
    private HashMap<String, Double> variables;
    MathParser mathParser;
    RK4 rk4;

    @FXML
    private CheckBox isControl;

    @FXML
    private LineChart<Double, Double> graph1;

    @FXML
    private LineChart<Double, Double> graph2;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField expressionField1;

    @FXML
    private TextField expressionField2;

    @FXML
    private TextField hField;

    @FXML
    private TextField tMaxField;

    @FXML
    private TextField x0Field;

    @FXML
    private TextField y0Field;

    @FXML
    private TextField t0Field;

    @FXML
    private Text resultText1;

    @FXML
    private Button startButton;

    @FXML
    private LineChart<Double, Double> graph;

    @FXML
    private TextField lokPogrField;

    @FXML
    private TextField nMaxField;

    @FXML
    private TextField EgrField;

    @FXML
    private TableView<SolutionData> table;

    @FXML
    private TableColumn<SolutionData, Integer> divCol;

    @FXML
    private TableColumn<SolutionData, Double> hCol;

    @FXML
    private TableColumn<SolutionData, Integer> multCol;

    @FXML
    private TableColumn<SolutionData, Integer> nCol;

    @FXML
    private TableColumn<SolutionData, Double> olpCol;

    @FXML
    private TableColumn<SolutionData, Double> tCol;

    @FXML
    private TableColumn<SolutionData, Double> x2Col;

    @FXML
    private TableColumn<SolutionData, Double> x2_xCol;

    @FXML
    private TableColumn<SolutionData, Double> xCol;

    @FXML
    private TableColumn<SolutionData, Double> y2Col;

    @FXML
    private TableColumn<SolutionData, Double> y2_yCol;

    @FXML
    private TableColumn<SolutionData, Double> yCol;

    @FXML
    private TextField KField;

    @FXML
    private TextField cField;

    @FXML
    private TextField kField;

    @FXML
    private TextField mField;

    @FXML
    private Text text1;

    @FXML
    private Text text2;

    @FXML
    private Text text3;

    @FXML
    private Text text4;

    @FXML
    private Text text5;

    @FXML
    private Text text6;

    @FXML
    private Text text7;

    @FXML
    private Text text8;

    @FXML
    private Text text9;

    @FXML
    private Text text10;

    @FXML
    private Text text11;

    @FXML
    private Text text12;

    @FXML
    private Text text13;

    @FXML
    private Text text14;

    @FXML
    private Text text15;

    @FXML
    private Text text16;



    @FXML
    void initialize() {
        assert expressionField1 != null : "fx:id=\"expressionField1\" was not injected: check your FXML file 'sample.fxml'.";
        assert resultText1 != null : "fx:id=\"resultText1\" was not injected: check your FXML file 'sample.fxml'.";
        assert startButton != null : "fx:id=\"startButton\" was not injected: check your FXML file 'sample.fxml'.";

        divCol.setCellValueFactory(new PropertyValueFactory<SolutionData, Integer>("div"));
        multCol.setCellValueFactory(new PropertyValueFactory<SolutionData, Integer>("multiply"));
        nCol.setCellValueFactory(new PropertyValueFactory<SolutionData, Integer>("n"));
        hCol.setCellValueFactory(new PropertyValueFactory<SolutionData, Double>("h"));
        olpCol.setCellValueFactory(new PropertyValueFactory<SolutionData, Double>("olp"));
        tCol.setCellValueFactory(new PropertyValueFactory<SolutionData, Double>("t"));
        xCol.setCellValueFactory(new PropertyValueFactory<SolutionData, Double>("x"));
        x2Col.setCellValueFactory(new PropertyValueFactory<SolutionData, Double>("x2"));
        x2_xCol.setCellValueFactory(new PropertyValueFactory<SolutionData, Double>("x2_x"));
        yCol.setCellValueFactory(new PropertyValueFactory<SolutionData, Double>("y"));
        y2Col.setCellValueFactory(new PropertyValueFactory<SolutionData, Double>("y2"));
        y2_yCol.setCellValueFactory(new PropertyValueFactory<SolutionData, Double>("y2_y"));

        mathParser = new MathParser(variables);
        rk4 = RungeKuttMethods.getMethod();

        graph.axisSortingPolicyProperty().setValue(LineChart.SortingPolicy.NONE);
        graph1.axisSortingPolicyProperty().setValue(LineChart.SortingPolicy.NONE);
        graph2.axisSortingPolicyProperty().setValue(LineChart.SortingPolicy.NONE);

        startButton.setOnAction(event -> expr());
    }

    private void expr(){
        Instant start = Instant.now();
        //resultText1.setText(Double.toString(result));
        List<List<Dot>> list = new ArrayList<>();


        double h = Double.parseDouble(hField.getText());
        double x0 = Double.parseDouble(x0Field.getText());
        double y0 = Double.parseDouble(y0Field.getText());
        double t0 = Double.parseDouble(t0Field.getText());
        double tMax = Double.parseDouble(tMaxField.getText());
        double eGr = Double.parseDouble(EgrField.getText());
        double nMax = Double.parseDouble(nMaxField.getText());
        double lokPogr = Double.parseDouble(lokPogrField.getText());
        double c = Double.parseDouble(cField.getText());
        double k = Double.parseDouble(kField.getText());
        double K = Double.parseDouble(KField.getText());
        double m = Double.parseDouble(mField.getText());

        ObservableList<SolutionData> solutionDataList = FXCollections.observableArrayList();

        rk4.setInitialConditions(h, x0, y0, t0, tMax, eGr, nMax, lokPogr, c, k, K, m);
        list = rk4.getSolution(expressionField1.getText(), expressionField2.getText(), isControl.isSelected(), solutionDataList);
        List<Dot> list1 = list.get(0);
        List<Dot> list2 = list.get(1);
        List<Dot> list3 = list.get(2);

        System.out.println(solutionDataList);

        drawGraph(list1, graph);
        drawGraph(list2, graph1);
        drawGraph(list3, graph2);

        table.setItems(solutionDataList);


        Instant finish = Instant.now();
        long elapsed = Duration.between(start, finish).toMillis();
        resultText1.setText("Прошло времени, мс: " + elapsed);

        text1.setText("Начальное отклонение груза: " + y0);
        text2.setText("Начальная скорость груза: " + x0);
        text3.setText("Начальное время счета: " + t0);
        text4.setText("Условие остановки счета: " + tMax);
        if(isControl.isSelected()){
            text5.setText("Контроль локальной погрешности включен");
        }else{
            text5.setText("Контроль локальной погрешности выключен");
        }
        text6.setText("Eps граничный: " + eGr);
        text7.setText("Число шагов: " + solutionDataList.get(solutionDataList.size()-1).getN());

        text8.setText("Масса груза: " + m);
        text9.setText("Коэффициент демпфирования: " + c);
        text10.setText("Жесткость пружины: " + k);
        text11.setText("Нелинейная хар-ка k*: " + K);

        text12.setText("Максимальная ОЛП: " + solutionDataList.get(solutionDataList.size()-1).getMaxOLP());
        text13.setText("Число удвоения шага: " + solutionDataList.get(solutionDataList.size()-1).getCountMult());
        text14.setText("Число деления шага: " + solutionDataList.get(solutionDataList.size()-1).getCountMult());
        text15.setText("Максимальный h: " + solutionDataList.get(solutionDataList.size()-1).getMaxH());
        text16.setText("Минимальный h: " + solutionDataList.get(solutionDataList.size()-1).getMinH());
    }

    private void drawGraph(List<Dot> list, LineChart<Double, Double> graph){
        XYChart.Series<Double, Double> series = new XYChart.Series<Double, Double>();
        series.setName("V");

        for (Dot dot : list) {
            series.getData().add(new XYChart.Data(dot.getXCor(), dot.getYCor()));
        }

        graph.setCreateSymbols(false);
        graph.getData().add(series);
    }

}
