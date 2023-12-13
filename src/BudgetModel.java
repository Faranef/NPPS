public class BudgetModel 
{
    public double Budget;
    
    public double GetBudget()
    {
        return Budget;
    }

    public void SetBudget(double budget)
    {
        Budget += budget;
    }

    public void Deduct(double value)
    {
        Budget -= value;
    }

    private double SoldElectricity = 0;
    private double MiscellaneausIncome = 0;
    private double TotalIncome = 0;
    
    private double FuelRodPrice = 0;
    private double Maintenance = 0;
    private double MiscellaneausLoss = 0;
    private double TotalLoss = 0;

    private int CurrentMonth;
    private int CurrentYear;

    public int GetCurrentYear()
    {
        return CurrentYear;
    }

    public void SetCurrentYear(int currentYear)
    {
        CurrentYear = currentYear;
    }

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
        SoldElectricity += value;
    }
    
    @Override
    public String toString()
    {
        return Integer.toString(CurrentMonth) + "/" + Integer.toString(CurrentYear);
    }


}
