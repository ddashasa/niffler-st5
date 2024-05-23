package guru.qa.niffler.data.jdbc.repository;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.jdbc.DataSourceProvider;
import guru.qa.niffler.data.jdbc.entity.CategoryEntity;
import guru.qa.niffler.data.jdbc.entity.SpendEntity;
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
  public CategoryEntity editCategory(CategoryEntity category) {
    try (Connection conn = spendDataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(
            "UPDATE category SET category = ?, username = ? WHERE id = ?"
        )) {
      ps.setString(1, category.getCategory());
      ps.setString(2, category.getUsername());
      ps.setObject(3, category.getId());
      int rowsAffected = ps.executeUpdate();
      if (rowsAffected == 0) {
        throw new IllegalStateException("No category found with ID: " + category.getId());
      }
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

  @Override
  public SpendEntity createSpend(SpendEntity spend) {
    try (Connection conn = spendDataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO spend (username, currency, spend_date, amount, description, category_id) VALUES (?, ?, ?, ?, ?, ?)",
            PreparedStatement.RETURN_GENERATED_KEYS
        )) {
      ps.setString(1, spend.getUsername());
      ps.setString(2, spend.getCurrency().name());
      ps.setDate(3, new java.sql.Date(spend.getSpendDate().getTime()));
      ps.setDouble(4, spend.getAmount());
      ps.setString(5, spend.getDescription());
      ps.setObject(6, spend.getCategory().getId());
      ps.executeUpdate();

      UUID generatedId = null;
      try (ResultSet resultSet = ps.getGeneratedKeys()) {
        if (resultSet.next()) {
          generatedId = UUID.fromString(resultSet.getString(1));
        } else {
          throw new IllegalStateException("Failed to retrieve generated ID");
        }
      }
      spend.setId(generatedId);
      return spend;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public SpendEntity editSpend(SpendEntity spend) {
    try (Connection conn = spendDataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(
            "UPDATE spend SET username = ?, currency = ?, spend_date = ?, amount = ?, description = ?, category_id = ? WHERE id = ?"
        )) {
      ps.setString(1, spend.getUsername());
      ps.setString(2, spend.getCurrency().name());
      ps.setDate(3, new java.sql.Date(spend.getSpendDate().getTime()));
      ps.setDouble(4, spend.getAmount());
      ps.setString(5, spend.getDescription());
      ps.setObject(6, spend.getCategory().getId());
      ps.setObject(7, spend.getId());
      int rowsAffected = ps.executeUpdate();
      if (rowsAffected == 0) {
        throw new IllegalStateException("No spend found with ID: " + spend.getId());
      }
      return spend;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void removeSpend(SpendEntity spend) {
    try (Connection conn = spendDataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(
            "DELETE FROM spend WHERE id = ?"
        )) {
      ps.setObject(1, spend.getId());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

}
