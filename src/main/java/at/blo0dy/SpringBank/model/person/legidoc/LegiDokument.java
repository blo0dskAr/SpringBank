package at.blo0dy.SpringBank.model.person.legidoc;


import at.blo0dy.SpringBank.model.enums.LegiDokumentStatusEnum;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name ="legi_dokument")
@NoArgsConstructor
public class LegiDokument {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  private String docName;
  private String docType;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "kunde_id", referencedColumnName = "id")
  private Kunde kunde;

  @Lob
  private byte[] data;

  @Enumerated(EnumType.STRING)
  private LegiDokumentStatusEnum status;


  public LegiDokument(String docName, String docType, Kunde kunde, byte[] data, LegiDokumentStatusEnum status) {
    this.docName = docName;
    this.docType = docType;
    this.kunde = kunde;
    this.data = data;
    this.status = status;
  }
}
