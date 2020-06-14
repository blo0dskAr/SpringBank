package at.blo0dy.SpringBank.model.person.rolle;


import at.blo0dy.SpringBank.model.person.mitarbeiter.Mitarbeiter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "rolle")
public class Rolle  {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column
  @NotBlank
  @Pattern(regexp = "^[a-z_-]{3,20}")
  private String name;

/*  @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH,CascadeType.REFRESH})
  @JoinColumn(name="mitarbeiter_id")
  private Mitarbeiter mitarbeiter;*/

  @ManyToMany(mappedBy = "rollen")
  private List<Mitarbeiter> mitarbeiter;



}
