package it.icon.cornercoffe.pojo;

import lombok.Data;

@Data
public class CoffeeChoose {

	public CoffeeChoose() {
		this.coffee = null;
		this.percentageArabica = 50;
		this.percentageRobusta = 50;
	}

	private String coffee;

	private double percentageArabica;

	private double percentageRobusta;

	public void moreArabica(double value) {
		if (this.percentageArabica <= 90 && this.percentageRobusta >= 10) {
			this.percentageArabica += value;
			this.percentageRobusta -= value;
		}
	}

	public void moreRobusta(double value) {
		if (this.percentageRobusta <= 90 && this.percentageArabica >= 10) {
			this.percentageArabica -= value;
			this.percentageRobusta += value;
		}

	}
}
