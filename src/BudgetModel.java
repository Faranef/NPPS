import java.io.Serializable;

public class BudgetModel  implements Serializable
{
    public double Budget;
    public double SoldElectricity;
    public double TotalIncome;

    public double FuelRodPrice;
    public double Maintenance;
    public double ResearchCost;
    
    public double TotalLoss;
    
    public int CurrentMonth;
    public int CurrentYear;
    
    public BudgetModel() 
    {
    }

    public double GetResearchCost()
    {
        return ResearchCost;
    }

    public void SetResearchCost(double researchCost)
    {
        ResearchCost = researchCost;
    }
    
    public double GetBudget()
    {
        return Budget;
    }

    public void Increase(double budget)
    {
        Budget += budget;
    }

    public void Deduct(double value)
    {
        Budget -= value;
    }


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
        Maintenance += maintenance;
    }
   
    public double GetFuelRodTotalCost()
    {
        return FuelRodPrice;
    }

    public void SetFuelRodTotalCost(double fuelRodPrice)
    {
        FuelRodPrice += fuelRodPrice;
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
