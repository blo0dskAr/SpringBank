package at.blo0dy.SpringBank.service.legidoc;

import at.blo0dy.SpringBank.dao.person.LegiDokumentRepository;
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
  @Transactional(readOnly = true)
  public LegiDokument findByKunde(Long kundeId) {
    log.debug("LegiDokument für KundeId: " + kundeId + " wird gesucht.");

    return legiDokumentRepository.findByKunde(kundeId);
  }

//  @Transactional
  @Override
  public LegiDokument saveFile(MultipartFile file, Kunde kunde) {
    log.debug("Legidokument für Kundennummer: " + kunde.getKundennummer() + " wird gespeichert." );
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
      LegiDokument savedLegiDoc = legiDokumentRepository.save(legidoc);
      log.debug("LegiDokument erfolgreich gespeichert. ID: " + savedLegiDoc.getId());
      return savedLegiDoc;
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<LegiDokument> getFile(Long fileId) {
    log.debug("LegiDokument mit FileId: " + fileId + " wird gesucht.");
    return legiDokumentRepository.findById(fileId);
  }

/*  @Override
  @Transactional(readOnly = true)
  public List<LegiDokument> getFiles() {
    return legiDokumentRepository.findAll();
  }*/


  @Override
  @Transactional(readOnly = true)
  public List<LegiDokument> getNewFiles() {

    log.debug("Neue Legidokumente werden gesucht");

    LegiDokument legiDokument = new LegiDokument(null,null,null,null,LegiDokumentStatusEnum.NEU, null);

    ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnoreNullValues()
            .withMatcher("status", match -> match.contains().ignoreCase())
            .withMatcher("docName", match -> match.contains().ignoreCase())
            .withIgnorePaths("docType","kunde","data");

    List<LegiDokument> legiDokumentList =  legiDokumentRepository.findAll(Example.of(legiDokument, matcher));
    log.debug(legiDokumentList.size() + " Neue Legidokumente wurden gefunden udn ggf. angezeigt");

    return legiDokumentList;
  }

  @Override
  @Transactional(readOnly = true)
  public List<LegiDokument> getSearchedFiles(LegiDokument legiDokument) {

    log.debug("Legidokumentensuche wird durchgeführt");

    ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnoreNullValues()
            .withMatcher("status", match -> match.contains().ignoreCase())
            .withMatcher("docName", match -> match.contains().ignoreCase())
            .withMatcher("kundennummer", match -> match.contains())
            .withIgnorePaths("docType","data","kunde");

    List<LegiDokument> legiDokumentList = legiDokumentRepository.findAll(Example.of(legiDokument,matcher)) ;
    log.debug(legiDokumentList.size() + " Neue Legidokumente wurden gefunden udn ggf. angezeigt");
    return legiDokumentList;
  }

  @Override
//  @Transactional
  public String delete(LegiDokument legiDokument) {

    log.debug("Legidokument für Kundennummer: " + legiDokument.getKundennummer() + " ID: " + legiDokument.getId() + " wird gelöscht.");

    legiDokumentRepository.delete(legiDokument);
    log.debug("Legidokument für Kundennummer: " + legiDokument.getKundennummer() + " erfolgreich gelöscht.");

    return "successfully deleted";
  }

  @Override
  @Transactional
  public String acceptLegiDokumentById(Long legiDokumentId) {

    log.debug("LegiDokument mit ID: " + legiDokumentId + " wird akzeptiert." );

//    legiDokumentRepository.acceptLegiDokumentById(legiDokumentId);
    LegiDokument legiDokument = legiDokumentRepository.findById(legiDokumentId).get();
    legiDokument.setStatus(LegiDokumentStatusEnum.BEARBEITET);
    log.debug("LegiDokument mit ID: " + legiDokument + " erfolgreich aktualisiert.");
    return "successfully accepted" ;
  }

}
