
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class FingerDB {

    private static final String tableName = "users";
    private static final String userColumn = "userID";
    private static final String print1Column = "print1";
    private static final String print2Column = "print2";

    private String URL = "jdbc:mysql://localhost:3306/";
    private String host;
    private String database;
    private String userName;
    private String pwd;
    private java.sql.Connection connection = null;
    private String preppedStmtInsert = null;
    private String preppedStmtUpdate = null;

    public class Record {

        String userID;
        byte[] fmdBinary;

        Record(String ID, byte[] fmd) {
            userID = ID;
            fmdBinary = fmd;
        }
    }

    public FingerDB(String _host, String db, String user, String password) {
        database = db;
        userName = user;
        pwd = password;
        host = _host;

        URL = "jdbc:mysql://" + host + ":3306/";
        preppedStmtInsert = "INSERT INTO " + tableName + "(" + userColumn + ","
                + print1Column + ") VALUES(?,?)";
    }

    public FingerDB() {
        initialise();
    }

    public void initialise() {
        try (InputStream input = FingerDB.class.getClassLoader().getResourceAsStream("application.properties")) {

            Properties prop = new Properties();

            if (input == null) {
                System.err.println("Sorry, unable to find application.properties");
                //return;
            }

            //load a properties file from class path, inside static method
            prop.load(input);
            //get the property value and print it out
            prop.list(System.out);

            database = prop.getProperty("database.database");
            userName = prop.getProperty("database.user");
            pwd = prop.getProperty("database.password");
            host = prop.getProperty("database.host");

            URL = "jdbc:mysql://" + host + ":3306/";
            preppedStmtInsert = "INSERT INTO " + tableName + "(" + userColumn + ","
                    + print1Column + ") VALUES(?,?)";
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void finalize() {
        try {
            connection.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void Open() throws SQLException {
        connection = DriverManager.getConnection(URL + database, userName, pwd);
    }

    public void Close() throws SQLException {
        connection.close();
    }

    public boolean UserExists(String userID) throws SQLException {
        String sqlStmt = "Select " + userColumn + " from " + tableName
                + " WHERE " + userColumn + "='" + userID + "'";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sqlStmt);
        return rs.next();
    }

    public void Insert(String userID, byte[] print1) throws SQLException {
        java.sql.PreparedStatement pst = connection
                .prepareStatement(preppedStmtInsert);
        pst.setString(1, userID);
        pst.setBytes(2, print1);
        pst.execute();
    }

    public List<Record> GetAllFPData() throws SQLException {
        List<Record> listUsers = new ArrayList<Record>();
        String sqlStmt = "Select * from " + tableName;
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sqlStmt);
        while (rs.next()) {
            if (rs.getBytes(print1Column) != null) {
                listUsers.add(new Record(rs.getString(userColumn), rs
                        .getBytes(print1Column)));
            }
        }
        return listUsers;
    }

    public String GetConnectionString() {
        return URL + " User: " + this.userName;
    }

    public String GetExpectedTableSchema() {
        return "Table: " + tableName + " PK(VARCHAR(32)): " + userColumn
                + "VARBINARY(4000): " + print1Column;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("--Testing database connection--");
        System.out.println("Inserting test data user:TestUser byte[]:TestUsersFringerprint1");
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyymmddhhmmss");
        System.out.println(formatter.format(date));

        //FingerDB fingerDatabase = new FingerDB("localhost", "vdoxxdb", "vdoxx", "vdoxx999");
        FingerDB fingerDatabase = new FingerDB();
        fingerDatabase.Open();
        fingerDatabase.Insert("TestUser-" + formatter.format(date), "TestUsersFringerprint1".getBytes(StandardCharsets.UTF_8));
        fingerDatabase.Close();

        System.out.println("Inserted data successfully");

        System.out.println("Fetching same data for verification");
        fingerDatabase.Open();
        List<Record> databaseRecordList = fingerDatabase.GetAllFPData();
        for (Record record : databaseRecordList) {
            System.out.println("userID:" + record.userID + " \nfmdBinary:" + new String(record.fmdBinary));
        }
        fingerDatabase.Close();

    }
}
