package leitor;

public class resultados {

	String name;
	public int[] totalSlots;
	public int[] totalSlotsEmpty;
	public int[] totalSlotsColided;
	public int[] totalBitsEtiquetaLeitor;
	public long[] tempoTotalExecucao;
	
	public resultados(String name,int k) {
		this.totalSlots = new int[k];
		this.name = name;
		this.totalSlotsEmpty = new int[k];
		this.totalSlotsColided = new int[k];
		this.totalBitsEtiquetaLeitor = new int[k];
		this.tempoTotalExecucao = new long[k];
	}
	
	
}
