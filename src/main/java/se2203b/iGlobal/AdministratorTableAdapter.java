package se2203b.iGlobal;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Abdelkader Ouda
 */
public class AdministratorTableAdapter implements DataStore {
    private Connection connection;
    private String DB_URL = "jdbc:derby:iGlobalDB";

    public AdministratorTableAdapter(Boolean reset) throws SQLException {
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        if (reset) {
            try {
                stmt.execute("DROP TABLE Administrator");
            } catch (SQLException ex) {
                // Table did not exist, ignore
            }
            try {
                stmt.execute("DROP TABLE UserAccount");
            } catch (SQLException ex) {
                // Table did not exist, ignore
            }
        }
        try {
            String command = "CREATE TABLE UserAccount ("
                    + "userAccountName VARCHAR(30) NOT NULL PRIMARY KEY,"
                    + "encryptedPassword VARCHAR(100) NOT NULL,"
                    + "passwordSalt VARCHAR(50) NOT NULL,"
                    + "accountType VARCHAR(10) NOT NULL"
                    + ")";
            stmt.execute(command);
        } catch (SQLException ex) {
            // Table exists; ignore error.
        }
        try {
            String command = "CREATE TABLE Administrator ("
                    + "id VARCHAR(9) NOT NULL PRIMARY KEY, "
                    + "firstName VARCHAR(60) NOT NULL, "
                    + "lastName VARCHAR(60) NOT NULL, "
                    + "email VARCHAR(60), "
                    + "phone VARCHAR(60), "
                    + "dateCreated DATE, "
                    + "userAccount VARCHAR(30) REFERENCES UserAccount(userAccountName)"
                    + ")";
            stmt.execute(command);
        } catch (SQLException ex) {
            // Table exists; ignore error.
        }

        try {
            addAmin();
        } catch (SQLException ex) {
            // If default admin already exists, ignore error.
        }

        connection.close();
    }

    // Add the default admin account
    private void addAmin() throws SQLException {
        Administrator administrator = new Administrator();

        administrator.setID("1");
        administrator.setFirstName("Default iGlobal");
        administrator.setLastName("Admin");
        administrator.setEmail("admin@iGlobal.com");
        administrator.setPhone("519 123 4567");
        // Get current date to be burned in with admin user data
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = new java.util.Date();
        administrator.setDateCreated(java.sql.Date.valueOf(dateFormat.format(date)));

        addNewRecord(administrator);

        Random random = new Random();
        String salt = Integer.toString(random.nextInt());
        String defaultAdminPassword = "admin";
        UserAccount account = new UserAccount("admin", encrypt(defaultAdminPassword, salt), salt, "admin");
        DataStore userAccount = new UserAccountTableAdapter(false);
        userAccount.addNewRecord(account);

        administrator.setUserAccount(account);

        updateRecord(administrator);
    }

    @Override
    public void addNewRecord(Object data) throws SQLException {
        Administrator administrator = (Administrator) data;
        connection = DriverManager.getConnection(DB_URL);
        // Use a PreparedStatement to insert the administrator record with a valid SQL Date
        String insertSQL = "INSERT INTO Administrator (id, firstName, lastName, email, phone, dateCreated) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
            ps.setString(1, administrator.getID());
            ps.setString(2, administrator.getFirstName());
            ps.setString(3, administrator.getLastName());
            ps.setString(4, administrator.getEmail());
            ps.setString(5, administrator.getPhone());
            if (administrator.getDateCreated() != null) {
                // Convert java.util.Date to java.sql.Date
                java.sql.Date sqlDate = new java.sql.Date(administrator.getDateCreated().getTime());
                ps.setDate(6, sqlDate);
            } else {
                ps.setDate(6, null);
            }
            ps.executeUpdate();
        }
        connection.close();
    }

    private String encrypt(String password, String salt) {
        try {
            String saltedPassword = password + salt;
            MessageDigest crypto = MessageDigest.getInstance("SHA-256");
            byte[] passBytes = saltedPassword.getBytes();
            byte[] passHash = crypto.digest(passBytes);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < passHash.length; i++) {
                sb.append(Integer.toString((passHash[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateRecord(Object data) throws SQLException {
        Administrator administrator = (Administrator) data;
        connection = DriverManager.getConnection(DB_URL);
        String updateSQL = "UPDATE Administrator SET firstName = ?, lastName = ?, email = ?, phone = ?, dateCreated = ?, userAccount = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(updateSQL)) {
            ps.setString(1, administrator.getFirstName());
            ps.setString(2, administrator.getLastName());
            ps.setString(3, administrator.getEmail());
            ps.setString(4, administrator.getPhone());
            if (administrator.getDateCreated() != null) {
                java.sql.Date sqlDate = new java.sql.Date(administrator.getDateCreated().getTime());
                ps.setDate(5, sqlDate);
            } else {
                ps.setDate(5, null);
            }
            if (administrator.getUserAccount() != null && administrator.getUserAccount().getUserAccountName() != null) {
                ps.setString(6, administrator.getUserAccount().getUserAccountName());
            } else {
                ps.setNull(6, Types.VARCHAR);
            }
            ps.setString(7, administrator.getID());
            ps.executeUpdate();
        }
        connection.close();
    }

    @Override
    public Object findOneRecord(String key) throws SQLException {
        Administrator administrator = new Administrator();
        connection = DriverManager.getConnection(DB_URL);
        String command = "SELECT * FROM Administrator WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(command)) {
            ps.setString(1, key);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                administrator.setID(rs.getString("id"));
                administrator.setFirstName(rs.getString("firstName"));
                administrator.setLastName(rs.getString("lastName"));
                administrator.setEmail(rs.getString("email"));
                administrator.setPhone(rs.getString("phone"));
                administrator.setDateCreated(rs.getDate("dateCreated"));
                if (rs.getString("userAccount") != null) {
                    UserAccount account = new UserAccount(rs.getString("userAccount"), "", "", "");
                    administrator.setUserAccount(account);
                }
            }
        }
        connection.close();
        return administrator;
    }

    @Override
    public Object findOneRecord(Object userAccount) throws SQLException {
        Administrator administrator = new Administrator();
        connection = DriverManager.getConnection(DB_URL);
        String command = "SELECT * FROM Administrator WHERE userAccount = ?";
        try (PreparedStatement ps = connection.prepareStatement(command)) {
            ps.setString(1, ((UserAccount) userAccount).getUserAccountName());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                administrator.setID(rs.getString("id"));
                administrator.setFirstName(rs.getString("firstName"));
                administrator.setLastName(rs.getString("lastName"));
                administrator.setEmail(rs.getString("email"));
                administrator.setPhone(rs.getString("phone"));
                administrator.setDateCreated(rs.getDate("dateCreated"));
            }
        }
        connection.close();
        return administrator;
    }

    @Override
    public void deleteOneRecord(Object data) throws SQLException {
        Administrator administrator = (Administrator) data;
        connection = DriverManager.getConnection(DB_URL);
        String deleteSQL = "DELETE FROM Administrator WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(deleteSQL)) {
            ps.setString(1, administrator.getID());
            ps.executeUpdate();
        }
        connection.close();
    }

    @Override
    public void deleteRecords(Object referencedObject) throws SQLException {
        // Not implemented
    }

    @Override
    public List<String> getKeys() throws SQLException {
        List<String> list = new ArrayList<>();
        connection = DriverManager.getConnection(DB_URL);
        String command = "SELECT id FROM Administrator";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(command)) {
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        }
        connection.close();
        return list;
    }

    @Override
    public List<Object> getAllRecords() throws SQLException {
        List<Object> list = new ArrayList<>();
        connection = DriverManager.getConnection(DB_URL);
        String command = "SELECT * FROM Administrator";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(command)) {
            while (rs.next()) {
                Administrator administrator = new Administrator();
                administrator.setID(rs.getString("id"));
                administrator.setFirstName(rs.getString("firstName"));
                administrator.setLastName(rs.getString("lastName"));
                administrator.setEmail(rs.getString("email"));
                administrator.setPhone(rs.getString("phone"));
                administrator.setDateCreated(rs.getDate("dateCreated"));
                if (rs.getString("userAccount") != null) {
                    UserAccount account = new UserAccount(rs.getString("userAccount"), "", "", "");
                    administrator.setUserAccount(account);
                }
                list.add(administrator);
            }
        }
        connection.close();
        return list;
    }

    @Override
    public List<Object> getAllRecords(Object referencedObject) throws SQLException {
        return null;
    }
}
