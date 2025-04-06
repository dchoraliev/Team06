

public class Damage
{
	private double randomDamage;
	private double conditionalDamage;

	private Pet reciever;
	private Pet damager;
	
	public Damage(double randomDamage, double conditionalDamage, Pet reciever, Pet damager)
	{
		this.randomDamage = randomDamage;
		this.conditionalDamage = conditionalDamage;
		this.reciever = reciever;
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

	public Pet getDamager() {
		return damager;
	}

	public Pet getReciever() {
		return reciever;
	}
}
