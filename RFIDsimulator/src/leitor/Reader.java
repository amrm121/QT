package leitor;
import tags.Tag;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;



public class Reader {
	
	public static void main(String[] args) throws IOException {
		
		
		
		ArrayList<ArrayList<Set<String>>> tags = new ArrayList<ArrayList<Set<String>>>(100);
		tags = tagsAlocation(tags);
		//TesteIDTags(tags); //verificação antiga pra ver se todas tags são unicas
		resultados[] result = new resultados[4];
		
		System.out.println("qt <----");
		result = QT(result, tags, 0, 10);	
		System.out.println("qt sc  <----");
		result = QTsc(result, tags, 1, 10);
		System.out.println("qt quaternario  <----");
		result = QTquaternario(result,tags,2,10);
		System.out.println("qt sc quaternario <-----");
		result = QTscQuaternario(result, tags, 3, 10);
		
		
		plotTotalSlots("Total Slots", "Total Slots", result, false, false);
		plotTempoExec("Execution Time", "Execution Time (ms)", result, false, false);
		//plotEtiquetaLeitor("Bits Etiqueta_Leitor", "Bits Etiqueta_Leitor", result, false, false);
		plotSlotsColided("Slots Collided", "Slots Collided", result, false, false);
		plotSlotsEmpty("Slots Empty", "Slots Empty", result, false, false);
		
		
	}
	
	public static ArrayList<ArrayList<Set<String>>> tagsAlocation(ArrayList<ArrayList<Set<String>>> tags){
		
		
		for(int j = 0; j < 100; ++j ) {
			tags.add(new ArrayList<Set<String>>());
		}
		for(int i=1;i<=10;i++) {
			for(int k=1;k<=100;k++) {
				Tag a = new Tag();
				Set<String> M;
				M = a.getTags(i*100);
				tags.get(k-1).add(M);
				System.out.println("Tags"+k+": "+i*100+" geradas");
			}	
		}
		return tags;
		
	}
	
	public static void TesteIDTags(ArrayList<ArrayList<Set<String>>> tags){
		
		System.out.println();
		System.out.println("Teste TAGS ---------");
		Set<String> teste = new HashSet<String>();
		for(int i=1;i<=10;i++) {
			for(int k=1;k<=100;k++) {
				teste = tags.get(k-1).get(i-1);
				int contagemAux = 0;
				System.out.println("teste tamanho tags "+k+": "+teste.size());
				Iterator<String> iterator = teste.iterator();
				Iterator<String> iteratorAux = teste.iterator();
				while(iteratorAux.hasNext()) {
					String atual = iteratorAux.next();
					int contagem = 0;
					while(iterator.hasNext()) {
						
						String next = iterator.next();
						if(atual.equals(next)) {
							contagem++;
						}
					}
					if(contagem >1) { //se for maior que 1 quer dizer que nao eh unica
						System.out.println("contagem: "+contagem);	
						contagemAux = contagem + contagemAux;
					}
				}
				if(contagemAux > 0) {
					System.out.println("algo errado");
				}else {
					System.out.println("tudo ok");
				}
			}
		}
	}
	
	public static resultados[] QT(resultados[] result, ArrayList<ArrayList<Set<String>>> tags, int indexArray, int numTestes) {
		
		resultados resultQT = new resultados("QT",numTestes);
		for(int i = 1; i <= 10; i++){ //inicio round query
		long startTime =System.currentTimeMillis();
		double cTotal = 0, sV = 0;
		double totalSlots=0, sucess=0;
		
		System.out.println("Teste " + i);
		for(int k = 1; k <= 100; k++) {
			Set<String> Memory = new HashSet<String>();
			Memory.addAll(tags.get(k-1).get(i-1));
			Queue<String> queue = new LinkedList<>();
			queue.add("0");
			queue.add("1");
			int sv = 0;
			int c = 0;
			int suc = 0;
			
			while(!Memory.isEmpty()) {
				
				if(queue.isEmpty()) {
					System.out.println("a fila esta vazia");
					break;
				}
				int colisao = 0;
				String query = queue.poll();
				int size = query.length();
				Iterator<String> iterador = Memory.iterator();
				String aux = "";
				while(iterador.hasNext()) {
					String id = iterador.next();
					String idSubsequence = (String) id.subSequence(0, size);
					if(query.equals(idSubsequence)){
						if(colisao<1) {
							aux = id;
						}
						colisao++;						
					}
					if(colisao>1) {
						
						break;
					}
				}
				if(colisao == 1) { //sucesso
					suc++;
					Memory.remove(aux);					
				}else if(colisao < 1) { // slot vazio
					sv++;
				}else if(colisao > 1) {// colisão
					queue.add(query+"0");
					queue.add(query+"1");
					c++;
				}
			}
			totalSlots = sv+ c + suc + totalSlots;			
			sV = sv + sV;
			cTotal = c + cTotal;
			sucess = suc + sucess;
			//System.out.println("---------------------------------->sucessos: "+suc);
		}//fim de 100 simulações por qtd
		long endTime =System.currentTimeMillis();
		long totalTime =endTime - startTime;
		resultQT.totalSlots[i-1] = totalSlots/100.0;
		resultQT.totalSlotsColided[i-1] = cTotal/100.0;
		resultQT.totalSlotsEmpty[i-1] = sV/100.0;
		resultQT.tempoTotalExecucao[i-1] = totalTime/100.0; //(ms)
		
		System.out.println(resultQT.totalSlotsColided[i-1] + " colisoes ");
		System.out.println(resultQT.totalSlotsEmpty[i-1] + " slots vazios ");
		System.out.println(resultQT.tempoTotalExecucao[i-1] + " milisegundos");		
		System.out.println(resultQT.totalSlots[i-1]+ " Total Slots");
		System.out.println("-----------------------------------------");
		
		
	}
		result[indexArray] = resultQT;
		return result;	
		
	}

	
	
public static resultados[] QTquaternario(resultados[] result, ArrayList<ArrayList<Set<String>>> tags, int indexArray, int numTestes) {
		
		resultados resultQT = new resultados("QTquaternario",numTestes);
		
		for(int i = 1; i <= 10; i++){ //inicio round query
		long startTime =System.currentTimeMillis();
		double cTotal = 0, sV = 0;
		double totalSlots=0, sucess=0;
		
		System.out.println("Teste " + i);
		for(int k = 1; k <= 100; k++) {
			Set<String> Memory = new HashSet<String>();
			Memory.addAll(tags.get(k-1).get(i-1));
			Queue<String> queue = new LinkedList<>();
			queue.add("00");
			queue.add("01");
			queue.add("10");
			queue.add("11");
			int sv = 0;
			int c = 0;
			int suc = 0;
			
			while(!Memory.isEmpty()) {
				
				if(queue.isEmpty()) {
					System.out.println("a fila esta vazia");
					break;
				}
				int colisao = 0;
				String query = queue.poll();
				int size = query.length();
				Iterator<String> iterador = Memory.iterator();
				String aux = "";
				while(iterador.hasNext()) {
					String id = iterador.next();
					String idSubsequence = (String) id.subSequence(0, size);
					if(query.equals(idSubsequence)){
						if(colisao<1) {
							aux = id;
						}
						colisao++;						
					}
					if(colisao>1) {
						
						break;
					}
				}
				if(colisao == 1) { //sucesso
					suc++;
					Memory.remove(aux);					
				}else if(colisao < 1) { // slot vazio
					sv++;
				}else if(colisao > 1) {// colisão
					queue.add(query+"00");
					queue.add(query+"01");
					queue.add(query+"10");
					queue.add(query+"11");
					c++;
				}
			}
			totalSlots = sv+ c + suc + totalSlots;			
			sV = sv + sV;
			cTotal = c + cTotal;
			sucess = suc + sucess;
			//System.out.println("---------------------------------->sucessos: "+suc);
		}//fim de 100 simulações por qtd
		long endTime =System.currentTimeMillis();
		long totalTime =endTime - startTime;
		resultQT.totalSlots[i-1] = totalSlots/100.0;
		resultQT.totalSlotsColided[i-1] = cTotal/100.0;
		resultQT.totalSlotsEmpty[i-1] = sV/100.0;
		resultQT.tempoTotalExecucao[i-1] = totalTime/100.0; //(ms)
		
		System.out.println(resultQT.totalSlotsColided[i-1] + " colisoes ");
		System.out.println(resultQT.totalSlotsEmpty[i-1] + " slots vazios ");
		System.out.println(resultQT.tempoTotalExecucao[i-1] + " milisegundos");		
		System.out.println(resultQT.totalSlots[i-1]+ " Total Slots");
		System.out.println("-----------------------------------------");
		
		
	}
		result[indexArray] = resultQT;
		return result;	
		
	}
	
	
public static resultados[] QTsc(resultados[] result, ArrayList<ArrayList<Set<String>>> tags, int indexArray, int numTestes) {
	
	resultados resultQT = new resultados("QTsc",numTestes);
	
	for(int i = 1; i <= 10; i++){ //inicio round query
	long startTime =System.currentTimeMillis();
	double cTotal = 0, sV = 0;
	double totalSlots=0, sucess=0;
	
	System.out.println("Teste " + i);
	for(int k = 1; k <= 100; k++) {
		Set<String> Memory = new HashSet<String>();
		Memory.addAll(tags.get(k-1).get(i-1));
		Queue<String> queue = new LinkedList<>();
		queue.add("0");
		queue.add("1");
		int sv = 0;
		int c = 0;
		int suc = 0;
		
		while(!Memory.isEmpty()) {
			
			if(queue.isEmpty()) {
				System.out.println("a fila esta vazia");
				break;
			}
			int colisao = 0;
			String query = queue.poll();
			int size = query.length();
			Iterator<String> iterador = Memory.iterator();
			String aux = "";
			while(iterador.hasNext()) {
				String id = iterador.next();
				String idSubsequence = (String) id.subSequence(0, size);
				if(query.equals(idSubsequence)){
					if(colisao<1) {
						aux = id;
					}
					colisao++;						
				}
				if(colisao>1) {
					
					break;
				}
			}
			if(colisao == 1) { //sucesso
				suc++;
				Memory.remove(aux);					
			}else if(colisao < 1) { // slot vazio
				sv++;
				query = queue.poll();
				queue.add(query+"0");
				queue.add(query+"1");
			}else if(colisao > 1) {// colisão
				queue.add(query+"0");
				queue.add(query+"1");
				c++;
			}
		}
		totalSlots = sv+ c + suc + totalSlots;			
		sV = sv + sV;
		cTotal = c + cTotal;
		sucess = suc + sucess;
		//System.out.println("---------------------------------->sucessos: "+suc);
	}//fim de 100 simulações por qtd
	long endTime =System.currentTimeMillis();
	long totalTime =endTime - startTime;
	resultQT.totalSlots[i-1] = totalSlots/100.0;
	resultQT.totalSlotsColided[i-1] = cTotal/100.0;
	resultQT.totalSlotsEmpty[i-1] = sV/100.0;
	resultQT.tempoTotalExecucao[i-1] = totalTime/100.0; //(ms)
	
	System.out.println(resultQT.totalSlotsColided[i-1] + " colisoes ");
	System.out.println(resultQT.totalSlotsEmpty[i-1] + " slots vazios ");
	System.out.println(resultQT.tempoTotalExecucao[i-1] + " milisegundos");		
	System.out.println(resultQT.totalSlots[i-1]+ " Total Slots");
	System.out.println("-----------------------------------------");
	
	
}
	result[indexArray] = resultQT;
	return result;	
	
}

public static resultados[] QTscQuaternario(resultados[] result, ArrayList<ArrayList<Set<String>>> tags, int indexArray, int numTestes) {
	
	resultados resultQT = new resultados("QTscQuaternario",numTestes);
	
	for(int i = 1; i <= 10; i++){ //inicio round query
	long startTime =System.currentTimeMillis();
	double cTotal = 0, sV = 0;
	double totalSlots=0, sucess=0;
	
	System.out.println("Teste " + i);
	for(int k = 1; k <= 100; k++) {
		Set<String> Memory = new HashSet<String>();
		Memory.addAll(tags.get(k-1).get(i-1));
		Queue<String> queue = new LinkedList<>();
		queue.add("00");
		queue.add("01");
		queue.add("10");
		queue.add("11");
		int sv = 0;
		int c = 0;
		int suc = 0;
		int svCount=0;
		while(!Memory.isEmpty()) {
			
			if(queue.isEmpty()) {
				System.out.println("a fila esta vazia");
				break;
			}
			int colisao = 0;
			String query = queue.poll();
			int size = query.length();
			Iterator<String> iterador = Memory.iterator();
			String aux = "";
			while(iterador.hasNext()) {
				String id = iterador.next();
				String idSubsequence = (String) id.subSequence(0, size);
				if(query.equals(idSubsequence)){
					if(colisao<1) {
						aux = id;
					}
					colisao++;						
				}
				if(colisao>1) {
					
					break;
				}
			}
			if(colisao == 1) { //sucesso
				suc++;
				Memory.remove(aux);					
			}else if(colisao < 1) { // slot vazio
				sv++;
				String queryLast = (String) query.subSequence(size-2,size);
				if(queryLast.equals("00")) {
					svCount++;
				}else if((queryLast.equals("01"))&&(svCount==1)) {
					svCount++;
				}else if((queryLast.equals("10"))&&(svCount==2)) {
					svCount++; // 3 slots vazios
					query = queue.poll();
					queue.add(query+"00");
					queue.add(query+"01");
					queue.add(query+"10");
					queue.add(query+"11");
					svCount=0; // resetar
				}else {
					svCount=0;
				}
			}else if(colisao > 1) {// colisão
				queue.add(query+"00");
				queue.add(query+"01");
				queue.add(query+"10");
				queue.add(query+"11");
				c++;
			}
		}
		totalSlots = sv+ c + suc + totalSlots;			
		sV = sv + sV;
		cTotal = c + cTotal;
		sucess = suc + sucess;
		//System.out.println("---------------------------------->sucessos: "+suc);
	}//fim de 100 simulações por qtd
	long endTime =System.currentTimeMillis();
	long totalTime =endTime - startTime;
	resultQT.totalSlots[i-1] = totalSlots/100.0;
	resultQT.totalSlotsColided[i-1] = cTotal/100.0;
	resultQT.totalSlotsEmpty[i-1] = sV/100.0;
	resultQT.tempoTotalExecucao[i-1] = totalTime/100.0; //(ms)
	
	System.out.println(resultQT.totalSlotsColided[i-1] + " colisoes ");
	System.out.println(resultQT.totalSlotsEmpty[i-1] + " slots vazios ");
	System.out.println(resultQT.tempoTotalExecucao[i-1] + " milisegundos");		
	System.out.println(resultQT.totalSlots[i-1]+ " Total Slots");
	System.out.println("-----------------------------------------");
	
	
}
	result[indexArray] = resultQT;
	return result;	
	
}	

public static void plotTempoExec(String title, String y_title, resultados[] result,
		boolean use_log, boolean scale_from_0 ) throws IOException
{
	
	 
	
	for(resultados algoritmos:result) {
		FileWriter arq = new FileWriter(title+"_"+algoritmos.name+".txt");
		PrintWriter gravarArq = new PrintWriter(arq);
		for(int i=1; i<=algoritmos.tempoTotalExecucao.length;i++) {
			gravarArq.printf(i*100+" "+algoritmos.tempoTotalExecucao[i-1]+"%n");			
		}
		arq.close();
	}
	
	
  String cmd = "gnuplot -e \"set grid;set terminal png size 800,600;set key fixed left top vertical Right noreverse enhanced autotitle box lt black linewidth 1.000 dashtype solid;set style data linespoints;set xlabel 'Tags';";
  cmd += "set ylabel '" + y_title + "';set output '" + title + ".png';";
  if(use_log)
      cmd += "set logscale y 10;";
  if(scale_from_0)
      cmd += "set yrange [0:*];";

  cmd += "plot ";
  for (resultados algoritmos : result)
      cmd += "'" + title + "_" + algoritmos.name + ".txt' title '" + algoritmos.name + "',";
  cmd += "\"";
  System.out.println(title+".png gerada. Abrir na pasta do projeto.");
  Runtime rt = Runtime.getRuntime();
  Process pr = rt.exec(cmd);
}

public static void plotTotalSlots(String title, String y_title, resultados[] result,
		boolean use_log, boolean scale_from_0 ) throws IOException
{
	
	
	for(resultados algoritmos:result) {
		FileWriter arq = new FileWriter(title+"_"+algoritmos.name+".txt");
		PrintWriter gravarArq = new PrintWriter(arq);
		for(int i=1; i<=algoritmos.totalSlots.length;i++) {
			gravarArq.printf(i*100+" "+algoritmos.totalSlots[i-1]+"%n");			
		}
		arq.close();
	}
	
	
  String cmd = "gnuplot -e \"set grid;set terminal png size 800,600;set key fixed left top vertical Right noreverse enhanced autotitle box lt black linewidth 1.000 dashtype solid;set style data linespoints;set xlabel 'Tags';";
  cmd += "set ylabel '" + y_title + "';set output '" + title + ".png';";
  if(use_log)
      cmd += "set logscale y 10;";
  if(scale_from_0)
      cmd += "set yrange [0:*];";

  cmd += "plot ";
  for (resultados algoritmos : result)
      cmd += "'" + title + "_" + algoritmos.name + ".txt' title '" + algoritmos.name + "',";
  cmd += "\"";
  System.out.println(title+".png gerada. Abrir na pasta do projeto.");
  Runtime rt = Runtime.getRuntime();
  Process pr = rt.exec(cmd);
}

public static void plotSlotsEmpty(String title, String y_title, resultados[] result,
		boolean use_log, boolean scale_from_0 ) throws IOException
{
	
	
	
	for(resultados algoritmos:result) {
		FileWriter arq = new FileWriter(title+"_"+algoritmos.name+".txt");
		PrintWriter gravarArq = new PrintWriter(arq);
		for(int i=1; i<=algoritmos.totalSlotsEmpty.length;i++) {
			gravarArq.printf(i*100+" "+algoritmos.totalSlotsEmpty[i-1]+"%n");			
		}
		arq.close();
	}
	
	
  String cmd = "gnuplot -e \"set grid;set terminal png size 800,600;set key fixed left top vertical Right noreverse enhanced autotitle box lt black linewidth 1.000 dashtype solid;set style data linespoints;set xlabel 'Tags';";
  cmd += "set ylabel '" + y_title + "';set output '" + title + ".png';";
  if(use_log)
      cmd += "set logscale y 10;";
  if(scale_from_0)
      cmd += "set yrange [0:*];";

  cmd += "plot ";
  for (resultados algoritmos : result)
      cmd += "'" + title + "_" + algoritmos.name + ".txt' title '" + algoritmos.name + "',";
  cmd += "\"";

  System.out.println(title+".png gerada. Abrir na pasta do projeto.");
  Runtime rt = Runtime.getRuntime();
  Process pr = rt.exec(cmd);
}

public static void plotSlotsColided(String title, String y_title, resultados[] result,
		boolean use_log, boolean scale_from_0 ) throws IOException
{
	
	
	
	for(resultados algoritmos:result) {
		FileWriter arq = new FileWriter(title+"_"+algoritmos.name+".txt");
		PrintWriter gravarArq = new PrintWriter(arq);
		for(int i=1; i<=algoritmos.totalSlotsColided.length;i++) {
			gravarArq.printf(i*100+" "+algoritmos.totalSlotsColided[i-1]+"%n");			
		}
		arq.close();
	}
	
	
  String cmd = "gnuplot -e \"set grid;set terminal png size 800,600;set key fixed left top vertical Right noreverse enhanced autotitle box lt black linewidth 1.000 dashtype solid;set style data linespoints;set xlabel 'Tags';";
  cmd += "set ylabel '" + y_title + "';set output '" + title + ".png';";
  if(use_log)
      cmd += "set logscale y 10;";
  if(scale_from_0)
      cmd += "set yrange [0:*];";

  cmd += "plot ";
  for (resultados algoritmos : result)
      cmd += "'" + title + "_" + algoritmos.name + ".txt' title '" + algoritmos.name + "',";
  cmd += "\"";
  
  System.out.println(title+".png gerada. Abrir na pasta do projeto.");
  Runtime rt = Runtime.getRuntime();
  Process pr = rt.exec(cmd);
}

public static void plotEtiquetaLeitor(String title, String y_title, resultados[] result,
		boolean use_log, boolean scale_from_0 ) throws IOException
{
	
	
	
	for(resultados algoritmos:result) {
		FileWriter arq = new FileWriter(title+"_"+algoritmos.name+".txt");
		PrintWriter gravarArq = new PrintWriter(arq);
		for(int i=1; i<=algoritmos.totalBitsEtiquetaLeitor.length;i++) {
			gravarArq.printf(i*100+" "+algoritmos.totalBitsEtiquetaLeitor[i-1]+"%n");			
		}
		arq.close();
	}
	
	
  String cmd = "gnuplot -e \"set grid;set terminal png size 800,600;set key fixed left top vertical Right noreverse enhanced autotitle box lt black linewidth 1.000 dashtype solid;set style data linespoints;set xlabel 'Tags';";
  cmd += "set ylabel '" + y_title + "';set output '" + title + ".png';";
  if(use_log)
      cmd += "set logscale y 10;";
  if(scale_from_0)
      cmd += "set yrange [0:*];";

  cmd += "plot ";
  for (resultados algoritmos : result)
      cmd += "'" + title + "_" + algoritmos.name + ".txt' title '" + algoritmos.name + "',";
  cmd += "\"";
  
  System.out.println(title+".png gerada. Abrir na pasta do projeto.");
  Runtime rt = Runtime.getRuntime();
  Process pr = rt.exec(cmd);
}
}