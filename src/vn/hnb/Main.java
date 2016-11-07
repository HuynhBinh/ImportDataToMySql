package vn.hnb;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main
{
    private static final String DriverClassName = "com.mysql.jdbc.Driver";

    private static final String DB_CONNECTION = "jdbc:mysql://asia.tihub.net:3306/";

    private static final String DB_NAME = "admin_access_asia_v1";

    private static final String DB_USER = "binhhn";

    private static final String DB_PASSWORD = "cicevn2007";


    public static void main(String[] args)
    {

        try
        {
            Class.forName(DriverClassName);
        }
        catch (Exception ex)
        {
            logError(ex.getMessage());
        }

        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(new Runnable()
        {
            @Override
            public void run()
            {
                worker();
            }
        });
    }

    private static void executeSQL(Connection conn, String sql)
    {
        try (Statement stmt = conn.createStatement())
        {
            stmt.execute(sql);
        }
        catch (Exception ex)
        {
            logError(ex.getMessage() + " " + sql);
        }
    }


    private static void worker()
    {
        List<ImportObject> importObjectList = readCSV();
        //insertData(importObjectList);
    }


    private static List<ImportObject> readCSV()
    {

        List<ImportObject> importObjectList = new ArrayList<>();

        try
        {
            FileReader fr = new FileReader("d:\\qwe.csv");
            BufferedReader br = new BufferedReader(fr);
            String stringRead = br.readLine();

            while (stringRead != null)
            {
                StringTokenizer st = new StringTokenizer(stringRead, ",");

                String id = st.nextToken().trim();
                String name = st.nextToken().trim();
                String parent = st.nextToken().trim();
                String level = st.nextToken().trim();

                ImportObject importObject = new ImportObject(id, name, parent, level);
                importObjectList.add(importObject);

                logInfo(importObject);

                stringRead = br.readLine();
            }
            br.close();
        }
        catch (Exception ex)
        {
            logError(ex.getMessage());
        }

        return importObjectList;
    }


    private static void insertData(List<ImportObject> data)
    {
        Properties properties = new Properties();
        properties.put("user", DB_USER);
        properties.put("password", DB_PASSWORD);
        try (Connection conn = DriverManager.getConnection(DB_CONNECTION, properties))
        {
            executeSQL(conn, "USE " + DB_NAME);

            for(int i = 0; i < data.size(); i++)
            {
                ImportObject object = data.get(i);
                executeSQL(conn, "INSERT INTO `ac_service_sector_and_product_services`(`service_id`, `service_name`, `parent_service_id`, `level`) VALUES ( "+ object.id +",'"+ object.name +"',"+object.parentId+","+object.level+");");
            }

        }
        catch (SQLException ex)
        {
           logError(ex.getMessage());
        }
    }


    private static void logError(String data)
    {
        System.out.println("ERROR: " + data);
    }

    private static void logInfo(ImportObject importObject)
    {
        System.out.println(importObject.toString());
    }
}

