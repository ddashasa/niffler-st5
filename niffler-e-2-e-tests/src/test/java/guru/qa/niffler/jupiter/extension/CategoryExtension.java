package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.jdbc.entity.CategoryEntity;
import guru.qa.niffler.data.jdbc.repository.SpendRepository;
import guru.qa.niffler.data.jdbc.repository.SpendRepositoryJdbc;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtension implements BeforeEachCallback, AfterEachCallback {

  public static final ExtensionContext.Namespace NAMESPACE
      = ExtensionContext.Namespace.create(CategoryExtension.class);

  private final SpendRepository spendRepository = new SpendRepositoryJdbc();

  @Override
  public void beforeEach(ExtensionContext extensionContext) throws Exception {
    AnnotationSupport.findAnnotation(
        extensionContext.getRequiredTestMethod(),
        Category.class
    ).ifPresent(
        cat -> {
          CategoryJson categoryJson = new CategoryJson(
              null,
              cat.category(),
              cat.username()
          );
          CategoryEntity category = new CategoryEntity();
          category.setCategory(cat.category());
          category.setUsername(cat.username());

          category = spendRepository.createCategory(category);

          extensionContext.getStore(NAMESPACE).put(
              extensionContext.getUniqueId(), CategoryJson.fromEntity(category)
          );
        }
    );
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    CategoryJson categoryJson = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
    spendRepository.removeCategory(CategoryEntity.fromJson(categoryJson));
  }
}
