package myCompany.BackEnd.restapi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sipios.springsearch.anotation.SearchSpec;

import myCompany.BackEnd.BackEndApplication;
import myCompany.BackEnd.ErrorObject;
import myCompany.BackEnd.databaser.Kunder;
import myCompany.BackEnd.datastrukturer.Kunde;
import myCompany.BackEnd.datastrukturer.Produkt;
import myCompany.BackEnd.datastrukturer.Statistikk;
import myCompany.BackEnd.datastrukturer.Produkt.ProduktType;
import myCompany.BackEnd.datastrukturer.Produkt.Status;
import myCompany.BackEnd.generators.View;

@RestController
@RequestMapping(path = "/kunder")
public class KundeKontroller 
{
    @Autowired
    private Kunder kundedb;
    
    @JsonView(View.Public.class)
    @GetMapping(path="/{id}", produces = "application/json")
    public ResponseEntity<?> get(@PathVariable Long id) 
    {
    	Optional<Kunde> copy = kundedb.findById(id);
    	if(copy.isPresent()) {
    		Kunde customer = copy.get();
    		
    		 return ResponseEntity.ok(customer);
    	}
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BackEndApplication.gson.toJson(new ErrorObject("Not found",HttpStatus.BAD_REQUEST)));
    }
    
    @JsonView(View.ExtendedPublic.class)
    @PutMapping(path="/{id}/produkter", produces = "application/json")
    public ResponseEntity<?> NyttProdukt(@PathVariable Long id,@RequestBody Produkt subject) 
    {
    	Optional<Kunde> copy = kundedb.findById(id);
    	Produkt produkt = new Produkt();
		produkt.setRegistrert_dato(Timestamp.from(Instant.now()));
		produkt.setProdukttype(ProduktType.ANDRE);
    	if(copy.isPresent()) {
    		Kunde customer = copy.get();
    		if(customer.getProdukter() != null) {
    			if(subject.getModellnummer() != null && !subject.getModellnummer().isEmpty()) {
    				produkt.setModellnummer(subject.getModellnummer());
    			}
    			if(subject.getMerke() != null && !subject.getMerke().isEmpty()) {
    				produkt.setMerke(subject.getMerke());
    			}
    			if(subject.getProdukttype() != null) {
    				produkt.setProdukttype(subject.getProdukttype());
    			}
    			if(subject.getStatus() != null) {
    				produkt.setStatus(subject.getStatus());
    			}
    			customer.getProdukter().add(produkt);
    			customer = kundedb.save(customer);
    			return new ResponseEntity<String>(BackEndApplication.gson.toJson(customer.getProdukter().get(customer.getProdukter().size()-1)),HttpStatus.ACCEPTED);
    		}
    		 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BackEndApplication.gson.toJson(new ErrorObject("Produkt not found",HttpStatus.BAD_REQUEST)));
    	}
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BackEndApplication.gson.toJson(new ErrorObject("Customer Not found",HttpStatus.BAD_REQUEST)));
    }
    
    @JsonView(View.ExtendedPublic.class)
    @GetMapping(path="/{id}/produkter", produces = "application/json")
    public ResponseEntity<?> listProdukter(@PathVariable Long id,@RequestParam("navn") String produktNavn) 
    {
    	Optional<Kunde> copy = kundedb.findById(id);
    	if(copy.isPresent()) {
    		Kunde customer = copy.get();
    		List<Produkt> removed = customer.getProdukter();
    		JsonArray objects = new JsonArray();
			for(Produkt item : removed) {
				if(produktNavn != null && !produktNavn.isEmpty() && !item.getModellnummer().toLowerCase().startsWith(produktNavn.toLowerCase())) {
					continue;
				}else if(item.getStatus().equals(Status.SOLGT)) {
					continue;
				}
				
				JsonObject obj = new JsonObject();
				obj.addProperty("barkode", item.getBarkode().toString());
				obj.addProperty("kundeid", item.getKunde_id().toString());
				obj.addProperty("registrertDato", item.getRegistrert_dato().toString());
				obj.addProperty("statusDato", item.getStatusDato().toString());
				obj.addProperty("modellnummer", item.getModellnummer());
				obj.addProperty("serienummer", item.getSerienummer());
				obj.addProperty("merke", item.getMerke());
				obj.addProperty("notater", item.getNotater());
				obj.addProperty("lokasjon", item.getLokasjon().toString());
				obj.addProperty("produkttype", item.getProdukttype().name().toString());
				obj.addProperty("status", item.getStatus().name().toString());
				objects.add(obj);
    		}
    		if(removed.size() > 0) {
    			
    			return new ResponseEntity<String>(objects.toString(),HttpStatus.OK);
    		}
    		 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BackEndApplication.gson.toJson(new ErrorObject("Produkt not found",HttpStatus.BAD_REQUEST)));
    	}
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BackEndApplication.gson.toJson(new ErrorObject("Not found",HttpStatus.BAD_REQUEST)));
    }
    

    @JsonView(View.ExtendedPublic.class)
    @DeleteMapping(path="/{id}/produkter/{produktid}", produces = "application/json")
    public ResponseEntity<?> Slettprodukt(@PathVariable Long id,@PathVariable("produktid") Long produktid) 
    {
    	Optional<Kunde> copy = kundedb.findById(id);
    	if(copy.isPresent()) {
    		Kunde customer = copy.get();
    		boolean removed = customer.getProdukter().removeIf(n -> (n.getBarkode().equals(produktid)));
    		if(removed) {
    			customer = kundedb.save(customer);
    			return new ResponseEntity<String>(BackEndApplication.gson.toJson(new ErrorObject("Deleted",HttpStatus.ACCEPTED)),HttpStatus.ACCEPTED);
    		}
    		 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BackEndApplication.gson.toJson(new ErrorObject("Produkt not found",HttpStatus.BAD_REQUEST)));
    	}
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BackEndApplication.gson.toJson(new ErrorObject("Not found",HttpStatus.GONE)));
    }
    
    @JsonView(View.Public.class)
    @PostMapping(path="/{id}",produces = "application/json")
    public ResponseEntity<?> oppdaterKunde(HttpServletRequest request,
    		@RequestBody Kunde subject, @PathVariable Long id){
    	        Optional<Kunde> copy = kundedb.findById(id);
    	        if(copy.isPresent()) {
    	        	 Kunde customer = copy.get();
    	        	 if(subject.getAdresse() != null) {
    	        		 customer.setAdresse(subject.getAdresse());
    	        	 }
    	        	 if(subject.getNavn() != null) {
    	        		 customer.setNavn(subject.getNavn());
    	        	 }
    	        	 if(subject.getNotater() != null) {
    	        		 customer.setNotater(subject.getNotater());
    	        	 }
    	        	 if(subject.getPostnummer() != 0) {
    	        		 customer.setPostnummer(subject.getPostnummer());
    	        	 }
    	        	 if(subject.getTelefonnummer() != 0) {
    	        		 customer.setPostnummer(subject.getPostnummer());
    	        	 }
    	        	 customer = kundedb.save(customer);
    	        	 return new ResponseEntity<Kunde>(customer,HttpStatus.CREATED);
    	        }
    	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BackEndApplication.gson.toJson(new ErrorObject("Not found",HttpStatus.BAD_REQUEST)));
    }
    
    @JsonView(View.Public.class)
    @DeleteMapping(path="/{id}",produces = "application/json")
    public ResponseEntity<?> slettKunde(HttpServletRequest request,
    		@PathVariable Long id){
    	        boolean copy = kundedb.existsById(id);
    	        if(copy) {
    	        	 kundedb.deleteById(id);
    	        	 return ResponseEntity.status(HttpStatus.OK).body(BackEndApplication.gson.toJson(new ErrorObject("Deleted entry",HttpStatus.OK)));
    	        }
    	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BackEndApplication.gson.toJson(new ErrorObject("Not found",HttpStatus.BAD_REQUEST)));
    	
    }
    
    @JsonView(View.Public.class)
    @PutMapping(path="/",produces = "application/json")
    public ResponseEntity<?> nyKunde(HttpServletRequest request,
    		@RequestBody Kunde subject){
    	       if(subject.getAdresse() == null ||
    	    		   subject.getTelefonnummer().equals(0) ||
    	    		   subject.getPostnummer().equals(0) ||
                       subject.getNavn() == null) {
    	    	   return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BackEndApplication.gson.toJson(new ErrorObject("Some fields missing",HttpStatus.BAD_REQUEST)));
    	       }
    	        Kunde copy = new Kunde();
    	        copy.setAdresse(subject.getAdresse());
    	        copy.setNavn(subject.getNavn());
    	        copy.setPostnummer(subject.getPostnummer());
    	        copy.setTelefonnummer(subject.getTelefonnummer());
    	        copy = kundedb.saveAndFlush(copy);
				return new ResponseEntity<Kunde>(copy,HttpStatus.CREATED);
    	
    }
    
    @JsonView(View.Public.class)
    @GetMapping(path="/{id}/mstats", produces = "application/json")
    public ResponseEntity<?> MonthlyStats(@PathVariable Long id) {
    	Optional<Kunde> copy = kundedb.findById(id);
    	if(!copy.isPresent()) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BackEndApplication.gson.toJson(new ErrorObject("Not found",HttpStatus.BAD_REQUEST)));
    	}
    	Kunde kunde = copy.get();
    	Statistikk stat = new Statistikk();
    	Timestamp now = Timestamp.from(Instant.now());
    	for(Produkt produkt : kunde.getProdukter()) {
    		if(produkt.getStatusDato().getMonth() == now.getMonth()) {
    			switch(produkt.getProdukttype()) {
    			case FRYSER:
    				stat.fryser = stat.fryser+1;
    				break;
    			case KJOLESKAP:
    				stat.kjoleskap = stat.kjoleskap+1;
    				break;
    			case KOMFYR:
    				stat.komfyr = stat.komfyr+1;
    				break;
    			case OPPVASKMASKIN:
    				stat.oppvaskmaskin = stat.oppvaskmaskin+1;
    				break;
    			case TORKEPRODUKT:
    				stat.torkeprodukt = stat.torkeprodukt+1;
    				break;
    			case VASKEMASKIN:
    				stat.vaskemaskin = stat.vaskemaskin+1;
    				break;
    			default:
    				break;
    			}
    			switch(produkt.getStatus()) {
    			case ANKOMMET:
    				stat.ankommet = stat.ankommet+1;
    				break;
    			case SKAL_HENTES:
    				stat.skal_hentes = stat.skal_hentes+1;
    				break;
    			case SKAL_VASKES:
    				stat.skal_vaskes = stat.skal_vaskes+1;
    				break;
    			case SKROTET:
    				stat.skrotet = stat.skrotet+1;
    				break;
    			case SOLGT:
    				stat.solgt = stat.solgt+1;
    				break;
    			default:
    				break;
    			}
    		}
    	}

    	return new ResponseEntity<Statistikk>(stat,HttpStatus.CREATED);
    }
    
    @JsonView(View.Public.class)
    @GetMapping(path="/rapportFull", produces = "application/json")
    public ResponseEntity<?> rapport() {
    	Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet();
		int rowCount = 0;
		Row row = sheet.createRow(rowCount++);
		Cell cell = row.createCell(0);
		cell.setCellValue("ProduktType");

		cell = row.createCell(1);
		cell.setCellValue("ProduktStatus");

		cell = row.createCell(2);
		cell.setCellValue("ButikkNavn");
		
		cell = row.createCell(3);
		cell.setCellValue("Registrert");
		Timestamp now = Timestamp.from(Instant.now());
		for(Kunde kunde : kundedb.findAll()) {
			for(Produkt produkt : kunde.getProdukter()) {
				if(produkt.getStatusDato().getMonth() == now.getMonth()) {
					row = sheet.createRow(rowCount++);
					int columnCount = 0;
					cell = row.createCell(columnCount++);
					cell.setCellValue(produkt.getProdukttype().name());

					cell = row.createCell(columnCount++);
					cell.setCellValue(produkt.getStatus().name());

					cell = row.createCell(columnCount++);
					cell.setCellValue(kunde.getNavn());
					
					cell = row.createCell(columnCount++);
					cell.setCellValue(produkt.getRegistrert_dato().toString());
				}
			}
		}
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		try {
			wb.write(os);
			wb.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(
				MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ProductExcelReport.xlsx");

		ResponseEntity<Resource> response = new ResponseEntity<Resource>(new InputStreamResource(is), headers,
				HttpStatus.OK);

		return response;
    }
    
    @JsonView(View.Public.class)
    @GetMapping(path="/søk", produces = "application/json")
    public ResponseEntity<List<Kunde>> kundesøk(@SearchSpec Specification<Kunde> specs) {
        return new ResponseEntity<List<Kunde>>(kundedb.findAll(Specification.where(specs)), HttpStatus.OK);
    }
}