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
			int cTotal = 0;
			writer.write("Teste " + i + "\n");
			System.out.println("Teste " + i);
			for(int k = 1; k <= 100; k++) {
				Tag a = new Tag();			
				Set<String> M;
				Queue<String> Q = new LinkedList<>();
				M = a.getTags(i*100);
				Q.add("0");
				Q.add("1");
				int c = 0;
				while(!M.isEmpty()) {
					String query = Q.poll();
					for(String id: M) {
						if(query.contentEquals(id.subSequence(0, query.length()))) {
							if(query.length() == 64) {
								M.remove(query);
							}else{
								c++;
								Q.add(query+"0");
								Q.add(query+"1");
							}
							break;
						}
						
					}
				}
				cTotal+=c;
				
			}//fim de 100 simulações por qtd
			long endTime =System.currentTimeMillis();
			long totalTime =endTime - startTime;
			System.out.println(cTotal/100 + " colisoes ");
			System.out.println(totalTime/1000 + "segundos");
			writer.write(totalTime/1000 + "segundos : " + cTotal/100 + "colisoes\n");
			
		}
		writer.close();
	}

}
