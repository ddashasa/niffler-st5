package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import guru.qa.niffler.data.jdbc.entity.CategoryEntity;
import java.util.UUID;

public record CategoryJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("category")
        String category,
        @JsonProperty("username")
        String username) {
        public static CategoryJson fromEntity(CategoryEntity category) {
                return new CategoryJson(
                    category.getId(),
                    category.getCategory(),
                    category.getUsername()
                );
        }
}
