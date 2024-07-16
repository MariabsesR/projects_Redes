import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * The class TrafficAnalysis analyzes the Trace A and B and gives the answers to
 * the questions Q1 to Q9
 * 
 * 
 * @author Maria Rocha nº58208,Sofia Lopes nº58175,Joao Vale nº58159
 *
 */

public class TrafficAnalysis {
	ArrayList<String[]> data;
	// list of all the ports associated with Main services of the internet
	String[] httpPort = ("80,280,443,488,591,593,623,631,664,777,832,1001").split(",");
	String[] dnsPort = "53,90,195,196,558,853".split(",");
	String[] ftpPort = "20,21,69,115,152,247,349,574,662,989,990".split(",");
	String[] pop3Port = "110,995".split(",");
	String[] imapPort = "143,220,993".split(",");
	String[] smtpPort = "25,587".split(",");
	ArrayList<String> smtpList = new ArrayList<String>(Arrays.asList(smtpPort));
	ArrayList<String> httpList = new ArrayList<String>(Arrays.asList(httpPort));
	ArrayList<String> dnsList = new ArrayList<String>(Arrays.asList(dnsPort));
	ArrayList<String> ftpList = new ArrayList<String>(Arrays.asList(ftpPort));
	ArrayList<String> pop3List = new ArrayList<String>(Arrays.asList(pop3Port));
	ArrayList<String> imapList = new ArrayList<String>(Arrays.asList(imapPort));

	public static void main(String[] args) {

		TrafficAnalysis traceA = new TrafficAnalysis("traceA.csv");
		int recetor = 3;
		int emissor = 2;

		System.out.println("RESPOSTAS trace A:\r\n");
		System.out.println("Q1:");  
		traceA.Q1();
		System.out.println("Q2:");
		traceA.Q2();
		System.out.println("Q3:");
		traceA.Q3();
		System.out.println("Q4:");
		traceA.Q4();
		System.out.println("Q5:");
		try {
			traceA.Q5(new PrintWriter("histogramTraceB.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Q6:");
		traceA.Q6();
		System.out.println("Q7:");
		traceA.Q7();
		System.out.println("Q8:");
		traceA.Q8and9(recetor);
		System.out.println("Q9:");
		traceA.Q8and9(emissor);

		TrafficAnalysis traceB = new TrafficAnalysis("traceB.csv");
		System.out.println("\r\nRESPOSTAS trace B:\r\n");
		System.out.println("Q1:");  
		traceB.Q1();
		System.out.println("Q2:");
		traceB.Q2();
		System.out.println("Q3:");
		traceB.Q3();
		System.out.println("Q4:");
		traceB.Q4();
		System.out.println("Q5:");
		try {
			traceB.Q5(new PrintWriter("histogramTraceB.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Q6:");
		traceB.Q6();
		System.out.println("Q7:");
		traceB.Q7();
		System.out.println("Q8:");
		traceB.Q8and9(recetor);
		System.out.println("Q9:");
		traceB.Q8and9(emissor);
		System.out.println("END");

	}

	/**
	 * Class constructor for the trace that will be analyzed,receives the file path
	 * of the trace that will be analyzed and reads the file and saves its rows in
	 * an arrayList
	 * 
	 * @param filePath file Path of the trace that need analyzing
	 * @throws Exception
	 */
	public TrafficAnalysis(String filePath) {
		try {

			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			data = new ArrayList<String[]>();

			// skip First Line
			reader.readLine();
			String currentLine = reader.readLine() + "\n\r";

			while (currentLine != null) {

				data.add(currentLine.split(","));
				currentLine = reader.readLine();
			}

			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Analizes the trace and answers question Q1:"Qual o número de pacotes que
	 * possuem emissor e recetor IPv4? E que possuem emissor e recetor IPv6? Qual o
	 * número dos que possuem emissor e recetor hostnames" by printing the answer in
	 * the console
	 * 
	 */
	public void Q1() {

		int counter = 0;
		int IPv6 = 0;
		int IPv4 = 0;
		int hostName = 0;
        
		while (counter < data.size()) {
			String currentSourceIp = data.get(counter)[2];
			String currentDestinationIp = data.get(counter)[3];
			//check if the current IP source and destination are  IPV6 
			if (currentSourceIp.contains("::") && currentDestinationIp.contains("::")) {
				IPv6++;
			} else if (currentSourceIp.contains(":") && currentDestinationIp.contains(":")) { //check if the current IP source and destination are  hostnames
				hostName++;
			} else {
				IPv4++; //check if the current IP source and destination are  IPV4 
			}

			counter++;
		}
		System.out.println("IPv6: " + IPv6 + " IPv4: " + IPv4 + " hostname: " + hostName);
	}

	/**
	 * Analyzes the trace and answers the question Q2:": Qual é a duração total dos
	 * traces e qual é o número de pacotes contidos?" by printing the answer in the
	 * console
	 * 
	 */
	public void Q2() {
		//checks the last packet on the trace to see his number and his time
		String lastTime = data.get(data.size() - 1)[1].replaceAll("\"", "");
		String lastPacket = data.get(data.size() - 1)[0].replaceAll("\"", "");
		System.out.println("Duração total dos traces:" + lastTime + " segundos Número de pacotes contidos:" + lastPacket);
	}

	/**
	 * Analyzes the trace and answers the question Q3:" Quantos portos TCP de origem
	 * únicos apareceram no trace? Quais deles correspondem aos principais serviços
	 * de rede conhecidos (p. ex., HTTP, FTP, DNS, etc.)" by printing the answer in
	 * the console
	 */
	public void Q3() {
		int counter = 0;

		ArrayList<String> sourcePorts = new ArrayList<String>();

		String smtpService = "SMTP: ";
		String httpService = "HTTP: ";
		String dnsService = "DNS: ";
		String ftpService = "FTP: ";
		String pop3Service = "POP3: ";
		String imapService = "IMAP: ";

		while (counter < data.size()) {

			String currentSourcePort = data.get(counter)[4].replaceAll("\"", "");
			String flags = data.get(counter)[9].replaceAll("\"", "").replaceAll("\n\r", "");
            //checks if the current source port hasnt appeared before and if it belong to tcp (hence has a flag)
			if (!sourcePorts.contains(currentSourcePort) && !flags.isEmpty()) {
				sourcePorts.add(currentSourcePort);
                 //checks if the port belong to one of the main internet services
				if (smtpList.contains(currentSourcePort))
					smtpService = smtpService + currentSourcePort + ", ";
				if (httpList.contains(currentSourcePort))
					httpService = httpService + currentSourcePort + ", ";
				if (dnsList.contains(currentSourcePort))
					dnsService = dnsService + " " + currentSourcePort + ", ";
				if (ftpList.contains(currentSourcePort))
					ftpService = ftpService + " " + currentSourcePort + ", ";
				if (pop3List.contains(currentSourcePort))
					pop3Service = pop3Service + " " + currentSourcePort + ", ";
				if (imapList.contains(currentSourcePort))
					imapService = imapService + " " + currentSourcePort + ", ";

			}

			counter++;

		}
		System.out.println("quantidade de portos TCP de origem únicos que apareceram no trace: " + sourcePorts.size()
				+ " associados aos principais servicos de redes:\r\n" + smtpService + "\r\n" + httpService + "\r\n"
				+ dnsService + "\r\n" + ftpService + "\r\n" + pop3Service + "\r\n" + imapService);
	}

	/**
	 * Analyzes the trace and answers the question Q4:"Qual é o número de pacotes
	 * ICMP contidos no trace? Quais são os tipos de pacotes?" by printing the
	 * answer in the console
	 */
	public void Q4() {
		int counter = 0;
		int ICMPcounter = 0;
		ArrayList<String> listOfTypesOfICMP = new ArrayList<String>();

		while (counter < data.size()) {

			String protocol = data.get(counter)[6].replaceAll("\"", "");
			String typeOfICMP = data.get(counter)[7].replaceAll("\"", "");
             //checks if the current protocol is ICMP
			if (protocol.contains("ICMP")) {
				ICMPcounter++;
                 //if it is ICMP and we hadnt seen its type before adds it to the list of types
				if (!listOfTypesOfICMP.contains(typeOfICMP) && protocol.equals("ICMP") ) {
					listOfTypesOfICMP.add(typeOfICMP);
				}
			}
			counter++;
		}

		System.out.println("tipos de pacotes ICMP: " + listOfTypesOfICMP.toString().replace("[", " ").replace("]", " ")
				+ "\r\nNúmero de pacotes ICMP contidos no trace: " + ICMPcounter);
	}

	/**
	 * Analyzes the trace and answers the question Q5:"Qual é o tamanho médio, máximo e mínimo dos pacotes no trace?
	 * Imprimir um gráfico do histograma."
	 * by printing the answer in the console. Writes the legth of all the packets in the trace to a file
	 * @param printWriter 
	 * 
	 * @param pw PrintWriter that will write to a file where the values of each packet's length will be written. Will be used to create the histogram
	 * @throws IOException 
	 */
	public void Q5 (PrintWriter pw) throws IOException {
		int biggestPacket = 0;
		int smallestPacket = Integer.parseInt(slice(data.get(0)[8]));
		int numOfPackets = 0, totalLength = 0; //to calculate the average packet length later
		int packetLength;
		pw.println("\n Tamanho de cada pacote:");
		for (String[] i : data) {
			String length = slice(i[8]);
			packetLength = Integer.parseInt(length);
			pw.println(packetLength);
			if(packetLength > biggestPacket)
				biggestPacket = packetLength;
			else if(packetLength < smallestPacket)
				smallestPacket = packetLength;
			totalLength+=packetLength;
			numOfPackets++;
		}
		System.out.println("O tamanho máximo de um pacote é: " + biggestPacket + ", o mínimo é: " + smallestPacket + 
				" e o tamanho médio é: " + totalLength/numOfPackets + ".");
	}

	/**
	 * Analyzes the trace and answers the question Q6:" Assumindo que o envio de um pacote SYN representa uma tentativa
	 * de estabelecer uma ligação TCP, indique quantas dessas tentativas aparecem no trace. Indique qual é o endereço IP 
	 * que fez mais tentativas deste tipo." by printing the answer in the console
	 */
	public void Q6 () {

		int counter = 0 ;
		int numPacoteSyn = 0;
		ArrayList <String> sourceIp = new ArrayList <String>();

		while(counter < data.size()) {
			String currentFlag = data.get(counter)[9];
			String currentSourceIp = data.get(counter)[2];
			//check if the current flag TCP contains the bits 0x002 representative of SYN
			if (currentFlag.contains("0x002")) {
				numPacoteSyn ++;
				sourceIp.add(currentSourceIp); //adds all the SYN packets to a list 
			}
			counter++;
		}
		String mostFrequentIp = elementoMaisFrequente(sourceIp);
		System.out.println("Apareceram no trace " + numPacoteSyn + " tentativas de estabelecer uma ligação TCP. "
				+ "O endereço IP "+ mostFrequentIp + " foi o que fez mais tentativas deste tipo.");

	}

	/**
	 * Auxiliary function that given a list, finds the most frequent element in it. 
	 * @param array list of Strings
	 * @return the most frequent element in a list 
	 */
	public static String elementoMaisFrequente (ArrayList <String> array) {

		int maxcount = 0;
		String elementMaisFreq=null;

		for (int i = 0; i < array.size(); i++) {
			int count = 0;
			for (int j = 0; j < array.size(); j++) {
				if (array.get(i) == array.get(j)) {
					count++;
				}
			}
			if (count > maxcount) {
				maxcount = count;
				elementMaisFreq = array.get(i);
			}
		}
		return elementMaisFreq;
	}

	/**
	 * Analyzes the trace and answers the question Q7: "Quantas ligações TCP existem no trace?"
	 * by printing the answer in the console
	 */
	public void Q7 () {

		int counter = 0 ;
		int numProtocols = 0;

		while(counter < data.size()) {
			String currentProtocol = data.get(counter)[6];
			//check if the current protocol corresponds to a TCP Link
			if( currentProtocol.contains("TCP") || currentProtocol.contains("TLSv1") )
				numProtocols ++;
			counter++;
		}
		System.out.println("Existem " + numProtocols + " ligações TCP no trace.");
	}

	/**
	 * Analyzes the trace and answers the questions Q8 and Q9:"Agregue o volume de tráfego baseado no IP recetor/emissor. 
	 * Qual é o endereço IP que recebe/envia a fração de tráfego maior? Quantos bytes? Quantos pacotes? Qual é o débito (
	 * em bits por segundo)? " by printing the answers in the console
	 * 
	 * @param ip represents the index of the column contaning either the Destination IP (Q8) or Source IP (Q9) 
	 */
	public void Q8and9 (int ip) {
		HashMap<String, Double[]> map = new HashMap<String, Double[]>();
		for (String[] i : data) {
			String currentIP = slice(i[ip]);
			double packetSize = Double.parseDouble(slice(i[8]));
			double packetTime = Double.parseDouble(slice(i[1]));
			//if the HashMap already has this IP, update the total number of bytes and packets received/sent, 
			//as well as the time at which the last packet was received/sent
			if(map.containsKey(currentIP)) {
				double oldTraffic = map.get(currentIP)[0];
				double newTraffic= oldTraffic + packetSize;
				double firstPacketTime = map.get(currentIP)[1];
				double numOfPackets = map.get(currentIP)[3];
				Double[] newValues = new Double[]{newTraffic,firstPacketTime,packetTime,numOfPackets+1};
				map.replace(currentIP, newValues);
			}
			//if the HashMap doesn't have IP yet, create an entry and register the time at which this first packet was received/sent
			else {
				Double[] newValues = new Double[]{packetSize,packetTime, null, 1.0};
				map.put(currentIP, newValues);
			}
		}
		String mostTrafficIP = null;
		double mostTraffic = 0;
		double initialTime = 0;
		double finalTime = 0;
		double numOfPackets = 0;
		//get the required information about the issuer/receiver with the most traffic
		for(Entry<String, Double[]> entry : map.entrySet()) {
			double traffic = entry.getValue()[0]; //this will give us the total traffic of this entry
			if(traffic > mostTraffic) {
				mostTraffic = traffic;
				mostTrafficIP = entry.getKey();
				initialTime = entry.getValue()[1];
				finalTime = entry.getValue()[2];
				numOfPackets = entry.getValue()[3];
			}
		}
		//for Q8, receiver
		if(ip == 3) {
			System.out.println("O endereço IP que recebe mais tráfego é o endereço " + 
					mostTrafficIP + " que recebe um total de " + (int) mostTraffic + " bytes, que constituem "
					+ (int) numOfPackets+ " pacotes, e cujo débito em bits por segundo é " + mostTraffic*8/(finalTime-initialTime));
		}
		//for 19, issuer
		else {
			System.out.println("O endereço IP que envia mais tráfego é o endereço " + 
					mostTrafficIP + " que envia um total de " + (int) mostTraffic + " bytes, que constituem "
					+ (int) numOfPackets+ " pacotes, e cujo débito em bits por segundo é " + mostTraffic*8/(finalTime-initialTime));
		}
	}
	
	/**
	 * Auxiliary function. Slices a string in order to cut the quotation marks from it and be able to parse it as a number.
	 * @param str string that will be sliced
	 * @return the string formatted as expected
	 */
	private String slice(String str) {
		return str.substring(1, str.length()-1);
	}
}