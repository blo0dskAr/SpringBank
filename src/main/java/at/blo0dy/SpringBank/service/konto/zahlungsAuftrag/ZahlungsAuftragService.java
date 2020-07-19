package at.blo0dy.SpringBank.service.konto.zahlungsAuftrag;

import at.blo0dy.SpringBank.model.enums.ZahlungAuftragArtEnum;
import at.blo0dy.SpringBank.model.konto.zahlungsAuftrag.ZahlungsAuftrag;
import at.blo0dy.SpringBank.model.zv.Datentraeger;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ZahlungsAuftragService {

  ZahlungsAuftrag save(ZahlungsAuftrag zahlungsAuftrag);

  List<ZahlungsAuftrag> findZahlungsAuftraegeByKontonummer(String kontonummer);

  Long countOffeneZahlungsAuftraegeByKontoId(Long kontoId);

  boolean checkAuszahlungWithVerfuegbarerSaldo(BigDecimal saldoKonto, BigDecimal auszahlungsBetrag);

  List<ZahlungsAuftrag> findAllAngelegteZahlungsAuftraegeByDateAndType(LocalDate auftragsdatum, String type);

  String processSingleZahlungsAuftrag(ZahlungsAuftrag zahlungsAuftrag, Datentraeger datentraeger);

  String processZahlungsAuftragsBatch(ZahlungsAuftrag zahlungAuftrag);

}
