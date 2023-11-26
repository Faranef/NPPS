public class EconomyService 
{
    private static EconomyService Instance = null;
    private double Balance = 0;
    private double TotalProfit = 0;
    private double TotalLoss = 0;
    private double FuelRodPrice = 50000;

    public double GetBalance()
    {
        return Balance;
    }

    public void SetBalance(double balance)
    {
        Balance = balance;
    }

    public void SetTotalProfit(double totalProfit)
    {
        TotalProfit = totalProfit;
    }

    public void SetTotalLoss(double totalLoss)
    {
        TotalLoss = totalLoss;
    }

    public void AddToBalance(double profit)
    {
        TotalProfit = TotalProfit + profit;
        Balance = Balance + profit;
    }

    public void DeductFromBalance(double loss)
    {
        TotalLoss = TotalLoss + loss;
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
