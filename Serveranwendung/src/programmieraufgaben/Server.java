package programmieraufgaben;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Die Server-Klasse enthält alle Methoden zum Erstellen, Verwenden und Schließen des Servers.
 *
 * Für die Lösung der Aufgabe müssen die Methoden execute, disconnect
 * und checkPort befüllt werden.
 * Es dürfen beliebig viele Methoden und Klassen erzeugt werden, solange
 * die von den oben genannten Methoden aufgerufen werden.
 */
public class Server{
    // Port-Nummer
    private int port;
    // Liste der vom Client ausgeführten Kommando
    private ArrayList<String> history;
    private ServerSocket serverSocket;

    /**
     * Diese Methode beinhaltet die gesamte Ausführung (Verbindungsaufbau und Beantwortung
     * der Client-Anfragen) des Servers.
     */
    public void execute() {
        try {
            serverSocket = new ServerSocket(getPort());
            Socket s = serverSocket.accept();
            InputStreamReader in = new InputStreamReader(s.getInputStream());

            BufferedReader bf = new BufferedReader(in);
            history= new ArrayList<>();
            String str = bf.readLine();
            PrintWriter pr,exit;
            pr = new PrintWriter(s.getOutputStream());
            while (str != null) {
                  switch(str) {
                        case "GET Time":
                            pr.println(getTime());
                            break;
                        case "GET Date":
                            pr.println(getDate());
                            break;
                        case "HISTORY": {
                            //zeigt die Liste der vom Client ausgeführten Aufträge an.
                            String temp="";
                            for (int i=0; i<history.size();i++) {
                                temp+=history.get(i).concat(" | ");
                            }
                            pr.println(temp);
                            pr.flush();
                            break;
                        }
                        default: {
                            String arr[] = str.split(" ", 2);
                            String firstWord = arr[0];
                            switch(firstWord) {
                                case "ADD":
                                case "SUB":
                                case "MUL":
                                case "DIV":
                                    operation(str,pr);
                                    break;
                                case "HISTORY":{
                                    history(str,pr);
                                    break;
                                }
                                case "ECHO":
                                    pr.println(str);
                                    break;
                                case "DISCARD":
                                    pr.println();
                                    break;
                                case "PING":
                                    pr.println("PONG");
                                    break;
                                case "EXIT":
                                    break;
                                default:
                                    pr.println("Unbekannte Anfrage!");
                            }
                        }
                  }
                  //fügt das aktuelle ausgeführte Kommando hinzu
                  history.add(str);
                  pr.flush();
                  str = bf.readLine();
            }

        } catch (IOException e) {
            System.out.println(e.toString());
        }

    }

    /**
     * Gibt die aktuelle Zeit in unsere Format zurück
     */
    private String getTime(){
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        Date date = new Date();
        String time=dateFormat.format(date);
        return time;
    }

    /**
     *Gibt den aktuelle Datum in unsere Format zurück
     */
    private String getDate(){

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();
        String time=dateFormat.format(date);
        return time;
    }

    /**
     * Hier soll die Verbindung und alle Streams geschlossen werden.
     */
    public void disconnect() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Überprüfung der Port-Nummer und Speicherung dieser in die Klassen-Variable "port"
     * @param port Portnummer als String
     * @return Port-Nummer ist akzeptabel TRUE oder nicht FALSE
     */
    public boolean checkPort(String port) {
        if(port.equals("localhost")||port.equals("2020"))
        {
            this.port=2020;
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Gibt die akzeptierte und gespeicherte Port-Nummer zurück
     * @return
     */
    public int getPort() {
        return port;
    }
    public void history(String arg,PrintWriter pr) {
        try {
            String[] args = arg.split(" ");
            if(args.length ==2)
            {
                int n1 = Integer.parseInt(args[1]);
                String temp="";
                for (int i=0; i<n1;i++) {
                    temp+=history.get(i).concat(" | ");
                }
                pr.println(temp);
                pr.flush();
            }else{
                pr.println( "Error : Wir brauchen maximal 2 Arguments! ");
            }
        }catch (Exception e ){
            System.out.println(e.toString());
        }
    }

    /**
     * Berechnen die verschiedene Operationen
     * @param arg
     * @param pr
     */
    public void operation(String arg,PrintWriter pr )
    {
        try
        {
            String[] args = arg.split(" ");
            if(args.length ==3)
            {
                String op = args[0];
                int n1 = Integer.parseInt(args[1]);
                int n2 = Integer.parseInt(args[2]);
                if(op.equals( "ADD"))
                {
                    pr.println(add(n1,n2));
                }
                else if(op.equals("DIV"))
                {
                    pr.println(div(n1 ,n2));
                }
                else if (op.equals("SUB"))
                {
                    pr.println(sub(n1,n2));
                }
                else if(op.equals("MUL"))
                {
                    pr.println(mul(n1,n2));
                }
                else
                {
                    pr.println("Error: kein gultiges Argument");
                }
            }
            else
            {
                pr.println( "Error : Wir brauchen genau 3 Arguments! ");
            }
        }
        catch ( NumberFormatException f)
        {
            pr.println("Falsches Format!");
        }
        catch ( Exception e)
        {

            pr.println(e.toString());

        }
    }

    public int add ( int n1,int n2)
    {
        return n1+n2;
    }

    public int mul(int n1, int n2)
    {
        return n1*n2;
    }

    public  int sub(int n1,int n2)
    {
        return n1-n2;
    }

    public String div(int n1, int n2)
    {
        if(n2!= 0)
        {
            float f= (float)n1/n2;
            String s=Float.toString(f);
            return s;
        }
        else
        {
            return "undefined";
        }
    }

}
