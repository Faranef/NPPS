public class EconomyService 
{
    private static EconomyService Instance = null;
    private double  Balance = 0;
    

    public double GetBalance()
    {
        return Balance;
    }

    public void SetBalance(double balance)
    {
        Balance = balance;
    }

    public void AddToBalance(double profit)
    {
        Balance = Balance + profit;
    }

    public void DeductFromBalance(double loss)
    {
        Balance = Balance - loss;
    }

    private EconomyService() 
    {
    }

    public static EconomyService GetInstance()
    {
        if(Instance == null)
        {
            Instance = new EconomyService();
        }

        return Instance;
    }


}
