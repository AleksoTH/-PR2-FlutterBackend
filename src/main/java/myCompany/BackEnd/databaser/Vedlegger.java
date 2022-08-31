package myCompany.BackEnd.databaser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import myCompany.BackEnd.datastrukturer.Vedlegg;

public interface Vedlegger extends JpaRepository<Vedlegg, Long>,JpaSpecificationExecutor<Vedlegg>  {}
