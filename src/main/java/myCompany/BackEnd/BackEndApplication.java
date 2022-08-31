package myCompany.BackEnd;

import javax.persistence.Transient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;

@SpringBootApplication
public class BackEndApplication {
	@Transient
	public
	static Gson gson = new GsonBuilder().setLongSerializationPolicy( LongSerializationPolicy.STRING ).setDateFormat("yyyy-MM-dd hh:mm:ss.S").create();
	
	public static void main(String[] args) {
		SpringApplication.run(BackEndApplication.class, args);
	}

}
