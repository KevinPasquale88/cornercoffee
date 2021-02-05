package it.icon.cornercoffe.configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.icon.cornercoffe.pojo.QuestionPOJO;

@Configuration
public class LoaderQuestions {

	@Bean
	@Primary
	public List<QuestionPOJO> questions() throws IOException {
		return new ObjectMapper().readValue(
				new String(Files.readAllBytes(Paths.get("src/main/resources/questions.json"))),
				new TypeReference<List<QuestionPOJO>>() {
				});
	}
	
}
