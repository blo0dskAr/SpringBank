package at.blo0dy.SpringBank.service.legidoc;

import at.blo0dy.SpringBank.dao.LegiDokumentRepository;
import at.blo0dy.SpringBank.model.enums.LegiDokumentStatusEnum;
import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.model.person.legidoc.LegiDokument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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
          legidoc = new LegiDokument(docname, file.getContentType(), kunde, file.getBytes(), LegiDokumentStatusEnum.NEU, kunde.getKundennummer());
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


  @Override
  @Transactional
  public List<LegiDokument> getNewFiles() {


    LegiDokument legiDokument = new LegiDokument(null,null,null,null,LegiDokumentStatusEnum.NEU, null);

    ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnoreNullValues()
            .withMatcher("status", match -> match.contains().ignoreCase())
            .withMatcher("docName", match -> match.contains().ignoreCase())
            .withIgnorePaths("docType","kunde","data");

    return legiDokumentRepository.findAll(Example.of(legiDokument, matcher));
  }

  @Override
  @Transactional
  public List<LegiDokument> getSearchedFiles(LegiDokument legiDokument) {

    ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnoreNullValues()
            .withMatcher("status", match -> match.contains().ignoreCase())
            .withMatcher("docName", match -> match.contains().ignoreCase())
            .withMatcher("kundennummer", match -> match.contains())
            .withIgnorePaths("docType","data","kunde");

    return legiDokumentRepository.findAll(Example.of(legiDokument,matcher)) ;
  }

  @Override
  @Transactional
  public String delete(LegiDokument legiDokument) {

    legiDokumentRepository.delete(legiDokument);

    return "successfully deleted";
  }

  @Override
  @Transactional
  public String acceptLegiDokumentById(Long legiDokumentId) {

    legiDokumentRepository.acceptLegiDokumentById(legiDokumentId);
    return "successfully accepted" ;
  }

}
