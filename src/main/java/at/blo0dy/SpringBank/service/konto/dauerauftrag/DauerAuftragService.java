package at.blo0dy.SpringBank.service.konto.dauerauftrag;


import at.blo0dy.SpringBank.model.konto.dauerauftrag.DauerAuftrag;

import java.util.List;


public interface DauerAuftragService {

  void saveNewDauerAuftrag(DauerAuftrag dauerAuftrag);

  Long countAktiveDauerAuftraegeByKontonummer(String kontonummer);

//  List<DauerAuftrag> findAllAngelegteDauerAuftraegeByDateAndType(int tagImMonat, String type);

  String processSingleDauerAuftrag(DauerAuftrag dauerAuftrag);

  String processDauerAuftragsBatch(DauerAuftrag dauerAuftragsSample);

  DauerAuftrag findById(Long dauerAuftragId);

  void storniereDauerAuftragById(Long dauerAuftragId);
}
