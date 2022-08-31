package myCompany.BackEnd.datastrukturer;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.google.gson.annotations.Expose;

import myCompany.BackEnd.generators.View;

@Entity
@Table(name="Produkter")
public class Produkt {
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "snowball")
    @GenericGenerator(
        name = "snowball", 
        strategy = "Elektrotec.BackEnd.generators.SnøballGen")
	@JsonSerialize(using=ToStringSerializer.class)
    private Long barkode;
	
	
	//Kommer fra kunde 
	@Column
	@JsonView(View.Public.class)
	@JsonSerialize(using=ToStringSerializer.class)
	private Long kunde_id;
	
    public static enum Status{
    	UKJENT,
    	SOLGT,
    	SKAL_VASKES,
    	ANKOMMET,
    	SKAL_HENTES,
    	SKROTET
    };
    
    public static enum ProduktType{
    	KJOLESKAP,
    	FRYSER,
    	VASKEMASKIN,
    	OPPVASKMASKIN,
    	TORKEPRODUKT,
    	KOMFYR,
    	ANDRE
    };
    
    @Column
    @JsonView(View.Public.class)
    private Timestamp registrertDato;
    
    @Column
    @JsonView(View.Public.class)
    private Timestamp statusDato;
    
    @Column
    @JsonView(View.Public.class)
    private String modellnummer= "";
    @Column
    @JsonView(View.Public.class)
    private String serienummer= "";
    @Column
    @JsonView(View.Public.class)
    private  String notater = "";
    
    @JsonView(View.ExtendedPublic.class)
    @Expose(serialize = false, deserialize = true) 
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "produkt_id")
    private List<Vedlegg> filer = new ArrayList<Vedlegg>();
    
    @JsonView(View.ExtendedPublic.class)
    @Expose(serialize = false, deserialize = true) 
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "produkt_id")
    private  List<Del> deler = new ArrayList<Del>();
    
    @Column
    @JsonView(View.Public.class)
    private String lokasjon = "";
    
    @Column
    @JsonView(View.Public.class)
    private String merke = "";
    
    @Column
    @Enumerated(EnumType.STRING)
    @JsonView(View.Public.class)
    private ProduktType produkttype = ProduktType.ANDRE;
    
    @Column
    @Enumerated(EnumType.STRING)
    @JsonView(View.Public.class)
    private Status status = Status.UKJENT;
    
    

	public String getMerke() {
		return merke;
	}

	public void setMerke(String merke) {
		this.merke = merke;
	}

	public Long getKunde_id() {
		return kunde_id;
	}

	public void setKunde_id(Long kunde_id) {
		this.kunde_id = kunde_id;
	}

	public String getModellnummer() {
		return modellnummer;
	}

	public void setModellnummer(String modellnummer) {
		this.modellnummer = modellnummer;
	}

	public String getSerienummer() {
		return serienummer;
	}

	public void setSerienummer(String serienummer) {
		this.serienummer = serienummer;
	}

	public String getNotater() {
		return notater;
	}

	public void setNotater(String notater) {
		this.notater = notater;
	}

	public List<Vedlegg> getFiler() {
		return filer;
	}

	public void setFiler(List<Vedlegg> filer) {
		this.filer = filer;
	}

	public List<Del> getDeler() {
		return deler;
	}

	public void setDeler(List<Del> deler) {
		this.deler = deler;
	}

	public String getLokasjon() {
		return lokasjon;
	}

	public void setLokasjon(String lokasjon) {
		this.lokasjon = lokasjon;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.statusDato = Timestamp.from(Instant.now());
		this.status = status;
	}
	
	public Timestamp getRegistrert_dato() {
		return registrertDato;
	}

	public void setRegistrert_dato(Timestamp registrert_dato) {
		this.registrertDato = registrert_dato;
	}

	public ProduktType getProdukttype() {
		return produkttype;
	}

	public void setProdukttype(ProduktType produkttype) {
		this.produkttype = produkttype;
	}

	public Timestamp getStatusDato() {
		if(statusDato == null)return Timestamp.from(Instant.MIN);
		return statusDato;
	}

	public Long getBarkode() {
		return barkode;
	}

	
}
