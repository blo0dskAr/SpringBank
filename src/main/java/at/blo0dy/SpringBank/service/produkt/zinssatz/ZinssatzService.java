package at.blo0dy.SpringBank.service.produkt.zinssatz;

import at.blo0dy.SpringBank.model.produkt.zinssatz.Zinssatz;

import java.math.BigDecimal;

public interface ZinssatzService {

 BigDecimal getAktuellerSparZinssatz();

 BigDecimal getAktuellerKreditZinssatz();

 BigDecimal getAktuellerGiroZinssatz();

// BigDecimal getAktuellerZinssatzByProdukt(String produkt);

 void save(Zinssatz zinssatz);



}
