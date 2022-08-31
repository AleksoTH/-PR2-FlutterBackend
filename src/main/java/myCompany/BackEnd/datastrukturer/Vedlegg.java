package myCompany.BackEnd.datastrukturer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Entity
@Table(name="Vedlegg")
public class Vedlegg {
	public static enum Type {
		Udefinert,
		Salgskvittering,
		Typeskilt,
		GLOBAL
	}
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "snowball")
    @GenericGenerator(
        name = "snowball", 
        strategy = "Elektrotec.BackEnd.generators.SnøballGen")
    @JsonSerialize(using=ToStringSerializer.class)
    private Long id;
    
    @Column
    @JsonSerialize(using=ToStringSerializer.class)
    private Long produkt_id;
    
    @Column
    private String lokasjon;
    
    @Column
    private String navn;
    
    @Column
    @Enumerated(EnumType.STRING)
    private Type type = Type.Udefinert;

	public Long getId() {
		return id;
	}

	public String getLokasjon() {
		return lokasjon;
	}

	public String getNavn() {
		return navn;
	}

	public Type getType() {
		return type;
	}

	public void setLokasjon(String lokasjon) {
		this.lokasjon = lokasjon;
	}

	public void setNavn(String navn) {
		this.navn = navn;
	}

	public void setType(Type type) {
		this.type = type;
	}
    
    
}
