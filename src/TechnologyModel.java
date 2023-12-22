import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TechnologyModel implements Serializable
{
    public List<TechnologyModel>  DependingTechnology;
    public double ResearchCost;
    public boolean IsResearched;
    public String ResearchName;
    public String ResearchDescription;
    public String LogoPath;
    public String LogoPathFinished;
    public TechnologyModelStyle TechnologyStyle;
    public int TechId;
    public String Effects;
    public String ImagePath;

    public TechnologyModel() 
    {
    }

    public TechnologyModel(double cost, boolean isResearched, String name, String description, String logoPath, 
                           TechnologyModelStyle technologyStyle, int techId, String effects,
                           String imagePath, String logoPathFinished) 
    {
        ResearchCost = cost;
        IsResearched = isResearched;
        ResearchName = name;
        ResearchDescription = description;
        LogoPath = logoPath;
        TechnologyStyle = technologyStyle;
        TechId = techId;
        Effects = effects;
        ImagePath = imagePath;
        LogoPathFinished = logoPathFinished;
    }

        public String GetLogoPathFinished()
    {
        return LogoPathFinished;
    }

    public void SetLogoPathFinished(String logoPathFinished)
    {
        LogoPathFinished = logoPathFinished;
    }
           
    public String GetImagePath()
    {
        return ImagePath;
    }

    public void SetImagePath(String imagePath)
    {
        ImagePath = imagePath;
    }

    public int GetTechId()
    {
        return TechId;
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
    
    public String GetLogoPath()
    {
        return LogoPath;
    }

    public void SetLogoPath(String imagePath)
    {
        LogoPath = imagePath;
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
    public boolean IsResearched()
    {
        return IsResearched;
    }
    public void SetIsResearched(boolean isResearched)
    {
        IsResearched = isResearched;
    }

    public String GetResearchEffect()
    {
        return Effects;
    }
    
}
