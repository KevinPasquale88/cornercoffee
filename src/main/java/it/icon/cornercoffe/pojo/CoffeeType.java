package it.icon.cornercoffe.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CoffeeType {
	
	public double acidity;
	
	public double aroma;
	
	public double bitter;
	
	public double body;
	
	public double sweetness;

}
