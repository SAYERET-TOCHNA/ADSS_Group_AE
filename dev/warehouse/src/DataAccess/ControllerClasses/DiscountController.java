package DataAccess.ControllerClasses;

import DataAccess.DtoClasses.DiscountDto;
import DataAccess.DtoClasses.Dto;
import DataAccess.DtoClasses.OrderDto;
import DomainLayer.Utils;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DiscountController extends Controller{

    public String TABLE_NAME = "discounts";
    public final String[] COLUMNS_NAMES = new String[] {"discountId","startDate", "endDate",
            "isToClients","discountParameter",
            "categories","discountedProducts"};
    public final String[] IDENTIFIERS = new String[] { "discountId" };
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
        boolean isToClients = reader.getBoolean("isToClients");
        LocalDate startDate = Utils.parseDate(reader.getString("startDate"));
        LocalDate endDate = Utils.parseDate(reader.getString("endDate"));
        String categories = reader.getString("categories");
        double discountParameter = reader.getDouble("discountParameter");
        String discountedProducts = reader.getString("discountedProducts");
        int discountId = reader.getInt("discountId");

        return new DiscountDto(isToClients, startDate, endDate, categories, discountParameter, discountedProducts, this, true, discountId);
    }
        public List<DiscountDto> selectAllDiscounts() {
        List<DiscountDto> results = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(connectionString)) {
            String sql = "SELECT * FROM " + TABLE_NAME;
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        results.add((DiscountDto) convertReaderToObject(resultSet));
                    }

                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return results;
    }
}