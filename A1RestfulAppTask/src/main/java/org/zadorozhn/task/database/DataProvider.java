package org.zadorozhn.task.database;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.zadorozhn.task.model.Login;
import org.zadorozhn.task.model.Posting;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

@Slf4j
@Component
public class DataProvider {

    private final String driverName = "org.sqlite.JDBC";
    private final String connectionString = "jdbc:sqlite:D:/sqlite/task.db";
    private final Connection connection;

    @SneakyThrows
    public DataProvider() {
        Class.forName(driverName);
        connection = DriverManager.getConnection(connectionString);
    }

    @SneakyThrows
    public void createLoginTable() {
        connection.createStatement().execute("create table login (" +
                "application text," +
                "appAccountName text," +
                "isActive integer," +
                "jobTitle text," +
                "department text," +
                "primary key(appAccountName)" +
                ");");
    }

    @SneakyThrows
    public void dropLoginTable() {
        connection.createStatement().execute("drop table login");
    }


    @SneakyThrows
    public void dropPostingTable() {
        connection.createStatement().execute("drop table posting");
    }

    @SneakyThrows
    public void createPostingTable() {
        connection.createStatement().execute("create table posting (" +
                "matDoc integer," +
                "item integer," +
                "docDate integer," +
                "postingDate integer," +
                "materialDescription text," +
                "quantity integer," +
                "bun text," +
                "amountLc real," +
                "crcy text," +
                "userName text," +
                "isActive integer," +
                "primary key(matDoc)" +
                ");");
    }

    public void saveLogin(Login login) {
        try {
            String query = "insert into login values (?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, login.getApplication());
            statement.setString(2, login.getAppAccountName());
            statement.setBoolean(3, login.isActive());
            statement.setString(4, login.getJobTitle());
            statement.setString(5, login.getDepartment());
            statement.execute();
        } catch (Exception ex) {
            log.info(ex.getMessage());
        }
    }

    public void savePosting(Posting posting) {
        try {
            String query = "insert into posting values (?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, posting.getMatDoc());
            statement.setInt(2, posting.getItem());
            statement.setLong(3, posting.getDocDate().getEpochSecond());
            statement.setLong(4, posting.getPostingDate().getEpochSecond());
            statement.setString(5, posting.getMaterialDescription());
            statement.setInt(6, posting.getQuantity());
            statement.setString(7, posting.getBun());
            statement.setDouble(8, posting.getAmountLc());
            statement.setString(9, posting.getCrcy());
            statement.setString(10, posting.getUserName());
            statement.setBoolean(11, posting.isAuthorizedDelivery());
            statement.execute();
        } catch (SQLException ex) {
            log.info(ex.getMessage());
        }
    }

    public List<Login> getAllLogins() {
        List<Login> logins = new ArrayList<>();
        try {
            ResultSet rs = connection.createStatement().executeQuery("select * from login");
            while (rs.next()) {
                Login login = new Login(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getBoolean(3),
                        rs.getString(4),
                        rs.getString(5)
                );
                logins.add(login);
            }
        } catch (SQLException ex) {
            log.info(ex.getMessage());
        }
        return logins;
    }

    public List<Posting> getAllPostings() {
        List<Posting> postings = new ArrayList<>();
        try {
            ResultSet rs = connection.createStatement().executeQuery("select * from posting");
            while (rs.next()) {
                Posting posting = new Posting(
                        rs.getLong(1),
                        rs.getInt(2),
                        Instant.ofEpochSecond(rs.getLong(3)),
                        Instant.ofEpochSecond(rs.getLong(4)),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getString(7),
                        rs.getDouble(8),
                        rs.getString(9),
                        rs.getString(10),
                        rs.getBoolean(11)
                );
                postings.add(posting);
            }
        } catch (SQLException ex) {
            log.info(ex.getMessage());
        }
        return postings;
    }

    public List<Posting> getPostingsByPeriod(String from, String to, boolean isActive) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        List<Posting> postings = new ArrayList<>();
        try {
            String query = "select * from posting where ? < docDate and ? > postingDate and isActive = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, LocalDate.parse(from, formatter).atStartOfDay(ZoneId.systemDefault()).toInstant().getEpochSecond());
            preparedStatement.setLong(2, LocalDate.parse(to, formatter).atStartOfDay(ZoneId.systemDefault()).toInstant().getEpochSecond());
            preparedStatement.setBoolean(3, isActive);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Posting posting = new Posting(
                        rs.getLong(1),
                        rs.getInt(2),
                        Instant.ofEpochSecond(rs.getLong(3)),
                        Instant.ofEpochSecond(rs.getLong(4)),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getString(7),
                        rs.getDouble(8),
                        rs.getString(9),
                        rs.getString(10),
                        rs.getBoolean(11)
                );
                postings.add(posting);
            }
        } catch (SQLException ex) {
            log.info(ex.getMessage());
        }
        return postings;
    }

    public Optional<Posting> getPostingById(long id) {
        Optional<Posting> posting = Optional.empty();

        try {
            String query = "select * from posting where matDoc = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                posting = Optional.of(new Posting(
                        rs.getLong(1),
                        rs.getInt(2),
                        Instant.ofEpochSecond(rs.getLong(3)),
                        Instant.ofEpochSecond(rs.getLong(4)),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getString(7),
                        rs.getDouble(8),
                        rs.getString(9),
                        rs.getString(10),
                        rs.getBoolean(11)));
            }
            return posting;
        } catch (SQLException ex) {
            log.info(ex.getMessage());
        }

        return posting;
    }

    public Optional<Login> getLoginById(String id) {
        Optional<Login> login = Optional.empty();

        try {
            String query = "select * from login where appAccountName = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                login = Optional.of(new Login(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getBoolean(3),
                        rs.getString(4),
                        rs.getString(5)
                ));
            }
            return login;
        } catch (SQLException ex) {
            log.info(ex.getMessage());
        }

        return login;
    }
}
