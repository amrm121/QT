package leitor;

public class resultados {

	String name;
	public double[] totalSlots;
	public double[] totalSlotsEmpty;
	public double[] totalSlotsColided;
	public double[] totalBitsEtiquetaLeitor;
	public double[] tempoTotalExecucao;
	
	public resultados(String name,int k) {
		this.totalSlots = new double[k];
		this.name = name;
		this.totalSlotsEmpty = new double[k];
		this.totalSlotsColided = new double[k];
		this.totalBitsEtiquetaLeitor = new double[k];
		this.tempoTotalExecucao = new double[k];
	}
	
	
}
