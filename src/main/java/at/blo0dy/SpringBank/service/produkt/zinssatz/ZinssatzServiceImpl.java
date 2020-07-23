package at.blo0dy.SpringBank.service.produkt.zinssatz;

import at.blo0dy.SpringBank.dao.produkt.zinssatz.ZinssatzRepository;
import at.blo0dy.SpringBank.model.produkt.zinssatz.Zinssatz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ZinssatzServiceImpl implements ZinssatzService {

  ZinssatzRepository zinssatzRepository;

  @Autowired
  public ZinssatzServiceImpl(ZinssatzRepository zinssatzRepository) {
    this.zinssatzRepository = zinssatzRepository;
  }


  @Override
  public BigDecimal getAktuellerSparZinssatz() {
    return zinssatzRepository.getAktuellerSparZinssatz();
  }

  @Override
  public BigDecimal getAktuellerKreditZinssatz() {
    return zinssatzRepository.getAktuellerKreditZinssatz();
  }

  @Override
  public BigDecimal getAktuellerGiroZinssatz() {
    return zinssatzRepository.getAktuellerGiroZinssatz();
  }

  public BigDecimal getAktuellerZinssatzByProdukt(String produkt) {

    return zinssatzRepository.getAktuellerZinssatzByProdukt(produkt);
  }

  @Override
  public void save(Zinssatz zinssatz) {
    zinssatzRepository.save(zinssatz);
  }


}
