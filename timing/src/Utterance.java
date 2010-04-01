public class Utterance implements Comparable {
	
	public enum Role { 
		CLIENT("Client"), 
		DEVELOPER("Developer"), 
		OTHER("Other");
		
		private final String role;
		
		Role(String role) {
			this.role = role;
		}
			
		@Override 
		public String toString() {
		    StringBuilder result = new StringBuilder();
		    result.append(role);
		    return result.toString();
		}
	};
	
	//enum CategoryType { };
	
	private Integer turn;
	private String who;
	private Role role;
	private String utterance;
	private /* CategoryType */ String category;
	
	public Utterance clona() {
		Utterance ret = new Utterance(turn, who, role, utterance, category);
		return ret;
	}
	
	public Utterance(Integer t, String w, Role r, String u, String c) {
		this.turn = t;
		this.who = w;
		this.role = r;
		this.utterance = u;
		this.category = c;
	}
	
	public Integer getTurn() {
		return turn;
	}

	public void setTurn(Integer turn) {
		this.turn = turn;
	}

	public String getWho() {
		return who;
	}

	public void setWho(String who) {
		this.who = who;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getUtterance() {
		return utterance;
	}

	public void setUtterance(String utterance) {
		this.utterance = utterance;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	@Override
	public int compareTo(Object o) {
		if (!(o instanceof Utterance))
			throw new ClassCastException("A Utterance object expected.");
		Utterance u = ((Utterance) o);
		int ret = turn.compareTo(u.getTurn());
		return ret;
	}
	
	@Override 
	public String toString() {
	    StringBuilder result = new StringBuilder();
	    result.append("(" + turn + " " + who + " " + role + " " + utterance + " " + category + ")");
	    return result.toString();
	}

}
