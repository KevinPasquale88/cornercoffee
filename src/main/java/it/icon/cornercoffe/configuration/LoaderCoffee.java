package it.icon.cornercoffe.configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.icon.cornercoffe.pojo.CoffeeType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class LoaderCoffee {

	@Bean
	public Map<String, CoffeeType> coffeeTable() {
		Map<String, CoffeeType> coffeeTable = new HashMap<>();
		try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/table_coffee.csv"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				coffeeTable.put(values[0],
						CoffeeType.builder()
							.body(Double.valueOf(values[1]))
							.bitter(Double.valueOf(values[2]))
							.acidity(Double.valueOf(values[3]))
							.aroma(Double.valueOf(values[4]))
							.sweetness(Double.valueOf(values[5]))
						.build());
			}
		} catch (IOException ex) {
			log.error("Error on read file csv table_coffee with coffee");
		}
		return coffeeTable;
	}
}
