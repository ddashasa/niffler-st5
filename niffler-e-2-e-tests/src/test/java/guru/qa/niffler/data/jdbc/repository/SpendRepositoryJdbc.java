package guru.qa.niffler.data.jdbc.repository;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.jdbc.DataSourceProvider;
import guru.qa.niffler.data.jdbc.entity.CategoryEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import javax.sql.DataSource;

public class SpendRepositoryJdbc implements SpendRepository {

  private static final DataSource spendDataSource = DataSourceProvider.dataSource(DataBase.SPEND);

  @Override
  public CategoryEntity createCategory(CategoryEntity category) {
    try (Connection conn = spendDataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO category (category, username) VALUES (?, ?)",
            PreparedStatement.RETURN_GENERATED_KEYS
        )) {
      ps.setString(1, category.getCategory());
      ps.setString(2, category.getUsername());
      ps.executeUpdate();

      UUID generatedId = null;
      try (ResultSet resultSet = ps.getGeneratedKeys()) {
        if (resultSet.next()) {
          generatedId = UUID.fromString(resultSet.getString("id"));
        } else {
          throw new IllegalStateException("Can't access to id");
        }
      }
      category.setId(generatedId);
      return category;

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }


  @Override
  public void removeCategory(CategoryEntity category) {
    try (Connection conn = spendDataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(
            "DELETE FROM category WHERE id = ?"
        )) {
      ps.setObject(1, category.getId());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
