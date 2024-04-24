package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.model.CurrencyValues;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GenerateSpend {
    String category();

    CurrencyValues currency();

    double amount();

    String description();

    String username();
}
