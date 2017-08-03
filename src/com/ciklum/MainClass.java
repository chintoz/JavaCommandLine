package com.ciklum;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

/**
 * MainClass to calculate GitHub User's favourite programming language
 * 
 * @author Jacinto J. Mena Lomena
 *
 */
public class MainClass {

	private static final String GITHUB_API_USER_URL = "https://api.github.com/users/";
	private static final String GITHUB_API_USER_REPOS = "/repos";

	/**
	 * Main method to show the favourite programming language for a GitHub User.
	 * 
	 * @param args
	 *            Input GitHub User Name
	 * @throws IOException
	 *             When I/O an error occurs
	 */
	public static void main(String[] args) throws IOException {

		// Variable declaration
		String userName = null;
		String encodedUserName = null;
		String reposContent = null;
		HashMap<String, Integer> count = new HashMap<String, Integer>();
		Integer maxCount = null;
		List<String> maxLanguages = null;

		// Check input parameters
		if (args == null || args.length != 1 || args[0] == null || args[0].trim().isEmpty()) {
			System.out.println("Check input parameters: <userName>");
			return;
		}

		// Assign input value to a local variable
		userName = args[0].trim();
		encodedUserName = URLEncoder.encode(userName, "UTF-8");

		// Do a REST request to get information about repositories
		try {
			reposContent = getRestContent(encodedUserName);
		} catch (FileNotFoundException e) {
			System.out.println("[" + userName + "] doesn't exist");
			return;
		} catch (IOException e) {
			System.out.println("Error on Github Call for user [" + userName + "]: " + e.getMessage());
			return;
		}

		// Count language count using Regular Expression
		Pattern pattern = Pattern.compile("\"language\":\"([^\"]*)\"");
		Matcher matcher = pattern.matcher(reposContent);
		while (matcher.find()) {
			if (count.get(matcher.group(1)) == null) {
				count.put(matcher.group(1), Integer.valueOf(1));
			} else {
				count.put(matcher.group(1), count.get(matcher.group(1)) + 1);
			}
		}

		// If user doesn't have a repository or doesn't exist show an error message
		if (count.isEmpty()) {
			System.out.println("[" + userName + "] doesn't have a repository");
			return;
		}

		// Calculate most used language
		for (Entry<String, Integer> entry : count.entrySet()) {

			// First step, the first element is the max at this moment
			if (maxCount == null) {
				maxCount = entry.getValue();
				maxLanguages = new ArrayList<>();
				maxLanguages.add(entry.getKey());
				continue;
			}

			// If this element has the same count then other favourite language
			if (maxCount.intValue() == entry.getValue().intValue()) {
				maxLanguages.add(entry.getKey());
			}

			// New favourite language beacuse it has more occurrences
			if (maxCount.intValue() < entry.getValue().intValue()) {
				maxCount = entry.getValue();
				maxLanguages.clear();
				maxLanguages.add(entry.getKey());
			}
		}

		System.out.println("[" + userName + "] favourite language/s: " + maxLanguages);

	}

	/**
	 * Method to do a REST request to get repositories information about an
	 * user.
	 * 
	 * @param userName
	 *            GitHub userName
	 * @return String with userContent (it's a JSON String) 
	 * @throws IOException
	 *             When I/O an error occurs
	 */
	private static String getRestContent(String userName) throws IOException {

		// Variable declaration
		URL url = null;
		HttpsURLConnection con = null;

		// Call URL to get content
		url = new URL(GITHUB_API_USER_URL + userName + GITHUB_API_USER_REPOS);
		con = (HttpsURLConnection) url.openConnection();

		// Get response from input
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// Return response content as String
		return response.toString();

	}
}
