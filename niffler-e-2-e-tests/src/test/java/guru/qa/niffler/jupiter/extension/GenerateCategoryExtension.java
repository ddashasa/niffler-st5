package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.CategoryApi;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.model.CategoryJson;
import java.io.IOException;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class GenerateCategoryExtension implements BeforeEachCallback {
  public static final ExtensionContext.Namespace NAMESPACE
      = ExtensionContext.Namespace.create(GenerateCategoryExtension.class);

  private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
      .build();

  private final Retrofit retrofit = new Retrofit.Builder()
      .client(okHttpClient)
      .baseUrl("http://127.0.0.1:8093/")
      .addConverterFactory(JacksonConverterFactory.create())
      .build();

  @Override
  public void beforeEach(ExtensionContext extensionContext) throws Exception {
    CategoryApi categoryApi = retrofit.create(CategoryApi.class);

    AnnotationSupport.findAnnotation(
        extensionContext.getRequiredTestMethod(),
        GenerateCategory.class
    ).ifPresent(
        category -> {
          CategoryJson categoryJson = new CategoryJson(
              null,
              category.description(),
              category.username()
          );
          try {
            CategoryJson result = categoryApi.createCategory(categoryJson).execute().body();
            extensionContext.getStore(NAMESPACE).put("category", result);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
    );
  }
}
