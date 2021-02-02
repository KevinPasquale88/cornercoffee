package it.icon.cornercoffe.component;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.icon.cornercoffe.pojo.CoffeeChoose;
import it.icon.cornercoffe.pojo.CoffeeType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CoffeeComponent {

	@Autowired
	KieContainer kContainer;

	CoffeeChoose coffeeChoose = new CoffeeChoose();

	public String getCoffeeChoose(CoffeeType coffeeType) {
		log.info("METHOD getCoffeeChoose - coffeeType {}", coffeeType.toString());
		KieSession kieSession = kContainer.newKieSession();
		kieSession.setGlobal("coffeeChoose", coffeeChoose);
		kieSession.insert(coffeeType);
		try {
			kieSession.fireAllRules();
			kieSession.dispose();
		} catch (Exception ex) {
			log.error(ExceptionUtils.getMessage(ex));
		}
		if (StringUtils.isNotBlank(coffeeChoose.getCoffee())) {
			log.info("FIND ! ! ! - coffee {}", coffeeChoose.getCoffee());
			return coffeeChoose.getCoffee();
		} else {
			log.info("Informazioni mancanti per il calcolo . . .");
			return null;
		}
	}

	public String getBlend() {
		return new StringBuilder("Miscela percentuali Arabica :").append(coffeeChoose.getPercentageArabica())
				.append("%").append(" Robusta :").append(coffeeChoose.getPercentageRobusta()).append("%").toString();
	}
}
