package at.blo0dy.SpringBank.service.legidoc;

import at.blo0dy.SpringBank.dao.LegiDokumentRepository;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.model.person.legidoc.LegiDokument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class LegiDokumentServiceImpl implements LegiDokumentService {

  private LegiDokumentRepository legiDokumentRepository;

  @Autowired
  public LegiDokumentServiceImpl(LegiDokumentRepository legiDokumentRepository) {
    this.legiDokumentRepository = legiDokumentRepository;
  }

  @Override
  @Transactional
  public LegiDokument findByKunde(Long kundeId) {
    return legiDokumentRepository.findByKunde(kundeId);
  }

  @Transactional
  @Override
  public LegiDokument saveFile(MultipartFile file, Kunde kunde) {
    String docname = file.getOriginalFilename();
    LegiDokument legidoc = null;


      if (findByKunde(kunde.getId()) != null) {
        legidoc = findByKunde(kunde.getId());
      } else {
        try {
          legidoc = new LegiDokument(docname, file.getContentType(), kunde, file.getBytes());
        } catch (Exception e) {
          e.getMessage();
        }
      }
    return legiDokumentRepository.save(legidoc);
  }

  @Override
  @Transactional
  public Optional<LegiDokument> getFile(Long fileId) {
    return legiDokumentRepository.findById(fileId);
  }

  @Override
  @Transactional
  public List<LegiDokument> getFiles() {
    return legiDokumentRepository.findAll();
  }

}
