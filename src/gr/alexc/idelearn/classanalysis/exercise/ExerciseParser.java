package gr.alexc.idelearn.classanalysis.exercise;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;

import gr.alexc.idelearn.classanalysis.exercise.domain.Exercise;

public class ExerciseParser {
	
	private ObjectMapper mapper;

    public ExerciseParser() {
    	mapper = new ObjectMapper();
    	mapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
    }

    public Exercise parseExercise(URI fileUri) throws IOException {
        return parseExercise(new File(fileUri));
    }

    public Exercise parseExercise(String filePath) throws IOException {
        return parseExercise(new File(filePath));
    }

    public Exercise parseExercise(File file) throws IOException {
        return mapper.readValue(file, Exercise.class);
    }
    
    public Exercise parseExercise(InputStream stream) throws IOException {
    	return mapper.readValue(stream, Exercise.class);
    }
    
    public Exercise parseExercise(byte[] bytes) throws IOException {
    	return mapper.readValue(bytes, Exercise.class);
    }
    
}
