package at.blo0dy.SpringBank.service.konto.zahlungsAuftrag;

import at.blo0dy.SpringBank.dao.konto.zahlungsAuftrag.ZahlungsAuftragRepository;
import at.blo0dy.SpringBank.model.konto.zahlungsAuftrag.ZahlungsAuftrag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ZahlungsAuftragServiceImpl implements ZahlungsAuftragService{

  ZahlungsAuftragRepository zahlungsAuftragRepository;

  @Autowired
  public ZahlungsAuftragServiceImpl(ZahlungsAuftragRepository zahlungsAuftragRepository) {
    this.zahlungsAuftragRepository = zahlungsAuftragRepository;
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

}
