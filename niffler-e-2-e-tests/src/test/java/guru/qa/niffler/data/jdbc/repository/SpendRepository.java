package guru.qa.niffler.data.jdbc.repository;

import guru.qa.niffler.data.jdbc.entity.CategoryEntity;

public interface SpendRepository {
  CategoryEntity createCategory(CategoryEntity category);
  void removeCategory(CategoryEntity category);
}
