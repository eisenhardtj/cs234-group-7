import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import java.sql.ResultSet;

public class MySQLConnection {
    public static void main(String[] args) {

        PreparedStatement q = null;
        ArrayList<String[]> players = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/MoravianWomensTeam24";
        String user = "project";
        String password = "project";
        Connection connection = null;
        ResultSet resultsAdd = null, resultsRemove = null;
        Scanner in = new Scanner(System.in);
        String idField = "", firstNameField = "", lastNameField = "", playerNumberField = "", positionField = "", expectedGraduationDateField = "";
        Boolean yorn;
        String fN, lN, ps, first, last;
        int pN, gY;

        SQLConnection conn = new SQLConnection();

        players = conn.dataToArrayList();
        for (String[] player : players) 
        {
            System.out.println(Arrays.toString(player));
        }

        conn.archivePlayer("Brody", "Long");


        // System.out.print("Do you want to add players? (True or False Case sensitive) ");
        // yorn = in.nextBoolean();

        // if(yorn)
        // {
        //     System.out.print("First: ");
        //     fN = in.next();
        //     System.out.print("Last: ");
        //     lN = in.next();
        //     System.out.print("Player number: ");
        //     pN = in.nextInt();
        //     System.out.print("Position: ");
        //     ps = in.next();
        //     System.out.print("Expected graduation year: ");
        //     gY = in.nextInt();
        //     //First name, Last name, Player number, Position, Graduation year
        //     conn.addPlayer(fN, lN, pN, ps, gY);

        //     try
        //     {
        //         connection = DriverManager.getConnection(url, user, password);

        //         if (connection != null) 
        //         {
        //             System.out.println("Successfully connected to the MySQL database.");
                    
        //             String sql = "SELECT * FROM teamroster;";
        //             q = connection.prepareStatement(sql);
        //             resultsAdd = q.executeQuery();
        //             System.out.println("+----+------------+-----------+---------------+----------+--------------------------+");
        //             System.out.println("| ID | First Name | Last Name | Player Number | Position | Expected Graduation Date |");
        //             System.out.println("+----+------------+-----------+---------------+----------+--------------------------+");
        //             while(resultsAdd.next())
        //             {
        //                 idField = resultsAdd.getString("player_id");
        //                 firstNameField = resultsAdd.getString("first_name");
        //                 lastNameField = resultsAdd.getString("last_name");
        //                 playerNumberField = resultsAdd.getString("player_number");
        //                 positionField = resultsAdd.getString("position");
        //                 expectedGraduationDateField = resultsAdd.getString("expected_graduation_date");
        //                 System.out.println("| " + idField + " | " + firstNameField + " | " + lastNameField + " | " + playerNumberField + " | " + positionField + " | " + expectedGraduationDateField + " |");
        //             }
        //         }
        //     }
        //     catch (SQLException e) 
        //     {
        //         e.printStackTrace();
        //         System.out.println("\nError connecting to the database.");
        //     }
        //     finally
        //     {
        //         try
        //         {
        //             if (q != null)
        //             {
        //                 q.close();
        //             }

        //             if (connection != null)
        //             {
        //                 connection.close();
        //             }

        //             if (resultsAdd != null)
        //             {
        //                 resultsAdd.close();
        //             }
        //         }
        //         catch (SQLException e)
        //         {
        //             e.printStackTrace();
        //             System.out.println("Error closing connections.");
        //         }
        //     }
        // }

        // System.out.print("Do you want to remove players? (True or False Case sensitive) ");
        // yorn = in.nextBoolean();

        // if(yorn)
        // {
        //     System.out.print("First name to remove: ");
        //     fN = in.next();
        //     System.out.print("Last name to remove: ");
        //     lN = in.next();

        //     conn.removePlayer(fN, lN);

        //     try
        //     {
        //         connection = DriverManager.getConnection(url, user, password);
        //         if (connection != null) 
        //         {
        //             System.out.println("Successfully connected to the MySQL database.");
                    
        //             String sql = "SELECT * FROM teamroster;";
        //             q = connection.prepareStatement(sql);
        //             resultsRemove = q.executeQuery();
        //             System.out.println("+----+------------+-----------+---------------+----------+--------------------------+");
        //             System.out.println("| ID | First Name | Last Name | Player Number | Position | Expected Graduation Date |");
        //             System.out.println("+----+------------+-----------+---------------+----------+--------------------------+");
        //             while(resultsRemove.next())
        //             {
        //                 idField = resultsRemove.getString("player_id");
        //                 firstNameField = resultsRemove.getString("first_name");
        //                 lastNameField = resultsRemove.getString("last_name");
        //                 playerNumberField = resultsRemove.getString("player_number");
        //                 positionField = resultsRemove.getString("position");
        //                 expectedGraduationDateField = resultsRemove.getString("expected_graduation_date");
        //                 System.out.println("| " + idField + " | " + firstNameField + " | " + lastNameField + " | " + playerNumberField + " | " + positionField + " | " + expectedGraduationDateField + " |");
        //             }
        //         }
        //     }
        //     catch(SQLException e)
        //     {
        //         e.printStackTrace();
        //         System.out.println("\nSQL Connection Error.");
        //     }
        //     finally
        //     {
        //         try
        //         {
        //             if (q != null)
        //             {
        //                 q.close();
        //             }

        //             if (connection != null)
        //             {
        //                 connection.close();
        //             }

        //             if (resultsRemove != null)
        //             {
        //                 resultsRemove.close();
        //             }
        //         }
        //         catch(SQLException e)
        //         {
        //             System.out.println("Error closing variables.");
        //             e.printStackTrace();
        //         }
        //     }
        // }
        // System.out.print("Do you want to edit any players? (True or False Case sensitive) ");
        // yorn = in.nextBoolean();
        // System.out.print("First name to edit: ");
        // first = in.next();
        // System.out.print("Last name to edit: ");
        // last = in.next();

        // if(yorn)
        // {
        //     System.out.print("First: ");
        //     fN = in.next();
        //     System.out.print("Last: ");
        //     lN = in.next();
        //     System.out.print("Player number: ");
        //     pN = in.nextInt();
        //     System.out.print("Position: ");
        //     ps = in.next();
        //     System.out.print("Expected graduation year: ");
        //     gY = in.nextInt();
        //     //First name, Last name, Player number, Position, Graduation year
        //     conn.editPlayer(first, last, fN, lN, pN, ps, gY);

        //     try
        //     {
        //         connection = DriverManager.getConnection(url, user, password);
        //         if (connection != null) 
        //         {
        //             System.out.println("Successfully connected to the MySQL database.");
                    
        //             String sql = "SELECT * FROM teamroster;";
        //             q = connection.prepareStatement(sql);
        //             resultsRemove = q.executeQuery();
        //             System.out.println("+----+------------+-----------+---------------+----------+--------------------------+");
        //             System.out.println("| ID | First Name | Last Name | Player Number | Position | Expected Graduation Date |");
        //             System.out.println("+----+------------+-----------+---------------+----------+--------------------------+");
        //             while(resultsRemove.next())
        //             {
        //                 idField = resultsRemove.getString("player_id");
        //                 firstNameField = resultsRemove.getString("first_name");
        //                 lastNameField = resultsRemove.getString("last_name");
        //                 playerNumberField = resultsRemove.getString("player_number");
        //                 positionField = resultsRemove.getString("position");
        //                 expectedGraduationDateField = resultsRemove.getString("expected_graduation_date");
        //                 System.out.println("| " + idField + " | " + firstNameField + " | " + lastNameField + " | " + playerNumberField + " | " + positionField + " | " + expectedGraduationDateField + " |");
        //             }
        //         }
        //     }
        //     catch(SQLException e)
        //     {
        //         e.printStackTrace();
        //         System.out.println("\nSQL Connection Error.");
        //     }
        //     finally
        //     {
        //         try
        //         {
        //             if (q != null)
        //             {
        //                 q.close();
        //             }

        //             if (connection != null)
        //             {
        //                 connection.close();
        //             }

        //             if (resultsRemove != null)
        //             {
        //                 resultsRemove.close();
        //             }
        //         }
        //         catch(SQLException e)
        //         {
        //             System.out.println("Error closing variables.");
        //             e.printStackTrace();
        //         }
        //     }
        // }
    }
}
