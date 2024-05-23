package guru.qa.niffler.data.jdbc.repository;

import guru.qa.niffler.data.jdbc.entity.CategoryEntity;
import guru.qa.niffler.data.jdbc.entity.SpendEntity;

public interface SpendRepository {
  CategoryEntity createCategory(CategoryEntity category);
  CategoryEntity editCategory(CategoryEntity category);
  void removeCategory(CategoryEntity category);
  SpendEntity createSpend(SpendEntity spend);
  SpendEntity editSpend(SpendEntity spend);
  void removeSpend(SpendEntity spend);
}
