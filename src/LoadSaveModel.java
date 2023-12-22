import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LoadSaveModel implements Serializable
{
    private RodService rodService;
    private BudgetService budgetService;
    private ResearchService researchService;

    private List<TechnologyModel> TechList = new ArrayList<TechnologyModel>();
    public void setTechList(List<TechnologyModel> techList)
    {
        TechList = techList;
    }
    public List<TechnologyModel> getTechList()
    {
        return TechList;
    }

    private List<RodModel> RodList = new ArrayList<RodModel>();
    public void setRodList(List<RodModel> rodList)
    {
        RodList = rodList;
    }
    public List<RodModel> getRodList()
    {
        return RodList;
    }

    private List<BudgetModel> BudgetList = new ArrayList<BudgetModel>();
    public void setBudgetList(List<BudgetModel> budgetList)
    {
        BudgetList = budgetList;
    }
    public List<BudgetModel> getBudgetList()
    {
        return BudgetList;
    }
   
    private int CurrentMonth;
    public int getCurrentMonth()
    {
        return CurrentMonth;
    }
    public void setCurrentMonth(int currentMonth)
    {
        CurrentMonth = currentMonth;
    }

    private int CurrentYear;
    public int getCurrentYear()
    {
        return CurrentYear;
    }
    public void setCurrentYear(int currentYear)
    {
        CurrentYear = currentYear;
    }

    private int CurrentDay;
    public int getCurrentDay()
    {
        return CurrentDay;
    }
    public void setCurrentDay(int currentDay)
    {
        CurrentDay = currentDay;
    }

    public LoadSaveModel() 
    {
        rodService = RodService.GetInstance();
        budgetService = BudgetService.GetInstance();   
        researchService = ResearchService.GetInstance();

        // for (var tech : researchService.GetTechList()) 
        // {
        //     TechList.add(tech);
        // }
        setTechList(researchService.GetTechList());

        // for (var rod : rodService.GetRodList()) 
        // {
        //     RodList.add(rod);
        // }
        setRodList(rodService.GetRodList());
        
        // for (var budget : budgetService.GetBudgetList()) 
        // {
        //     BudgetList.add(budget);
        // }
        setBudgetList(budgetService.GetBudgetList());
    }



}
