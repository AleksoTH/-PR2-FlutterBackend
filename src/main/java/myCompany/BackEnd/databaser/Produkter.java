package myCompany.BackEnd.databaser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import myCompany.BackEnd.datastrukturer.Produkt;

public interface Produkter extends JpaRepository<Produkt, Long>,JpaSpecificationExecutor<Produkt> {}
