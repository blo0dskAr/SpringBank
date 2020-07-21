package at.blo0dy.SpringBank.service.zv;

import at.blo0dy.SpringBank.dao.DatenTraegerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatentraegerServiceImpl implements DatentraegerService {

  DatenTraegerRepository datenTraegerRepository;

  @Autowired
  public DatentraegerServiceImpl(DatenTraegerRepository datenTraegerRepository) {
    this.datenTraegerRepository = datenTraegerRepository;
  }







}
