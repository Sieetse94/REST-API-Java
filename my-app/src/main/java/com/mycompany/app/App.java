package com.mycompany.app;

import java.io.*;
import com.fasterxml.jackson.databind.*;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;

public class App extends HttpServlet{
    
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String[] references = {"1", "2", "3"};

    @Override
    protected void doPost(
        final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException
    {
        // Open the json parser
        final JsonNode statement = objectMapper.readTree(req.getInputStream());

        // Get required data for validation
        final String reference = getReference(statement);
        final double startBalance = getStartBalance(statement);
        final double mutation = getMutation(statement);
        final double endBalance = getEndBalance(statement);
        final String accountNumber = getAccountNumber(statement);

        // Validate reference and endbalance
        final boolean refValidated = validateReference(reference, references);
        final boolean endBalanceValidated = validateEndBalance(startBalance, mutation, endBalance);

        // Set the outputstream and respond based on validation
        ServletOutputStream outputStream = res.getOutputStream();
        Response response;

        if (refValidated == true && endBalanceValidated == true) {
            res.setStatus(HttpServletResponse.SC_OK);
            String result = "SUCCESFUL";
            response = createResponse(result, reference, accountNumber);
            outputStream.println("SUCCESFUL");
        } else if (refValidated == false && endBalanceValidated == false) {
            res.setStatus(HttpServletResponse.SC_OK);
            outputStream.println("DUPLICATE_REFERENCE_INCORRECT_END_BALANCE");
        } else if (refValidated == false) {
            res.setStatus(HttpServletResponse.SC_OK);
            outputStream.println("DUPLICATE_REFERENCE");
        } else if (endBalanceValidated == false) {
            res.setStatus(HttpServletResponse.SC_OK);
            outputStream.println("INCORRECT_END_BALANCE");
        } else {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            outputStream.println("INTERNAL_SERVER_ERROR"); 
        }
    }
    

    /******************************* 
    * GETTERS FOR DATA
    *******************************/
    private String getReference(final JsonNode statement)
    {
        JsonNode referenceNode = statement.get("ref");
        if (referenceNode != null) {
            return referenceNode.asText();
        }
        else {
            return "0";
        }
    }

    private double getStartBalance(final JsonNode statement)
    {
        JsonNode referenceNode = statement.get("startBalance");
        if (referenceNode != null) {
            return referenceNode.asDouble();
        }
        else {
            return 0;
        }
    }

    private double getMutation(final JsonNode statement)
    {
        JsonNode referenceNode = statement.get("mutation");
        if (referenceNode != null) {
            return referenceNode.asDouble();
        }
        else {
            return 0;
        }
    } 

    private double getEndBalance(final JsonNode statement)
    {
        JsonNode referenceNode = statement.get("endBalance");
        if (referenceNode != null) {
            return referenceNode.asDouble();
        }
        else {
            return 0;
        }
    }

    private String getAccountNumber(final JsonNode statement)
    {
        JsonNode referenceNode = statement.get("accountNumber");
        if (referenceNode != null) {
            return referenceNode.asText();
        }
        else {
            return "0";
        }
    }


    /******************************* 
    * VALIDATORS 
    *******************************/
    private boolean validateReference(final String reference, final String[] references)
    {   
        for (String i : references) {
            if (i.equals(reference)) {
                return false;
            }
        }
        return true;
    }

    private boolean validateEndBalance(final double startBalance, final double mutation, final double endBalance)
    {
        if (startBalance + mutation != endBalance)
        {
            return false;
        }
        return true;
    }


    /********************************
    * RESPONSE CREATOR
    *********************************/
    private Response createResponse(final String result, final String reference, final String accountNumber) {

        Response response = new Response();
        response.setResult(result);
        if (response.result.equals("SUCCESFUL") || response.result.equals("INTERNAL_SERVER_ERROR")) {
            return response;
        } else {
            Record record = new Record();
            record.setAccountNumber(accountNumber);
            record.setReference(reference);
            response.addRecord(record);
            if (response.result.equals("DUPLICATE_REFERENCE_INCORRECT_END_BALANCE") {
                response.addRecord(record);
            }
            return response;
        }
    }

    /******************************* 
    * MAIN METHOD
    *******************************/
    public static void main(String[] args) throws IOException
    {
        SimpleJettyService.run(App.class);
    }

}