package myCompany.BackEnd;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import myCompany.BackEnd.datastrukturer.Vedlegg;

@Service
public class FileSystemStorageService implements StorageService {

	private final Path rootLocation = new File("C:\\vedlegg").toPath();

	@Override
	public void store(MultipartFile file,Vedlegg id) {
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file.");
			}
			Path destinationFile = this.rootLocation.resolve(
					Paths.get(id.getId()+""))
					.normalize().toAbsolutePath();
			if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
				// This is a security check
				throw new StorageException(
						"Cannot store file outside current directory.");
			}
			
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFile,
					StandardCopyOption.REPLACE_EXISTING);
			}
		}
		catch (IOException e) {
			throw new StorageException("Failed to store file.", e);
		}
	}

	@Override
	public Resource loadAsResource(Vedlegg id) {
		try {
			Path destinationFile = this.rootLocation.resolve(
					Paths.get(id.getId()+""))
					.normalize().toAbsolutePath();
			Resource resource = new UrlResource(destinationFile.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"Could not read file: " + destinationFile.getFileName());

			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + id.getId(), e);
		}
	}

	@Override
	public void init() {
		try {
			Files.createDirectories(rootLocation);
		}
		catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}

	@Override
	public void delete(Vedlegg id) {
		Path destinationFile = this.rootLocation.resolve(
				Paths.get(id.getId()+""))
				.normalize().toAbsolutePath();
		try {
			Files.delete(destinationFile);
		} catch (IOException e) {
			throw new StorageFileNotFoundException("Could not delete file: " + id.getId(), e);
		}
	}
}
