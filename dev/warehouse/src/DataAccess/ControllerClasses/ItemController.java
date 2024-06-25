package DataAccess.ControllerClasses;

import DataAccess.DtoClasses.Dto;
import DataAccess.DtoClasses.ItemDto;
import DataAccess.DtoClasses.OrderDto;
import DomainLayer.Utils;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ItemController extends Controller{
    public String TABLE_NAME = "items";
    public final String[] COLUMNS_NAMES = new String[] {"itemID","productName", "expiredDate","isInWareHouse","isDamaged"};
    public final String[] IDENTIFIERS = new String[] { "itemID" };
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
        String productName = reader.getString("productName");
        int itemID = reader.getInt("itemID");
        LocalDate expiredDate = Utils.parseDate(reader.getString("expiredDate"));
        boolean isInWareHouse = reader.getBoolean("isInWareHouse");
        boolean isDamaged = reader.getBoolean("isDamaged");

        return new ItemDto(productName, itemID, expiredDate, isInWareHouse, isDamaged, this, true);
    }

    public List<ItemDto> selectAllItems() {
        List<ItemDto> results = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(connectionString)) {
            String sql = "SELECT * FROM " + TABLE_NAME;
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        results.add((ItemDto) convertReaderToObject(resultSet));
                    }

                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return results;
    }
}
