package at.blo0dy.SpringBank.model.person.rolle;


import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rolle")
public class Rolle  {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column
  @NotBlank
  @Pattern(message = "Darf nicht leer sein. Darf nur Buchstaben, \"_\" und \"-\" enthalten", regexp = "^[a-z_-]{3,20}")
  private String name;

  @ManyToMany(mappedBy = "rollen")
  private List<Mitarbeiter> mitarbeiter;



}
