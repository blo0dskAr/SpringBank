package at.blo0dy.SpringBank.service.konto.zahlungsAuftrag;

import at.blo0dy.SpringBank.dao.konto.zahlungsAuftrag.ZahlungsAuftragRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ZahlungsAuftragServiceImpl {

  ZahlungsAuftragRepository zahlungsAuftragRepository;

  @Autowired
  public ZahlungsAuftragServiceImpl(ZahlungsAuftragRepository zahlungsAuftragRepository) {
    this.zahlungsAuftragRepository = zahlungsAuftragRepository;
  }

  



}
