import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.colors.XChartSeriesColors;
import org.knowm.xchart.*; 

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChartPanel extends JPanel 
{
    private SQLConnection conn = new SQLConnection();
    private CategoryChart chart1, chart2;
    private JPanel inputPanel;
    private JTextField firstNameSearchField, lastNameSearchField, dateSearchField;
    private JButton searchButton, dateSearchButton, cumulitiveButton;
    private JPanel chartPanel;

    public ChartPanel() {
        setLayout(new BorderLayout());

        inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(new JLabel("Player Search: "));
        firstNameSearchField = new JTextField(20);
        lastNameSearchField = new JTextField(20);
        dateSearchField = new JTextField(20);
        inputPanel.add(firstNameSearchField);
        inputPanel.add(lastNameSearchField);
        searchButton = new JButton("Search");
        inputPanel.add(searchButton);
        inputPanel.add(new JLabel("Date Search: "));
        inputPanel.add(dateSearchField);
        dateSearchButton = new JButton("Date Search");
        inputPanel.add(dateSearchButton);
        cumulitiveButton = new JButton("View Cumulative Chart");
        inputPanel.add(cumulitiveButton);

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

        dateSearchButton.addActionListener(e -> {
            String date = dateSearchField.getText();
            if (date.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a date to search for.");
            } else {
                JTable table1 = createDataTable("Freethrow chart", "freethrows", dateSearchField.getText());
                JTable table2 = createDataTable("Three point chart", "threepointshots", dateSearchField.getText());
                if (table1 == null || table2 == null) {
                    JOptionPane.showMessageDialog(null, "No data found for " + date);
                } else {
                    JPanel tablePanel1 = new JPanel(new BorderLayout());
                    JLabel label1 = new JLabel("Date of Freethrow Session");
                    label1.setHorizontalAlignment(SwingConstants.CENTER);
                    label1.setFont(new Font(label1.getFont().getName(), Font.BOLD, 16));
                    tablePanel1.add(label1, BorderLayout.NORTH);
                    tablePanel1.add(new JScrollPane(table1), BorderLayout.CENTER);
        
                    JPanel tablePanel2 = new JPanel(new BorderLayout());
                    JLabel label2 = new JLabel("Date of Three Point Session");
                    label2.setHorizontalAlignment(SwingConstants.CENTER);
                    label2.setFont(new Font(label2.getFont().getName(), Font.BOLD, 16));
                    tablePanel2.add(label2, BorderLayout.NORTH);
                    tablePanel2.add(new JScrollPane(table2), BorderLayout.CENTER);
        
                    chartPanel.removeAll();
                    chartPanel.add(tablePanel1, BorderLayout.WEST);
                    chartPanel.add(tablePanel2, BorderLayout.EAST);
                    add(inputPanel, BorderLayout.PAGE_END);
                    revalidate();
                    repaint();
                }
            }
        });        

        cumulitiveButton.addActionListener(e -> {
            CategoryChart chart1 = createCummulitaveChart("Freethrow chart", "freethrows");
            CategoryChart chart2 = createCummulitaveChart("Three point chart", "threepointshots");
            chartPanel.removeAll();
            chartPanel.add(new XChartPanel<>(chart1), BorderLayout.LINE_START);
            chartPanel.add(new XChartPanel<>(chart2), BorderLayout.LINE_END);
            chartPanel.revalidate();
            chartPanel.repaint();
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
            chart.getStyler().setXAxisLabelRotation(45);

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
            chart.getStyler().setXAxisLabelRotation(45);

            List<String> sessionDatesList = new ArrayList<>();
            List<Double> yData = new ArrayList<>();
            String[] dates = conn.searchPlayerDates(firstName, lastName, type, 15);
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

    public CategoryChart createCummulitaveChart(String title, String type) {
        CategoryChart chart = new CategoryChartBuilder()
                .width(800)
                .height(600)
                .title(title)
                .xAxisTitle("Date of Session")
                .yAxisTitle("Shot Percentage")
                .build();
    
        chart.getStyler().setChartFontColor(XChartSeriesColors.BLACK);
        chart.getStyler().setXAxisLabelRotation(45);
    
        List<String> sessionDatesList = new ArrayList<>();
        List<Double> yData = new ArrayList<>();
        String[] dates = conn.getDates(100);
    
        for (String date : dates)
        {
            sessionDatesList.add(date);
            yData.add(conn.findStatisticsBasedOnDate(date, type));
        }
        
        chart.addSeries(type, sessionDatesList, yData);
    
        return chart;
    }
    
    

    public JTable createDataTable(String title, String type, String date) {
        String[] columnNames = {"Date", "Shot Percentage"};
        String[][] data = generateData(type, date);
    
        JTable table = new JTable(data, columnNames);
        table.setFont(new Font(table.getFont().getName(), Font.PLAIN, 14)); // Adjust the font size as needed
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
    
        return table;
    }    

    private String[][] generateData(String type, String date) {
        List<String> sessionDatesList = new ArrayList<>();
        List<Double> yData = new ArrayList<>();
        String[] dates = conn.searchDate(date, type);
        for (String sessionDate : dates) {
            sessionDatesList.add(sessionDate);
            yData.add(conn.findStatisticsBasedOnDate(sessionDate, type));
        }

        String[][] data = new String[sessionDatesList.size()][2];
        for (int i = 0; i < sessionDatesList.size(); i++) {
            data[i][0] = sessionDatesList.get(i);
            data[i][1] = String.valueOf(yData.get(i));
        }
        return data;
    }

    private String[] getSessionDates() {
        return conn.getDates(15);
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
