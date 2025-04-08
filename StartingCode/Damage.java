

public class Damage
{
	private double randomDamage;
	private double conditionalDamage;

	private Pet receiver;
	private Pet damager;
	
	public Damage(double randomDamage, double conditionalDamage, Pet receiver, Pet damager)
	{
		this.randomDamage = randomDamage;
		this.conditionalDamage = conditionalDamage;
		this.receiver = receiver;
		this.damager = damager;
	}

	public double getRandomDamage()
	{
		return randomDamage;
	}

	public void setRandomDamage(double randomDamage)
	{
		this.randomDamage = randomDamage;
	}
	
	public double getConditionalDamage()
	{
		return conditionalDamage;
	}
	
	public void setConditionalDamage(double conditionalDamage)
	{
		this.conditionalDamage = conditionalDamage;
	}

	public double calculateTotalDamage()
	{
		return randomDamage + conditionalDamage;
	}

	/**
	 * Gets the pet that is dealing the damage
	 * @return Damage dealer
	 */
	public Pet getDamager() {
		return damager;
	}

	/**
	 * Gets the pet that is receiving the damage
	 * @return Damage receiver
	 */
	public Pet getReceiver() {
		return receiver;
	}
}
