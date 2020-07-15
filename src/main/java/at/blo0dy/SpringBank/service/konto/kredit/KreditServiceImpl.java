package at.blo0dy.SpringBank.service.konto.kredit;

import at.blo0dy.SpringBank.dao.konto.kredit.KreditKontoRepository;
import at.blo0dy.SpringBank.dao.kreditZinsDAO.KreditRechnerRepository;
import at.blo0dy.SpringBank.model.konto.kredit.KreditKonto;
import at.blo0dy.SpringBank.model.produkt.kredit.KreditRechnerErgebnis;
import at.blo0dy.SpringBank.model.produkt.kredit.KreditRechnerVorlage;
import at.blo0dy.SpringBank.model.produkt.kredit.KreditUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Service
public class KreditServiceImpl implements KreditService {

  KreditRechnerRepository kreditRechnerRepository;
  KreditKontoRepository kreditKontoRepository;

  @Autowired
  public KreditServiceImpl(KreditRechnerRepository kreditRechnerRepository, KreditKontoRepository kreditKontoRepository) {
    this.kreditRechnerRepository = kreditRechnerRepository;
    this.kreditKontoRepository = kreditKontoRepository;
  }


  @Override
  public List<KreditKonto> findAll() {
    return kreditKontoRepository.findAll();
  }

  @Override
  public KreditKonto findById(Long theId) {
    Optional<KreditKonto> result = kreditKontoRepository.findById(theId);
    KreditKonto kreditKonto;
    kreditKonto = result.get();

    return kreditKonto ;
  }

  @Override
  public void save(KreditKonto kreditKonto) {
    kreditKontoRepository.save(kreditKonto);
  }

  @Override
  public void deleteById(Long theId) {
    kreditKontoRepository.deleteById(theId);

  }

  @Override
  public BigDecimal getZinssatz() {
    return KreditUtility.getZinssatz();
  }


  @Override
  public KreditRechnerErgebnis getKreditRechnerErgebnis(KreditRechnerVorlage kreditRechnerVorlage) {
    return kreditRechnerRepository.getKreditRechnerErgebnis(kreditRechnerVorlage);
  }

  @Override
  @Transactional
  public List<KreditKonto> findKreditKontenByKundennummer(String kundennummer) {
    return kreditKontoRepository.findKreditKontenByKundennummer(kundennummer);
  }

  @Override
  @Transactional
  public List<String> findKontoNummerOffenerKreditKontenByKundennummer(String kundennummer) {
    return kreditKontoRepository.findKontoNummerOffenerKreditKontenByKundennummer(kundennummer);
  }

  @Override
  @Transactional
  public KreditKonto findKreditKontoByKontonummerAndKundennummer(String kontonummer, String kundennummer) {
    return kreditKontoRepository.findKreditKontoByKontonummerAndKundennummer(kontonummer, kundennummer);
  }

}
