package com.bt.gpa;

public class Grade {
	private String subject;
	private double gradePoints;
	private double credits;
	
	public Grade(String subject, double gradePoints, double credits) { 
		this.subject = subject;
		this.gradePoints = gradePoints;
		this.credits = credits;
	}

	public String getSubject() {
		return this.subject;
	}
	
	public double getGrade() {
		return this.gradePoints;
	}
	
	public double getCredits() {
		return this.credits;
	}
	
	public String toString() {
		return "[Subject]" + subject + "[gradePoints]" + gradePoints + "[credits]" + credits;
	}
}

