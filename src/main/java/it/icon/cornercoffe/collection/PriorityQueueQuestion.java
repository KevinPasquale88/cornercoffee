package it.icon.cornercoffe.collection;

import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.icon.cornercoffe.pojo.QuestionPOJO;
import it.icon.cornercoffe.pojo.QuestionPriorityPOJO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PriorityQueueQuestion {

	PriorityQueue<QuestionPriorityPOJO> queue = new PriorityQueue<QuestionPriorityPOJO>();

	public PriorityQueueQuestion(@Autowired List<QuestionPOJO> questions) {
		questions.stream().forEach(elem -> queue.add(new QuestionPriorityPOJO(elem, questions.indexOf(elem) + 1)));
	}

	public List<QuestionPOJO> getQuestionsRemain() {
		log.info("Get Questions Remain from Queue size {}" + queue.size());
		return queue.stream().map(elem -> elem.getQuestion()).collect(Collectors.toList());
	}

	public boolean isEmpty() {
		log.info("PriorityQueue isEmpty {}" + queue.isEmpty());
		return this.queue.isEmpty();
	}

	public QuestionPOJO getNextQuestion(QuestionPOJO questionPOJO, String answer) {
		if (StringUtils.isNotBlank(answer) && questionPOJO != null) {
			calculateWeigthQueue(questionPOJO, answer);
		}
		return queue.peek().getQuestion();
	}

	public int size() {
		return this.queue.size();
	}

	private void calculateWeigthQueue(QuestionPOJO question, String answer) {
		log.info("START method calculateWeigthQueue - Remove question {} - answer {}", question, answer);
		this.queue.poll();
	}
}
