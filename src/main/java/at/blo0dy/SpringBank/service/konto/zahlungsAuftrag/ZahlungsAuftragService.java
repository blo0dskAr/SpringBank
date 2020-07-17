package at.blo0dy.SpringBank.service.konto.zahlungsAuftrag;

import at.blo0dy.SpringBank.model.konto.zahlungsAuftrag.ZahlungsAuftrag;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.util.List;

public interface ZahlungsAuftragService {

  ZahlungsAuftrag save(ZahlungsAuftrag zahlungsAuftrag);

  List<ZahlungsAuftrag> findZahlungsAuftraegeByKontonummer(String kontonummer);

  Long countOffeneZahlungsAuftraegeByKontoId(Long kontoId);

  BindingResult checkAuszahlungWithVerfuegbarerSaldo(BindingResult result, BigDecimal saldoKonto, BigDecimal auszahlungsBetrag);

}
