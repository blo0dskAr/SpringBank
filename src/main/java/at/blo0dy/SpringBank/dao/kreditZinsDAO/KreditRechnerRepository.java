package at.blo0dy.SpringBank.dao.kreditZinsDAO;

import at.blo0dy.SpringBank.model.produkt.kredit.KreditRechnerErgebnis;
import at.blo0dy.SpringBank.model.produkt.kredit.KreditRechnerVorlage;
import at.blo0dy.SpringBank.model.produkt.sparen.AdvancedSparZinsRechnerErgebnis;
import at.blo0dy.SpringBank.model.produkt.sparen.AdvancedSparZinsRechnerVorlage;
import at.blo0dy.SpringBank.model.produkt.sparen.SparZinsRechnerErgebnis;
import at.blo0dy.SpringBank.model.produkt.sparen.SparZinsRechnerVorlage;

import java.math.BigDecimal;
import java.util.List;


public interface KreditRechnerRepository {

  BigDecimal getZinssatz();

  KreditRechnerErgebnis getKreditRechnerErgebnis(KreditRechnerVorlage kreditRechnerVorlage) ;


}
