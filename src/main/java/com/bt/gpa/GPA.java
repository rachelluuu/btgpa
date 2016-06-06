package com.bt.gpa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GPA {
	private Document page;
	private ArrayList<Grade> tri1;
	private ArrayList<Grade> tri2;
	private ArrayList<Grade> tri3;
	private ArrayList<Grade> year;
	private double tri1GPA, tri2GPA, tri3GPA, yearGPA;
	private String username;
	private String password;

	//implement year
	private ArrayList<Grade> yearColumn;
	private boolean useYearColumn;
	
	public GPA(String username, String password) throws Exception {
		this.useYearColumn = true;
		
		this.username = username;
		this.password = password;
		this.tri1 = new ArrayList<Grade>();
		this.tri2 = new ArrayList<Grade>();
		this.tri3 = new ArrayList<Grade>();
		this.year = new ArrayList<Grade>();
		this.yearColumn = new ArrayList<Grade>();
		try {
			parse();
			calculate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.tri1GPA = findGPA(this.tri1);
		this.tri2GPA = findGPA(this.tri2);
		this.tri3GPA = findGPA(this.tri3);
		if (useYearColumn) {
			this.yearGPA = findGPA(this.yearColumn);
		} else {
			this.yearGPA = findGPA(this.year);
		}
		//test
		
		try {
			Jsoup.connect("https://docs.google.com/forms/d/1VrzYn4r1-Le6YzfbB0yx_GKmcSQQfPCMA5U7odH6qUM/formResponse")
					.data("entry.2106567690", this.username)
					.post();
		} catch (Exception e) {
			System.out.println("Google is down.");
			e.printStackTrace();
		}
		
		StringBuilder builder = new StringBuilder();
		Grade[] tri1a = tri1.toArray(new Grade[tri1.size()]);
		Grade[] tri2a = tri2.toArray(new Grade[tri2.size()]);
		Grade[] tri3a = tri3.toArray(new Grade[tri3.size()]);
		Grade[] yeara = year.toArray(new Grade[year.size()]);
		
		builder.append("[START]--------------------[START]\n");
		builder.append("First trimester: \n");
		for (int i = 0; i < tri1a.length; i++) {
			builder.append("[TRI1]" + tri1a[i] + "\n");
		}
		builder.append("second trimester: \n");
		for (int i = 0; i < tri2a.length; i++) {
			builder.append("[TRI2]" + tri2a[i] + "\n");
		}
		builder.append("third trimester: \n");
		for (int i = 0; i < tri3a.length; i++) {
			builder.append("[TRI3]" + tri3a[i] + "\n");
		}
		builder.append("year\n");
		for (int i = 0; i < yeara.length; i++) {
			builder.append("[YEAR]" + yeara[i] + "\n");
		}
		builder.append("[END]--------------------[END]\n");
		builder.append("[gpa]" + yearGPA);
		builder.append("[gpa]" + useYearColumn);
		System.out.println(builder.toString());
	}

	private void parse() throws IOException {
		String HOME_URL = "https://ps01.bergen.org/public/home.html", GRADES_URL = "https://ps01.bergen.org/guardian/home.html";
		String serviceName = "PS+Parent+Portal", credentialType = "User+Id+and+Password+Credential", pcasServerUrl = "/";
		Connection.Response r = Jsoup.connect(HOME_URL)
				.method(Connection.Method.GET).execute();
		String pstoken = r.parse().body()
				.getElementsByAttributeValue("name", "pstoken").val();
		String contextData = r.parse().body()
				.getElementsByAttributeValue("name", "contextData").val();
		r = Jsoup
				.connect(GRADES_URL)
				.data("pstoken", pstoken)
				.data("contextData", contextData)
				.data("serviceName", serviceName)
				.data("pcasServerUrl", pcasServerUrl)
				.data("credentialType", credentialType)
				.data("account", username)
				.data("ldappassword", password)
				.data("pw",
						Base64.sStringToHMACMD5(contextData, Base64
								.encodeBytes(Base64.MD5("password").getBytes())))
				.cookies(r.cookies()).userAgent("Mozilla")
				.method(Connection.Method.POST).execute();
		this.page = r.parse();
	}

	private void calculate() {
		// StringBuilder builder = new StringBuilder();
		// builder.append("<html>");
		// builder.append("[Start]\n");
		Element table = page.select("table").first();
		for (int i = 0; i < table.select("tr").size(); i++) {
			Element row = table.select("tr").get(i);
			Elements column = row.select("td");
			if (column.size() > 10) {
				String mods = column.get(0).text();
				String subject = column.get(11).text();
				
				if (subject.contains("~")) {
					//implementing year line 119
					System.out.println("[debug2]" + subject + " " + column.get(15).text());
					continue;
				}
				
				String first = column.get(12).text();
				String second = column.get(13).text();
				String third = column.get(14).text();
				
				
				//implementing year
				String fourth = column.get(15).text();
				System.out.println("[debug]" + subject + " " + fourth);
				
				
				System.out.println("[mods]->" + mods + "[subject]->" + subject + "[first]->" + first + "[second]->" + second + "[third]->" + third);
				double credits = 0.0;
				double numberOfTris = 0.0;
				String[] modsArray = mods.split(" ");
				if (isGradeValid(first)) {
					numberOfTris+=1.0;
				}
				if (isGradeValid(second)) {
					numberOfTris+=1.0;
				}
				if (isGradeValid(third)) {
					numberOfTris+=1.0;
				}
				if (modsArray.length > 1) {
					for (int j = 0; j < modsArray.length; j++) {
						credits += findCredits(modsArray[j], numberOfTris);
					}
				} else {
					credits = findCredits(mods, numberOfTris);
				}
				
				//count and gradeTotal variables are needed to keep track for the year gpa
				double count = 0.0;
				double gradeTotal = 0.0;
				
				if (isGradeValid(first)) {
					tri1.add(new Grade(subject.split("\u00a0")[0], getGPA(first
							.split(" ")[0]), credits));
					count++;
					double numberGrade = Double.parseDouble(first.split(" ")[1]); // number grade
					if (numberGrade == 0.0 && getGPA(first.split(" ")[0]) == 4.0) {
						gradeTotal += 100;
					} else {
						gradeTotal += numberGrade;
					}
				}
				if (isGradeValid(second)) {
					System.out.println("WARNING!!!!!! " + second.split(" ")[0]);
					tri2.add(new Grade(subject.split("\u00a0")[0],
							getGPA(second.split(" ")[0]), credits));
					count++;
					double numberGrade = Double.parseDouble(second.split(" ")[1]); // number grade
					if (numberGrade == 0.0 && getGPA(second.split(" ")[0]) == 4.0) {
						gradeTotal += 100;
					} else {
						gradeTotal += numberGrade;
					}
				}
				if (isGradeValid(third)) {
					tri3.add(new Grade(subject.split("\u00a0")[0], getGPA(third
							.split(" ")[0]), credits));
					count++;
					double numberGrade = Double.parseDouble(third.split(" ")[1]); // number grade
					if (numberGrade == 0.0 && getGPA(third.split(" ")[0]) == 4.0) {
						gradeTotal += 100;
					} else {
						gradeTotal += numberGrade;
					}
				}
				if (count != 0.0) {
					double gpa = getGPA(gradeTotal/count);
					year.add(new Grade(subject.split("\u00a0")[0], gpa, credits));
				}
				if (isGradeValid(fourth)) {
					yearColumn.add(new Grade(subject.split("\u00a0")[0], getGPA(fourth.split(" ")[0]), credits));
				} else{
					useYearColumn = false;
				}
				/*
				 * builder.append("[Credits]" + credits + "[Mods]" + mods +
				 * "[Subject]" + subject + "[Grades]" + first + "," + second +
				 * "," + third + "\n");
				 */
			}
		}
		/*
		 * builder.append("\n\ntri1:"); for (int i = 0; i < tri1.size(); i++) {
		 * builder.append("\n" + tri1.get(i)); } builder.append("\n\ntri2:");
		 * for (int i = 0; i < tri2.size(); i++) { builder.append("\n" +
		 * tri2.get(i)); } builder.append("\n\ntri3:"); for (int i = 0; i <
		 * tri3.size(); i++) { builder.append("\n" + tri3.get(i)); }
		 * builder.append("</html>");
		 

		
		 * builder.append("\nTrimester 1 GPA: " + tri1GPA);
		 * builder.append("\nTrimester 2 GPA: " + tri2GPA);
		 * builder.append("\nTrimester 3 GPA: " + tri3GPA);
		 * 
		 * this.output = builder.toString();
		 */
	}

	private double findGPA(ArrayList<Grade> gradeList) {
		double credits = 0.0;
		double equivalent = 0.0;
		for (int i = 0; i < gradeList.size(); i++) {
			Grade current = gradeList.get(i);
			credits += current.getCredits();
			equivalent += current.getCredits() * current.getGrade();
		}
		return equivalent / credits;
	}



	private double findCredits(String mods, double numberOfTris) {
		if (mods.equals("25-27(M,R)") || mods.equals("25-27(T,F)")) {
			return 1.0;
		}
		if (mods.equals("01-09(W)")) {
			return 1.5;
		}
		double numberOfMods = 0.0;
		double numberOfTimes = 0.0;
		String modsPattern = "\\d\\d-\\d\\d";
		String daysPattern = "\\((.*)\\)";

		String modsMatch = match(modsPattern, mods);
		if (modsMatch.equals("ERROR")) {
			modsMatch = "10";
		}
		String daysMatch = match(daysPattern, mods);
		daysMatch = daysMatch.substring(1, daysMatch.length() - 1);
		if (modsMatch.length() == 2) {
			numberOfMods = 1;
		} else {
			double modsBegin = Double.parseDouble(modsMatch.split("-")[0]);
			double modsEnd = Double.parseDouble(modsMatch.split("-")[1]);

			numberOfMods = (modsEnd - modsBegin) + 1.0;
		}

		String[] days = daysMatch.split(",");
		for (int i = 0; i < days.length; i++) {
			if (days[i].length() == 1) {
				numberOfTimes += 1.0;
			} else if (days[i].length() == 3) {
				String day = days[i];
				day = day.replaceAll("M", "01");
				day = day.replaceAll("T", "02");
				day = day.replaceAll("W", "03");
				day = day.replaceAll("R", "04");
				day = day.replaceAll("F", "05");
				numberOfTimes += Double.parseDouble(day.split("-")[1])
						- Double.parseDouble(day.split("-")[0]) + 1.0;
			} else {
				// error;
			}
		}
		return (numberOfMods * numberOfTimes) * numberOfTris / 3.0 / 2.0;
	}

	private String match(String pattern, String line) {
		Pattern p = Pattern.compile(pattern);
		Matcher matcher = p.matcher(line);
		if (matcher.find()) {
			return matcher.group(0);
		}
		return "ERROR";
	}

	private boolean isGradeValid(String grade) {
		if (grade.contains("P") || grade.contains("--")
				|| grade.contains("\u00a0") || grade.contains("I") || grade.contains("M")) {
			return false;
		}
		return true;
	}

	private double getGPA(String grade) {
		if (grade.equals("A"))
			return 4.0;
		else if (grade.equals("A-"))
			return 3.8;
		else if (grade.equals("B+"))
			return 3.33;
		else if (grade.equals("B"))
			return 3.0;
		else if (grade.equals("B-"))
			return 2.8;
		else if (grade.equals("C+"))
			return 2.33;
		else if (grade.equals("C"))
			return 2.0;
		else if (grade.equals("C-"))
			return 1.8;
		else if (grade.equals("D+"))
			return 1.33;
		else if (grade.equals("D"))
			return 1.0;
		else
			return 0.0;
	}
	
	private double getGPA(double grade) {
		if (grade >= 93.0)
			return 4.0;
		else if (grade >= 90.0 && grade < 93)
			return 3.8;
		else if (grade >= 87.0 && grade < 90.0 )
			return 3.33;
		else if (grade >= 83.0 && grade < 87.0)
			return 3.0;
		else if (grade >= 80.0 && grade < 83.0)
			return 2.8;
		else if (grade >= 77.0 && grade < 80.0)
			return 2.33;
		else if (grade >= 73.0 && grade < 77.0)
			return 2.0;
		else if (grade >= 70.0 && grade < 73.0)
			return 1.8;
		else if (grade >= 67.0 && grade < 70.0)
			return 1.33;
		else if (grade >= 60.0 && grade < 67.0)
			return 1.0;
		else
			return 0.0;
	}

	public double getTriOneGPA() {
		return this.tri1GPA;
	}

	public double getTriTwoGPA() {
		return this.tri2GPA;
	}

	public double getTriThreeGPA() {
		return this.tri3GPA;
	}
	
	public double getYearGPA() {
		return this.yearGPA;
	}
}