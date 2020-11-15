package it.icon.cornercoffe.component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.icon.cornercoffe.pojo.AnswerPOJO;
import it.icon.cornercoffe.pojo.CoffeeChoose;
import it.icon.cornercoffe.pojo.CoffeeType;
import it.icon.cornercoffe.pojo.QuestionPOJO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CoffeeComponent {

	@Autowired
	KieContainer kContainer;
	
	public QuestionPOJO getNextQuestion(List<QuestionPOJO> questions) {
		log.info("METHOD getNextQuestion - questions size {}", questions.size());
		if (!questions.isEmpty()) {
			Collections.shuffle(questions);
			QuestionPOJO nextQuestion = questions.get(0);
			log.info("nextQuestion {}", nextQuestion);
			return nextQuestion;
		} else {
			log.error("FINISH questions . . .");
			return null;
		}
	}
	
	public String getCoffeeChoose(Map<String, String> answers) {
		log.info("METHOD getCoffeeChoose - answers size {}", answers.size());
		KieSession kieSession = kContainer.newKieSession();
		CoffeeChoose coffeeChoose = new CoffeeChoose();
		kieSession.setGlobal("coffeeChoose", coffeeChoose);
		for (Entry<String, String> elem : answers.entrySet()) {
			kieSession.insert(AnswerPOJO.builder().question(elem.getKey()).answer(elem.getValue()).build());
		}
		kieSession.insert(new CoffeeType());
		kieSession.fireAllRules();
		kieSession.dispose();
		if (StringUtils.isNotBlank(coffeeChoose.getCoffee())) {
			log.info("FIND ! ! ! - coffee {}", coffeeChoose.getCoffee());
			return coffeeChoose.getCoffee();
		} else {
			log.info("Informazioni mancanti per il calcolo . . .");
			return null;
		}
	}
}
