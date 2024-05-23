package guru.qa.niffler.data.jdbc.entity;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

@Getter
@Setter
@Entity
@Table(name = "spend")
public class SpendEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private UUID id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CurrencyValues currency;

    @Column(name = "spend_date", columnDefinition = "DATE", nullable = false)
    private Date spendDate;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String description;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private CategoryEntity category;

    public static SpendEntity fromJson(SpendJson spendJson) {
        SpendEntity spendEntity = new SpendEntity();
        spendEntity.setId(spendJson.id());
        spendEntity.setUsername(spendJson.username());
        spendEntity.setCurrency(spendJson.currency());
        spendEntity.setSpendDate(spendJson.spendDate());
        spendEntity.setAmount(spendJson.amount());
        spendEntity.setDescription(spendJson.description());
        spendEntity.category.setCategory(spendJson.category());

        return spendEntity;
    }
}
