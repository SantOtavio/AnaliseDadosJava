package com.example.exemplodatascience;

import org.apache.commons.math3.stat.StatUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DataVisualizationExample {
    public static void main(String[] args) {
        String csvFile = "C:\\Users\\otavio_a_santos\\Documents\\GitHub\\exemploDataScience\\src\\main\\java\\com\\example\\exemplodatascience\\dados_rh.csv";
        String line;
        String cvsSplitBy = ",";

        List<Double> salarioBaseList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Ler cabeçalho
            br.readLine();

            // Encontrar o índice da coluna "Salario Base"
            int salarioBaseIndex = 11;

            // Dentro do loop while para ler os dados do arquivo
            while ((line = br.readLine()) != null) {
                // Separar os valores por vírgula
                String[] data = line.split(cvsSplitBy);
                // Obter o valor do salário base
                String salarioBaseString = data[salarioBaseIndex].replaceAll("[^\\d.]", "").replace(",", "."); // Remove caracteres não numéricos e substitui vírgulas por pontos
                double salarioBase = Double.parseDouble(salarioBaseString);
                salarioBaseList.add(salarioBase);
            }

            // Gerar histograma
            double[] salarioArray = salarioBaseList.stream().mapToDouble(Double::doubleValue).toArray();
            HistogramDataset histogramDataset = new HistogramDataset();
            histogramDataset.addSeries("Salário Base", salarioArray, 10);
            JFreeChart histogramChart = ChartFactory.createHistogram("Distribuição de Salário Base", "Salário Base", "Frequência", histogramDataset, PlotOrientation.VERTICAL, true, true, false);
            ChartFrame histogramFrame = new ChartFrame("Histograma", histogramChart);
            histogramFrame.pack();
            histogramFrame.setVisible(true);

            // Gerar diagrama de dispersão
            double[] xData = new double[salarioBaseList.size()];
            double[] yData = new double[salarioBaseList.size()];
            for (int i = 0; i < salarioBaseList.size(); i++) {
                xData[i] = i + 1;
                yData[i] = salarioBaseList.get(i);
            }
            double[][] data = {xData, yData};
            XYDataset scatterDataset = new DefaultXYDataset();
            ((DefaultXYDataset) scatterDataset).addSeries("Salário Base", data);
            JFreeChart scatterChart = ChartFactory.createScatterPlot(
                    "Diagrama de Dispersão",
                    "Índice",
                    "Salário Base",
                    scatterDataset,
                    PlotOrientation.VERTICAL,
                    true,
                    true,
                    false
            );

            // Definir intervalo personalizado para o eixo do salário base
            NumberAxis yAxis = (NumberAxis) scatterChart.getXYPlot().getRangeAxis();
            yAxis.setAutoRangeIncludesZero(false);
            yAxis.setRange(0, 30000); // Defina o intervalo desejado aqui

            ChartFrame scatterFrame = new ChartFrame("Diagrama de Dispersão", scatterChart);
            scatterFrame.pack();
            scatterFrame.setVisible(true);

            // Gerar boxplot
            BoxAndWhiskerCategoryDataset boxplotDataset = createBoxplotDataset(salarioArray);
            JFreeChart boxplotChart = ChartFactory.createBoxAndWhiskerChart(
                    "Boxplot",
                    "Salário Base",
                    "Valor",
                    boxplotDataset,
                    true
            );
            ChartFrame boxplotFrame = new ChartFrame("Boxplot", boxplotChart);
            boxplotFrame.pack();
            boxplotFrame.setVisible(true);

            // Calcular medidas estatísticas
            double media = StatUtils.mean(salarioArray);
            double mediana = StatUtils.percentile(salarioArray, 50);
            double desvioPadrao = Math.sqrt(StatUtils.variance(salarioArray));
            double primeiroQuartil = StatUtils.percentile(salarioArray, 25);
            double terceiroQuartil = StatUtils.percentile(salarioArray, 75);

            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            System.out.println("Média: " + decimalFormat.format(media));
            System.out.println("Mediana: " + decimalFormat.format(mediana));
            System.out.println("Desvio Padrão: " + decimalFormat.format(desvioPadrao));
            System.out.println("Primeiro Quartil: " + decimalFormat.format(primeiroQuartil));
            System.out.println("Terceiro Quartil: " + decimalFormat.format(terceiroQuartil));

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static BoxAndWhiskerCategoryDataset createBoxplotDataset(double[] data) {
        DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
        List<Double> dataList = new ArrayList<>();
        for (double value : data) {
            dataList.add(value);
        }
        dataset.add(dataList, "Salário Base", "");
        return dataset;
    }
}
