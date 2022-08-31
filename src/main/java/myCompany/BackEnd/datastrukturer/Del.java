package myCompany.BackEnd.datastrukturer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Entity
@Table(name="Deler")
public class Del {
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
	private String varenummer;
	@Column
	private String beskrivelse;
	@Column
	private Integer pris;
	@Column
	private String plassering;
	@Column
	private Integer antall_i_lager;
	@Column
	private String leverandor;
	
	public Long getId() {
		return id;
	}
	public String getVarenummer() {
		return varenummer;
	}
	public String getBeskrivelse() {
		return beskrivelse;
	}
	public Integer getPris() {
		return pris;
	}
	public String getPlassering() {
		return plassering;
	}
	public Integer getAntall_i_lager() {
		return antall_i_lager;
	}
	public String getLeverandør() {
		return leverandor;
	}
	public void setVarenummer(String varenummer) {
		this.varenummer = varenummer;
	}
	public void setBeskrivelse(String beskrivelse) {
		this.beskrivelse = beskrivelse;
	}
	public void setPris(Integer pris) {
		this.pris = pris;
	}
	public void setPlassering(String plassering) {
		this.plassering = plassering;
	}
	public void setAntall_i_lager(Integer antall_i_lager) {
		this.antall_i_lager = antall_i_lager;
	}
	public void setLeverandør(String leverandør) {
		this.leverandor = leverandør;
	}
	
	
}
