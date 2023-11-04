import java.util.*;

public class RodModel
{
    private int RodLevel;
    private String RodName;
    private RodModelStyle Style;
    private List<RodModel> RodModelList = new ArrayList<RodModel>();

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

    public RodModel(int rodLevel, String rodName, RodModelStyle style)
    {
        RodLevel = rodLevel;
        RodName = rodName;
        Style = style;
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
