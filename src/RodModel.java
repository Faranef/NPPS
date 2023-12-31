import java.io.Serializable;
import java.util.*;

public class RodModel implements Serializable
{
    public int RodLevel;
    public double LifeSpan;
    public String RodName;
    public boolean IsActive;
    public RodModelStyle Style;
    public List<RodModel> RodModelList = new ArrayList<RodModel>();

    public RodModel() 
    {
    }

    public String GetRodName()
    {
        return RodName;
    }

    public RodModelStyle GetRodStyle()
    {
        return Style;
    }

    public int GetRodLevel()
    {
        return RodLevel;
    }

    public void SetRodLevel(int rodLevel)
    {
        RodLevel = rodLevel;
    }

    public double GetLifeSpan()
    {
        return LifeSpan;
    }

    public void SetLifeSpan(double lifeSpan)
    {
        LifeSpan = lifeSpan;
    }

    public boolean IsActive()
    {
        return IsActive;
    }

    public void SetActivity(Boolean isAcitve)
    {
        IsActive = isAcitve;
    }
    
    public RodModel(int rodLevel, String rodName, RodModelStyle style)
    {
        RodLevel = rodLevel;
        RodName = rodName;
        Style = style;
    }

    public RodModel(int rodLevel, String rodName, RodModelStyle style, double lifeSpan, boolean isActive)
    {
        RodLevel = rodLevel;
        RodName = rodName;
        Style = style;
        LifeSpan = lifeSpan;
        IsActive = isActive;
    }

    public RodModel(int rodLevel, String rodName, RodModelStyle style, List<RodModel> rodModelList)
    {
        RodLevel = rodLevel;
        RodName = rodName;
        Style = style;
        RodModelList = rodModelList;
    }

    public void AddRodToList(RodModel rodModel)
    {
        RodModelList.add(rodModel);
    }

    public List<RodModel> GetRodModelList()
    {
        return RodModelList;
    }

    @Override
    public String toString() 
    {
      return GetRodName();
    }
}
