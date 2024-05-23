package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

public abstract class AbstractCategoryExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {
  protected static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(AbstractCategoryExtension.class);

  @Override
  public void beforeEach(ExtensionContext extensionContext) {
    AnnotationSupport.findAnnotation(
        extensionContext.getRequiredTestMethod(),
        Category.class
    ).ifPresent(
        cat -> extensionContext
            .getStore(NAMESPACE)
            .put(extensionContext.getUniqueId(), createCategory(cat))
    );
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    removeCategory(context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class));
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext
        .getParameter()
        .getType()
        .isAssignableFrom(CategoryJson.class);
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId());
  }

  protected abstract CategoryJson createCategory(Category category);

  protected abstract void removeCategory(CategoryJson category);
}

