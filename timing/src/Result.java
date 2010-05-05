public class Result {
	public Integer collId = 0;

	public Utterance utterance = null;
	public Utterance translatedUtterance = null;
	
	public Integer rater1 = 0;
	public Integer rater2 = 0;
	public Integer rater3 = 0;
	public Integer rater4 = 0;
	
	public Result(Integer c, Utterance u, Utterance tu, Integer r1, Integer r2, Integer r3, Integer r4) {
		collId = c;

		utterance = u;
		translatedUtterance = tu;
		
		rater1 = r1;
		rater2 = r2;
		rater3 = r3;
		rater4 = r4;
	}
}
