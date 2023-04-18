package jm.task.core.jdbc.dao;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private Connection connectionVar;
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Connection connection = Util.getConnecton()) {
            PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS userstable (id bigint AUTO_INCREMENT, name varchar(20), lastName varchar(20), age tinyint, primary key (id))");
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try (Connection connection = Util.getConnecton()){
            PreparedStatement statement = connection.prepareStatement("DROP TABLE IF EXISTS userstable" );
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = Util.getConnecton()) {
            connection.setAutoCommit(false);
            connectionVar = connection;
            PreparedStatement statement = connection.prepareStatement("INSERT INTO userstable (name, lastName, age) VALUES (?, ?, ?)");
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);
            statement.execute();
            System.out.println("User с именем " + name +" добавлен в базу данных");
            connectionVar.commit();
        } catch (SQLException e) {
            e.printStackTrace();
                try {
                    connectionVar.rollback();
                } catch (SQLException ex) {
                    System.out.println(">Unsuccessful rollback<");
                }
        }
    }

    public void removeUserById(long id) {
        try (Connection connection = Util.getConnecton()) {
            connection.setAutoCommit(false);
            connectionVar = connection;
            PreparedStatement statement = connection.prepareStatement("DELETE FROM userstable WHERE id="+ id);
            statement.execute();
            connectionVar.commit();
        } catch (SQLException e) {
            e.printStackTrace();
                try {
                    connectionVar.rollback();
                } catch (SQLException ex) {
                    System.out.println(">Unsuccessful rollback<");
                }
        }
    }

    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        try (Connection connection = Util.getConnecton()) {
            connection.setAutoCommit(false);
            connectionVar = connection;
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("SELECT * FROM userstable");
            while (set.next()) {
                User user = new User();
                user.setId(set.getLong("id"));
                user.setName(set.getString("name"));
                user.setLastName(set.getString("lastName"));
                user.setAge(set.getByte("age"));
                result.add(user);
            }
            connectionVar.commit();
        } catch (SQLException e) {
            e.printStackTrace();
                try {
                    connectionVar.rollback();
                } catch (SQLException ex) {
                    System.out.println(">Unsuccessful rollback<");
                }
        }
        return result;
    }

    public void cleanUsersTable() {
        try (Connection connection = Util.getConnecton()) {
            connection.setAutoCommit(false);
            connectionVar = connection;
            PreparedStatement statement = connection.prepareStatement("TRUNCATE TABLE userstable");
            statement.execute();
            connectionVar.commit();
        } catch (SQLException e) {
            e.printStackTrace();
                try {
                    connectionVar.rollback();
                } catch (SQLException ex) {
                    System.out.println(">Unsuccessful rollback<");
                }
        }
    }
}
