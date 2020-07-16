package at.blo0dy.SpringBank.dao.konto.zahlungsAuftrag;

import at.blo0dy.SpringBank.model.konto.zahlungsAuftrag.DauerAuftrag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DauerAuftragRepository extends JpaRepository<DauerAuftrag, Long> {


}
