import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.colors.XChartSeriesColors;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

public class ChartPanel extends JPanel {

    private SQLConnection conn = new SQLConnection();
    private CategoryChart chart1, chart2;
    private JPanel inputPanel;
    private JTextField firstNameSearchField, lastNameSearchField;
    private JButton searchButton;

    public ChartPanel() {
        setLayout(new BorderLayout());

        inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(new JLabel("Player Search: "));
        firstNameSearchField = new JTextField(20);
        lastNameSearchField = new JTextField(20);
        inputPanel.add(firstNameSearchField);
        inputPanel.add(lastNameSearchField);

        searchButton = new JButton("Search");
        inputPanel.add(searchButton);

        chart1 = createChart("Chart 1");
        chart2 = createChart("Chart 2");

        JPanel chartPanel = new JPanel(new GridLayout(1, 2));
        chartPanel.add(new XChartPanel<>(chart1));
        chartPanel.add(new XChartPanel<>(chart2));

        add(chartPanel, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.PAGE_END);

        searchButton.addActionListener(e -> {
            ResultSet rs = search();
            updateCharts(rs);
        });
    }

    private CategoryChart createChart(String title) {
        CategoryChart chart = new CategoryChartBuilder()
                .width(400)
                .height(500)
                .title(title)
                .xAxisTitle("X-Axis")
                .yAxisTitle("Y-Axis")
                .theme(Styler.ChartTheme.Matlab)
                .build();

        chart.getStyler().setPlotGridLinesVisible(false);
        chart.getStyler().setChartFontColor(XChartSeriesColors.BLACK);

        List<String> xData = Arrays.asList("Category1", "Category2", "Category3", "Category4", "Category5", "Category6");
        List<Double> yData = Arrays.asList(1.0, 4.0, 3.0, 5.0, 5.0, 7.0);
        chart.addSeries("Series1", xData, yData);

        return chart;
    }

    public ResultSet search() {
        ResultSet rs = null;
        // Perform search operation and return result set
        return rs;
    }

    public void updateCharts(ResultSet rs) {
        // Update charts data or properties here if needed
        repaint();
    }
}
