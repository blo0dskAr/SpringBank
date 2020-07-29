package at.blo0dy.SpringBank.service.konto.dauerauftrag;

import at.blo0dy.SpringBank.dao.konto.dauerauftrag.DauerAuftragRepository;
import at.blo0dy.SpringBank.dao.konto.zahlungsAuftrag.ZahlungsAuftragRepository;
import at.blo0dy.SpringBank.model.enums.DauerAuftragStatusEnum;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragStatusEnum;
import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.konto.dauerauftrag.DauerAuftrag;
import at.blo0dy.SpringBank.model.konto.zahlungsAuftrag.ZahlungsAuftrag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class DauerAuftragServiceImpl implements DauerAuftragService {

  DauerAuftragRepository dauerAuftragRepository;
  ZahlungsAuftragRepository zahlungsAuftragRepository;


  @Autowired
  public DauerAuftragServiceImpl(DauerAuftragRepository dauerAuftragRepository, ZahlungsAuftragRepository zahlungsAuftragRepository) {
    this.dauerAuftragRepository = dauerAuftragRepository;
    this.zahlungsAuftragRepository = zahlungsAuftragRepository;
  }

  @Override
//  @Transactional
  public void saveNewDauerAuftrag(DauerAuftrag dauerAuftrag) {
    log.debug("Neuer Dauerauftrag wird angelegt. (" + dauerAuftrag + ")");

    dauerAuftrag.setDatAnlage(LocalDateTime.now());
    dauerAuftrag.setAuftragsStatus(DauerAuftragStatusEnum.ANGELEGT);

    DauerAuftrag savedDauerAuftrag = dauerAuftragRepository.save(dauerAuftrag);
    log.debug("Dauerauftrag mit ID: " + savedDauerAuftrag.getId() + " + erfolgreich gespeichert. (" + savedDauerAuftrag + ")");
  }

  @Override
  @Transactional(readOnly = true)
  public Long countAktiveDauerAuftraegeByKontonummer(Long kontonummer) {
    log.debug("Anzahl der angelegter DauerAufträge für Kontonummer: " + kontonummer + " werden ermittelt.");

    Long anzahlAngelegterDauerAuftraege = dauerAuftragRepository.countAktiveDauerAuftraegeByKontonummer(kontonummer);
    log.debug("Anzahl der angelegter DauerAufträge für Kontonummer: " + kontonummer + " wurden ermittelt: " + anzahlAngelegterDauerAuftraege);

    return anzahlAngelegterDauerAuftraege;
  }

  @Override
  @Transactional(readOnly = true)
  public List<DauerAuftrag> findAllAngelegteDauerAuftraegeByDateAndType(int tagImMonat, String type) {
    log.debug("Alle Angelegten Dauerauftraege mit Tag: " + tagImMonat + " und Typ: " + type + " werden ermittelt");

    List<DauerAuftrag> dauerAuftragsList = dauerAuftragRepository.findAllAngelegteDauerAuftraegeByDateAndType(tagImMonat, type);
    log.debug("Alle Angelegten Dauerauftraege mit Tag: " + tagImMonat + " und Typ: " + type + " wurden ermittelt, Anzahl: " + dauerAuftragsList.size());

    return dauerAuftragsList ;
  }


  @Override
//  @Transactional
  public String processSingleDauerAuftrag(DauerAuftrag dauerAuftrag) {

    Long tmpDauerAuftragId = dauerAuftrag.getId();

    log.debug("Dauerauftrag mit ID: " + tmpDauerAuftragId + " und Tag: " + dauerAuftrag.getTagImMonat() + " wird zu Zahlungsauftrag verarbeitet"  );

    Konto tmpKonto = dauerAuftrag.getKonto();
    ZahlungsAuftrag neuerZahlungsAuftrag = new ZahlungsAuftrag(LocalDate.now(), LocalDateTime.now(), null, dauerAuftrag.getBetrag(), tmpKonto, dauerAuftrag.getKontonummer(), ZahlungAuftragStatusEnum.ANGELEGT, dauerAuftrag.getAuftragsArt(),
                                                                tmpKonto.getKunde().getConnectedGiro(), dauerAuftrag.getKontonummer()) ;

    ZahlungsAuftrag savedZahlungsAuftrag = zahlungsAuftragRepository.save(neuerZahlungsAuftrag);
    log.debug("Dauerauftrag mit ID: " + tmpDauerAuftragId + " erfolgreich zu Zahlungsauftrag mit ID: " + savedZahlungsAuftrag.getId() + " verarbeitet." );

    return "success";
  }


  @Override
  @Transactional
  public String processDauerAuftragsBatch(DauerAuftrag dauerAuftragsSample) {

    int tmpTagImMonat = dauerAuftragsSample.getTagImMonat();

    log.debug("DauerAuftragsBatch für Typ: " + dauerAuftragsSample.getAuftragsArt().getDisplayName() + " und Tag: " + tmpTagImMonat + " wird durchgeführt.");
    log.debug("--------------------- Running ---------------------");
    List<DauerAuftrag> dauerAuftragList = dauerAuftragRepository.findAllAngelegteDauerAuftraegeByDateAndType(tmpTagImMonat,dauerAuftragsSample.getAuftragsArt().toString());
    log.debug(dauerAuftragList.size() + " Stück werden verarbeitet");

    dauerAuftragList.forEach(za ->  processSingleDauerAuftrag(za)) ;

    log.debug("--------------------- End ---------------------");
    log.debug("DauerAuftragsBatch erfolgreich durchgeführt.");
    return "";
  }

  @Override
  @Transactional(readOnly = true)
  public DauerAuftrag findById(Long dauerAuftragId) {
    log.debug("Dauerauftrag mit ID: " + dauerAuftragId + " wird gesucht.");
    DauerAuftrag dauerAuftrag = dauerAuftragRepository.findById(dauerAuftragId).get();
    if (dauerAuftrag == null){
      log.error("Dauerauftrag mit ID: " + dauerAuftragId + " nicht gefunden.");
      // TODO: ExceptionHandling wenn null
//      throw new NotFoundException("Dauerauftrag mit ID: " + dauerAuftragId + " nicht gefunden.");
    }
    log.debug("Dauerauftrag mit ID: " + dauerAuftragId + " gefunden und wird retourniert.");
    return dauerAuftrag ;
  }

  @Override
  @Transactional
  public void storniereDauerAuftragById(Long dauerAuftragId) {

    log.debug("Dauerauftrag mit ID: " + dauerAuftragId + " wird storniert.");

    DauerAuftrag updatedDauerAuftrag = dauerAuftragRepository.findById(dauerAuftragId).get();
    if (updatedDauerAuftrag == null){
      log.error("Dauerauftrag mit ID: " + dauerAuftragId + " nicht gefunden.");
      // TODO: ExceptionHandling wenn null
//      throw new NotFoundException("Dauerauftrag mit ID: " + dauerAuftragId + " nicht gefunden.");
    }
    updatedDauerAuftrag.setAuftragsStatus(DauerAuftragStatusEnum.STORNIERT);
    updatedDauerAuftrag.setDatAend(LocalDateTime.now());
//    dauerAuftragRepository.storniereDauerAuftragById(dauerAuftragId);

    log.debug("Dauerauftrag mit ID: " + dauerAuftragId + " erfolgreich storniert.");
  }
}
