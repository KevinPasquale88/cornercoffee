package it.icon.cornercoffe.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class QuestionPOJO {

	private String question;
	
	private String[] answers;
}
