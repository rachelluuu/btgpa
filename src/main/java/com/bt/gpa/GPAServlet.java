package com.bt.gpa;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GPAServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3234516020496050331L;
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.sendRedirect("/");
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			String username = req.getParameter("username").replace("@bergen.org", "").replace("bca/", "");
			String password = req.getParameter("password");
			GPA user = new GPA(username, password);
			req.setAttribute("mp1GPA", round(user.getMpOneGPA(), 3));
			req.setAttribute("mp2GPA", round(user.getMpTwoGPA(), 3));
			req.setAttribute("mp3GPA", round(user.getMpThreeGPA(), 3));
			req.setAttribute("mp4GPA", round(user.getMpFourGPA(), 3));
			req.setAttribute("yearGPA", round(user.getYearGPA(), 3));
			req.getRequestDispatcher("gpa.jsp").forward(req, resp);
			System.out.println("Fulfilled request for user: " + username);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.out.println("[cError]" + e.getMessage());
			req.setAttribute("error", "<div style=\"\" class=\"alert\"><b>Woops!</b> Looks like your username/password is wrong. Try again.</div>");
			req.getRequestDispatcher("index.jsp").forward(req,resp);
		}
	}
	private double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();
	    if (Double.isNaN(value)) value = 0;
	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}

}
