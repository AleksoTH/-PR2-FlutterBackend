package myCompany.BackEnd;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import myCompany.BackEnd.datastrukturer.Vedlegg;

public interface StorageService {

	void init();

	Resource loadAsResource(Vedlegg id);

	void delete(Vedlegg id);

	void store(MultipartFile file, Vedlegg id);

}
