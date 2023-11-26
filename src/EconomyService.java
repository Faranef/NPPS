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
    
    public double GetFuelRodPrice()
    {
        return FuelRodPrice;
    }

    public void SetFuelRodPrice(double fuelRodPrice)
    {
        FuelRodPrice = fuelRodPrice;
    }

    public void SetTotalProfit(double totalProfit)
    {
        TotalProfit = totalProfit;
    }

    public void SetTotalLoss(double totalLoss)
    {
        TotalLoss = totalLoss;
    }

    public void AddToBalance(double income)
    {
        TotalProfit = TotalProfit + income;
        Balance = Balance + income;
    }

    public void DeductFromBalance(double expenditure)
    {
        TotalLoss = TotalLoss + expenditure;
        Balance = Balance - expenditure;
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

    public void NewGameSetting()
    {
        Balance = 0;
        TotalProfit = 0;
        TotalLoss = 0;
        FuelRodPrice = 50000;
    }
}
