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
	private String errorString = null;

	//implement year
	private ArrayList<Grade> yearColumn;
	private boolean useYearColumn;
	
	public GPA(String username, String password) throws IOException {
		this.useYearColumn = true;
		
		this.username = username;
		this.password = password;
		this.mp1 = new ArrayList<Grade>();
		this.mp2 = new ArrayList<Grade>();
		this.mp3 = new ArrayList<Grade>();
		this.mp4 = new ArrayList<Grade>();
		this.year = new ArrayList<Grade>();
		this.yearColumn = new ArrayList<Grade>();
		parse();
		calculate();
		if (errorString == null) {	
			this.mp1GPA = findGPA(this.mp1);
			this.mp2GPA = findGPA(this.mp2);
			this.mp3GPA = findGPA(this.mp3);
			this.mp4GPA = findGPA(this.mp4);
			if (useYearColumn) {
				this.yearGPA = findGPA(this.yearColumn);
			} else {
				this.yearGPA = findGPA(this.year);
			}
			
			Jsoup.connect("https://docs.google.com/forms/d/1joItByFoIKzjFB5Mm0q8YbcXFFXVchtJ6QasviXtPdk/formResponse")
						.data("entry.1584323777", this.username)
						.post();
			logData();
		}
	}
	
	private void logData() {
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
		String title = page.select("title").text();
		if (title.indexOf("Sign In") >= 0) {
			errorString = "Your username or password is incorrect";
			System.out.println(errorString + " for user " + username + ", password " + password);
			return;
		}
		if (title.indexOf("Grades") < 0) {
			errorString = "PowerSchool grades page is not returned";
			System.out.println(errorString + title);
			return;
		}
		Element table = page.select("table").first();
		if (table == null) {
			errorString = "PowerSchool grades page does not have a table";
			System.out.println(errorString + page.text());
			return;
		}
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

				double credits = 0.0;
//				String[] modsArray = mods.split(" ");
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
			}
		}
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
/*		double numberOfMods = 0.0;
		double numberOfTimes = 0.0; */
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
			return numberOfMPs > 2 ? 2.0 : 1.0;
		}
		if (!labPeriodMatchA.equals("ERROR") || !labPeriodMatchB.equals("ERROR")){
			return 6.0;
		}
		if (subject.startsWith("Phys Ed.")){		
			return 3.0;
		}
		if (subject.startsWith("Social Issues") || subject.startsWith("Driver Ed Theory") 
				|| subject.startsWith("First Aid") || subject.startsWith("Family Living")){		
			return 1.0;
		}
		if (!dblPeriodMatch.equals("ERROR")){
			return 10.0;
		}
		return 5.0;
	}

	private String match(String pattern, String line) {
		Pattern p = Pattern.compile(pattern);
		Matcher matcher = p.matcher(line);
		if (matcher.find())
			return matcher.group(0);
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
		if (grade.equals("A+"))	return 4.0;
		if (grade.equals("A"))	return 4.0;
		if (grade.equals("A-"))	return 3.67;
		if (grade.equals("B+"))	return 3.33;
		if (grade.equals("B"))	return 3.0;
		if (grade.equals("B-"))	return 2.67;
		if (grade.equals("C+"))	return 2.33;
		if (grade.equals("C"))	return 2.0;
		if (grade.equals("C-"))	return 1.67;
		if (grade.equals("D+"))	return 1.33;
		if (grade.equals("D"))	return 1.0;
		if (grade.equals("D-"))	return 0.67;
		return 0.0;
	}
	
	private double getGPA(double grade) {
		if (grade >= 93.0) return 4.0;
		if (grade >= 90.0) return 3.67;
		if (grade >= 87.0) return 3.33;
		if (grade >= 83.0) return 3.0;
		if (grade >= 80.0) return 2.67;
		if (grade >= 77.0) return 2.33;
		if (grade >= 73.0) return 2.0;
		if (grade >= 70.0) return 1.67;
		if (grade >= 67.0) return 1.33;
		if (grade >= 63.0) return 1.0;
		if (grade >= 60.0) return 0.67;
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
	
	public String errorString() {
		return errorString;
	}
}
