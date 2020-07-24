package at.blo0dy.SpringBank.dao.produkt.zinssatz;

import at.blo0dy.SpringBank.model.produkt.zinssatz.Zinssatz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface ZinssatzRepository extends JpaRepository<Zinssatz, Long> {

  @Query(value = "select zs.zinssatz from zinssatz zs " +
          " where zs.produkt = 'SPAREN'", nativeQuery = true)
  BigDecimal getAktuellerSparZinssatz();

  @Query(value = "select zs.zinssatz from zinssatz zs " +
          " where zs.produkt = 'KREDIT'" , nativeQuery = true)
  BigDecimal getAktuellerKreditZinssatz();

  @Query(value = "select zs.zinssatz from zinssatz zs " +
          " where zs.produkt = 'GIRO'", nativeQuery = true)
  BigDecimal getAktuellerGiroZinssatz();


  @Query(value = "select zs.zinssatz from zinssatz zs " +
                  " where zs.produkt = ?1", nativeQuery = true)
  BigDecimal getAktuellerZinssatzByProdukt(String produkt);
}
