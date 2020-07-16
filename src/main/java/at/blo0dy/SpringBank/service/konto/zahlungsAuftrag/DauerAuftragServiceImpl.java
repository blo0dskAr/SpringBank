package at.blo0dy.SpringBank.service.konto.zahlungsAuftrag;

import at.blo0dy.SpringBank.dao.konto.zahlungsAuftrag.DauerAuftragRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DauerAuftragServiceImpl implements DauerAuftragService {

  DauerAuftragRepository dauerAuftragRepository;

  @Autowired
  public DauerAuftragServiceImpl(DauerAuftragRepository dauerAuftragRepository) {
    this.dauerAuftragRepository = dauerAuftragRepository;
  }


}
