package edu.team06.entity;

import edu.team06.util.Utils;

public enum PetTypes
{
	POWER,
	SPEED,
	INTELLIGENCE;
	
	@Override
	public String toString()
	{		
		return Utils.convertEnumString(this.name());
	}
}
