package at.blo0dy.SpringBank.service.konto.zahlungsAuftrag;

import at.blo0dy.SpringBank.dao.DatenTraegerRepository;
import at.blo0dy.SpringBank.dao.konto.KontoRepository;
import at.blo0dy.SpringBank.dao.konto.kontoBuchung.KontoBuchungRepository;
import at.blo0dy.SpringBank.dao.konto.zahlungsAuftrag.ZahlungsAuftragRepository;
import at.blo0dy.SpringBank.model.enums.BuchungsArtEnum;
import at.blo0dy.SpringBank.model.enums.KontoProduktEnum;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragArtEnum;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragStatusEnum;
import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.konto.giro.GiroKonto;
import at.blo0dy.SpringBank.model.konto.kontoBuchung.KontoBuchung;
import at.blo0dy.SpringBank.model.konto.kredit.KreditKonto;
import at.blo0dy.SpringBank.model.konto.zahlungsAuftrag.ZahlungsAuftrag;
import at.blo0dy.SpringBank.model.zv.Datentraeger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
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
//  @Transactional
  public ZahlungsAuftrag save(ZahlungsAuftrag zahlungsAuftrag) {
    log.debug("Zahlungsauftrag wird gespeichert.");

    ZahlungsAuftrag savedZahlungsAuftrag = zahlungsAuftragRepository.save(zahlungsAuftrag);
    log.debug("Zahlungsauftrag wurde erfolgreich gespeichert. ID: " + savedZahlungsAuftrag.getId());

    return savedZahlungsAuftrag;
  }

  @Override
  @Transactional(readOnly = true)
  public List<ZahlungsAuftrag> findZahlungsAuftraegeByKontonummer(String kontonummer) {
    log.debug("Zahlungsaufträge für Kontonummer: " + kontonummer + " werden gesucht.");
    return zahlungsAuftragRepository.findZahlungsAuftraegeByKontonummer(kontonummer);
  }

  @Override
  @Transactional(readOnly = true)
  public Long countOffeneZahlungsAuftraegeByKontoId(Long kontoId) {
    log.debug("Anzahl offener Zahlungsaufträge for KontoId: " + " wird ermittelt");
    return zahlungsAuftragRepository.countOffeneZahlungsAuftraegeByKontoId(kontoId);
  }

/*  @Override
  @Transactional(readOnly = true)
  public BigDecimal getSummeOffenerAuszahlungenByKontoId(Long kontoId) {
    return zahlungsAuftragRepository.getSummeOffenerAuszahlungsAuftraegeByKontoId(kontoId);
  }*/

  @Override
  @Transactional(readOnly = true)
  public boolean checkAuszahlungWithVerfuegbarerBetrag(Konto konto, BigDecimal auszahlungsBetrag, Boolean isZV) {
    log.debug("Auszahlung von EUR " +  auszahlungsBetrag + " wird mit verfügbaren Betrg am Konto: " + konto.getKontonummer() + " geprüft.");
    BigDecimal verfuegbarerBetrag = getVerfügbarerSaldoByKontoId(konto.getId());
    // Wenn der Check aus dem ZV kommt, und nicht vom Kunden eingegeben wird
    if (isZV = true) {
      // wenn auszahlungsbetrag groesser als der verfuegbare betrag
      if (auszahlungsBetrag.compareTo(konto.getAktSaldo()) == 1) {
        log.debug("--> Betrag nicht verfügbar.");
        return false;
      } else {
        log.debug("--> Betrag verfügbar.");
        return true;
      }
      // Wenn der Check aus einer Kunden/Mitarbeiter Eingabe stammt
    } else {
      // wenn verfuegbarer betrag (aktsaldo - offene auszahlungen bis heute) kleiner als Auszahlung
      if (verfuegbarerBetrag.compareTo(auszahlungsBetrag) == -1) {
        log.debug("--> Betrag nicht verfügbar.");
        return false;
      } else {
        log.debug("--> Betrag verfügbar.");
        return true;
      }
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<ZahlungsAuftrag> findAllAngelegteZahlungsAuftraegeByDateAndType(LocalDate auftragsdatum, String type) {
    log.debug("Angelegte Zahlungsaufträge mit Datum: " + auftragsdatum + "und Typ: " + type + " werden gesucht.");
    return zahlungsAuftragRepository.findAllAngelegteZahlungsAuftraegeByDateAndType(auftragsdatum, type);
  }

  @Override
  @Transactional
  public BigDecimal processSingleZahlungsAuftrag(ZahlungsAuftrag zahlungsAuftrag, Datentraeger datentraeger) {

    log.debug("Zahlungsauftrag wird bearbeitet.");
    // Hier das Konto aus der DB neu laden, weil sonst aus dem zahlungsauftrag.getkonto() der alte saldo übermittelt wird bei > 1 buchung.
    Konto tmpKonto = kontoRepository.findByKontonummer(zahlungsAuftrag.getKontonummer());
    BigDecimal neuerSaldo;
    KontoBuchung neueKontoBuchung;
    BuchungsArtEnum buchungsArt;
    String buchungsText;

    // Check Art der Auszahlung
    if (zahlungsAuftrag.getAuftragsArt().equals(ZahlungAuftragArtEnum.AUSZAHLUNG)) {
      buchungsText = "Auszahlung";
      // Check Ob Saldo Verfügbar
      if (checkAuszahlungWithVerfuegbarerBetrag(tmpKonto, zahlungsAuftrag.getBetrag(), true) || tmpKonto.getProdukt() == KontoProduktEnum.KREDIT) {
        // true = erstelle KontoBuchung, update zahlungsAuftrag, (sammle in file(tbd), sammle in DB)
        neuerSaldo = tmpKonto.getAktSaldo().subtract(zahlungsAuftrag.getBetrag());
        buchungsArt = BuchungsArtEnum.SOLL;
      } else {
        // Update ZahlungsAuftrag
        zahlungsAuftrag.setDatAend(LocalDateTime.now());
        zahlungsAuftrag.setAuftragsStatus(ZahlungAuftragStatusEnum.STORNIERT);
        return tmpKonto.getAktSaldo();
      }
    } else {
      buchungsText = "Einzahlung";
      neuerSaldo = tmpKonto.getAktSaldo().add(zahlungsAuftrag.getBetrag());
      buchungsArt = BuchungsArtEnum.HABEN;
    }

    log.debug("Prüfung erfolgreich " + buchungsText + " wird erstellt");

    neueKontoBuchung =  new KontoBuchung(0L, LocalDate.now(),LocalDateTime.now(),null,zahlungsAuftrag.getBetrag(), neuerSaldo,buchungsText,tmpKonto, buchungsArt );

    // Update KontoSaldo
    // TODO: jetzt hab ichs erst checkt was das Transactional genau macht. Da ein update machen ist bescheuert, durchs Transactional wird der neue Saldo in der DB schon nur beim  setten persistiert....
//    kontoRepository.updateKontoSaldoById(tmpKonto.getId(),neuerSaldo);
    log.debug("Saldo für Konto: " + tmpKonto.getKontonummer() + " wird aktualisiert --> Alt: " + tmpKonto.getAktSaldo() + " - Neu: " + neuerSaldo);
    tmpKonto.setAktSaldo(neuerSaldo);
    // Speichere Buchung
    KontoBuchung savedKontoBuchung = kontoBuchungRepository.save(neueKontoBuchung);
    log.debug("KontoBuchung erfolgreich durchgeführt. ID: " + savedKontoBuchung.getId());

    // Update ZahlungsAuftrag
//    zahlungsAuftragRepository.UpdateZahlungsAuftragById(zahlungsAuftrag.getId(), LocalDateTime.now(), ZahlungAuftragStatusEnum.DURCHGEFUEHRT.toString(), datentraeger);
    zahlungsAuftrag.setDatAend(LocalDateTime.now());
    zahlungsAuftrag.setAuftragsStatus(ZahlungAuftragStatusEnum.DURCHGEFUEHRT);
    zahlungsAuftrag.setDatentraeger(datentraeger);
    log.debug("Zahlungsauftrag erfolgreich auf Datenträger: " + datentraeger.getId() + " gespeichert.");
    return neuerSaldo;
  }

  @Override
  @Transactional
  public String processZahlungsAuftragsBatch(ZahlungsAuftrag zahlungsAuftragsSample) {
    log.debug("ZahlungsAuftragsBatch für " + zahlungsAuftragsSample.getAuftragsArt() + " wird durchgeführt.");

    List<ZahlungsAuftrag> zahlungsAuftragsList = findAllAngelegteZahlungsAuftraegeByDateAndType(zahlungsAuftragsSample.getAuftragsDatum(), zahlungsAuftragsSample.getAuftragsArt().toString());

    if (zahlungsAuftragsList.isEmpty()) {
      log.debug("Keine Daten zum verarbeiten gefunden.");
      return "Keine Daten zum verarbeiten gefunden";
    }

    log.debug("Datenträger wird erstellt.");
    Datentraeger datentraeger = new Datentraeger(0, BigDecimal.ZERO, LocalDateTime.now(), null, zahlungsAuftragsSample.getAuftragsArt());
    datenTraegerRepository.save(datentraeger);
    log.debug("Datenträger erfolgreich gespeichert. ID: " + datentraeger.getId());

    zahlungsAuftragsList.forEach(za ->  processSingleZahlungsAuftrag(za, datentraeger));

    log.debug("Alle Zahlungsaufträge erfolgreich verarbeitet.");
    int anzahlZahlungsAuftraege = zahlungsAuftragRepository.countByDatentraeger(datentraeger);
    BigDecimal summeZahlungsAuftraege = zahlungsAuftragRepository.sumByDatentraeger(datentraeger);

    datentraeger.setAnzahl(anzahlZahlungsAuftraege);
    datentraeger.setSumme(summeZahlungsAuftraege);

    return "DatenträgerId=" + datentraeger.getId() + "  Art=" + zahlungsAuftragsSample.getAuftragsArt().getDisplayName() + "  Anzahl=" + datentraeger.getAnzahl() + "Summe=" + datentraeger.getSumme();
  }

  @Override
  @Transactional(readOnly = true)
  public BigDecimal getVerfügbarerSaldoByKontoId(Long kontoId) {

    log.debug("Verfügbarer Saldo für KontoId: " + kontoId + " wird ermittelt.");
    Konto konto = kontoRepository.findById(kontoId).get();

    BigDecimal verfügbarerBetrag = konto.getAktSaldo().subtract(zahlungsAuftragRepository.getSummeOffenerAuszahlungsAuftraegeBisSysdateByKontoId(kontoId)) ;
    if (konto instanceof GiroKonto) {
      verfügbarerBetrag = verfügbarerBetrag.add(((GiroKonto) konto).getUeberziehungsRahmen());
    }

    return verfügbarerBetrag;
  }


}
