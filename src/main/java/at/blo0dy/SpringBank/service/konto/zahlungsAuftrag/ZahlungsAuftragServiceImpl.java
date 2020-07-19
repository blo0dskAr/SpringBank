package at.blo0dy.SpringBank.service.konto.zahlungsAuftrag;

import at.blo0dy.SpringBank.dao.konto.KontoRepository;
import at.blo0dy.SpringBank.dao.konto.kontoBuchung.KontoBuchungRepository;
import at.blo0dy.SpringBank.dao.konto.zahlungsAuftrag.ZahlungsAuftragRepository;
import at.blo0dy.SpringBank.model.enums.BuchungsArtEnum;
import at.blo0dy.SpringBank.model.enums.KontoStatusEnum;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragArtEnum;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragStatusEnum;
import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.konto.kontoBuchung.KontoBuchung;
import at.blo0dy.SpringBank.model.konto.zahlungsAuftrag.ZahlungsAuftrag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ZahlungsAuftragServiceImpl implements ZahlungsAuftragService{

  ZahlungsAuftragRepository zahlungsAuftragRepository;
  KontoRepository kontoRepository;
  KontoBuchungRepository kontoBuchungRepository;

  @Autowired
  public ZahlungsAuftragServiceImpl(ZahlungsAuftragRepository zahlungsAuftragRepository, KontoRepository kontoRepository, KontoBuchungRepository kontoBuchungRepository) {
    this.zahlungsAuftragRepository = zahlungsAuftragRepository;
    this.kontoRepository = kontoRepository;
    this.kontoBuchungRepository = kontoBuchungRepository;
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
  public String processSingleZahlungsAuftrag(ZahlungsAuftrag zahlungsAuftrag) {

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
    zahlungsAuftragRepository.UpdateZahlungsAuftragById(zahlungsAuftrag.getId(), LocalDateTime.now(), ZahlungAuftragStatusEnum.DURCHGEFUEHRT.toString());
    return "DURCHGEFUEHRT";
  }

  @Override
  @Transactional
  public String processZahlungsAuftragsBatch(ZahlungsAuftrag zahlungsAuftragsSample) {

    List<ZahlungsAuftrag> zahlungsAuftragsList = findAllAngelegteZahlungsAuftraegeByDateAndType(zahlungsAuftragsSample.getAuftragsDatum(),zahlungsAuftragsSample.getAuftragsArt().toString());
    int storniertCounter;
    int durchgefuehrtCounter;

    System.out.println("ZAHLUNGSAUFTRAGSLISTE VORHER: " + zahlungsAuftragsList);
    zahlungsAuftragsList.forEach(za ->  processSingleZahlungsAuftrag(za) );
    System.out.println("ZAHLUNGSAUFTRAGSLISTE NACHHER: " + zahlungsAuftragsList);

    return "myFakeCounter";
  }


}
