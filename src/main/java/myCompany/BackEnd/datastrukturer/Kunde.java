package myCompany.BackEnd.datastrukturer;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

import myCompany.BackEnd.generators.View;

@Entity
@Table(name="Kunder")
public class Kunde {
	    @Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "snowball")
	    @GenericGenerator(
	        name = "snowball", 
	        strategy = "Elektrotec.BackEnd.generators.SnøballGen")
	    @JsonView(View.Public.class)
	    @JsonSerialize(using=ToStringSerializer.class)
	    private Long kundeid;
	    
	    @Column
	    @JsonView(View.Public.class)
	    private String navn= "";
	    @Column
	    @JsonView(View.Public.class)
	    private String adresse= "";
	    @Column
	    @JsonView(View.Public.class)
	    private Integer postnummer;
	    @Column
	    @JsonView(View.Public.class)
	    private String notater= "";
	    @Column
	    @JsonView(View.Public.class)
	    private Integer telefonnummer;
	    
	    @Expose(serialize = false, deserialize = true) 
	    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
		@JoinColumn(name = "kunde_id")
	    @JsonView(View.ExtendedPublic.class)
	    private List<Produkt> produkter = new ArrayList<Produkt>();

		public String getNavn() {
			return navn;
		}

		public void setNavn(String navn) {
			this.navn = navn;
		}

		public String getAdresse() {
			return adresse;
		}

		public void setAdresse(String adresse) {
			this.adresse = adresse;
		}

		public Integer getPostnummer() {
			return postnummer;
		}

		public void setPostnummer(Integer postnummer) {
			this.postnummer = postnummer;
		}

		public String getNotater() {
			return notater;
		}

		public void setNotater(String notater) {
			this.notater = notater;
		}

		public Integer getTelefonnummer() {
			return telefonnummer;
		}

		public void setTelefonnummer(Integer telefonnummer) {
			this.telefonnummer = telefonnummer;
		}

		public List<Produkt> getProdukter() {
			if(produkter == null)return new ArrayList<Produkt>();
			return produkter;
		}

		public void setProdukter(List<Produkt> produkter) {
			this.produkter = produkter;
		}

		public Long getKundeid() {
			return kundeid;
		}
}
