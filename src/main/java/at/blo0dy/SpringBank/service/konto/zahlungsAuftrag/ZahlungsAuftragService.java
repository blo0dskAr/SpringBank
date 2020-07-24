package at.blo0dy.SpringBank.service.konto.zahlungsAuftrag;

import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.konto.zahlungsAuftrag.ZahlungsAuftrag;
import at.blo0dy.SpringBank.model.zv.Datentraeger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ZahlungsAuftragService {

  ZahlungsAuftrag save(ZahlungsAuftrag zahlungsAuftrag);

  List<ZahlungsAuftrag> findZahlungsAuftraegeByKontonummer(String kontonummer);

  Long countOffeneZahlungsAuftraegeByKontoId(Long kontoId);

//  BigDecimal getSummeOffenerAuszahlungenByKontoId(Long kontoId);

  boolean checkAuszahlungWithVerfuegbarerBetrag(Konto konto, BigDecimal auszahlungsBetrag);

  List<ZahlungsAuftrag> findAllAngelegteZahlungsAuftraegeByDateAndType(LocalDate auftragsdatum, String type);

  BigDecimal processSingleZahlungsAuftrag(ZahlungsAuftrag zahlungsAuftrag, Datentraeger datentraeger);

//  Datentraeger createDatentraegerWithAuszahlungen(ZahlungsAuftrag zahlungsAuftragsSample);
//
//  Datentraeger saveFinalDatentraeger(Datentraeger datentraeger);

  String processZahlungsAuftragsBatch(ZahlungsAuftrag zahlungAuftrag);

  BigDecimal getVerfügbarerSaldoByKontoId(Long kontoId);

}
