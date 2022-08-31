package myCompany.BackEnd.restapi;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sipios.springsearch.anotation.SearchSpec;

import myCompany.BackEnd.BackEndApplication;
import myCompany.BackEnd.ErrorObject;
import myCompany.BackEnd.StorageService;
import myCompany.BackEnd.databaser.Vedlegger;
import myCompany.BackEnd.datastrukturer.Vedlegg;
import myCompany.BackEnd.datastrukturer.Vedlegg.Type;

@RestController
@RequestMapping(path = "/vedlegg")
public class VedleggsKontroller 
{
	@Autowired
	private StorageService storageService;
	
	@Autowired
	private Vedlegger fildb;
	
	@GetMapping(path="/{vedleggid}/meta", produces = "application/json")
    public ResponseEntity<?> get(@PathVariable Long vedleggid) 
    {
    	Optional<Vedlegg> del = fildb.findById(vedleggid);
    	if(del.isPresent()) {
    		 Vedlegg delen = del.get();
             return ResponseEntity.ok(delen);
    	}
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BackEndApplication.gson.toJson(new ErrorObject("Del Not found",HttpStatus.BAD_REQUEST)));
    }
     
    @GetMapping(path="/", produces = "application/json")
    public Page<Vedlegg> AlleVedlegg(@RequestParam("page") int page) 
    {
        Pageable restrictions = PageRequest.of(page, 100);
		return fildb.findAll(restrictions);
    }
    
    @GetMapping("/{vedleggid}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable Long vedleggid) {
    	Optional<Vedlegg> del = fildb.findById(vedleggid);
    	if(!del.isPresent()) {
    		 return null;
    	}
		Resource file = storageService.loadAsResource(del.get());
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}
    
    @PostMapping(path="/{vedleggid}/opplasting", produces = "application/json")
    public ResponseEntity<?> opplastning(@PathVariable Long vedleggid,@RequestParam("file") MultipartFile file) 
    {
    	Optional<Vedlegg> del = fildb.findById(vedleggid);
    	if(del.isPresent()) {
    		 Vedlegg delen = del.get();
             storageService.store(file, delen); 
    		 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BackEndApplication.gson.toJson(new ErrorObject("Produkt not found",HttpStatus.BAD_REQUEST)));
    	}
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BackEndApplication.gson.toJson(new ErrorObject("Del Not found",HttpStatus.BAD_REQUEST)));
    }
    
    @PostMapping(path="/{id}",produces = "application/json")
    public ResponseEntity<?> oppdaterVedlegg(HttpServletRequest request,
    		@RequestBody Vedlegg subject, @PathVariable Long id){
    	        Optional<Vedlegg> fil = fildb.findById(id);
    	        if(fil.isPresent()) {
    	        	 Vedlegg filen = fil.get();
					 if(subject.getLokasjon() != null) {
						 filen.setLokasjon(subject.getLokasjon());
					 }
					 if(subject.getNavn() != null) {
						 filen.setNavn(subject.getNavn());
					 }
					 if(subject.getType() != null) {
						 filen.setType(subject.getType());
					 }
    	        	 filen = fildb.save(filen);
    	        	 return new ResponseEntity<Vedlegg>(filen,HttpStatus.CREATED);
    	        }
    	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BackEndApplication.gson.toJson(new ErrorObject("Not found",HttpStatus.BAD_REQUEST)));
    }
    
    @DeleteMapping(path="/{id}",produces = "application/json")
    public ResponseEntity<?> slettVedlegg(HttpServletRequest request,
    		@PathVariable Long id){
    	        Optional<Vedlegg> copy = fildb.findById(id);
    	        if(copy.isPresent()) {
    	        	storageService.delete(copy.get());
    	        	 fildb.deleteById(id);
    	        	 
    	        	 return ResponseEntity.status(HttpStatus.OK).body(BackEndApplication.gson.toJson(new ErrorObject("Deleted entry",HttpStatus.OK)));
    	        }
    	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BackEndApplication.gson.toJson(new ErrorObject("Not found",HttpStatus.BAD_REQUEST)));
    	
    }
    
    @PutMapping(path="/nyttvedlegg",produces = "application/json")
    public ResponseEntity<?> nyttVedlegg(HttpServletRequest request,
    		@RequestBody Vedlegg subject){
    	       if(subject.getNavn() == null) {
    	    	   return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BackEndApplication.gson.toJson(new ErrorObject("Some fields missing",HttpStatus.BAD_REQUEST)));
    	       }
    	        Vedlegg copy = new Vedlegg();
    	        copy.setLokasjon("");
    	        copy.setNavn(new Random().nextInt()+"");
				copy.setType(Type.Udefinert);
    	        copy =  fildb.saveAndFlush(copy);
				return new ResponseEntity<Vedlegg>(copy,HttpStatus.CREATED);
    	
    }
    
    
    @GetMapping(path="/søk", produces = "application/json")
    public ResponseEntity<List<Vedlegg>> kundesøk(@SearchSpec Specification<Vedlegg> specs) {
        return new ResponseEntity<List<Vedlegg>>(fildb.findAll(Specification.where(specs)), HttpStatus.OK);
    }
}