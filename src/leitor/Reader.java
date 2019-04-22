package leitor;
import tags.Tag;
import java.util.*;
public class Reader {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		for(int i = 1; i <= 10; i++) { //inicio round query
			for(int k = 1; k <= 100; k++) {
				Tag a = new Tag();			
				Set<String> M;
				Queue<String> Q = new LinkedList<>();
				M = a.getTags(i*100);
				Q.add("0");
				Q.add("1");
				int c = 0, sv = 0;
				while(!M.isEmpty()) {
					String query = Q.poll();
					for(String id: M) {
						if(query.contentEquals(id.subSequence(0, query.length()))) {
							if(query.length() == 64) {
								M.remove(query);
								sv++;
							}else{
								c++;
								Q.add(query+"0");
								Q.add(query+"1");
							}
							break;
						}
						
					}
				}
				
			}

		}

	}

}
