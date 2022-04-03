package pt.unl.fct.di.apdc.apdc2022ai.utils;

public enum State {
	ACTIVE("ACTIVE"), INACTIVE("INACTIVE");
	
	private String state; 
	
	State(String state) {
		this.state = state;
	}
	
	public String getState() {
		return state;
	}
}