package pt.unl.fct.di.apdc.apdc2022ai.utils;

import java.util.HashMap;
import java.util.Map;

public enum Role {
	
	USER("USER",1), GBO("GBO",2), GS("GS",3), SU("SU",4);
	
	public String role;
	public long weight;
	
	Role( String role, long weight) {
		this.role = role;
		this.weight = weight;
	}
	
	private static final Map<Long, String> map = new HashMap<Long, String>();
    static
    {
    	long i = 1;
        for (Role role : Role.values())
            map.put(i++, role.role);
    }

    /**
    * Get difficulty from value
    * @param value Value
    * @return Difficulty
    */
    public static String from(long weight)
    {
        return map.get(weight);
    }
    
}

