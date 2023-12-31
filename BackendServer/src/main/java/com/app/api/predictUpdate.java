package com.app.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class App
 */
@WebServlet("/predictUpdate")
public class predictUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String QUERY = "update winter_internship set aging_bucket = ? where sl_no = ?";
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public predictUpdate() {
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.addHeader("Access-Control-Allow-Origin", "*");
	    response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
	    response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
	    response.addHeader("Access-Control-Max-Age", "1728000");
		String success = "Predict data successfully";
		String fail = "Somthing went wrong";
        
		try (Connection connection = DBConnect.createC(); PreparedStatement preparedStatement = connection.prepareStatement(QUERY)) {		
            preparedStatement.setString(1, request.getParameter("aging_bucket"));
            preparedStatement.setInt(2, Integer.parseInt(request.getParameter("sl_no")));
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
            
            if(preparedStatement.executeUpdate() == 1) {
            	
            	// converting list into JSON Data
        		Gson gson = new Gson();
        		String result = gson.toJson(success);   
         			
                 // sending JSON Data
        		 PrintWriter writer = response.getWriter();
        		 writer.print(result);
        		 writer.flush();
        		 writer.close();            	
            }else {
            	 // converting list into JSON Data
        		Gson gson = new Gson();
        		String result = gson.toJson(fail);
            	
            	// sending JSON Data
    			PrintWriter writer = response.getWriter();
    			writer.print(result);
    			writer.flush();
    			writer.close();
            }
    		 
        } catch (SQLException e) {
        	System.out.println(e);
        	
        	 // converting list into JSON Data
    		Gson gson = new Gson();
    		String result = gson.toJson(fail);
        	
        	// sending JSON Data
			PrintWriter writer = response.getWriter();
			writer.print(result);
			writer.flush();
			writer.close();
        }
		
	}

}
