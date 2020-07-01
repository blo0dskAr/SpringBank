package at.blo0dy.SpringBank.service.kunde.kredit;

import at.blo0dy.SpringBank.dao.kreditZinsDAO.KreditRechnerRepository;
import at.blo0dy.SpringBank.model.produkt.kredit.KreditRechnerErgebnis;
import at.blo0dy.SpringBank.model.produkt.kredit.KreditRechnerVorlage;
import at.blo0dy.SpringBank.model.produkt.kredit.KreditUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class KreditServiceImpl implements KreditService {

  KreditRechnerRepository kreditRechnerRepository;

  @Autowired
  public KreditServiceImpl(KreditRechnerRepository kreditRechnerRepository) {
    this.kreditRechnerRepository = kreditRechnerRepository;
  }



  @Override
  public BigDecimal getZinssatz() {
    return KreditUtility.getZinssatz();
  }


  @Override
  public KreditRechnerErgebnis getKreditRechnerErgebnis(KreditRechnerVorlage kreditRechnerVorlage) {
    return kreditRechnerRepository.getKreditRechnerErgebnis(kreditRechnerVorlage);
  }

}
