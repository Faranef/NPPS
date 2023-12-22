import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LoadSaveService 
{
    private static LoadSaveService Instance;
    private RodService rodService;
    private BudgetService budgetService;
    private ResearchService researchService;

    public static LoadSaveService GetInstance()
    {
        if (Instance == null)
        {
            Instance = new LoadSaveService();
        }

        return Instance;
    }

    private LoadSaveService() 
    {
        rodService = RodService.GetInstance();
        budgetService = BudgetService.GetInstance();   
        researchService = ResearchService.GetInstance();
    }

    public LoadSaveModel LoadGame(File file)
    {
        LoadSaveModel loadModel = null;

        try 
        {
            ObjectMapper mapper = new ObjectMapper();
            LoadSaveModel loadModels = mapper.readValue(file, LoadSaveModel.class);

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }


        return loadModel;
    }

    public void SaveGame(File file, Date gameDate)
    {
        try 
        {
            var saveModel = CreateSaveModel(gameDate);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            objectMapper.writeValue(file, saveModel);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    private LoadSaveModel CreateSaveModel(Date gameDate)
    {
        LoadSaveModel loadSaveModel = new LoadSaveModel();
        
        Calendar c = Calendar.getInstance(); 
        c.setTime(gameDate); 
        loadSaveModel.setCurrentDay(c.get(Calendar.DAY_OF_MONTH));
        loadSaveModel.setCurrentMonth(c.get(Calendar.MONTH)+1);
        loadSaveModel.setCurrentYear( c.get(Calendar.YEAR));

        return loadSaveModel;
    }

}

