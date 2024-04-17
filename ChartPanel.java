import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.colors.XChartSeriesColors;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ChartPanel extends JPanel 
{
    private SQLConnection conn = new SQLConnection();
    private CategoryChart chart1, chart2;
    private JPanel inputPanel;
    private JTextField firstNameSearchField, lastNameSearchField;
    private JButton searchButton;
    private JPanel chartPanel;

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

        chart1 = createChart("Freethrow chart", "freethrows", firstNameSearchField.getText(), lastNameSearchField.getText());
        chart2 = createChart("Three point chart", "threepointshots", firstNameSearchField.getText(), lastNameSearchField.getText());

        chartPanel = new JPanel(new GridLayout(1, 2));
        chartPanel.add(new XChartPanel<>(chart1));
        chartPanel.add(new XChartPanel<>(chart2));

        add(chartPanel, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.PAGE_END);

        searchButton.addActionListener(e -> {
            updateCharts();
        });
    }

    private CategoryChart createChart(String title, String type, String firstName, String lastName) 
    {
        String seriesName = "";
        if (type.equals("freethrows")) 
        {
            if(firstName.isEmpty() || lastName.isEmpty())
            {
                seriesName = "Freethrows";
            }
            else
            {
                seriesName = firstName + ", " + lastName  + "'s "+ "Freethrows";
            }
        } 
        else if (type.equals("threepointshots")) 
        {
            if(firstName.isEmpty() || lastName.isEmpty())
            {
                seriesName = "Three Point Shots";
            }
            else
            {
                seriesName = firstName + ", " + lastName  + "'s "+ "Three Point Shots";
            }
        }
        if (firstName.isEmpty() || lastName.isEmpty()) 
        {

            CategoryChart chart = new CategoryChartBuilder()
                .width(400)
                .height(500)
                .title(title)
                .xAxisTitle("Date of Session")
                .yAxisTitle("Shot Percentage")
                .theme(Styler.ChartTheme.Matlab)
                .build();

            chart.getStyler().setPlotGridLinesVisible(false);
            chart.getStyler().setChartFontColor(XChartSeriesColors.BLACK);
            String[] sessionDates = getSessionDates();
            List<String> sessionDatesList = new ArrayList<>();
            List<Double> yData = new ArrayList<>();
            for (String date : sessionDates) 
            {
                sessionDatesList.add(date);
                yData.add(conn.findStatisticsBasedOnDate(date, type));
            }
            System.out.println(sessionDatesList);
            System.out.println(yData);
            if (yData.isEmpty())
            {
                return null;
            }
            chart.addSeries(seriesName, sessionDatesList, yData);

            return chart;
        }
        else
        {
            CategoryChart chart = new CategoryChartBuilder()
                .width(400)
                .height(500)
                .title(title)
                .xAxisTitle("Date of Session")
                .yAxisTitle("Shot Percentage")
                .theme(Styler.ChartTheme.Matlab)
                .build();

            chart.getStyler().setPlotGridLinesVisible(false);
            chart.getStyler().setChartFontColor(XChartSeriesColors.BLACK);

            List<String> sessionDatesList = new ArrayList<>();
            List<Double> yData = new ArrayList<>();
            String[] dates = conn.searchPlayerDates(firstName, lastName, type);
            if(dates != null)
            {
                for (String date : dates) 
                {
                    sessionDatesList.add(date);
                    yData.add(conn.findPlayerStatisticsBasedOnDate(type, date, firstName, lastName));
                }
                System.out.println(sessionDatesList);
                System.out.println(yData);
                if (yData.isEmpty())
                {
                    return null;
                }
                chart.addSeries(seriesName, sessionDatesList, yData);

                return chart;
            }
            else
            {
                return chart;
            }
        }
    }

    private String[] getSessionDates() {
        return conn.getDates();
    }

    public void updateCharts()
    {
        if (firstNameSearchField.getText().isEmpty() || lastNameSearchField.getText().isEmpty())
        {
            chart1 = createChart("Freethrow chart", "freethrows", "", "");
            chart2 = createChart("Three point chart", "threepointshots", "", "");
        }
        else
        {
            chart1 = createChart("Freethrow chart", "freethrows", firstNameSearchField.getText(), lastNameSearchField.getText());
            if (chart1 == null) 
            {
                JOptionPane.showMessageDialog(null, "No freethrow data found for " + firstNameSearchField.getText() + " " + lastNameSearchField.getText());
            }
            chart2 = createChart("Three point chart", "threepointshots", firstNameSearchField.getText(), lastNameSearchField.getText());
            if (chart2 == null)
            {
                JOptionPane.showMessageDialog(null, "No three point data found for " + firstNameSearchField.getText() + " " + lastNameSearchField.getText());
            }
        }
        if(chart1 != null && chart2 != null)
        {
            chartPanel.removeAll();
            chartPanel.add(new XChartPanel<>(chart1));
            chartPanel.add(new XChartPanel<>(chart2));
            add(inputPanel, BorderLayout.PAGE_END);
            revalidate();
            repaint();
        }
    }
}
