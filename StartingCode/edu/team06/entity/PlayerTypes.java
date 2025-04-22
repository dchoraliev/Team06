package edu.team06.entity;

import edu.team06.util.Utils;

public enum PlayerTypes
{
	HUMAN,
	COMPUTER;
	
	@Override
	public String toString()
	{		
		return Utils.convertEnumString(this.name());
	}
}
