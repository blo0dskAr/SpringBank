package at.blo0dy.SpringBank.service.konto.zahlungsAuftrag;

import at.blo0dy.SpringBank.dao.DatenTraegerRepository;
import at.blo0dy.SpringBank.dao.konto.KontoRepository;
import at.blo0dy.SpringBank.dao.konto.kontoBuchung.KontoBuchungRepository;
import at.blo0dy.SpringBank.dao.konto.zahlungsAuftrag.ZahlungsAuftragRepository;
import at.blo0dy.SpringBank.model.enums.BuchungsArtEnum;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragArtEnum;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragStatusEnum;
import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.konto.giro.GiroKonto;
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
//  @Transactional
  public ZahlungsAuftrag save(ZahlungsAuftrag zahlungsAuftrag) {
    return zahlungsAuftragRepository.save(zahlungsAuftrag);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ZahlungsAuftrag> findZahlungsAuftraegeByKontonummer(String kontonummer) {
    return zahlungsAuftragRepository.findZahlungsAuftraegeByKontonummer(kontonummer);
  }

  @Override
  @Transactional(readOnly = true)
  public Long countOffeneZahlungsAuftraegeByKontoId(Long kontoId) {
    return zahlungsAuftragRepository.countOffeneZahlungsAuftraegeByKontoId(kontoId);
  }

//  @Override
//  @Transactional(readOnly = true)
//  public BigDecimal getSummeOffenerAuszahlungenByKontoId(Long kontoId) {
//    return zahlungsAuftragRepository.getSummeOffenerAuszahlungsAuftraegeByKontoId(kontoId);
//  }

  @Override
  @Transactional(readOnly = true)
  public boolean checkAuszahlungWithVerfuegbarerBetrag(Konto konto, BigDecimal auszahlungsBetrag) {

    BigDecimal verfuegbarerBetrag = getVerfügbarerSaldoByKontoId(konto.getId());

    if (verfuegbarerBetrag.compareTo(auszahlungsBetrag) == -1) {
      return false;
    } else {
      return true;
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<ZahlungsAuftrag> findAllAngelegteZahlungsAuftraegeByDateAndType(LocalDate auftragsdatum, String type) {

    return zahlungsAuftragRepository.findAllAngelegteZahlungsAuftraegeByDateAndType(auftragsdatum, type);
  }

  @Override
  @Transactional
  public BigDecimal processSingleZahlungsAuftrag(ZahlungsAuftrag zahlungsAuftrag, Datentraeger datentraeger) {

    // Hier das Konto aus der DB neu laden, weil sonst aus dem zahlungsauftrag.getkonto() der alte saldo übermittelt wird bei > 1 buchung.
    Konto tmpKonto = kontoRepository.findByKontonummer(Long.valueOf(zahlungsAuftrag.getKontonummer()));
    BigDecimal neuerSaldo;
    KontoBuchung neueKontoBuchung;
    BuchungsArtEnum buchungsArt;
    String buchungsText;

    // Check Art der Auszahlung
    if (zahlungsAuftrag.getAuftragsArt().equals(ZahlungAuftragArtEnum.AUSZAHLUNG)) {
      buchungsText = "Auszahlung";
      // Check Ob Saldo Verfügbar
      if (checkAuszahlungWithVerfuegbarerBetrag(tmpKonto, zahlungsAuftrag.getBetrag())) {
        // true = erstelle KontoBuchung, update zahlungsAuftrag, (sammle in file, sammle in DB)
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

    neueKontoBuchung =  new KontoBuchung(0L, LocalDate.now(),LocalDateTime.now(),null,zahlungsAuftrag.getBetrag(), neuerSaldo,buchungsText,tmpKonto, buchungsArt );

    // Update KontoSaldo
    // TODO: jetzt hab ichs erst checkt was das Transactional genau macht. Da ein update machen ist bescheuert, durchs Transactional wird der neue Saldo in der DB schon nur beim  setten persistiert....
//    kontoRepository.updateKontoSaldoById(tmpKonto.getId(),neuerSaldo);
    tmpKonto.setAktSaldo(neuerSaldo);
    // Speichere Buchung
    kontoBuchungRepository.save(neueKontoBuchung);

    // Update ZahlungsAuftrag
//    zahlungsAuftragRepository.UpdateZahlungsAuftragById(zahlungsAuftrag.getId(), LocalDateTime.now(), ZahlungAuftragStatusEnum.DURCHGEFUEHRT.toString(), datentraeger);
    zahlungsAuftrag.setDatAend(LocalDateTime.now());
    zahlungsAuftrag.setAuftragsStatus(ZahlungAuftragStatusEnum.DURCHGEFUEHRT);
    zahlungsAuftrag.setDatentraeger(datentraeger);
    return neuerSaldo;
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

    zahlungsAuftragsList.forEach(za ->  processSingleZahlungsAuftrag(za, datentraeger));

    int anzahlZahlungsAuftraege = zahlungsAuftragRepository.countByDatentraeger(datentraeger);
    BigDecimal summeZahlungsAuftraege = zahlungsAuftragRepository.sumByDatentraeger(datentraeger);

    datentraeger.setAnzahl(anzahlZahlungsAuftraege);
    datentraeger.setSumme(summeZahlungsAuftraege);

    return "DatenträgerId=" + datentraeger.getId() + "  Art=" + zahlungsAuftragsSample.getAuftragsArt().getDisplayName() + "  Anzahl=" + datentraeger.getAnzahl() + "Summe=" + datentraeger.getSumme();
  }

  @Override
  @Transactional(readOnly = true)
  public BigDecimal getVerfügbarerSaldoByKontoId(Long kontoId) {

    Konto konto = kontoRepository.findById(kontoId).get();

    BigDecimal verfügbarerBetrag = konto.getAktSaldo().subtract(zahlungsAuftragRepository.getSummeOffenerAuszahlungsAuftraegeByKontoId(kontoId)) ;
    if (konto instanceof GiroKonto) {
      verfügbarerBetrag = verfügbarerBetrag.add(((GiroKonto) konto).getUeberziehungsRahmen());
    }

    return verfügbarerBetrag;
  }


}
