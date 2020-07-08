package at.blo0dy.SpringBank.service.konto;

import at.blo0dy.SpringBank.dao.konto.KontoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.transaction.TransactionScoped;

@Service
public class KontoServiceImpl implements KontoService {

  KontoRepository kontoRepository;

  @Autowired
  public KontoServiceImpl(KontoRepository kontoRepository) {
    this.kontoRepository = kontoRepository;
  }

  @Override
  @Transactional
  public Long countKontenGesamtByKundennummer(String kundennummer) {
    return kontoRepository.countKontenGesamtByKundennummer(kundennummer);
  }

  @Override
  @Transactional
  public Long countOffeneKontenGesamtByKundennummer(String kundennummer) {
    return kontoRepository.countOffeneKontenGesamtByKundennummer(kundennummer);
  }
}
