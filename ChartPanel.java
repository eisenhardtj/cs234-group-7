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
    private JTextField firstNameSearchField, lastNameSearchField, dateSearchField;
    private JButton searchButton, dateSearchButton, cumulativeButton;
    private JPanel chartPanel;
    private JLabel noDataLabel;

    public ChartPanel()
    {
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
        cumulativeButton = new JButton("View Cumulative Chart");
        inputPanel.add(cumulativeButton);
    
        chart1 = createChart("Freethrow chart", "freethrows", firstNameSearchField.getText(), lastNameSearchField.getText());
        chart2 = createChart("Three point chart", "threepointshots", firstNameSearchField.getText(), lastNameSearchField.getText());
    
        chartPanel = new JPanel(new GridLayout(1, 2));
        if (chart1 != null)
        {
            chartPanel.add(new XChartPanel<>(chart1));
        }
        if (chart2 != null)
        {
            chartPanel.add(new XChartPanel<>(chart2));
        }
    
        add(chartPanel, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.PAGE_END);

        noDataLabel = new JLabel("No data available"); // Initialize the JLabel
        noDataLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the text
        noDataLabel.setFont(new Font(noDataLabel.getFont().getName(), Font.BOLD, 16)); // Set font
        noDataLabel.setForeground(Color.RED); // Set text color
        chartPanel.add(noDataLabel);
        noDataLabel.setVisible(false); // Initially invisible

        searchButton.addActionListener(e -> {
            updateCharts();
        });

        dateSearchButton.addActionListener(e ->
        {
            String date = dateSearchField.getText();
            if (date.isEmpty())
            {
                JOptionPane.showMessageDialog(null, "Please enter a date to search for.");
            }
            else
            {
                JTable table1 = createDataTable("Freethrow chart", "freethrows", dateSearchField.getText());
                JTable table2 = createDataTable("Three point chart", "threepointshots", dateSearchField.getText());
                if (table1 == null || table2 == null)
                {
                    JOptionPane.showMessageDialog(null, "No data found for " + date);
                }
                else
                {
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

        cumulativeButton.addActionListener(e ->
        {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            SwingWorker<JTable, Void> worker = new SwingWorker<JTable, Void>()
            {
                @Override
                protected JTable doInBackground() throws Exception
                {
                    return createCumulativeDataTable("Freethrow chart", "freethrows");
                }

                @Override
                protected void done()
                {
                    try
                    {
                        JTable table1 = get();
                        JTable table2 = createCumulativeDataTable("Three point chart", "threepointshots");

                        JPanel averagePanel1 = new JPanel(new BorderLayout());
                        JLabel averageLabel1 = new JLabel("Average Shooting Percentage (Freethrows): " + calculateAveragePercentage("freethrows"));
                        averageLabel1.setHorizontalAlignment(SwingConstants.CENTER);
                        averageLabel1.setFont(new Font(averageLabel1.getFont().getName(), Font.BOLD, 16));
                        averagePanel1.add(averageLabel1, BorderLayout.NORTH);
                        averagePanel1.add(new JScrollPane(table1), BorderLayout.CENTER);

                        JPanel averagePanel2 = new JPanel(new BorderLayout());
                        JLabel averageLabel2 = new JLabel("Average Shooting Percentage (Three Point Shots): " + calculateAveragePercentage("threepointshots"));
                        averageLabel2.setHorizontalAlignment(SwingConstants.CENTER);
                        averageLabel2.setFont(new Font(averageLabel2.getFont().getName(), Font.BOLD, 16));
                        averagePanel2.add(averageLabel2, BorderLayout.NORTH);
                        averagePanel2.add(new JScrollPane(table2), BorderLayout.CENTER);

                        chartPanel.removeAll();
                        chartPanel.add(averagePanel1, BorderLayout.WEST);
                        chartPanel.add(averagePanel2, BorderLayout.EAST);
                        chartPanel.revalidate();
                        chartPanel.repaint();
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    finally
                    {
                        setCursor(Cursor.getDefaultCursor());
                    }
                }
            };

            worker.execute();
        });
    }

    private String calculateAveragePercentage(String type)
    {
        double totalPercentage = 0.0;
        int totalCount = 0;
        String[] dates = conn.getDates(100, type);
        for (String date : dates)
        {
            Double percentage = conn.findStatisticsBasedOnDate(date, type);
            if (percentage != null)
            {
                double value = percentage.doubleValue();
                if (value >= 0)
                {
                    totalPercentage += value;
                    totalCount++;
                }
            }
        }
        if (totalCount > 0)
        {
            double averagePercentage = totalPercentage / totalCount;
            return String.format("%.2f", averagePercentage) + "%";
        }
        else
        {
            return "No data available";
        }
    }

    private CategoryChart createChart(String title, String type, String firstName, String lastName)
    {
        String seriesName = "";
        if (type.equals("freethrows"))
        {
            if (firstName.isEmpty() || lastName.isEmpty())
            {
                seriesName = "Freethrows";
            }
            else
            {
                seriesName = firstName + ", " + lastName + "'s " + "Freethrows";
            }
        }
        else if (type.equals("threepointshots"))
        {
            if (firstName.isEmpty() || lastName.isEmpty())
            {
                seriesName = "Three Point Shots";
            }
            else
            {
                seriesName = firstName + ", " + lastName + "'s " + "Three Point Shots";
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
    
            String[] sessionDates = getSessionDates(type);
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
            if (dates != null)
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

    public JTable createCumulativeDataTable(String title, String type)
    {
        String[] columnNames = {"Date", "Shot Percentage"};
        String[][] data = generateCumulativeData(type);

        JTable table = new JTable(data, columnNames);
        table.setFont(new Font(table.getFont().getName(), Font.PLAIN, 14));
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);

        return table;
    }

    private String[][] generateCumulativeData(String type)
    {
        List<String> sessionDatesList = new ArrayList<>();
        List<Double> yData = new ArrayList<>();
        String[] dates = conn.getDates(100, type);
        for (int i = 0; i < dates.length; i++)
        {
            sessionDatesList.add(dates[i]);
            Double percentage = conn.findStatisticsBasedOnDate(dates[i], type);
            if (percentage != null && percentage >= 0)
            {
                yData.add(percentage);
            }
        }

        String[][] data = new String[sessionDatesList.size()][2];
        for (int i = 0; i < sessionDatesList.size(); i++)
        {
            data[i][0] = sessionDatesList.get(i);
            if (!yData.isEmpty() && i < yData.size())
            {
                data[i][1] = String.format("%.2f", yData.get(i));
            }
            else
            {
                data[i][1] = "";
            }
        }
        return data;
    }

    public JTable createDataTable(String title, String type, String date)
    {
        String[] columnNames = {"Date", "Shot Percentage"};
        String[][] data = generateData(type, date);

        JTable table = new JTable(data, columnNames);
        table.setFont(new Font(table.getFont().getName(), Font.PLAIN, 14));
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);

        return table;
    }

    private String[][] generateData(String type, String date)
    {
        List<String> sessionDatesList = new ArrayList<>();
        List<Double> yData = new ArrayList<>();
        String[] dates = conn.searchDate(date, type);
        for (String sessionDate : dates)
        {
            sessionDatesList.add(sessionDate);
            double percentage = conn.findStatisticsBasedOnDate(sessionDate, type);
            if (percentage >= 0)
            {
                yData.add(percentage);
            }
        }

        String[][] data = new String[sessionDatesList.size()][2];
        for (int i = 0; i < sessionDatesList.size(); i++)
        {
            data[i][0] = sessionDatesList.get(i);
            data[i][1] = String.format("%.2f", yData.get(i));
        }
        return data;
    }

    private String[] getSessionDates(String type)
    {
        return conn.getDates(15, type);
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
        if (chart1 != null && chart2 != null) {
            if (chart1.getSeriesMap().isEmpty() && chart2.getSeriesMap().isEmpty()) {
                noDataLabel.setVisible(true);
            }
            else
            {
                noDataLabel.setVisible(false);
            }
            chartPanel.removeAll();
            chartPanel.add(new XChartPanel<>(chart1));
            chartPanel.add(new XChartPanel<>(chart2));
            add(inputPanel, BorderLayout.PAGE_END);
            revalidate();
            repaint();
        }
    }
}
