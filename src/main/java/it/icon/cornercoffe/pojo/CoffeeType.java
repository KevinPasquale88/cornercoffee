package it.icon.cornercoffe.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoffeeType {

	public double acidity;

	public double aroma;

	public double bitter;

	public double body;

	public double sweetness;

	public void addAcidity(double value) {
		this.acidity += value;
	}

	public void addAroma(double value) {
		this.aroma += value;
	}

	public void addBitter(double value) {
		this.bitter += value;
	}

	public void addBody(double value) {
		this.body += value;
	}

	public void addSweetness(double value) {
		this.sweetness += value;
	}

}
