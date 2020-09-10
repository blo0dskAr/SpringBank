package at.blo0dy.SpringBank.service.konto.dauerauftrag;


import at.blo0dy.SpringBank.model.konto.dauerauftrag.DauerAuftrag;


public interface DauerAuftragService {

  void saveNewDauerAuftrag(DauerAuftrag dauerAuftrag);

  Long countAktiveDauerAuftraegeByKontonummer(String kontonummer);

  String processSingleDauerAuftrag(DauerAuftrag dauerAuftrag);

  String processDauerAuftragsBatch(DauerAuftrag dauerAuftragsSample);

  DauerAuftrag findById(Long dauerAuftragId);

  void storniereDauerAuftragById(Long dauerAuftragId);
}
