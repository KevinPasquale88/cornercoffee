package it.icon.cornercoffe.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.icon.cornercoffe.component.CoffeeComponent;
import it.icon.cornercoffe.pojo.CoffeeType;
import it.icon.cornercoffe.pojo.JSONString;
import it.icon.cornercoffe.pojo.QuestionPOJO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class CornercoffeeController {

	@Autowired
	Map<String, CoffeeType> coffeeTable;

	@Autowired
	KieContainer kContainer;

	@Autowired
	List<QuestionPOJO> questions;

	Map<String, String> answers = new HashMap<>();

	@Autowired
	CoffeeComponent coffeeComponent;

	@GetMapping("/Description")
	public ResponseEntity<String> getDescription(@RequestParam("coffeeName") String coffeeName) throws JsonProcessingException {
		log.info("METHOD getDescription - PATH GET /Description ATTRIBUTE - {}", coffeeName);
		StringBuilder contentBuilder = new StringBuilder();
		String filePath = "src/main/resources/coffeeDescription/"
				+ StringUtils.capitalize(StringUtils.lowerCase(coffeeName)) + ".txt";
		try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
			stream.forEach(s -> contentBuilder.append(s).append("\n"));
		} catch (IOException e) {
			log.error("Error on read {}", filePath);
			return new ResponseEntity<String>(new ObjectMapper().writeValueAsString(JSONString.builder().result("Error on read description about " + coffeeName).build()), HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok(new ObjectMapper().writeValueAsString(JSONString.builder().result(contentBuilder.toString()).build()));
	}

	@GetMapping("/AvailabilityCoffee")
	public ResponseEntity<Map<String, CoffeeType>> getAvailabilityCoffee() {
		log.info("METHOD getAvailabilityCoffee - coffeTable size {}", coffeeTable.size());
		return ResponseEntity.ok(coffeeTable);
	}

	@GetMapping("/QuestionsList")
	public ResponseEntity<List<QuestionPOJO>> getQuestions() {
		log.info("METHOD getQuestions - questions size {}", questions.size());
		return ResponseEntity.ok(questions);
	}

	@PostMapping("/SubmitAnswer")
	public ResponseEntity<String> submitAnswer(@RequestParam("question") String question,
			@RequestParam("answer") String answer) throws JsonProcessingException {
		
		log.info("METHOD submitAnswer - questions {} - answer {}", question, answer);
		Optional<QuestionPOJO> questionOption = questions.stream()
				.filter(elem -> StringUtils.equalsIgnoreCase(elem.getQuestion(), question)).findAny();
		if (!questionOption.isPresent()) {
			log.error("ERROR ON QUESTION TO PASS . . . ");
			return new ResponseEntity<String>(new ObjectMapper().writeValueAsString(JSONString.builder().result("ERROR ON QUESTION TO PASS . . . ").build()), HttpStatus.NOT_FOUND);
		} else {
			log.info("Question finded . . .");
			QuestionPOJO elemQuestion = questionOption.get();
			boolean find = false;
			for (int i = 0; i < elemQuestion.getAnswers().length; i++) {
				if (StringUtils.equalsIgnoreCase(elemQuestion.getAnswers()[i], answer)) {
					find = true;
					break;
				}
			}
			if (!find) {
				log.error("ERROR ON ANSWER TO PASS . . . ");
				return new ResponseEntity<String>(new ObjectMapper().writeValueAsString(JSONString.builder().result("ERROR ON ANSWER TO PASS . . . ").build()), HttpStatus.NOT_FOUND);
			} else {
				log.info("Also Answer finded . . .");
				answers.put(elemQuestion.getQuestion(), answer);
				// need to remove questionPojo
				questions.remove(questionOption.get());
			}
		}
		String coffeeChoose = coffeeComponent.getCoffeeChoose(answers);

		if (StringUtils.isNotBlank(coffeeChoose)) {
			return ResponseEntity.ok(new ObjectMapper().writeValueAsString(JSONString.builder().result(coffeeChoose).build()));
		} else {
			return ResponseEntity.ok(new ObjectMapper().writeValueAsString(JSONString.builder().result("Nothing.").build()));
		}
	}

	@GetMapping("/Question")
	public ResponseEntity<QuestionPOJO> getQuestion() {
		QuestionPOJO nextQuestionElement = coffeeComponent.getNextQuestion(questions);
		if (nextQuestionElement != null) {
			return ResponseEntity.ok(nextQuestionElement);
		} else {
			log.error("Error on build response . . .");
			return new ResponseEntity<QuestionPOJO>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
}
