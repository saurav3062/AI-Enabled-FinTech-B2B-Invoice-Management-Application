package com.app.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app.pojo.Analytics;
import com.google.gson.Gson;

/**
 * Servlet implementation class analytics
 */
@WebServlet("/analytics")
public class analytics extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public analytics() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
	    response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
	    response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
	    response.addHeader("Access-Control-Max-Age", "1728000");
		String clear_date_from = request.getParameter("clear_date_from");
		String clear_date_to = request.getParameter("clear_date_to");
		String due_date_from = request.getParameter("due_date_from");
		String due_date_to = request.getParameter("due_date_to");
		String baseline_create_date_from = request.getParameter("baseline_create_date_from");
		String baseline_create_date_to = request.getParameter("baseline_create_date_to");
		String invoice_currency = request.getParameter("invoice_currency");
		
		String sql ="SELECT business_code, COUNT(distinct cust_number) AS no_of_cust, SUM(total_open_amount) AS total_open_amount FROM winter_internship WHERE is_deleted != 1";
		String sqlCoin ="SELECT invoice_currency, COUNT(*) AS TOTAL FROM winter_internship WHERE is_deleted != 1";
		
		if(clear_date_to != "" || due_date_to != "" || baseline_create_date_to != "" || invoice_currency != "") {
			sql += " AND";
			sqlCoin += " AND";
		}
		if((clear_date_from != "" && clear_date_to != "") || clear_date_to != "" ) {
			sql += " clear_date BETWEEN \""+clear_date_from+"\" AND \""+clear_date_to+"\" AND";
			sqlCoin += " clear_date BETWEEN \""+clear_date_from+"\" AND \""+clear_date_to+"\" AND";
		}
		if((due_date_from != "" && due_date_to != "") || due_date_to != "" ) {
			sql += " due_in_date BETWEEN \""+due_date_from+"\" AND \""+due_date_to+"\" AND";
			sqlCoin += " due_in_date BETWEEN \""+due_date_from+"\" AND \""+due_date_to+"\" AND";
		}
		if((baseline_create_date_from != "" && baseline_create_date_to != "") || baseline_create_date_to != "" ) {
			sql += " baseline_create_date BETWEEN \""+baseline_create_date_from+"\" AND \""+baseline_create_date_to+"\" AND";
			sqlCoin += " baseline_create_date BETWEEN \""+baseline_create_date_from+"\" AND \""+baseline_create_date_to+"\" AND";
		}
		if(invoice_currency != "") {
			sql += " invoice_currency = \""+invoice_currency+"\"";
			sqlCoin += " invoice_currency = \""+invoice_currency+"\"";
		}
		
		boolean isFound = sql.indexOf("WHERE") !=-1? true: false;
		boolean isFound_coin = sqlCoin.indexOf("WHERE") !=-1? true: false;
		
		if(isFound == true && invoice_currency == "") {
			sql = sql.substring(0,(sql.length()-4));
		}
		if(isFound_coin == true && invoice_currency == "") {
			sqlCoin = sqlCoin.substring(0,(sqlCoin.length()-4));
		}
		
		sql+= " GROUP BY business_code";
		sqlCoin+= " GROUP BY invoice_currency";
		
		HashMap<String, List<Analytics>> analytics = new HashMap<String, List<Analytics>>();
		
		List<Analytics> businessAnalyticsList = new ArrayList<>();
		List<Analytics> invoiceAnalyticsList = new ArrayList<>();
		try {
			
			// creating connection object
			// jdbc connection setup
			try (Connection connection = DBConnect.createC();){
			Statement statement = connection.createStatement();
			Statement statement2 = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			ResultSet resultSetCoin = statement2.executeQuery(sqlCoin);
			while(resultSet.next()) {
				Analytics businessRecord = new Analytics();
				
				//getting businessRecord based on column name				
				businessRecord.setBusinessCode(resultSet.getString("business_code"));
				businessRecord.setNumberOfCust(resultSet.getInt("no_of_cust"));
				businessRecord.setTotalAmount(resultSet.getDouble("total_open_amount"));
				
				businessAnalyticsList.add(businessRecord);
			}
			while(resultSetCoin.next()) {
				Analytics invoiceRecord = new Analytics();
				
				//getting invoiceRecord based on column name				
				invoiceRecord.setInvoice(resultSetCoin.getString("invoice_currency"));
				invoiceRecord.setCount(resultSetCoin.getInt("TOTAL"));
				
				invoiceAnalyticsList.add(invoiceRecord);
			}
			
			analytics.put("business", businessAnalyticsList);
			analytics.put("count", invoiceAnalyticsList);
			
			// converting list into JSON Data
			Gson gson = new Gson();
			String result = gson.toJson(analytics);
			System.out.println(result);
			
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
	}finally {
	    System.out.println("In finally");
	}	
}
	
}
