public class BudgetModel 
{
    private double Budget = 1000000.00;
    
    private double SoldElectricity = 0;
    private double MiscellaneausIncome = 0;
    private double TotalIncome = 0;
    
    private double FuelRodPrice = 0;
    private double Maintenance = 0;
    private double MiscellaneausLoss = 0;
    private double TotalLoss = 0;

    private int CurrentMonth;

    public int GetCurrentMonth()
    {
        return CurrentMonth;
    }

    public void SetCurrentMonth(int currentMonth)
    {
        CurrentMonth = currentMonth;
    }

    public double GetMaintenance()
    {
        return Maintenance;
    }

    public void SetMaintenance(double maintenance)
    {
        Maintenance = maintenance;
    }

    public double GetMiscellaneausLoss()
    {
        return MiscellaneausLoss;
    }

    public void SetMiscellaneausLoss(double miscellaneausLoss)
    {
        MiscellaneausLoss = miscellaneausLoss;
    }

    public double GetMiscellaneausIncome()
    {
        return MiscellaneausIncome;
    }

    public void SetMiscellaneausIncome(double miscellaneausIncome)
    {
        MiscellaneausIncome = miscellaneausIncome;
    }

   
    public double GetFuelRodPrice()
    {
        return FuelRodPrice;
    }

    public void SetFuelRodPrice(double fuelRodPrice)
    {
        FuelRodPrice = fuelRodPrice;
    }

    public double GetTotalLoss()
    {
        return TotalLoss;
    }

    public void SetTotalLoss(double totalLoss)
    {
        TotalLoss = totalLoss;
    }

    public double GetTotalProfit()
    {
        return TotalIncome;
    }

    public void SetTotalProfit(double totalIncome)
    {
        TotalIncome = totalIncome;
    }


    public double GetSoldElectricity()
    {
        return SoldElectricity;
    }

    public void SetSoldElectricity(double value)
    {
        SoldElectricity = value;
    }
    
}
