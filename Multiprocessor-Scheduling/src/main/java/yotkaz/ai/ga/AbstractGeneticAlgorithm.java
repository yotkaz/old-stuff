package yotkaz.ai.ga;

/**
 * @author yotkaz
 *
 */
public abstract class AbstractGeneticAlgorithm<SolutionType> {
	
	public abstract void init();
	
	public abstract void crossovers();
	
	public abstract void mutations();
	
	public abstract void selection();
	
	public abstract boolean isSatisfyingSolution();
	
	public abstract SolutionType chooseSolution();
	
	public SolutionType process(int maxGenerations) {
		init();
		for (int i = 0; i < maxGenerations; i++) {
			loopStart();
			crossovers();
			mutations();
			selection();
			if (isSatisfyingSolution()) {
				break;
			}
		}
		return chooseSolution();
	}
	
	public void loopStart() {
		/** to implement in subclasses */
	}
	
}