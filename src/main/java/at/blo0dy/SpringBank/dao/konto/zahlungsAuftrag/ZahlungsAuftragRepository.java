package at.blo0dy.SpringBank.dao.konto.zahlungsAuftrag;

import at.blo0dy.SpringBank.model.konto.zahlungsAuftrag.ZahlungsAuftrag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZahlungsAuftragRepository extends JpaRepository<ZahlungsAuftrag, Long> {
}
