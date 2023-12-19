import java.util.ArrayList;
import java.util.List;

public class ResearchService 
{
    private BudgetService budgetService;
    private static ResearchService Instance;
    private List<TechnologyModel> techs;

    
    private ResearchService() 
    {
        budgetService = BudgetService.GetInstance();
        CreateResearchList();
    }

    private void CreateResearchList()
    {
        techs = new ArrayList<TechnologyModel>();
        
        var techOne = new TechnologyModel(1,1,false,"Research","Enables research and development.", "./Images/tech00.png", TechnologyModelStyle.StartTech);
        techs.add(techOne);
        
        var techTwo = new TechnologyModel(1,1,false,"Stronger case","Unlocks a stronger case.", "./Images/tech01.png", TechnologyModelStyle.SaftyTech);
        techTwo.AddDependingTechnology(techOne);
        techs.add(techTwo);
        
        var techTwoTwo = new TechnologyModel(1,1,false,"Even stronger case","Unlocks an even stronger case.", "./Images/tech02.png", TechnologyModelStyle.SaftyTech);
        techTwoTwo.AddDependingTechnology(techOne);
        techTwoTwo.AddDependingTechnology(techTwo);
        techs.add(techTwoTwo);

        var techThree = new TechnologyModel(1,1,false,"Stronger case","Unlocks a stronger case.", "./Images/tech03.png", TechnologyModelStyle.FinaceTech);
        techThree.AddDependingTechnology(techOne);
        techs.add(techThree);
    }
    
    public List<TechnologyModel> GetTechList()
    {
        return techs;
    }

    public static ResearchService GetInstance()
    {
        if(Instance == null)
        {
            Instance = new ResearchService();
        }

        return Instance;
    }
}
