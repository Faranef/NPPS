public class LoadSaveModel 
{
    private double budget;
    private double soldElectricity = 0;
    private double miscellaneausIncome = 0;
    private double totalIncome = 0;
    private double fuelRodPrice = 0;
    private double maintenance = 0;
    private double miscellaneausLoss = 0;
    private double totalLoss = 0;
    private int currentMonth;
    private int currentYear;

    private String rodName;
    private int rodLevel;
    private double lifeSpan;
    private boolean isActive;
    //private RodModelStyle Style;
    //private List<RodModel> RodModelList = new ArrayList<RodModel>();
    
    public LoadSaveModel() {
    }

    public double getBudget()
    {
        return budget;
    }
    public void setBudget(double budget)
    {
        this.budget = budget;
    }
    
    public double getSoldElectricity()
    {
        return soldElectricity;
    }
    public void setSoldElectricity(double soldElectricity)
    {
        this.soldElectricity = soldElectricity;
    }

    public double getMiscellaneausIncome()
    {
        return miscellaneausIncome;
    }
    public void setMiscellaneausIncome(double miscellaneausIncome)
    {
        this.miscellaneausIncome = miscellaneausIncome;
    }

    public double getTotalIncome()
    {
        return totalIncome;
    }
    public void setTotalIncome(double totalIncome)
    {
        this.totalIncome = totalIncome;
    }

    public double getFuelRodPrice()
    {
        return fuelRodPrice;
    }
    public void setFuelRodPrice(double fuelRodPrice)
    {
        this.fuelRodPrice = fuelRodPrice;
    }

    public double getMaintenance()
    {
        return maintenance;
    }
    public void setMaintenance(double maintenance)
    {
        this.maintenance = maintenance;
    }

    public double getMiscellaneausLoss()
    {
        return miscellaneausLoss;
    }
    public void setMiscellaneausLoss(double miscellaneausLoss)
    {
        this.miscellaneausLoss = miscellaneausLoss;
    }

    public double getTotalLoss()
    {
        return totalLoss;
    }
    public void setTotalLoss(double totalLoss)
    {
        this.totalLoss = totalLoss;
    }
    

    public int getCurrentMonth()
    {
        return currentMonth;
    }
    public void setCurrentMonth(int currentMonth)
    {
        this.currentMonth = currentMonth;
    }



    public int getCurrentYear()
    {
        return currentYear;
    }
    public void setCurrentYear(int currentYear)
    {
        this.currentYear = currentYear;
    }

    
    public int getRodLevel()
    {
        return rodLevel;
    }
    public void setRodLevel(int rodLevel)
    {
        this.rodLevel = rodLevel;
    }


    public double getLifeSpan()
    {
        return lifeSpan;
    }
    public void setLifeSpan(double lifeSpan)
    {
        this.lifeSpan = lifeSpan;
    }

    
    public String getRodName()
    {
        return rodName;
    }
    public void setRodName(String rodName)
    {
        this.rodName = rodName;
    }


    public boolean isIsActive()
    {
        return isActive;
    }
    public void setIsActive(boolean isActive)
    {
        this.isActive = isActive;
    }

    
    // public RodModelStyle getStyle()
    // {
    //     return Style;
    // }
    // public void setStyle(RodModelStyle style)
    // {
    //     Style = style;
    // }

    
    // public List<RodModel> getRodModelList()
    // {
    //     return RodModelList;
    // }
    // public void setRodModelList(List<RodModel> rodModelList)
    // {
    //     RodModelList = rodModelList;
    // }
}
