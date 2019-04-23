package leitor;
import tags.Tag;
import java.util.*;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class Reader {
	
	public static void main(String[] args) throws IOException {
		Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("QT.txt"), "utf-8"));
		
		for(int i = 1; i <= 10; i++) { //inicio round query
			long startTime =System.currentTimeMillis();
			int cTotal = 0, sV = 0;
			writer.write("Teste " + i + "\n");
			System.out.println("Teste " + i);
			for(int k = 1; k <= 100; k++) {
				Tag a = new Tag();			
				Set<String> M;
				Queue<String> Q = new LinkedList<>();
				M = a.getTags(i*100);
				int c = 0, sv = 0;
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
							if(siz == 64) { M.remove(query);  break;} 
							if(t == true && c > 1) {
								Q.add(query+"0");
								Q.add(query+"1");							
								t = false; 
							}
						}else{
							if(!t) sv++;
						}						
					}
					cTotal+=c;
					sV+=sv;
					c = 0;
					sv = 0;
					it = M.iterator();
				}
				
				
			}//fim de 100 simulações por qtd
			long endTime =System.currentTimeMillis();
			long totalTime =endTime - startTime;
			System.out.println(cTotal/100 + " colisoes ");
			System.out.println(sV/100 + " slots vazios ");
			System.out.println(totalTime + " milisegundos");
			writer.write(totalTime + " milisegundos,  " + cTotal/100 + " colisoes, " +  sV/100 + " slots vazios " +"\n");
			
		}
		writer.close();
	}

}
