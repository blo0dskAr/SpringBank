package at.blo0dy.SpringBank.service.konto.zahlungsAuftrag;

import at.blo0dy.SpringBank.dao.DatenTraegerRepository;
import at.blo0dy.SpringBank.dao.konto.KontoRepository;
import at.blo0dy.SpringBank.dao.konto.kontoBuchung.KontoBuchungRepository;
import at.blo0dy.SpringBank.dao.konto.zahlungsAuftrag.ZahlungsAuftragRepository;
import at.blo0dy.SpringBank.model.enums.BuchungsArtEnum;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragArtEnum;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragStatusEnum;
import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.konto.kontoBuchung.KontoBuchung;
import at.blo0dy.SpringBank.model.konto.zahlungsAuftrag.ZahlungsAuftrag;
import at.blo0dy.SpringBank.model.zv.Datentraeger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ZahlungsAuftragServiceImpl implements ZahlungsAuftragService{

  ZahlungsAuftragRepository zahlungsAuftragRepository;
  KontoRepository kontoRepository;
  KontoBuchungRepository kontoBuchungRepository;
  DatenTraegerRepository datenTraegerRepository;

  @Autowired
  public ZahlungsAuftragServiceImpl(ZahlungsAuftragRepository zahlungsAuftragRepository, KontoRepository kontoRepository, KontoBuchungRepository kontoBuchungRepository, DatenTraegerRepository datenTraegerRepository) {
    this.zahlungsAuftragRepository = zahlungsAuftragRepository;
    this.kontoRepository = kontoRepository;
    this.kontoBuchungRepository = kontoBuchungRepository;
    this.datenTraegerRepository = datenTraegerRepository;
  }

  @Override
  @Transactional
  public ZahlungsAuftrag save(ZahlungsAuftrag zahlungsAuftrag) {
    return zahlungsAuftragRepository.save(zahlungsAuftrag);
  }

  @Override
  @Transactional
  public List<ZahlungsAuftrag> findZahlungsAuftraegeByKontonummer(String kontonummer) {
    return zahlungsAuftragRepository.findZahlungsAuftraegeByKontonummer(kontonummer);
  }

  @Override
  @Transactional
  public Long countOffeneZahlungsAuftraegeByKontoId(Long kontoId) {
    return zahlungsAuftragRepository.countOffeneZahlungsAuftraegeByKontoId(kontoId);
  }

  @Override
  public boolean checkAuszahlungWithVerfuegbarerSaldo(BigDecimal saldoKonto, BigDecimal auszahlungsBetrag) {
    if (saldoKonto.compareTo(auszahlungsBetrag) == -1) {
      return false;
    } else {
      return true;
    }
  }

  @Override
  @Transactional
  public List<ZahlungsAuftrag> findAllAngelegteZahlungsAuftraegeByDateAndType(LocalDate auftragsdatum, String type) {

    return zahlungsAuftragRepository.findAllAngelegteZahlungsAuftraegeByDateAndType(auftragsdatum, type);
  }

  @Override
  @Transactional
  public String processSingleZahlungsAuftrag(ZahlungsAuftrag zahlungsAuftrag, Datentraeger datentraeger) {

    Konto tmpKonto = zahlungsAuftrag.getKonto();
    BigDecimal neuerSaldo;
    KontoBuchung neueKontoBuchung;

    // Check Art der Auszahlung
    if (zahlungsAuftrag.getAuftragsArt().equals(ZahlungAuftragArtEnum.AUSZAHLUNG)) {
      String buchungsText = "Auszahlung";
      // Check Ob Saldo Verfügbar
      if (checkAuszahlungWithVerfuegbarerSaldo(tmpKonto.getAktSaldo(), zahlungsAuftrag.getBetrag())) {
        // true = erstelle KontoBuchung, update zahlungsAuftrag, (sammle in file, sammle in DB)
        neuerSaldo = tmpKonto.getAktSaldo().subtract(zahlungsAuftrag.getBetrag());

        neueKontoBuchung =  new KontoBuchung(0L, LocalDate.now(),LocalDateTime.now(),null,zahlungsAuftrag.getBetrag(),neuerSaldo,"Auszahlung",tmpKonto, BuchungsArtEnum.SOLL );

      } else {
        // Update ZahlungsAuftrag
        zahlungsAuftragRepository.UpdateZahlungsAuftragById(zahlungsAuftrag.getId(), LocalDateTime.now(), ZahlungAuftragStatusEnum.STORNIERT.toString());
        return "STORNIERT";
      }
    } else {
      String buchungsText = "Einzahlung";
      neuerSaldo = tmpKonto.getAktSaldo().add(zahlungsAuftrag.getBetrag());

      neueKontoBuchung =  new KontoBuchung(0L, LocalDate.now(),LocalDateTime.now(),null,zahlungsAuftrag.getBetrag(),neuerSaldo,"Einzahlung",tmpKonto, BuchungsArtEnum.HABEN );
    }

    // Speichere Buchung
    kontoBuchungRepository.save(neueKontoBuchung);
    // Update KontoSaldo
    kontoRepository.UpdateKontoSaldoById(tmpKonto.getId(),neuerSaldo);

    // Update ZahlungsAuftrag
    zahlungsAuftragRepository.UpdateZahlungsAuftragById(zahlungsAuftrag.getId(), LocalDateTime.now(), ZahlungAuftragStatusEnum.DURCHGEFUEHRT.toString(), datentraeger);
    return "DURCHGEFUEHRT";
  }

  @Override
  @Transactional
  public String processZahlungsAuftragsBatch(ZahlungsAuftrag zahlungsAuftragsSample) {

    List<ZahlungsAuftrag> zahlungsAuftragsList = findAllAngelegteZahlungsAuftraegeByDateAndType(zahlungsAuftragsSample.getAuftragsDatum(), zahlungsAuftragsSample.getAuftragsArt().toString());

    if (zahlungsAuftragsList.isEmpty()) {
      return "Keine Daten zum verarbeiten gefunden";
    }

    Datentraeger datentraeger = new Datentraeger(0, BigDecimal.ZERO, LocalDateTime.now(), null, zahlungsAuftragsSample.getAuftragsArt());
    datenTraegerRepository.save(datentraeger);

    zahlungsAuftragsList.forEach(za -> processSingleZahlungsAuftrag(za, datentraeger));

    int anzahlZahlungsAuftraege = zahlungsAuftragRepository.countByDatentraeger(datentraeger);
    BigDecimal summeZahlungsAuftraege = zahlungsAuftragRepository.sumByDatentraeger(datentraeger);

    datentraeger.setAnzahl(anzahlZahlungsAuftraege);
    datentraeger.setSumme(summeZahlungsAuftraege);
    datenTraegerRepository.save(datentraeger);

    return "DatenträgerId=" + datentraeger.getId() + "  Art=" + zahlungsAuftragsSample.getAuftragsArt().getDisplayName() + "  Anzahl=" + anzahlZahlungsAuftraege + "Summe=" + summeZahlungsAuftraege;
  }


}
