package com.mdd.test;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.collection.IsIn;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.text.StringSubstitutor;
import com.moandjiezana.toml.Toml;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Test {

	Map<String, Object> paramsMap = new HashMap();
	ScriptEngine engine;
	StringSubstitutor substitutor;
	private static final boolean IS_WINDOWS = System.getProperty( "os.name" ).contains( "indow" );

	public Test(String configFile) throws Exception {
		try {
			String osAppropriatePath = IS_WINDOWS ? configFile.substring(1) : configFile;
			Path path = Paths.get(osAppropriatePath);
			String paramsFileContent = new String(Files.readAllBytes(path));
			paramsMap = unnestTomlMap(new Toml().read(paramsFileContent).toMap());
			substitutor = new StringSubstitutor(paramsMap);
			ScriptEngineManager factory = new ScriptEngineManager();
			engine = factory.getEngineByName("JavaScript");
		} catch(Exception exception) {
			System.out.println("An Error occured, possible during reading the TOML config file: " + exception);
			throw exception;
		}
	}

	public void test() throws Exception {
		try {
			Response roundtrip1 = RestAssured.given()
					.auth().basic(substitutor.replace("${B.username}"), substitutor.replace("${B.password}"))
				.when()
					.get(substitutor.replace("${B.path}") + substitutor.replace("/hello"))
				.then()
					.assertThat()
					    .statusCode(IsIn.isIn(Arrays.asList(200)));
		} catch (Exception exception) {
			System.out.println("An error occured during evaluating the communication with testReceiver: ");
			exception.printStackTrace();
			throw exception;
		}
	}

	public static Map<String, Object> unnestTomlMap(Map<String, Object> tomlMap) {
		Map<String, Object> resultMap = new HashMap<>();
		for (Map.Entry<String, Object> entry : tomlMap.entrySet()){
			if(entry.getValue() instanceof Map){
				Map<String, Object> innerMap = (Map<String, Object>) entry.getValue();
				for (Map.Entry<String, Object> nestedEntry : innerMap.entrySet()){
					resultMap.put(entry.getKey() + "." + nestedEntry.getKey(), nestedEntry.getValue());
				}
			} else {
				resultMap.put(entry.getKey(), entry.getValue());
			}
		}
		return resultMap;
	}
}
