import java.util.*;

public class EconomyService 
{
    private static EconomyService Instance = null;
    private List<BudgetModel> BudgetList = new ArrayList<BudgetModel>();

    public List<BudgetModel> GetBudgetList()
    {
        return BudgetList;
    }

    public BudgetModel GetCurrentBudgetList(int currentMonth)
    {
        Optional<BudgetModel> budget = BudgetList.stream().filter(x -> x.GetCurrentMonth() == currentMonth).findFirst();
        return budget.get();
    }

    public void SetBudgetList(List<BudgetModel> budgetList)
    {
        BudgetList = budgetList;
    }

    public void CreateBudgetModel()
    {
        BudgetModel budgetModel = new BudgetModel();
        budgetModel.SetSoldElectricity(0);
        BudgetList.add(budgetModel);
    }

    private EconomyService() 
    {
        CreateBudgetModel();
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
