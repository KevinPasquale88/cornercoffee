package it.icon.cornercoffe.component;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.icon.cornercoffe.collection.PriorityQueueQuestion;
import it.icon.cornercoffe.pojo.QuestionPOJO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class QuestionManageComponent {

	@Autowired
	PriorityQueueQuestion priorityQueueQuestion;

	QuestionPOJO elemQuestion = null;

	String answer = null;

	public QuestionPOJO getNextQuestion() {
		log.info("METHOD getNextQuestion - questions size {}", priorityQueueQuestion.size());
		if (!priorityQueueQuestion.isEmpty()) {
			QuestionPOJO nextQuestion = priorityQueueQuestion.getNextQuestion(elemQuestion, answer);
			log.info("nextQuestion {}", nextQuestion);
			return nextQuestion;
		} else {
			log.error("FINISH questions . . .");
			return null;
		}
	}

	public List<QuestionPOJO> getQuestionsRemain() {
		return this.priorityQueueQuestion.getQuestionsRemain();
	}

	public boolean isEmpty() {
		return this.priorityQueueQuestion.isEmpty();
	}

	public void saveAnswer(QuestionPOJO questionPOJO, String answer) {
		log.info("Save Last Question {} with answer {}", questionPOJO, answer);
		this.elemQuestion = questionPOJO;
		this.answer = answer;
	}
}
