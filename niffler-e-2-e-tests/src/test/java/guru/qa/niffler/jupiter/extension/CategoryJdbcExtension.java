package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.jdbc.entity.CategoryEntity;
import guru.qa.niffler.data.jdbc.repository.SpendRepository;
import guru.qa.niffler.data.jdbc.repository.SpendRepositoryJdbc;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;

public class CategoryJdbcExtension extends AbstractCategoryExtension {
  private final SpendRepository spendRepository = new SpendRepositoryJdbc();

  @Override
  protected CategoryJson createCategory(Category category) {
    CategoryEntity categoryEntity = new CategoryEntity();
    categoryEntity.setCategory(category.category());
    categoryEntity.setUsername(category.username());
    categoryEntity = spendRepository.createCategory(categoryEntity);
    return CategoryJson.fromEntity(categoryEntity);
  }

  @Override
  protected void removeCategory(CategoryJson category) {
    spendRepository.removeCategory(CategoryEntity.fromJson(category));
  }
}
