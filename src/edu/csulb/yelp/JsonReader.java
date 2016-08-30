package edu.csulb.yelp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonReader {

	public static JSONArray readFile(String path) {

		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String sCurrentLine;
			JSONArray json = new JSONArray();
			while ((sCurrentLine = br.readLine()) != null) {
				json.put(new JSONObject(sCurrentLine));
			}
			return json;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String readFileAsString(String path) {
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			return new String(encoded);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}