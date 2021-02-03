package it.icon.cornercoffe.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionPriorityPOJO implements Comparable<QuestionPriorityPOJO> {

	private QuestionPOJO question;

	private int value;

	@Override
	public int compareTo(QuestionPriorityPOJO questionPriorityPOJO) {
		return questionPriorityPOJO.getValue();
	}
}
