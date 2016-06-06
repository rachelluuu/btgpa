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
			req.setAttribute("tri1GPA", round(user.getTriOneGPA(), 3));
			req.setAttribute("tri2GPA", round(user.getTriTwoGPA(), 3));
			req.setAttribute("tri3GPA", round(user.getTriThreeGPA(), 3));
			req.setAttribute("yearGPA", round(user.getYearGPA(), 3));
			req.getRequestDispatcher("gpa.jsp").forward(req, resp);
			System.out.println("Fulfilled request for user: " + username);
		} catch (Exception e) {
			System.err.println(new Throwable().getStackTrace()[0].getLineNumber());
			System.out.println(e);
			System.out.println("[cError]" + e.getMessage());
			req.setAttribute("error", "<div style=\"\" class=\"alert\"><b>Woops!</b> Looks like your username/password is wrong. Try again.</div>");
			req.getRequestDispatcher("index.jsp").forward(req,resp);
		}
	}
	private double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}

}
