package at.blo0dy.SpringBank.service.konto;

import at.blo0dy.SpringBank.dao.konto.KontoRepository;
import at.blo0dy.SpringBank.model.konto.Konto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

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

  @Override
  @Transactional
  public BigDecimal getGesamtSaldoOffenerKontenByKundennummer(String kundennummer) {
    return kontoRepository.getGesamtSaldoOffenerKontenByKundennummer(kundennummer);
  }

  @Override
  @Transactional
  public String findKontonummerById(Long kontoId) {
    return kontoRepository.findKontonummerById(kontoId);
  }

  // TODO: atm ned verwendet
  @Override
  @Transactional
  public Konto findByKontonummer(Long kontonummer) {
    return kontoRepository.findByKontonummer(kontonummer);
  }

  @Override
  @Transactional
  public List<Konto> findAll(Konto konto) {
    return kontoRepository.findAll(Example.of(konto));
  }

}
