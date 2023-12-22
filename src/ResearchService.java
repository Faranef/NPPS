import java.util.ArrayList;
import java.util.List;

public class ResearchService 
{
    private static ResearchService Instance;
    private List<TechnologyModel> techs;
    
    private ResearchService() 
    {
        CreateResearchList();
    }

    private void CreateResearchList()
    {
        techs = new ArrayList<TechnologyModel>();
        
        // 0
        var techOne = new TechnologyModel(1_000,false,"Research","Enables research and development.",
         "./Images/tech00.png", TechnologyModelStyle.StartTech,0, "", "", 
         "./Images/finishedTech00.png");
        techs.add(techOne);
        
        // 1
        var techTwo = new TechnologyModel(10_000,false,"Stronger case","Unlocks a stronger case.", 
        "./Images/tech01.png", TechnologyModelStyle.SafetyTech,1, "Reactor" ,"./Images/caseUpgrade0.png",
        "./Images/finishedTech01.png");
        techTwo.AddDependingTechnology(techOne);
        techs.add(techTwo);
        
        // 2
        var techTwoTwo = new TechnologyModel(25_000,false,"Even stronger case","Unlocks an even stronger case.",
         "./Images/tech02.png", TechnologyModelStyle.SafetyTech,2,"Reactor", "./Images/caseUpgrade1.png",
         "./Images/finishedTech02.png");
        techTwoTwo.AddDependingTechnology(techOne);
        techTwoTwo.AddDependingTechnology(techTwo);
        techs.add(techTwoTwo);

        // 3
        var techThree = new TechnologyModel(1_000_000,false,"Decrease costs","Unlocks the possibility to reduce maintenance cost.",
         "./Images/tech03.png", TechnologyModelStyle.FinaceTech,3, "","",
          "./Images/finishedTech03.png");        
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
