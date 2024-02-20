package com.example.parcial1;

import java.net.*;
import java.io.*;

public class ServiceFacade {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }

        boolean running = true;

        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine;

            boolean calculator = false;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Recibí: " + inputLine);
                
                if (inputLine.contains("GET") && inputLine.contains("/calculadora")) {
                    System.out.println(inputLine);
                    calculator = true;
                } else if (inputLine.contains("GET") && inputLine.contains("/computar")) {
                    String comandos = inputLine.split("comando=")[1];
                    System.out.println(comandos);
                    // reflex calculator
                    
                }

                if (!in.ready()) {
                    break;
                }
            }

            if (calculator) {
                outputLine = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: text/html\r\n"
                        + "\r\n"
                        + "<!DOCTYPE html>\r\n" 
                        + "<html>\r\n" 
                        + "    <head>\r\n" 
                        + "        <title>Calculadora</title>\r\n" 
                        + "        <meta charset=\"UTF-8\">\r\n" 
                        + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n" 
                        + "    </head>\r\n" 
                        + "    <body>\r\n" 
                        + "        <h1>Calculadora</h1>\r\n" 
                        + "        <form action=\"/operation\">\r\n" 
                        + "            <label for=\"name\">Operation:</label><br>\r\n" 
                        + "            <input type=\"text\" id=\"operation\" name=\"operation\"><br><br>\r\n" 
                        + "            <input type=\"button\" value=\"Submit\" onclick=\"loadGetMsg()\">\r\n" 
                        + "        </form> \r\n" 
                        + "        <div id=\"getrespmsg\"></div>\r\n" 
                        + "        <script>\r\n" 
                        + "            function loadGetMsg() {\r\n" 
                        + "                let nameVar = document.getElementById(\"operation\").value;\r\n" 
                        + "                const xhttp = new XMLHttpRequest();\r\n" 
                        + "                xhttp.onload = function() {\r\n" 
                        + "                    document.getElementById(\"getrespmsg\").innerHTML =\r\n" 
                        + "                    this.responseText;\r\n" 
                        + "                }\r\n" 
                        + "                xhttp.open(\"GET\", \"/computador?comando=\"+nameVar);\r\n" 
                        + "                xhttp.send();\r\n" 
                        + "            }\r\n" 
                        + "        </script>\r\n" 
                        + "    </body>\r\n" 
                        + "</html>";

                out.println(outputLine);
                System.out.println(outputLine);

                out.close();
                in.close();
                clientSocket.close();
            
            }
        }
        serverSocket.close();
    }
}