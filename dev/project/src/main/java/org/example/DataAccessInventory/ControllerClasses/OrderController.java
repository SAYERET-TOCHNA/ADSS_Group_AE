package org.example.DataAccessInventory.ControllerClasses;

import org.example.DataAccessInventory.DtoClasses.Dto;
import org.example.DataAccessInventory.DtoClasses.OrderDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderController extends Controller{

    public String TABLE_NAME = "orders";
    public final String[] COLUMNS_NAMES = new String[] {"orderId","productName", "amount","dayOfMonth"};
    public final String[] IDENTIFIERS = new String[] { "orderId" };
    @Override
    public boolean update(Object[] identifiersValues, String varToUpdate, Object valueToUpdate) {
        return update(IDENTIFIERS, identifiersValues, TABLE_NAME, varToUpdate, valueToUpdate);
    }

    @Override
    public boolean insert(Object[] attributesValues) {
        return insert(COLUMNS_NAMES, attributesValues, TABLE_NAME);
    }

    @Override
    public boolean delete(Object[] identifiersValues) {
        return delete(IDENTIFIERS, identifiersValues, TABLE_NAME);    }

    @Override
    public Dto convertReaderToObject(ResultSet reader) throws SQLException {
        int orderId = reader.getInt("orderId");
        String productName = reader.getString("productName");
        int amount = reader.getInt("amount");
        int dayOfMonth = reader.getInt("dayOfMonth");

        return new OrderDto(productName, amount, dayOfMonth,this,true,orderId);
    }
    public List<OrderDto> selectAllOrders() {
        List<OrderDto> results = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(connectionString)) {
            String sql = "SELECT * FROM " + TABLE_NAME;
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        results.add((OrderDto) convertReaderToObject(resultSet));
                    }

                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return results;
    }
}




