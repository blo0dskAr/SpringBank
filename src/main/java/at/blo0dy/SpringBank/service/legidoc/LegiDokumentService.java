package at.blo0dy.SpringBank.service.legidoc;

import at.blo0dy.SpringBank.model.person.kunde.Kunde;
import at.blo0dy.SpringBank.model.person.legidoc.LegiDokument;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface LegiDokumentService {

  LegiDokument findByKunde(Long kundeId);

  LegiDokument saveFile(MultipartFile file, Kunde kunde);

  Optional<LegiDokument> getFile(Long fileId);

  List<LegiDokument> getFiles();



}
