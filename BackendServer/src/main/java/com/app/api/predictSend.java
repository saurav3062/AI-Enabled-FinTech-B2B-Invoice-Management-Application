package com.app.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app.pojo.Highradius;
import com.google.gson.Gson;

/**
 * Servlet implementation class predictSend
 */
@WebServlet("/predictSend")
public class predictSend extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public predictSend() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
	    response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
	    response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
	    response.addHeader("Access-Control-Max-Age", "1728000");
		List<Highradius> invoiceRecords = new ArrayList<>();
		String value = request.getParameter("sl_no");
		try {
			
			// creating connection object
			// jdbc connection setup
			try (Connection connection = DBConnect.createC();){
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select business_code, cust_number, clear_date, buisness_year, doc_id, posting_date, due_in_date, total_open_amount, baseline_create_date, cust_payment_terms from winter_internship where sl_no in ("+value+")");
			while(resultSet.next()) {
				Highradius invoice = new Highradius();
				
				//getting invoice based on column name	
				invoice.setBusinessCode(resultSet.getString("business_code"));
				invoice.setCustNumber(resultSet.getInt("cust_number"));
				invoice.setClearDate(resultSet.getString("clear_date"));
				invoice.setBuisnessYear(resultSet.getInt("buisness_year"));
				invoice.setDocId(resultSet.getString("doc_id"));
				invoice.setPostingDate(resultSet.getString("posting_date"));
				invoice.setDueInDate(resultSet.getString("due_in_date"));
				invoice.setTotalOpenAmount(resultSet.getDouble("total_open_amount"));
				invoice.setBaselineCreateDate(resultSet.getString("baseline_create_date"));
				invoice.setCustPaymentTerms(resultSet.getString("cust_payment_terms"));
				
				invoiceRecords.add(invoice);
			}
			
			// converting list into JSON Data
			Gson gson = new Gson();
			String result = gson.toJson(invoiceRecords);
			
			//setting the response headers
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			
			// sending JSON Data
			PrintWriter writer = response.getWriter();
			writer.print(result);
			writer.flush();
			writer.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		
	}catch(Exception e) {
		System.out.println(e);
		}
	}
}
