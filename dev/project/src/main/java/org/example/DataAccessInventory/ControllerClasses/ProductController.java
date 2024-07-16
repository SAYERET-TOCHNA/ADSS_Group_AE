package org.example.DataAccessInventory.ControllerClasses;

import org.example.DataAccessInventory.DtoClasses.Dto;
import org.example.DataAccessInventory.DtoClasses.OrderDto;
import org.example.DataAccessInventory.DtoClasses.ProductDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductController extends Controller{
    public String TABLE_NAME = "products";
    public final String[] COLUMNS_NAMES = new String[] {"name","category", "buyingCost","sellingCost"
            ,"manufacturer","minimumAmountAllowed","wareHouseLocation","storeLocation"};
    public final String[] IDENTIFIERS = new String[] {"name"};
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
       // System.out.println("size values = " +identifiersValues.length);
        return delete(IDENTIFIERS, identifiersValues, TABLE_NAME);    }

    @Override
    public Dto convertReaderToObject(ResultSet reader) throws SQLException {
        String name = reader.getString("name");
        double buyingCost = reader.getDouble("buyingCost");
        double sellingCost = reader.getDouble("sellingCost");
        String category = reader.getString("category");
        String manufacturer = reader.getString("manufacturer");
        int minimumAmountAllowed = reader.getInt("minimumAmountAllowed");
        String wareHouseLocation = reader.getString("wareHouseLocation");
        String storeLocation = reader.getString("storeLocation");

        return new ProductDto(name, buyingCost, sellingCost, category, manufacturer, minimumAmountAllowed, wareHouseLocation, storeLocation, this, true);
    }
    public List<ProductDto> selectAllProducts() {
        List<ProductDto> results = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(connectionString)) {
            String sql = "SELECT * FROM " + TABLE_NAME;
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        results.add((ProductDto) convertReaderToObject(resultSet));
                    }

                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return results;
    }
}
