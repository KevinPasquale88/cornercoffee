package it.icon.cornercoffe.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.icon.cornercoffe.pojo.AnswerPOJO;
import it.icon.cornercoffe.pojo.CoffeeChoose;
import it.icon.cornercoffe.pojo.CoffeeType;
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

	@GetMapping("/Description")
	public ResponseEntity<String> getDescription(@RequestParam("coffeeName") String coffeeName) {
		log.info("METHOD getDescription - PATH GET /Description ATTRIBUTE - {}", coffeeName);
		StringBuilder contentBuilder = new StringBuilder();
		String filePath = "src/main/resources/coffeeDescription/"
				+ StringUtils.capitalize(StringUtils.lowerCase(coffeeName)) + ".txt";
		try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
			stream.forEach(s -> contentBuilder.append(s).append("\n"));
		} catch (IOException e) {
			log.error("Error on read {}", filePath);
			return new ResponseEntity<String>("Error on read description about " + coffeeName, HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok(contentBuilder.toString());
	}

	@GetMapping("/GetCoffee")
	public ResponseEntity<CoffeeChoose> getCoffee(Double acidity, Double aroma, Double bitter, Double body,
			Double sweetness) {
		KieSession kieSession = kContainer.newKieSession();
		CoffeeChoose coffeeChoose = new CoffeeChoose();
		kieSession.setGlobal("coffeeChoose", coffeeChoose);
		kieSession.insert(CoffeeType.builder().acidity(acidity).aroma(aroma).bitter(bitter).body(body)
				.sweetness(sweetness).build());
		kieSession.fireAllRules();
		kieSession.dispose();
		return ResponseEntity.ok(coffeeChoose);
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
			@RequestParam("answer") String answer) {
		log.info("METHOD submitAnswer - questions {} - answer {}", question, answer);
		Optional<QuestionPOJO> questionOption = questions.stream()
				.filter(elem -> StringUtils.equalsIgnoreCase(elem.getQuestion(), question)).findAny();
		if (!questionOption.isPresent()) {
			log.error("ERROR ON QUESTION TO PASS . . . ");
			return new ResponseEntity<String>("ERROR ON QUESTION TO PASS . . . ", HttpStatus.UNPROCESSABLE_ENTITY);
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
				return new ResponseEntity<String>("ERROR ON ANSWER TO PASS . . . ", HttpStatus.UNPROCESSABLE_ENTITY);
			} else {
				log.info("Also Answer finded . . .");
				answers.put(elemQuestion.getQuestion(), answer);
				//need to remove questionPojo
				questions.remove(questionOption.get());
			}
		}
		return ResponseEntity.ok("OK");
	}

	@GetMapping("/NextQuestion")
	public ResponseEntity<QuestionPOJO> getNextQuestion() {
		log.info("METHOD getNextQuestion - questions size {}", questions.size());
		if (!questions.isEmpty()) {
			Collections.shuffle(questions);
			QuestionPOJO nextQuestion = questions.get(0);
			log.info("nextQuestion {}", nextQuestion);
			return ResponseEntity.ok(nextQuestion);
		} else {
			log.error("FINISH questions . . .");
			return new ResponseEntity<QuestionPOJO>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/CoffeeChoose")
	public ResponseEntity<String> getCoffeeChoose() {
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
			return ResponseEntity.ok(coffeeChoose.getCoffee());
		} else {
			log.info("Informazioni mancanti per il calcolo . . .");
			return new ResponseEntity<String>("Informazioni mancanti per il calcolo ", HttpStatus.NOT_FOUND);
		}
	}

}
