import java.util.*;

public class BudgetService 
{
    private double MaintenanceCost = 7.5;
    private double FuelRodPrice  = 50_000;
    private static BudgetService Instance = null;
    private List<BudgetModel> BudgetList = new ArrayList<BudgetModel>();

    public double GetMaintenanceCost()
    {
        return MaintenanceCost;
    }

    public void SetMaintenanceCost(double maintenanceCost)
    {
        MaintenanceCost = maintenanceCost;
    }

        public double GetFuelRodPrice()
    {
        return FuelRodPrice;
    }

    public void SetFuelRodPrice(double fuelRodPrice)
    {
        FuelRodPrice = fuelRodPrice;
    }

    public List<BudgetModel> GetBudgetList()
    {
        return BudgetList;
    }

    /// Gets the last inserted budget entry
    public BudgetModel GetLastBudgetFromList()
    {
        return BudgetList.get(BudgetList.size()-1);
    }

    ///Gets the first budget entry
    public BudgetModel GetFirstBudgetList()
    {
        return BudgetList.get(0);
    }

    public void CreateBudgetModel(double currentBudget, int month, int year)
    {
        BudgetModel budgetModel = new BudgetModel();
        budgetModel.Increase(currentBudget);
        budgetModel.SetCurrentMonth(month + 1);
        budgetModel.SetCurrentYear(year);
        BudgetList.add(budgetModel);
    }

    private BudgetService() 
    {
        Calendar cal = Calendar.getInstance();
        var month = cal.get(Calendar.MONTH);
        var year = cal.get(Calendar.YEAR);

        CreateBudgetModel(100000.00,month,year);
    }

    public static BudgetService GetInstance()
    {
        if(Instance == null)
        {
            Instance = new BudgetService();
        }

        return Instance;
    }

    
}
