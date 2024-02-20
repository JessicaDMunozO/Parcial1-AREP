package com.example.parcial1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

public class ReflexCalculator {

    public static String calculate(String commands) throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        Class c = Math.class;
        String result = "";

        String[] elements = commands.split("(");
        String operation = elements[0];
        String parameters = elements[1].split(")")[0];
        String[] numbers = parameters.split(",");

        if (operation != "qck") {
            Method m = c.getMethod(operation, Double.TYPE);
            result = (String) m.invoke(null, numbers);
        } else {
            // sort
        }

        return result;
    }

    public static void main(String[] args) throws IOException, NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(36000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 36000.");
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
            String result = "";
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Recibí: " + inputLine);

                if (inputLine.contains("GET") && inputLine.contains("/compreflex")) {
                    String comandos = inputLine.split("=")[1];
                    System.out.println(comandos);
                    result = calculate(comandos);
                    calculator = true;
                }

                if (!in.ready()) {
                    break;
                }
            }

            if (calculator) {
                outputLine = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: text/html\r\n"
                        + "\r\n"
                        + result;

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
