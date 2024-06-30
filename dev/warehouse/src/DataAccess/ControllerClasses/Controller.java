package DataAccess.ControllerClasses;

import DataAccess.DtoClasses.Dto;

import java.io.IOException;
import java.sql.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Controller {
    protected String connectionString;
    private static final Logger log = Logger.getLogger(Controller.class.getName());

    protected Controller() {
        try {
            String currentPath = new File(".").getCanonicalPath();

            // Construct the path to the Inventory.db file in the 'dev' folder
            String path = currentPath+ File.separator+ ".." + File.separator + "Inventory.db";
            //String path = currentPath + File.separator + ".." + File.separator + "dev" + File.separator + "Inventory.db";
            this.connectionString = "jdbc:sqlite:" + path;
        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to get the canonical path for the database file.", e);
            throw new RuntimeException(e.getMessage());
        }

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            log.log(Level.SEVERE, "SQLite JDBC driver not found.", e);
            throw new RuntimeException(e);
        }
    }



    public abstract boolean update(Object[] identifiersValues, String varToUpdate, Object valueToUpdate);
    public abstract boolean insert(Object[] attributesValues);
    public abstract boolean delete(Object[] identifiersValues);
    public abstract Dto convertReaderToObject(ResultSet reader) throws SQLException;

    /**
     * This method inserts the data of the given table into the database.
     * @param attributesNames names of the fields of the table
     * @param attributeValues values of the fields of the table
     * @param tableName name of the table in the database
     * @return true/false depending on whether the insertion worked
     * @throws IllegalArgumentException
     */
    protected boolean insert(String[] attributesNames, Object[] attributeValues, String tableName) {
        if (attributesNames.length != attributeValues.length) {
            throw new IllegalArgumentException("Amount of attributes and values differ!");
        }

        String sql = "INSERT INTO " + tableName + " (";
        for (int i = 0; i < attributesNames.length; i++) {
            sql += attributesNames[i];
            if (i < attributesNames.length - 1) {
                sql += ",";
            } else {
                sql += ") VALUES (";
            }
        }
        for (int i = 0; i < attributeValues.length; i++) {
            sql += "?";
            if (i < attributeValues.length - 1) {
                sql += ",";
            } else {
                sql += ");";
            }
        }

        try (Connection connection = DriverManager.getConnection(connectionString);
             PreparedStatement command = connection.prepareStatement(sql)) {

            for (int i = 0; i < attributeValues.length; i++) {
                command.setObject(i + 1, attributeValues[i]);
            }

            connection.setAutoCommit(false);
            int res = command.executeUpdate();
            connection.commit();
            return res == 1;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * This method updates a table's data field within the database.
     * @param identifiers identifiers of the fields of the table
     * @param identifiersValues values of the identifiers of the fields of the table
     * @param tableName name of the table in the database
     * @param varToUpdate the data field to update
     * @param valueToUpdate the field's new value
     * @return true/false depending on whether the update worked
     * @throws IllegalArgumentException
     */
    protected boolean update(String[] identifiers, Object[] identifiersValues, String tableName, String varToUpdate, Object valueToUpdate) {
        if (identifiers.length != identifiersValues.length) {
            throw new IllegalArgumentException("Amount of identifiers and values differ!");
        }

        String sql = "UPDATE " + tableName + " SET " + varToUpdate + " = ? WHERE ";
        for (int i = 0; i < identifiersValues.length; i++) {
            sql += identifiers[i] + " = ?";
            if (i < identifiersValues.length - 1) {
                sql += " AND ";
            }
        }

        try (Connection connection = DriverManager.getConnection(connectionString);
             PreparedStatement command = connection.prepareStatement(sql)) {

            command.setObject(1, valueToUpdate);
            for (int i = 0; i < identifiersValues.length; i++) {
                command.setObject(i + 2, identifiersValues[i]);
            }

            connection.setAutoCommit(false);
            int res = command.executeUpdate();
            connection.commit();
            return res == 1;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * This method deletes an entry from the table in the database.
     * @param identifiers identifiers of the fields of the table
     * @param identifiersValues values of the identifiers of the fields of the table
     * @param tableName name of the table in the database
     * @return true/false depending on whether the deletion worked
     * @throws IllegalArgumentException
     */
    protected boolean delete(String[] identifiers, Object[] identifiersValues, String tableName) {
        if (identifiers.length != identifiersValues.length) {
            throw new IllegalArgumentException("Amount of identifiers and values differ!");
        }

        String sql = "DELETE FROM " + tableName + " WHERE ";
        for (int i = 0; i < identifiersValues.length; i++) {
            sql += identifiers[i] + " = ?";
            if (i < identifiersValues.length - 1) {
                sql += " AND ";
            }
        }

        try (Connection connection = DriverManager.getConnection(connectionString);
             PreparedStatement command = connection.prepareStatement(sql)) {

            for (int i = 0; i < identifiersValues.length; i++) {
                command.setObject(i + 1, identifiersValues[i]);
            }

            connection.setAutoCommit(false);
            int res = command.executeUpdate();
            connection.commit();
            return res == 1;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * This method deletes all data in the database.
     * @throws IllegalArgumentException
     */
    public void deleteAll() {
        String[] tables = {"discounts", "items", "products", "orders"};

        try (Connection connection = DriverManager.getConnection(connectionString)) {
            connection.setAutoCommit(false);
            for (String table : tables) {
                try (PreparedStatement command = connection.prepareStatement("DELETE FROM " + table)) {
                    command.executeUpdate();
                } catch (Exception e) {
                    throw new IllegalArgumentException(e.getMessage());
                }
            }
            connection.commit();
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
