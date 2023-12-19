import java.util.ArrayList;
import java.util.List;

public class TechnologyModel 
{
    private List<TechnologyModel>  DependingTechnology;
    private double ResearchCost;
    private int ResearchTime;
    private boolean IsResearched;
    private String ResearchName;
    private String ResearchDescription;
    private String ImagePath;
    private TechnologyModelStyle TechnologyStyle;
    
    public TechnologyModel(double cost, int researchTime, boolean isResearched, String name, String description, String imagePath, TechnologyModelStyle technologyStyle) 
    {
        ResearchCost = cost;
        ResearchTime = researchTime;
        IsResearched = isResearched;
        ResearchName = name;
        ResearchDescription = description;
        ImagePath = imagePath;
        TechnologyStyle = technologyStyle;
    }
    
    public TechnologyModelStyle GetTechnologyStyle()
    {
        return TechnologyStyle;
    }

    public void SetTechnologyStyle(TechnologyModelStyle technologyStyle)
    {
        TechnologyStyle = technologyStyle;
    }
    
    public void AddDependingTechnology(TechnologyModel tech)
    {
        if (DependingTechnology == null)
        {
            DependingTechnology = new ArrayList<TechnologyModel>();
        }

        DependingTechnology.add(tech);
    }
    
    public String GetImagePath()
    {
        return ImagePath;
    }

    public void SetImagePath(String imagePath)
    {
        ImagePath = imagePath;
    }

    public String GetResearchName()
    {
        return ResearchName;
    }
    public void SetResearchName(String researchName)
    {
        ResearchName = researchName;
    }
    public String GetResearchDescription()
    {
        return ResearchDescription;
    }
    public void SetResearchDescription(String researchDescription)
    {
        ResearchDescription = researchDescription;
    }
    public List<TechnologyModel> GetDependingTechnology()
    {
        return DependingTechnology;
    }
    public void SetDependingTechnology(List<TechnologyModel> dependingTechnology)
    {
        DependingTechnology = dependingTechnology;
    }
    public double GetResearchCost()
    {
        return ResearchCost;
    }
    public void SetResearchCost(double researchCost)
    {
        ResearchCost = researchCost;
    }
    public int GetResearchTime()
    {
        return ResearchTime;
    }
    public void SetResearchTime(int researchTime)
    {
        ResearchTime = researchTime;
    }
    public boolean IsResearched()
    {
        return IsResearched;
    }
    public void setIsResearched(boolean isResearched)
    {
        IsResearched = isResearched;
    }
    
}
