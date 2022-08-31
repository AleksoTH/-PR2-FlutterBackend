package myCompany.BackEnd.databaser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import myCompany.BackEnd.datastrukturer.Kunde;

@RepositoryRestResource
public interface Kunder extends JpaRepository<Kunde, Long>,JpaSpecificationExecutor<Kunde> {}
