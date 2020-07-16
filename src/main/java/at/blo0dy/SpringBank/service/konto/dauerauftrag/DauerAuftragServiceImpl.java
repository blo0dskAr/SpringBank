package at.blo0dy.SpringBank.service.konto.dauerauftrag;

import at.blo0dy.SpringBank.dao.konto.dauerauftrag.DauerAuftragRepository;
import at.blo0dy.SpringBank.model.enums.DauerAuftragStatusEnum;
import at.blo0dy.SpringBank.model.konto.dauerauftrag.DauerAuftrag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class DauerAuftragServiceImpl implements DauerAuftragService {

  DauerAuftragRepository dauerAuftragRepository;

  @Autowired
  public DauerAuftragServiceImpl(DauerAuftragRepository dauerAuftragRepository) {
    this.dauerAuftragRepository = dauerAuftragRepository;
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
}
