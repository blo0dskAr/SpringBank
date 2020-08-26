package at.blo0dy.SpringBank.service.konto;

import at.blo0dy.SpringBank.dao.konto.KontoRepository;
import at.blo0dy.SpringBank.dao.konto.dauerauftrag.DauerAuftragRepository;
import at.blo0dy.SpringBank.dao.konto.giro.GiroKontoAntragRepository;
import at.blo0dy.SpringBank.dao.konto.giro.GiroKontoRepository;
import at.blo0dy.SpringBank.dao.konto.kredit.KreditKontoAntragRepository;
import at.blo0dy.SpringBank.dao.konto.sparen.SparKontoAntragRepository;
import at.blo0dy.SpringBank.dao.konto.zahlungsAuftrag.ZahlungsAuftragRepository;
import at.blo0dy.SpringBank.model.antrag.giro.GiroKontoAntrag;
import at.blo0dy.SpringBank.model.antrag.kredit.KreditKontoAntrag;
import at.blo0dy.SpringBank.model.antrag.sparen.SparKontoAntrag;
import at.blo0dy.SpringBank.model.enums.DauerAuftragStatusEnum;
import at.blo0dy.SpringBank.model.enums.KontoStatusEnum;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragArtEnum;
import at.blo0dy.SpringBank.model.enums.ZahlungAuftragStatusEnum;
import at.blo0dy.SpringBank.model.konto.Konto;
import at.blo0dy.SpringBank.model.konto.dauerauftrag.DauerAuftrag;
import at.blo0dy.SpringBank.model.konto.giro.GiroKonto;
import at.blo0dy.SpringBank.model.konto.kredit.KreditKonto;
import at.blo0dy.SpringBank.model.konto.sparen.SparKonto;
import at.blo0dy.SpringBank.model.konto.zahlungsAuftrag.ZahlungsAuftrag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class KontoServiceImpl implements KontoService {

  KontoRepository kontoRepository;
  ZahlungsAuftragRepository zahlungsAuftragRepository;
  DauerAuftragRepository dauerAuftragRepository;
  SparKontoAntragRepository sparKontoAntragRepository;
  KreditKontoAntragRepository kreditKontoAntragRepository;
  GiroKontoAntragRepository giroKontoAntragRepository;
  GiroKontoRepository giroKontoRepository;


  @Autowired
  public KontoServiceImpl(KontoRepository kontoRepository, ZahlungsAuftragRepository zahlungsAuftragRepository, DauerAuftragRepository dauerAuftragRepository, SparKontoAntragRepository sparKontoAntragRepository,
                          KreditKontoAntragRepository kreditKontoAntragRepository, GiroKontoAntragRepository giroKontoAntragRepository, GiroKontoRepository giroKontoRepository) {
    this.kontoRepository = kontoRepository;
    this.zahlungsAuftragRepository = zahlungsAuftragRepository;
    this.dauerAuftragRepository = dauerAuftragRepository;
    this.sparKontoAntragRepository = sparKontoAntragRepository;
    this.kreditKontoAntragRepository = kreditKontoAntragRepository;
    this.giroKontoAntragRepository = giroKontoAntragRepository;
    this.giroKontoRepository = giroKontoRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public Long countKontenGesamtByKundennummer(String kundennummer) {
    log.debug("Anzahl Konten für Kundennummer: " + kundennummer + " wird ermittelt");
    return kontoRepository.countKontenGesamtByKundennummer(kundennummer);
  }

  @Override
  @Transactional(readOnly = true)
  public Long countOffeneKontenGesamtByKundennummer(String kundennummer) {
    log.debug("Anzahl offener Konten für Kundennummer: " + kundennummer + " wird ermittelt.");
    return kontoRepository.countOffeneKontenGesamtByKundennummer(kundennummer);
  }

  @Override
  @Transactional(readOnly = true)
  public BigDecimal getGesamtSaldoOffenerKontenByKundennummer(String kundennummer) {
    log.debug("GesamtSaldo offener Konten für Kundennummer: " + kundennummer + " wird ermittelt.");
    return kontoRepository.getGesamtSaldoOffenerKontenByKundennummer(kundennummer);
  }

  @Override
  @Transactional(readOnly = true)
  public String findKontonummerById(Long kontoId) {
    log.debug("Kontonummer für KontoId: " + kontoId + " wird gesucht.");
    return kontoRepository.findKontonummerById(kontoId);
  }

/*
  @Override
  @Transactional(readOnly = true)
  public Konto findByKontonummer(String kontonummer) {
    return kontoRepository.findByKontonummer(kontonummer);
  }*/

  @Override
  @Transactional(readOnly = true)
  public List<Konto> findAll(Konto konto) {
   log.debug("Kontosuche wird durchgeführt.");
    return kontoRepository.findAll(Example.of(konto, ExampleMatcher.matching().withMatcher("kontonummer", match -> match.contains())));
  }

  @Override
  @Transactional(readOnly = true)
  public Konto findById(Long kontoId) {
    log.debug("Konto mit KontoId: " + kontoId + " wird gesucht.");
    Optional<Konto> tmpKonto = kontoRepository.findById(kontoId);
    Konto konto = tmpKonto.get();

    log.debug("Konto mit KontoId: " + kontoId + " wurde gefunden.");
    return konto ;
  }

  @Override
  @Transactional
  public void UpdateKontoSaldoById(Long kontoId, BigDecimal neuerSaldo) {

    log.debug("Saldo für KontoId: " + kontoId + " wird aktualisiert.");

    Konto konto = kontoRepository.findById(kontoId).get();
    konto.setAktSaldo(neuerSaldo);
//    kontoRepository.updateKontoSaldoById(kontoId, neuerSaldo);
    log.debug("Saldoaktualisierung erfolgreich.");

  }

  @Override
  @Transactional
  public String processKontoStatusById(Long kontoId, KontoStatusEnum neuerKontoStatus, KontoStatusEnum bestMoeglicherStatus) {
    log.debug("KontoStatus für KontoId: " + kontoId + "wird geprüft.");

    Optional<Konto> tmpKonto = kontoRepository.findById(kontoId);
    Konto konto = tmpKonto.get();
    KontoStatusEnum alterKontoStatus = konto.getKontoStatus();

    // Keine Änderungen
    if (alterKontoStatus.equals(neuerKontoStatus)) {
      log.debug("KontoStatus für KontoId: " + konto + " nicht geändert.");
      return "NO_CHANGES";
    }

    // Änderung nicht möglich
    if ((alterKontoStatus.equals(KontoStatusEnum.GESCHLOSSEN))
            // TODO:  werd ich auch nicht brauchen  glaub ich =)
            || ((neuerKontoStatus.equals(KontoStatusEnum.IN_EROEFFNUNG)))) {
      log.debug("KontoStatusÄnderung für KontoId: " + konto + " nicht möglich.");
      return "TRANSITION_NOT_POSSIBLE";
    }

    // Wird geschlossen/storniert
    if (neuerKontoStatus.equals(KontoStatusEnum.GESCHLOSSEN)) {
      // Konto ist noch offen
      if (alterKontoStatus.equals(KontoStatusEnum.OFFEN)) {
        // Konto muss Saldo == 0 haben. Kann nicht geschlossen werden
        if (konto.getAktSaldo().compareTo(BigDecimal.ZERO) != 0) {
          log.debug("KontoStatusÄnderung für KontoId: " + konto + " wegen vorhandenen Saldos nicht möglich.");
          return "SALDO_NOT_ZERO";
          // Konto kann geschlossen werden
        } else {
          log.debug("Kontoschließung für KontoId: " + kontoId + " wird durchgeführt.");
          List<DauerAuftrag> dauerAuftragList = dauerAuftragRepository.findAktiveDauerAuftraegeByKontoId(konto.getId());
          List<ZahlungsAuftrag> zahlungsAuftragList = zahlungsAuftragRepository.findAktiveZahlungsAuftraegeByKontoId(konto.getId());

          log.debug("Anzahl zu stornierender Daueraufträge: " + dauerAuftragList.size());
          log.debug("Anzahl zu stornierender Zahlungsaufträge: " + zahlungsAuftragList.size());
          // ggf. Daueraufträge löschen
          if (!dauerAuftragList.isEmpty()) {
            dauerAuftragList.forEach(dauerAuftrag -> { dauerAuftrag.setAuftragsStatus(DauerAuftragStatusEnum.STORNIERT);
                                                        dauerAuftrag.setDatAend(LocalDateTime.now());
//                                                       dauerAuftragRepository.save(dauerAuftrag);
            });
          }

          // ggf. Zahlungsauftraege stornieren
          if (!zahlungsAuftragList.isEmpty()) {
            zahlungsAuftragList.forEach(zahlungsAuftrag -> {zahlungsAuftrag.setAuftragsStatus(ZahlungAuftragStatusEnum.STORNIERT);
                                                            zahlungsAuftrag.setDatAend(LocalDateTime.now());
//                                                            zahlungsAuftragRepository.save(zahlungsAuftrag);
            });
          }

          // Kontostatus aktualisieren (geschlossen setzen)
//          kontoRepository.updateKontoStatusByIdAndStatus(konto.getId(), neuerKontoStatus.toString());
          konto.setKontoStatus(neuerKontoStatus);

          log.debug("Konto mit KontoId: " + kontoId + " erfolgreich geschlossen.");
          return "KONTO_NOW_CLOSED";
        }
        // Konto Ist noch In Eröffnung --> wird storniert
      } else if (alterKontoStatus.equals(KontoStatusEnum.IN_EROEFFNUNG)) {
//        kontoRepository.updateKontoStatusByIdAndStatus(konto.getId(), neuerKontoStatus.toString());
        konto.setKontoStatus(neuerKontoStatus);
        log.debug("Konto mit KontoId: " + kontoId + " erfolgreich storniert.");
        return "KONTO_NOW_CLOSED";
      }
      // Dann sollt nur noch "In_eroeffnung --> Offen" übrig bleiben
    } else if (neuerKontoStatus.equals(KontoStatusEnum.OFFEN)) {
      // TODO, Alle Zahlungsaufträge, DauerAufträge, ÜberziehungsRahmen erstellen.
      log.debug("Konto mit Id: " +  kontoId + " wird eröffnet.");

      // Erstaufträge und Daueraufträge von Sparkonten anlegen
      if (konto instanceof SparKonto) {
        SparKontoAntrag tmpKontoAntrag = sparKontoAntragRepository.findByKontoId(kontoId);
        if (tmpKontoAntrag.getDauerAuftrag().compareTo(BigDecimal.ZERO) != 0) {
          log.debug("DauerAuftrag vorhanden, wird angelegt.");
          DauerAuftrag tmpDauerAuftrag =  new DauerAuftrag();
          tmpDauerAuftrag.setAuftragsStatus(DauerAuftragStatusEnum.ANGELEGT);
          tmpDauerAuftrag.setDatAnlage(LocalDateTime.now());
          tmpDauerAuftrag.setId(0L);
          tmpDauerAuftrag.setKonto(konto);
          tmpDauerAuftrag.setBetrag(tmpKontoAntrag.getDauerAuftrag());
          tmpDauerAuftrag.setKontonummer(konto.getKontonummer().toString());
          tmpDauerAuftrag.setText("Noch leer");
          // TODO: das muss noch in die registration form rein (und vermutlich weitere)
          tmpDauerAuftrag.setTagImMonat(1);
          tmpDauerAuftrag.setAuftragsArt(ZahlungAuftragArtEnum.EINZAHLUNG);
          dauerAuftragRepository.save(tmpDauerAuftrag);
        }

        if (tmpKontoAntrag.getErstAuftrag().compareTo(BigDecimal.ZERO) != 0) {
          log.debug("ErstAuftrag vorhanden, wird angelegt");
          ZahlungsAuftrag tmpZahlungsAuftrag = new ZahlungsAuftrag();
          tmpZahlungsAuftrag.setAuftragsStatus(ZahlungAuftragStatusEnum.ANGELEGT);
          tmpZahlungsAuftrag.setDatAnlage(LocalDateTime.now());
          tmpZahlungsAuftrag.setId(0L);
          tmpZahlungsAuftrag.setKonto(konto);
          tmpZahlungsAuftrag.setBetrag(tmpKontoAntrag.getErstAuftrag());
          tmpZahlungsAuftrag.setKontonummer(konto.getKontonummer().toString());
          tmpZahlungsAuftrag.setAuftragsArt(ZahlungAuftragArtEnum.EINZAHLUNG);
          tmpZahlungsAuftrag.setSenderKonto(konto.getKunde().getConnectedGiro());
          tmpZahlungsAuftrag.setEmpfaengerKonto(konto.getKontonummer().toString());
          // TODO in den zahlungsauftrag muss auch noch mal text rein und auch ein gscheites auftragsdatum
          tmpZahlungsAuftrag.setAuftragsDatum(LocalDate.now());
//          tmpZahlungsAuftrag.setText("ErstAuftrag");
          zahlungsAuftragRepository.save(tmpZahlungsAuftrag);
        }
      }

      // KreditBetrag als Auszahlung anlegen und Rate als Einzahlung anlegen
      if (konto instanceof KreditKonto) {
        KreditKontoAntrag tmpKontoAntrag = kreditKontoAntragRepository.findByKontoId(kontoId);

        log.debug("Kreditauszahlung wird angelegt.");
        ZahlungsAuftrag tmpZahlungsAuftrag = new ZahlungsAuftrag();
        tmpZahlungsAuftrag.setAuftragsStatus(ZahlungAuftragStatusEnum.ANGELEGT);
        tmpZahlungsAuftrag.setDatAnlage(LocalDateTime.now());
        tmpZahlungsAuftrag.setId(0L);
        tmpZahlungsAuftrag.setBetrag(tmpKontoAntrag.getKreditBetrag());
        tmpZahlungsAuftrag.setKonto(konto);
        tmpZahlungsAuftrag.setKontonummer(konto.getKontonummer().toString());
        tmpZahlungsAuftrag.setAuftragsArt(ZahlungAuftragArtEnum.AUSZAHLUNG);
        tmpZahlungsAuftrag.setEmpfaengerKonto(konto.getKunde().getConnectedGiro());
        tmpZahlungsAuftrag.setSenderKonto(konto.getKontonummer().toString());
        tmpZahlungsAuftrag.setAuftragsDatum(LocalDate.now());
        zahlungsAuftragRepository.save(tmpZahlungsAuftrag);

        log.debug("KreditRate wird angelegt.");
        DauerAuftrag tmpDauerAuftrag =  new DauerAuftrag();
        tmpDauerAuftrag.setAuftragsStatus(DauerAuftragStatusEnum.ANGELEGT);
        tmpDauerAuftrag.setDatAnlage(LocalDateTime.now());
        tmpDauerAuftrag.setId(0L);
        tmpDauerAuftrag.setKonto(konto);
        tmpDauerAuftrag.setBetrag(tmpKontoAntrag.getRate());
        tmpDauerAuftrag.setKontonummer(konto.getKontonummer().toString());
        tmpDauerAuftrag.setText("Rate");
        // TODO: das muss noch in die registration form rein (und vermutlich weitere)
        tmpDauerAuftrag.setTagImMonat(31);
        tmpDauerAuftrag.setAuftragsArt(ZahlungAuftragArtEnum.EINZAHLUNG);
        dauerAuftragRepository.save(tmpDauerAuftrag);

      }
      // GirokontoRahmen am Konto einrichten
      if (konto instanceof GiroKonto) {
        GiroKontoAntrag tmpKontoAntrag = giroKontoAntragRepository.findByKontoId(kontoId);
        if (tmpKontoAntrag.isUeberziehungsrahmenGewuenscht()) {
          log.debug("überziehungsrahmen wird eingerichtet.");
          ((GiroKonto) konto).setUeberziehungsRahmen(BigDecimal.valueOf(500));
//          giroKontoRepository.UpdateUeberziehungsRahmenByKontoId(kontoId, BigDecimal.valueOf(500));
        }
      }

      // Kontostatus aktualisieren (offen setzen)
//      kontoRepository.updateKontoStatusByIdAndStatus(konto.getId(), neuerKontoStatus.toString());
      konto.setKontoStatus(neuerKontoStatus);
      log.debug("Konto mit KontoId: " + kontoId + " wurde erfolgreich eröffnet");
      return "KONTO_NOW_OPEN";
    }
    // ?? fehlt wohl theoretisch.. bestimmt nur theoretisch :)
    log.error("Das sollte nicht vorkommen :) aber meine If-Abfrage da ist eher mäßig");
    return "TRANSITION_NOT_POSSIBLE";
  }

}
