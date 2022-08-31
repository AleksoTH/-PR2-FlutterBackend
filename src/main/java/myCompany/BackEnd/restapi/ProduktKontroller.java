package myCompany.BackEnd.restapi;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import myCompany.BackEnd.BackEndApplication;
import myCompany.BackEnd.ErrorObject;
import myCompany.BackEnd.databaser.Produkter;
import myCompany.BackEnd.datastrukturer.Produkt;
import myCompany.BackEnd.generators.QRCodeGen;
import myCompany.BackEnd.generators.View;

@RestController
@RequestMapping(path = "/produkter")
public class ProduktKontroller 
{
    
    @Autowired
    private Produkter produktdb;
    
    @JsonView(View.Public.class)
    @GetMapping(path="/{produktid}/print", produces = "image/png")
    public ResponseEntity<?> print(@PathVariable Long produktid) 
    {
    	Optional<Produkt> prod = produktdb.findById(produktid);
    	if(prod.isPresent()) {
    		Produkt customer = prod.get();
    		
    		String serial = customer.getSerienummer();
    		String modell = customer.getModellnummer();
            BufferedImage file = QRCodeGen.generate_qr(customer.getBarkode(), modell, serial);
    		ByteArrayOutputStream os = new ByteArrayOutputStream();

    		try {
    			ImageIO.write(file, "png", os);
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		

    		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());

    		HttpHeaders headers = new HttpHeaders();
    		headers.setContentType(MediaType.IMAGE_PNG);
    		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
    		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=image.png");

    		ResponseEntity<Resource> response = new ResponseEntity<Resource>(new InputStreamResource(is), headers,
    				HttpStatus.OK);
    		 return response;
    	}
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BackEndApplication.gson.toJson(new ErrorObject("Not found",HttpStatus.BAD_REQUEST)));
    }
    
    
    @JsonView(View.Public.class)
    @GetMapping(path="/{produktid}", produces = "application/json")
    public ResponseEntity<?> get(@PathVariable Long produktid) 
    {
    	Optional<Produkt> prod = produktdb.findById(produktid);
    	if(prod.isPresent()) {
    		Produkt customer = prod.get();
    		
    		 return ResponseEntity.ok(customer);
    	}
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BackEndApplication.gson.toJson(new ErrorObject("Not found",HttpStatus.BAD_REQUEST)));
    }
    
    @JsonView(View.Public.class)
    @PostMapping(path="/{id}",produces = "application/json")
    public ResponseEntity<?> oppdaterProdukt(HttpServletRequest request,
    		@RequestBody Produkt subject, @PathVariable Long id){
    	        Optional<Produkt> copy = produktdb.findById(id);
    	        if(copy.isPresent()) {
    	        	 Produkt produkt = copy.get();
						if (subject.getStatus() != null) {
							produkt.setStatus(subject.getStatus());
						}
						if (subject.getLokasjon() != null) {
							produkt.setLokasjon(subject.getLokasjon());
						}
						if (subject.getModellnummer() != null) {
							produkt.setModellnummer(subject.getModellnummer());
						}
						if (subject.getSerienummer() != null) {
							produkt.setSerienummer(subject.getSerienummer());
						}
						if (subject.getProdukttype() != null) {
							produkt.setProdukttype(subject.getProdukttype());
						}
    	        	 produkt = produktdb.save(produkt);
    	        	 return new ResponseEntity<Produkt>(produkt,HttpStatus.CREATED);
    	        }
    	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BackEndApplication.gson.toJson(new ErrorObject("Not found",HttpStatus.BAD_REQUEST)));
    }
}