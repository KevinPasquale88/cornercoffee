package it.icon.cornercoffe.component;

import java.util.List;

import org.springframework.stereotype.Component;

import it.icon.cornercoffe.pojo.CoffeeType;
import it.icon.cornercoffe.pojo.QuestionPOJO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class QuestionComponent {

	private static final String ANSWER = "Not Found Answer ...";

	CoffeeType coffeeType = new CoffeeType();

	public CoffeeType checkAnswers(String question, String answer) {
		log.info("METHOD checkAnswers - status coffeeType {} - question {} - answer {}", coffeeType.toString(),
				question, answer);
		switch (question) {
		case "Bevi il caffè amaro":
			switch (answer) {
			case "Si":
				coffeeType.addBitter(20);
				break;
			case "No":
				coffeeType.addBitter(40);
				break;
			default:
				log.error(ANSWER);
				break;
			}
			break;
		case "Hai bisogno di energia":
			switch (answer) {
			case "Si":
				coffeeType.addBody(40);
				break;
			case "No":
				coffeeType.addBody(10);
				break;
			default:
				log.error(ANSWER);
				break;
			}
			break;
		case "Quanti caffè bevi normalmente in un giorno":
			switch (answer) {
			case "1-2":
				coffeeType.addBody(50);
				break;
			case "3-4":
				coffeeType.addBody(30);
				break;
			case "5 o +":
				coffeeType.addBody(10);
				break;
			default:
				log.error(ANSWER);
				break;
			}
			break;
		case "In quale momento della giornata ci troviamo":
			switch (answer) {
			case "Mattina":
				coffeeType.addSweetness(40);
				break;
			case "Pomeriggio":
				coffeeType.addSweetness(30);
				break;
			case "Sera":
				coffeeType.addSweetness(20);
				break;
			default:
				log.error(ANSWER);
				break;
			}
			break;
		case "Ti piace molto profumato":
			switch (answer) {
			case "Si":
				coffeeType.addAroma(50);
				break;
			case "No":
				coffeeType.addAroma(25);
				break;
			default:
				log.error(ANSWER);
				break;
			}
			break;
		case "Ti piace più persistente":
			switch (answer) {
			case "Si":
				coffeeType.addAroma(40);
				break;
			case "No":
				coffeeType.addAroma(20);
				break;
			default:
				log.error(ANSWER);
				break;
			}
			break;
		case "Lo accompagni con un dolce":
			switch (answer) {
			case "Si":
				coffeeType.addSweetness(20);
				break;
			case "No":
				coffeeType.addSweetness(40);
				break;
			default:
				log.error(ANSWER);
				break;
			}
			break;
		case "Bevi con il latte":
			switch (answer) {
			case "Si":
				coffeeType.addAcidity(50);
				break;
			case "No":
				coffeeType.addAcidity(10);
				break;
			default:
				log.error(ANSWER);
				break;
			}
			break;
		case "In che stato si trova il caffè":
			switch (answer) {
			case "Grani":
				coffeeType.addAcidity(40);
				break;
			case "Macinato":
				coffeeType.addAcidity(10);
				break;
			default:
				log.error(ANSWER);
				break;
			}
			break;
		case "Quale strumento utilizzi per il caffè":
			switch (answer) {
			case "Moka":
				coffeeType.addBitter(10);
				break;
			case "Espresso":
				coffeeType.addBitter(30);
				break;
			case "Filtro":
				coffeeType.addBitter(20);
				break;
			default:
				log.error(ANSWER);
				break;
			}
			break;
		default:
			log.error("Not Found Question ...");
			break;
		}
		log.info("Status coffeeType {}", coffeeType.toString());
		return coffeeType;
	}
	
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
}
