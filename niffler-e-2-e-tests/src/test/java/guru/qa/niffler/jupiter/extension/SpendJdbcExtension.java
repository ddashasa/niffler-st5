package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.jdbc.entity.SpendEntity;
import guru.qa.niffler.data.jdbc.repository.SpendRepository;
import guru.qa.niffler.data.jdbc.repository.SpendRepositoryJdbc;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.model.SpendJson;
import java.util.Date;

public class SpendJdbcExtension extends AbstractSpendExtension {
  private final SpendRepository spendRepository = new SpendRepositoryJdbc();

  @Override
  protected SpendJson createSpend(Spend spend) {
    SpendEntity spendEntity = new SpendEntity();
    spendEntity.setSpendDate(new Date());
    spendEntity.getCategory().setCategory(spend.category());
    spendEntity.setCurrency(spend.currency());
    spendEntity.setAmount(spend.amount());
    spendEntity.setDescription(spend.description());
    spendEntity.setUsername(spend.username());

    return SpendJson.fromEntity(spendRepository.createSpend(spendEntity));
  }

  @Override
  protected void removeSpend(SpendJson spend) {
    spendRepository.removeSpend(SpendEntity.fromJson(spend));
  }
}
