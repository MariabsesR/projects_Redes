import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that initializes and runs a server. Contains multiple auxiliary classes.
 * @author Maria Rocha fc58208
 * @author Joao Vale fc58159
 * @author Sofia Lopes fc58175
 *
 */
public class MyHttpServer {
	private static int numClients;
	private static ArrayList<Integer> activeClients = new ArrayList<Integer>();
	
	/**
	 * The main method receives the port number that the server will be listening on as an argument.
	 * 
	 * @param args argument that will be used as port number
	 * @throws IOException
	 */
    public static void main(String[] args) throws IOException {
    if (args.length != 1) {
        System.err.println("Usage: java MyHttpServer <port number>");
        System.exit(1);
    }
        int portNumber = Integer.parseInt(args[0]);
        numClients = 0;
        boolean listening = true;
        
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) { 
            while (listening) {
               new MyHttpServerThread(serverSocket.accept()).start();
               numClients++;
	           activeClients.add(numClients);
	        }
	    } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    /**
     * Inner class that initializes a thread for each new client that connects to the server.
     * This way, the server is able to deal with multiple clients at the same time.
     *
     */  
    private static class MyHttpServerThread extends Thread {
        private final int clientNum = numClients+1;
		private Socket socket = null;
		
		/**
		 * Class constructor that receives the socket used for server-client communication.
		 * 
		 * @param socket Socket 
		 */		
        private MyHttpServerThread(Socket socket) {
            super("MyHttpServerThread");
            this.socket = socket;
        }
        
        /**
		 * The run method is initialized when the server begins execution of the thread.
		 * It will receive input from the client and send an appropriate response back.
		 * 
		 */
        public void run() {

            try (
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                        socket.getInputStream()));
            ) {
                while(!this.socket.isClosed()) {
                String inputLine, outputLine;
                String test = "";
                HttpRequest req = new HttpRequest();
                
                while (!(inputLine = in.readLine()).isBlank()) {
                	test+=inputLine+" "; 
                }
                test.substring(0, test.length()-1);
            	outputLine = req.processInput(test,clientNum);
                out.println(outputLine);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (NullPointerException e) {
                activeClients.remove(activeClients.indexOf(clientNum));
			}
        }
    }
    
    /**
     * Inner class that processes a client's request.
     *
     */  
    private static class HttpRequest {
    	
    	/**
		 * Takes the input given by the client, in order to analyze it and answer the client's
		 * request in an the appropriate way.
		 * @param inputLine message/request from the client
		 * @param clientNum int number that represents which thread, and by extension, which client,
		 * made the request. Useful to enforce the limitation of handling at most five clients at a time.
		 * @return the reply of the server to the client's request.
		 */
    	private String processInput(String inputLine, int clientNum) {
    		System.out.println(inputLine);
    		if(activeClients.indexOf(clientNum) >= 5)
    			return "500 Internal Server Error\\r\\n\r\n";
    		Pattern getRequest = Pattern.compile("^GET");
    		Matcher getMatcher = getRequest.matcher(inputLine);
    		Pattern postRequest = Pattern.compile("^POST");
    		Matcher postMatcher = postRequest.matcher(inputLine);
    		if (getMatcher.find()) {
				String[] splitBySpace = inputLine.split(" ");
				if(!inputLine.contains("/index.html"))
					return "404 Not Found\\r\\n\r\n";
				else if(!splitBySpace[2].contains("\\r\\n") || inputLine.contains("  ") || !inputLine.contains("HTTP/1.1"))
					return "400 Bad Request\\r\\n\r\n";
				else {
					String htmlPage = "<h1 style=\\\"color: blue; text-align: center;\\\">Redes de Computadores</h1>\n"
							+ "<h2 style=\\\"color: green; text-align: center;\\\">Mini-Projeto 1: Servidor HTTP em Java.</h2>\n"
							+ "<p style=\\\"text-align: center;\\\">It is a good sign that you see this message on your web browser. Well Done!</p>\r\n";
					return "200 OK\\r\\n\r\n" + "Content-Length: " + htmlPage.getBytes().length + "\\r\\n\r\n" + "\\r\\n\r\n" + htmlPage;
				}
			}
    		 if (postMatcher.find()) {
    			Pattern goodPost = Pattern.compile("StudentName=[a-zA-Z \\-]+&StudentID=fc.{5}");
    			Matcher goodPostMatcher = goodPost.matcher(inputLine);
    			if(goodPostMatcher.find() && inputLine.contains("POST /simpleForm.html HTTP/1.1\\r\\n"))
    				return "200 OK\\r\\n\r\n";
    			else 
    				return "400 Bad Request\\r\\n\r\n";
			}
    			return "501 Not Implemented\\r\\n\r\n";
    	}

    }
}

