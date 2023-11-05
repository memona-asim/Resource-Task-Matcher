import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    static DefaultListModel<Resource>resourceDefaultListModel;
    static DefaultListModel<Task>taskDefaultListModel;
    public MainFrame() {
        setTitle("Main Frame");

        JTabbedPane tabbedPane = new JTabbedPane();
        taskDefaultListModel = new DefaultListModel<>();
        resourceDefaultListModel = new DefaultListModel<>();
        initialize(taskDefaultListModel,resourceDefaultListModel);

        MFrame mFrame = new MFrame(resourceDefaultListModel,taskDefaultListModel);
        TFrame tFrame = new TFrame(taskDefaultListModel);
        RFrame rFrame = new RFrame(resourceDefaultListModel);

        resourceDefaultListModel= rFrame.getAllResources();
        taskDefaultListModel=tFrame.getAllTasks();

        tabbedPane.addTab("RFrame", rFrame);
        tabbedPane.addTab("TFrame", tFrame);
        tabbedPane.addTab("MFrame", mFrame);

        add(tabbedPane);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setSize(800, 400);
        setLocationRelativeTo(null);
        setVisible(true);
        this.pack();
    }
    void initialize(DefaultListModel<Task> TM, DefaultListModel<Resource> RM) {
        Resource[] R = new Resource[5];
        Task[] T = new Task[5];
        R = Main.getTotalResources();
        T = Main.getTotalTasks();

        // Add Task objects to TM
        for (Task task : T) {
            if (task != null) TM.addElement(task);
        }

        // Add Resource objects to RM
        for (Resource resource : R) {
            RM.addElement(resource);
        }
    }
    public static DefaultListModel<Resource>getAllResources(){
        return resourceDefaultListModel;
    }
    public static DefaultListModel<Task>getAllTasks(){
        return taskDefaultListModel;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame();
        });
    }
}
