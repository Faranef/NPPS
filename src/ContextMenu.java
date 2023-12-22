import javax.swing.*;

public class ContextMenu extends JPopupMenu
{
    RodService rodService;
    JMenuItem item;
    
    //maybe used later
    public ContextMenu() 
    {
        item = new JMenuItem("Menu Item");
        item.addActionListener(null);
        add(item);
    }

    public ContextMenu(int id) 
    {
        rodService = RodService.GetInstance();
        item = new JMenuItem("Replace");
        item.addActionListener(e -> 
        {
            rodService.ReplaceFuelRodById(id);
        });
        add(item);
    }
}
