package it.icon.cornercoffe.component;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
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

	List<AnswerPOJO> list = new ArrayList<AnswerPOJO>();

	AnswerPOJO last = null;
	
	CoffeeChoose coffeeChoose = new CoffeeChoose();

	CoffeeType coffeeType = new CoffeeType();

	public QuestionPOJO getNextQuestion(List<QuestionPOJO> questions) {
		log.info("METHOD getNextQuestion - questions size {}", questions.size());
		if (!questions.isEmpty()) {
			QuestionPOJO nextQuestion = questions.get(0);
			log.info("nextQuestion {}", nextQuestion);
			return nextQuestion;
		} else {
			log.error("FINISH questions . . .");
			return null;
		}
	}

	public void updateKnowledge(String question, String answer) {
		last = new AnswerPOJO(question, answer);
		list.add(last);
	}

	public String getCoffeeChoose(boolean empty) {
		log.info("METHOD getCoffeeChoose - coffeeType {}", coffeeType.toString());
		KieSession kieSession = kContainer.newKieSession();
		kieSession.setGlobal("coffeeChoose", coffeeChoose);
		kieSession.insert(coffeeType);
		kieSession.insert(list);
		kieSession.insert(last);
		try {
			kieSession.fireAllRules();
			kieSession.dispose();
		} catch (Exception ex) {
			log.error(ExceptionUtils.getMessage(ex));
		}
		log.info("coffeeType {}", coffeeType.toString());
		if (StringUtils.isNotBlank(coffeeChoose.getCoffee())) {
			log.info("FIND ! ! ! - coffee {}", coffeeChoose.getCoffee());
			return coffeeChoose.getCoffee();
		} else if (empty) {
			log.info("FIND A BLEND OF COFFEE ! ! ! - coffee {}", coffeeChoose.getCoffee());
			return getBlend();
		} else {
			log.info("Informazioni mancanti per il calcolo . . .");
			return null;
		}
	}

	public String getBlend() {
		return new StringBuilder("Miscela percentuali Arabica : ").append(coffeeChoose.getPercentageArabica())
				.append("%").append(" Robusta : ").append(coffeeChoose.getPercentageRobusta()).append("%").toString();
	}
}
