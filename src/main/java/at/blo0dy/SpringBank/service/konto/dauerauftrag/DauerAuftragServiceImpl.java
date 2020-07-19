package at.blo0dy.SpringBank.service.konto.dauerauftrag;

import at.blo0dy.SpringBank.dao.konto.dauerauftrag.DauerAuftragRepository;
import at.blo0dy.SpringBank.dao.konto.zahlungsAuftrag.ZahlungsAuftragRepository;
import at.blo0dy.SpringBank.model.enums.BuchungsArtEnum;
import at.blo0dy.SpringBank.model.enums.DauerAuftragStatusEnum;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragArtEnum;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragStatusEnum;
import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.konto.dauerauftrag.DauerAuftrag;
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
public class DauerAuftragServiceImpl implements DauerAuftragService {

  DauerAuftragRepository dauerAuftragRepository;
  ZahlungsAuftragRepository zahlungsAuftragRepository;


  @Autowired
  public DauerAuftragServiceImpl(DauerAuftragRepository dauerAuftragRepository, ZahlungsAuftragRepository zahlungsAuftragRepository) {
    this.dauerAuftragRepository = dauerAuftragRepository;
    this.zahlungsAuftragRepository = zahlungsAuftragRepository;
  }

  @Override
  @Transactional
  public void saveNewDauerAuftrag(DauerAuftrag dauerAuftrag) {

    dauerAuftrag.setDatAnlage(LocalDateTime.now());
    dauerAuftrag.setAuftragsStatus(DauerAuftragStatusEnum.ANGELEGT);

    dauerAuftragRepository.save(dauerAuftrag);
  }

  @Override
  @Transactional
  public Long countAktiveDauerAuftraegeByKontonummer(Long kontonummer) {
    return dauerAuftragRepository.countAktiveDauerAuftraegeByKontonummer(kontonummer);
  }

  @Override
  @Transactional
  public List<DauerAuftrag> findAllAngelegteDauerAuftraegeByDateAndType(int tagImMonat, String type) {

    return dauerAuftragRepository.findAllAngelegteDauerAuftraegeByDateAndType(tagImMonat, type) ;
  }


  @Override
  @Transactional
  public String processSingleDauerAuftrag(DauerAuftrag dauerAuftrag) {

    Konto tmpKonto = dauerAuftrag.getKonto();

    ZahlungsAuftrag neuerZahlungsAuftrag = new ZahlungsAuftrag(LocalDate.now(), LocalDateTime.now(), null, dauerAuftrag.getBetrag(), tmpKonto, dauerAuftrag.getKontonummer(), ZahlungAuftragStatusEnum.ANGELEGT, dauerAuftrag.getAuftragsArt(),
                                                                tmpKonto.getKunde().getConnectedGiro(), dauerAuftrag.getKontonummer()) ;

    zahlungsAuftragRepository.save(neuerZahlungsAuftrag);

    return "muh";
  }


  @Override
  @Transactional
  public String processDauerAuftragsBatch(DauerAuftrag dauerAuftragsSample) {

    List<DauerAuftrag> dauerAuftragList = dauerAuftragRepository.findAllAngelegteDauerAuftraegeByDateAndType(dauerAuftragsSample.getTagImMonat(),dauerAuftragsSample.getAuftragsArt().toString());

    dauerAuftragList.forEach(za ->  processSingleDauerAuftrag(za)) ;

    return "";
  }


}
