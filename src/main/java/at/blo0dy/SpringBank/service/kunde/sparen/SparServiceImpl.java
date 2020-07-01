package at.blo0dy.SpringBank.service.kunde.sparen;

import at.blo0dy.SpringBank.dao.sparZinsDAO.SparZinsRechnerRepository;
import at.blo0dy.SpringBank.model.produkt.sparen.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SparServiceImpl implements SparService {

  SparZinsRechnerRepository sparZinsRechnerRepository;

  @Autowired
  public SparServiceImpl(SparZinsRechnerRepository sparZinsRechnerRepository) {
    this.sparZinsRechnerRepository = sparZinsRechnerRepository;
  }

  @Override
  public double getZinssatz() {
    return SparenUtility.getZinssatz();
  }

//  @Override
//  public SparZinsRechnerErgebnis getSparZinsRechnerEregebnis(LocalDate datum, double betrag) {
//    return sparZinsRechnerRepository.getSparZinsRechnerEregebnis(datum, betrag);
//  }

  @Override
  public SparZinsRechnerErgebnis getSparZinsRechnerEregebnis(SparZinsRechnerVorlage sparZinsRechnerVorlage) {
    return sparZinsRechnerRepository.getSparZinsRechnerEregebnis(sparZinsRechnerVorlage);
  }

  @Override
  public List<AdvancedSparZinsRechnerErgebnis> getAdvancedSparZinsRechnerErgebnis(AdvancedSparZinsRechnerVorlage sparZinsRechnerVorlage) {
    return sparZinsRechnerRepository.getAdvancedSparZinsRechnerErgebnis(sparZinsRechnerVorlage);
  };





}
