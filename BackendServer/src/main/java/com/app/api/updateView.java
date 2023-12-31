package com.app.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
 * Servlet implementation class updateView
 */
@WebServlet("/updateView")
public class updateView extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String QUERY = "SELECT invoice_currency, cust_payment_terms FROM winter_internship where sl_no=?";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public updateView() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
	    response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
	    response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
	    response.addHeader("Access-Control-Max-Age", "1728000");
		List<Highradius> invoiceRecords = new ArrayList<>();
		try {
			
			// creating connection object
			// jdbc connection setup
			try (Connection connection = DBConnect.createC();PreparedStatement preparedStatement = connection.prepareStatement(QUERY);){
//				Statement statement = connection.createStatement();
				preparedStatement.setString(1, request.getParameter("sl_no"));
	            System.out.println(preparedStatement);
				ResultSet resultSet = preparedStatement.executeQuery();
				System.out.println(resultSet);
				
				while(resultSet.next()) {
					Highradius invoice = new Highradius();
					
					invoice.setInvoiceCurrency(resultSet.getString("invoice_currency"));
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
		System.out.println(e);}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
