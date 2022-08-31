package myCompany.BackEnd.databaser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import myCompany.BackEnd.datastrukturer.Del;

public interface Deler extends JpaRepository<Del, Long>,JpaSpecificationExecutor<Del>  {}