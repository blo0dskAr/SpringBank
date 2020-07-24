package at.blo0dy.SpringBank.dao;

import at.blo0dy.SpringBank.model.person.legidoc.LegiDokument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LegiDokumentRepository extends JpaRepository<LegiDokument, Long> {


  @Query(value ="select * from legi_dokument doc " +
                  " where doc.kunde_id = ?1 ", nativeQuery = true)
  LegiDokument findByKunde(Long kundeId);


//  @Modifying
//  @Query(value = "update legi_dokument doc set " +
//                  "      doc.status = 'BEARBEITET'" +
//                 " where doc.id = ?1", nativeQuery = true)
//  void acceptLegiDokumentById(Long legiDokumentId);
}
