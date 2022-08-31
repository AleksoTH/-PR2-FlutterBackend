package myCompany.BackEnd.datastrukturer;

import com.fasterxml.jackson.annotation.JsonView;

import myCompany.BackEnd.generators.View;

public class Statistikk{
	
	@JsonView(View.Public.class)
	public int fryser = 0;
	@JsonView(View.Public.class)
	public int kjoleskap= 0;
	@JsonView(View.Public.class)
	public int komfyr = 0;
	@JsonView(View.Public.class)
	public int vaskemaskin = 0;
	@JsonView(View.Public.class)
	public int torkeprodukt = 0;
	@JsonView(View.Public.class)
	public int oppvaskmaskin = 0;
	@JsonView(View.Public.class)
	public int ankommet = 0;
	@JsonView(View.Public.class)
	public int skal_hentes = 0;
	@JsonView(View.Public.class)
	public int skal_vaskes = 0;
	@JsonView(View.Public.class)
	public int skrotet = 0;
	@JsonView(View.Public.class)
	public int solgt = 0;
	
}
