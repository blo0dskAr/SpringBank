package at.blo0dy.SpringBank.dao.kreditZinsDAO;

import at.blo0dy.SpringBank.model.produkt.kredit.KreditRechnerErgebnis;
import at.blo0dy.SpringBank.model.produkt.kredit.KreditRechnerVorlage;
import at.blo0dy.SpringBank.model.produkt.kredit.KreditUtility;
import org.springframework.stereotype.Component;


@Component
public class KreditRechnerRepositoryImpl implements KreditRechnerRepository {

  @Override
  public KreditRechnerErgebnis getKreditRechnerErgebnis(KreditRechnerVorlage kreditRechnerVorlage) {
    return KreditUtility.getKreditRechnerErgebnis(kreditRechnerVorlage);
  }




}
