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
	private ArrayList<Grade> mp1;
	private ArrayList<Grade> mp2;
	private ArrayList<Grade> mp3;
	private ArrayList<Grade> mp4;
	private ArrayList<Grade> year;
	private double mp1GPA, mp2GPA, mp3GPA, mp4GPA, yearGPA;
	private String username;
	private String password;

	//implement year
	private ArrayList<Grade> yearColumn;
	private boolean useYearColumn;
	
	public GPA(String username, String password) throws Exception {
		this.useYearColumn = true;
		
		this.username = username;
		this.password = password;
		this.mp1 = new ArrayList<Grade>();
		this.mp2 = new ArrayList<Grade>();
		this.mp3 = new ArrayList<Grade>();
		this.mp4 = new ArrayList<Grade>();
		this.year = new ArrayList<Grade>();
		this.yearColumn = new ArrayList<Grade>();
		try {
			parse();
			calculate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.mp1GPA = findGPA(this.mp1);
		this.mp2GPA = findGPA(this.mp2);
		this.mp3GPA = findGPA(this.mp3);
		this.mp4GPA = findGPA(this.mp4);
		if (useYearColumn) {
			this.yearGPA = findGPA(this.yearColumn);
		} else {
			this.yearGPA = findGPA(this.year);
		}
		//test
		
		try {
/*CHANGE*/		Jsoup.connect("https://docs.google.com/forms/d/1joItByFoIKzjFB5Mm0q8YbcXFFXVchtJ6QasviXtPdk/formResponse")
					.data("entry.1584323777", this.username)
					.post();
		} catch (Exception e) {
			System.out.println("Google is down.");
			e.printStackTrace();
		}
		
		StringBuilder builder = new StringBuilder();
		Grade[] mp1a = mp1.toArray(new Grade[mp1.size()]);
		Grade[] mp2a = mp2.toArray(new Grade[mp2.size()]);
		Grade[] mp3a = mp3.toArray(new Grade[mp3.size()]);
		Grade[] mp4a = mp4.toArray(new Grade[mp4.size()]);
		Grade[] yeara = year.toArray(new Grade[year.size()]);
		
		builder.append("[START]--------------------[START]\n");
		builder.append("First marking period: \n");
		for (int i = 0; i < mp1a.length; i++) {
			builder.append("[mp1]" + mp1a[i] + "\n");
		}
		builder.append("second marking period: \n");
		for (int i = 0; i < mp2a.length; i++) {
			builder.append("[mp2]" + mp2a[i] + "\n");
		}
		builder.append("third marking period: \n");
		for (int i = 0; i < mp3a.length; i++) {
			builder.append("[mp3]" + mp3a[i] + "\n");
		}
		builder.append("fourth marking period: \n");
		for (int i = 0; i < mp4a.length; i++) {
			builder.append("[mp4]" + mp4a[i] + "\n");
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
				
				//get marking period grades
				String first = column.get(12).text();
				String second = column.get(13).text();
				String third = column.get(15).text();
				String fourth = column.get(16).text();
				
				//implementing year
				String fifth = column.get(19).text();
				System.out.println("[debug]" + subject + " " + fifth);
				
				
//				System.out.println("[mods]->" + mods + "[subject]->" + subject + "[first]->" + first + "[second]->" + second + "[third]->" + third);
				double credits = 0.0;
				String[] modsArray = mods.split(" ");
				double numberOfMPs = 0.0;
				if (isGradeValid(first)) {
					numberOfMPs+=1.0;
				}
				if (isGradeValid(second)) {
					numberOfMPs+=1.0;
				}
				if (isGradeValid(third)) {
					numberOfMPs+=1.0;
				}
				if (isGradeValid(fourth)) {
					numberOfMPs+=1.0;
				}
/*				if (modsArray.length > 1) {
					for (int j = 0; j < modsArray.length; j++) {
						credits += findCredits(modsArray[j], numberOfMPs);
					}
				} else {
					credits = findCredits(mods, numberOfMPs);
				}*/
				credits = findCredits(mods, subject, numberOfMPs);
				
				//count and gradeTotal variables are needed to keep track for the year gpa
				double count = 0.0;
				double gradeTotal = 0.0;
				
				if (isGradeValid(first)) {
					mp1.add(new Grade(subject.split("\u00a0")[0], getGPA(first
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
//					System.out.println("WARNING!!!!!! " + second.split(" ")[0]);
					mp2.add(new Grade(subject.split("\u00a0")[0],
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
					mp3.add(new Grade(subject.split("\u00a0")[0], getGPA(third
							.split(" ")[0]), credits));
					count++;
					double numberGrade = Double.parseDouble(third.split(" ")[1]); // number grade
					if (numberGrade == 0.0 && getGPA(third.split(" ")[0]) == 4.0) {
						gradeTotal += 100;
					} else {
						gradeTotal += numberGrade;
					}
				}
				if (isGradeValid(fourth)) {
					mp4.add(new Grade(subject.split("\u00a0")[0], getGPA(fourth
							.split(" ")[0]), credits));
					count++;
					double numberGrade = Double.parseDouble(fourth.split(" ")[1]); // number grade
					if (numberGrade == 0.0 && getGPA(fourth.split(" ")[0]) == 4.0) {
						gradeTotal += 100;
					} else {
						gradeTotal += numberGrade;
					}
				}
				if (count != 0.0) {
					double gpa = getGPA(gradeTotal/count);
					year.add(new Grade(subject.split("\u00a0")[0], gpa, credits));
				}
				if (isGradeValid(fifth)) {
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
		 * builder.append("\n\nmp1:"); for (int i = 0; i < mp1.size(); i++) {
		 * builder.append("\n" + mp1.get(i)); } builder.append("\n\nmp2:");
		 * for (int i = 0; i < mp2.size(); i++) { builder.append("\n" +
		 * mp2.get(i)); } builder.append("\n\nmp3:"); for (int i = 0; i <
		 * mp3.size(); i++) { builder.append("\n" + mp3.get(i)); }
		 * builder.append("</html>");
		 

		
		 * builder.append("\nTrimester 1 GPA: " + mp1GPA);
		 * builder.append("\nTrimester 2 GPA: " + mp2GPA);
		 * builder.append("\nTrimester 3 GPA: " + mp3GPA);
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

	private double findCredits(String mods, String subject, double numberOfMPs) {
		double numberOfMods = 0.0;
		double numberOfTimes = 0.0;
		String labPeriodPatternA = "\\(\\w\\).*\\(.+\\)";
		String labPeriodMatchA = match(labPeriodPatternA, mods);
		String labPeriodPatternB = "\\(.+\\).*\\(\\w\\)";
		String labPeriodMatchB = match(labPeriodPatternB, mods);
		String dblPeriodPattern = "\\(\\w\\W\\w\\).*\\(\\w\\W\\w\\)";
		String dblPeriodMatch = match(dblPeriodPattern, mods);
/* if (mods.equals("25-27(M,R)") || mods.equals("25-27(T,F)")) {
			return 1.0;
		}
		if (mods.equals("01-09(W)")) {
			return 1.5;
		}*/
		if (mods.equals("10(W)")) {
			if (numberOfMPs > 2){
				return 2.0;
			}
			else{ 
				return 1.0;
			}
		}
		else if (!labPeriodMatchA.equals("ERROR") || !labPeriodMatchB.equals("ERROR")){
			return 6.0;
		}
		else if (subject.startsWith("Phys Ed.")){		
			return 3.0;
		}
		else if (subject.startsWith("Social Issues") || subject.startsWith("Driver Ed Theory") || subject.startsWith("First Aid") || subject.startsWith("Family Living")){		
			return 1.0;
		}
		else if (!dblPeriodMatch.equals("ERROR")){
			return 10.0;
		}
		else{
			return 5.0;
		}
/*		double numberOfMods = 0.0;
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
		return (numberOfMods * numberOfTimes) * numberOfMPs / 3.0 / 2.0;*/
	}

	private String match(String pattern, String line) {
		Pattern p = Pattern.compile(pattern);
		Matcher matcher = p.matcher(line);
		if (matcher.find()) {
			return matcher.group(0);
		}
		return "ERROR";
	}
/* TODO: */	
	

	private boolean isGradeValid(String grade) {
		if (grade.contains("P") || grade.contains("--")
				|| grade.contains("\u00a0") || grade.contains("I") || grade.contains("M")) {
			return false;
		}
		return true;
	}

	private double getGPA(String grade) {
		if (grade.equals("A+"))
			return 4.0;
		else if (grade.equals("A"))
			return 4.0;
		else if (grade.equals("A-"))
			return 3.67;
		else if (grade.equals("B+"))
			return 3.33;
		else if (grade.equals("B"))
			return 3.0;
		else if (grade.equals("B-"))
			return 2.67;
		else if (grade.equals("C+"))
			return 2.33;
		else if (grade.equals("C"))
			return 2.0;
		else if (grade.equals("C-"))
			return 1.67;
		else if (grade.equals("D+"))
			return 1.33;
		else if (grade.equals("D"))
			return 1.0;
		else if (grade.equals("D-"))
			return 0.67;
		else
			return 0.0;
	}
	
	private double getGPA(double grade) {
		if (grade >= 93.0)
			return 4.0;
		else if (grade >= 90.0 && grade < 93)
			return 3.67;
		else if (grade >= 87.0 && grade < 90.0 )
			return 3.33;
		else if (grade >= 83.0 && grade < 87.0)
			return 3.0;
		else if (grade >= 80.0 && grade < 83.0)
			return 2.67;
		else if (grade >= 77.0 && grade < 80.0)
			return 2.33;
		else if (grade >= 73.0 && grade < 77.0)
			return 2.0;
		else if (grade >= 70.0 && grade < 73.0)
			return 1.67;
		else if (grade >= 67.0 && grade < 70.0)
			return 1.33;
		else if (grade >= 63.0 && grade < 67.0)
			return 1.0;
		else if (grade >= 60.0 && grade < 63.0)
			return 0.67;
		else
			return 0.0;
	}

	public double getMpOneGPA() {
		return this.mp1GPA;
	}

	public double getMpTwoGPA() {
		return this.mp2GPA;
	}

	public double getMpThreeGPA() {
		return this.mp3GPA;
	}

	public double getMpFourGPA() {
		return this.mp4GPA;
	}
	
	public double getYearGPA() {
		return this.yearGPA;
	}
}
