package tags;
import java.util.*;

public class Tag {
	private Random r = new Random();
	private Set<String> M = new HashSet<String>();
	public Set<String> getTags(int n) {
		while(M.size() < n) {
			String aux = "";
			for(int i = 0; i < 64; i++) {
				aux += ""+r.nextInt(2);
			}
			
			M.add(aux);
		}
		return this.M;
	}
	
}