import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.awt.Desktop;

/**
 * Class that has multiple methods to send different HTTP requests to the given
 * TCP port number that connects to the server with the given name
 * 
 * @author Maria Rocha fc58208
 * @author Joao Vale fc58159
 * @author Sofia Lopes fc58175
 *
 */
public class MyHttpClient {

	PrintWriter outConection;
	BufferedReader inConection;
	Socket clientSocket;
	String saveHost;

	/**
	 * Class constructor that receives the name of the server and the TCP port
	 * number
	 * 
	 * @param hostName   name of the server
	 * @param portNumber destination TCP port number that the client is connected to
	 * @throws IOException
	 */

	public MyHttpClient(String hostName, int portNumber) throws IOException {

		clientSocket = new Socket(hostName, portNumber);
		saveHost = hostName;
		outConection = new PrintWriter(clientSocket.getOutputStream());
		inConection = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

	}

	/**
	 * Sends a HTTP GET request of the given object to the server and creates a new
	 * file with the given response and opens it on the browser
	 * 
	 * @param ObjectName URL of the object the GET method will request
	 * @throws IOException
	 */

	public void getResource(String ObjectName) throws IOException {

		outConection.println("GET /" + ObjectName + " HTTP/1.1\\r\\n\r\n" + "Host:" + saveHost + "\\r\\n\r\n"
				+ "Connection: keep-alive\\r\\n\r\n" + "\\r\\n\r\n");

		String response = getAndPrintResponse();

		// creating and opening file in the browser with the given html code
		if(ObjectName == "index.html"){
		File htmlFile = new File(ObjectName);
		htmlFile.createNewFile();
		FileWriter writer = new FileWriter(htmlFile);
		String[] getCode = response.split("\\r\\n");
		writer.write(getCode[getCode.length - 3]+getCode[getCode.length - 2]+getCode[getCode.length - 1]);
		writer.close();

		Desktop.getDesktop().browse(htmlFile.toURI());
	}

	}

	/**
	 * Sends an HTTP POST request to the URL simpleForm.html with the necessary
	 * entity body to fill the fields and value of which
	 * 
	 * @param data fields and its corresponding value in each array position
	 *             necessary to the form
	 * @throws IOException
	 */

	public void postData(String[] data) throws IOException {

		String[] studentName = data[0].split(": ");
		String[] studentID = data[1].split(": ");
		String information = studentName[0] + "=" + studentName[1] + "&" + studentID[0] + "=" + studentID[1]
				+ "\\r\\n\r\n";
		outConection.println("POST /simpleForm.html HTTP/1.1\\r\\n\r\n" + "Host: " + saveHost + "\\r\\n\r\n"
				+ "\\r\\n\r\n" + "Connection: keep-alive\\r\\n\r\n" + "Content-Length = " + information.length()
				+ "\\r\\n\r\n" + information + "\\r\\n\r\n");

		getAndPrintResponse();

	}

	/**
	 * Sends a HTTP request with the name of a method that has not been implemented
	 * by server with the index.html URL
	 * 
	 * @param wrongMethodName name of a method not implemented by the server
	 * @throws IOException
	 */

	public void sendUnimplementedMethod(String wrongMethodName) throws IOException {
		outConection.println(wrongMethodName + " /index.html HTTP/1.1\\r\\n\r\n" + "Host: " + saveHost + "\\r\\n\r\n"
				+ "Connection: keep-alive\\r\\n\r\n" + "\\r\\n\r\n");
		getAndPrintResponse();

	}

	/**
	 * Sends a HTTP GET request with the index.html URL but with the incorrect
	 * format, the format will be decided depending on the given Int, with 1 being
	 * \r\n missing after the request line, 2 has too many spaces between the field
	 * and 3 is missing the HTTP version field in the request line
	 * 
	 * @param type Int that chooses the type of incorrect format request to send
	 * @throws IOException
	 */

	public void malformedRequest(int type) throws IOException {

		switch (type) {
		case 1: {
			outConection.println("GET /index.html HTTP/1.1 " + "Host:" + saveHost + "\\r\\n\r\n"
					+ "Connection: keep-alive\\r\\n\r\n" + "\\r\\n\r\n");
			break;

		}
		case 2: {
			outConection.println("GET   /index.html   HTTP/1.1\\r\\n\r\n " + "Host:" + saveHost + "\\r\\n\r\n"
					+ "Connection: keep-alive\\r\\n\r\n" + "\\r\\n\r\n");
			break;

		}
		case 3: {
			outConection.println("GET /index.html \\r\\n\r\n " + "Host:" + saveHost + "\\r\\n\r\n"

					+ "Connection: keep-alive\\r\\n\r\n" + "\\r\\n\r\n");
			break;

		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + type);
		}

		getAndPrintResponse();

	}

	/**
	 * Closes the socket, the printWriter and the BufferedReader
	 * 
	 * @throws IOException
	 */

	public void close() throws IOException {

		clientSocket.close();
		outConection.close();
		inConection.close();

	}

	/**
	 * Flushes the PrintWriter and receives and prints the response of the server to
	 * the sent method
	 * 
	 * @return the answer of the server to the method used
	 * @throws IOException
	 */

	public String getAndPrintResponse() throws IOException {

		outConection.flush();
		String response = "";
		String addResponde = "";

		do {
			addResponde = inConection.readLine();
			response = response + addResponde+"\r\n";
		} while (!addResponde.isEmpty());

		System.out.println("Server replied :" + response);
		return response;
	}

}
