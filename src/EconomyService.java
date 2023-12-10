import java.util.*;

public class EconomyService 
{
    private static EconomyService Instance = null;
    private List<BudgetModel> BudgetList = new ArrayList<BudgetModel>();

    public List<BudgetModel> GetBudgetList()
    {
        return BudgetList;
    }

    /// Gets the last inserted budget entry
    public BudgetModel GetLastBudgetList()
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
        budgetModel.SetBudget(currentBudget);
        budgetModel.SetCurrentMonth(month + 1);
        budgetModel.SetCurrentYear(year);
        BudgetList.add(budgetModel);
    }

    private EconomyService() 
    {
        Calendar cal = Calendar.getInstance();
        var month = cal.get(Calendar.MONTH);
        var year = cal.get(Calendar.YEAR);
        CreateBudgetModel(100000.00,month,year);
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
