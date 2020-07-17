package at.blo0dy.SpringBank.service.konto;

import at.blo0dy.SpringBank.dao.KundeRepository;
import at.blo0dy.SpringBank.dao.konto.KontoRepository;
import at.blo0dy.SpringBank.model.enums.KontoStatusEnum;
import at.blo0dy.SpringBank.model.konto.Konto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class KontoServiceImpl implements KontoService {

  KontoRepository kontoRepository;
  KundeRepository kundeRepository;

  @Autowired
  public KontoServiceImpl(KontoRepository kontoRepository, KundeRepository kundeRepository) {
    this.kontoRepository = kontoRepository;
    this.kundeRepository = kundeRepository;
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

  @Override
  @Transactional
  public Konto findById(Long kontoId) {
    Optional<Konto> tmpKonto = kontoRepository.findById(kontoId);
    Konto konto = tmpKonto.get();

    return konto ;
  }

  @Override
  @Transactional
  public void UpdateKontoSaldoById(Long kontoId, BigDecimal neuerSaldo) {
    kontoRepository.UpdateKontoSaldoById(kontoId, neuerSaldo);

  }

  @Override
  @Transactional
  public String processKontoStatusById(Long kontoId, KontoStatusEnum neuerKontoStatus, KontoStatusEnum bestMoeglicherStatus) {

    Optional<Konto> tmpKonto = kontoRepository.findById(kontoId);
    Konto konto = tmpKonto.get();
    KontoStatusEnum alterKontoStatus = konto.getKontoStatus();

    // Keine Änderungen
    if (alterKontoStatus.equals(neuerKontoStatus)) {
      return "NO_CHANGES";
    }

    // Änderung nicht möglich
    if ((alterKontoStatus.equals(KontoStatusEnum.GESCHLOSSEN))
            || ((alterKontoStatus.equals(KontoStatusEnum.IN_EROEFFNUNG)) && bestMoeglicherStatus.equals(KontoStatusEnum.IN_EROEFFNUNG))
            || ((neuerKontoStatus.equals(KontoStatusEnum.IN_EROEFFNUNG)))) {
      return "TRANSITION_NOT_POSSIBLE";
    }


    // Wird geschlossen/storniert
    if (neuerKontoStatus.equals(KontoStatusEnum.GESCHLOSSEN)) {
      // Konto ist noch offen
      if (alterKontoStatus.equals(KontoStatusEnum.OFFEN)) {
        // Konto muss Saldo == 0 haben. Kann nicht geschlossen werden
        if (konto.getAktSaldo().compareTo(BigDecimal.ZERO) != 0) {
          return "SALDO_NOT_ZERO";
          // Konto kann geschlossen werden
        } else {
          // TODO: Alle Zahlunsaufträge stornieren, alle DauerAufträge stornieren.
          kontoRepository.updateKontoStatusByIdAndStatus(konto.getId(), neuerKontoStatus.toString());
          return "KONTO_NOW_CLOSED";
        }
        // Konto Ist noch In Eröffnung --> wird storniert
      } else if (alterKontoStatus.equals(KontoStatusEnum.IN_EROEFFNUNG)) {
        kontoRepository.updateKontoStatusByIdAndStatus(konto.getId(), neuerKontoStatus.toString());
        return "KONTO_NOW_CLOSED";
      }
      // Dann sollt nur noch In_eroeffnung --> Offen übrig bleiben: (daher prüf ich sonst nix)
    } else if (neuerKontoStatus.equals(KontoStatusEnum.OFFEN)) {
      // TODO, Alle Zahlungsaufträge  erstellen.
      kontoRepository.updateKontoStatusByIdAndStatus(konto.getId(), neuerKontoStatus.toString());
      return "KONTO_NOW_OPEN";
    }
    // ?? welche fehlt ?
    return "TRANSITION_NOT_POSSIBLE";
  }


}
