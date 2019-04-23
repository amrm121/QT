package leitor;
import tags.Tag;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;



public class Reader {
	
	public static void main(String[] args) throws IOException {
		
		
		
		List<Set<String>> tags = new ArrayList<Set<String>>();
		resultados[] result = new resultados[2];
		tags = tagsAlocation(tags);
		result = QT(result, tags, 0, 10);		
		result = QTsc(result, tags, 1, 10);
		
		
		
		plotTotalSlots("Total Slots", "Total Slots", result, false, false);
		plotTempoExec("Execution Time", "Execution Time (ms)", result, false, true);
		//plotEtiquetaLeitor("Bits Etiqueta_Leitor", "Bits Etiqueta_Leitor", result, false, false);
		plotSlotsColided("Slots Colided", "Slots Colided", result, false, false);
		plotSlotsEmpty("Slots Empty", "Slots Empty", result, false, false);
		
		
	}
	
	public static List<Set<String>> tagsAlocation(List<Set<String>> tags){
		
		
		
		for(int i=1;i<=10;i++) {
			Tag a = new Tag();
			Set<String> M;
			M = a.getTags(i*100);
			tags.add(M);
			System.out.println("Tags: "+i*100+" geradas");
		}
		return tags;
		
	}

	public static resultados[] QT(resultados[] result, List<Set<String>> tags, int indexArray, int numTestes) {
	
		resultados resultQT = new resultados("QT",numTestes);
		
		for(int i = 1; i <= 10; i++){ //inicio round query
		long startTime =System.currentTimeMillis();
		int cTotal = 0, sV = 0;
		int totalSlots=0, sucess=0;
		
		System.out.println("Teste " + i);
		for(int k = 1; k <= 100; k++) {
					
			Set<String> M;
			Queue<String> Q = new LinkedList<>();
			M = tags.get(i-1);
			int c=0,sv =0;
			Iterator<String> it = M.iterator();
			Q.add("0");
			Q.add("1");
			while(!M.isEmpty()) {
				boolean t = true;
				if(Q.isEmpty()) break;
				String query = Q.poll();
				int siz = query.length();
				while(it.hasNext()) {
					String id = it.next();
					if(query.contentEquals(id.subSequence(0, siz))) {
						c++;
						if(siz == 64) { 
							M.remove(query);
							t=false; totalSlots++;
							break;
							} 
						if(t == true && c > 1) {
							Q.add(query+"0");
							Q.add(query+"1");							
							t = false; 
						}
					}						
				}if(t) {
					sv++;
				}
				cTotal+=c;
				sV+=sv;				
				totalSlots = sv + c + totalSlots;
				c = 0;
				sv = 0;
				it = M.iterator();
			}
		}//fim de 100 simulações por qtd
		long endTime =System.currentTimeMillis();
		long totalTime =endTime - startTime;
		resultQT.totalSlots[i-1] = totalSlots/100;
		resultQT.totalSlotsColided[i-1] = cTotal/100;
		resultQT.totalSlotsEmpty[i-1] = sV/100;
		resultQT.tempoTotalExecucao[i-1] = totalTime; //(ms)
		
		System.out.println(cTotal/100 + " colisoes ");
		System.out.println(sV/100 + " slots vazios ");
		System.out.println(totalTime + " milisegundos");
		
		System.out.println(resultQT.totalSlots[i-1]+ " Total Slots");
		System.out.println("-----------------------------------------");
		
		
	}
		result[indexArray] = resultQT;
		return result;	
		
	}
	
	
	public static resultados[] QTsc(resultados[] result, List<Set<String>> tags, int indexArray, int numTestes) {
		
		resultados resultQTsc = new resultados("QTsc",numTestes);
		
		for(int i = 1; i <= 10; i++){ //inicio round query
		long startTime =System.currentTimeMillis();
		int cTotal = 0, sV = 0;
		int totalSlots=0, sucess=0;
		
		System.out.println("Teste " + i);
		for(int k = 1; k <= 100; k++) {		
			Set<String> M;
			Queue<String> Q = new LinkedList<>();
			M = tags.get(i-1);
			int c=0,sv =0;
			Iterator<String> it = M.iterator();
			Q.add("0");
			Q.add("1");
			while(!M.isEmpty()) {
				boolean t = true;
				if(Q.isEmpty()) break;
				String query = Q.poll();
				int siz = query.length();
				while(it.hasNext()) {
					String id = it.next();
					if(query.contentEquals(id.subSequence(0, siz))) {
						c++;
						if(siz == 64) { M.remove(query); t = false; sucess++;  break;} 
						if(t == true && c > 1) {
							Q.add(query+"0");
							Q.add(query+"1");							
							t = false; 
						}
					}				
				}if(t) {
					sv++;
					if(query.length()>=64) {
					
					}else {
						Q.add(query+"0");						
					}
					Q.poll();
					
				}
				cTotal+=c;
				sV+=sv;
				totalSlots = c + sv + totalSlots + sucess;
				c = 0;
				sv = 0;
				sucess = 0;
				it = M.iterator();
			}
		}//fim de 100 simulações por qtd
		long endTime =System.currentTimeMillis();
		long totalTime =endTime - startTime;
		resultQTsc.totalSlots[i-1] = totalSlots/100;
		resultQTsc.totalSlotsColided[i-1] = cTotal/100;
		resultQTsc.totalSlotsEmpty[i-1] = sV/100;
		resultQTsc.tempoTotalExecucao[i-1] = totalTime; //(ms)
		
		System.out.println(cTotal/100 + " colisoes ");
		System.out.println(sV/100 + " slots vazios ");
		System.out.println(totalTime + " milisegundos");
		
		System.out.println(resultQTsc.totalSlots[i-1]+ " Total Slots");
		System.out.println("-----------------------------------------");
		
		
	}
		result[indexArray] = resultQTsc;
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

  Runtime rt = Runtime.getRuntime();
  Process pr = rt.exec(cmd);
}
}