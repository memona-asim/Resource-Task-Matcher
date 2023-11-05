import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

public class MFrame extends JPanel{
    MFrame(DefaultListModel<Resource>resourceDefaultListModel,DefaultListModel<Task>taskDefaultListModel){
        //this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setSize(500, 500);
        this.setVisible(true);

        JRadioButton exactMatch=new JRadioButton("Exact Match");
        JRadioButton skillOnlyMatch=new JRadioButton("Skill Only Match");

        if(MainFrame.getAllResources()==null){
            System.out.println("mem is lve");
        }

        ButtonGroup matchingButtonGroup=new ButtonGroup();
        matchingButtonGroup.add(exactMatch);
        matchingButtonGroup.add(skillOnlyMatch);

        JButton generateButton=new JButton("Generate");
        generateButton.setFocusable(false);

        JTextArea textArea = new JTextArea(10, 60);
        textArea.setEditable(false);

        JPanel generatePanel=new JPanel();

        generatePanel.setLayout(new FlowLayout());

        ((FlowLayout) generatePanel.getLayout()).setHgap(100);

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() instanceof JButton){
                    Resource[] resources=getResourceArray(resourceDefaultListModel);
                    Task[]tasks=getTaskArray(taskDefaultListModel);
                    textArea.setText(" ");

                if(exactMatch.isSelected()){
                    HashMap<String,List<String>>exactMatchResult=Main.getExactMatch(new ExactMatch(),resources,tasks);
                    //textArea.setText(" ");
                    displayMatches(textArea,exactMatchResult);
                }

                if(skillOnlyMatch.isSelected()){
                    HashMap<String,List<String>>skillOnlyMatchResult=Main.getSkillOnlyMatch(new SkillOnlyMatch(),resources,tasks);
                    displayMatches(textArea,skillOnlyMatchResult);
                }

                }
            }
        });

        generatePanel.add(exactMatch,FlowLayout.LEFT);
        generatePanel.add(skillOnlyMatch,FlowLayout.CENTER);
        generatePanel.add(generateButton,FlowLayout.RIGHT);

        this.setLayout(new BorderLayout());
        this.add(generatePanel,BorderLayout.NORTH);
        this.add(textArea,BorderLayout.SOUTH);
        //this.pack();
    }
    public Resource[]getResourceArray(DefaultListModel<Resource>resourceDefaultListModel){
        int s=resourceDefaultListModel.getSize();
        System.out.println(s);
        Resource[] Resources=new Resource[s];
        for(int i=0;i<resourceDefaultListModel.getSize();i++){
            Resources[i]=resourceDefaultListModel.getElementAt(i);
        }
        return Resources;
    }
    public Task[]getTaskArray(DefaultListModel<Task>T){
        int s=T.getSize();
        Task[] Tasks=new Task[s];
        for(int i=0;i<T.getSize();i++){
            Tasks[i]=T.getElementAt(i);
        }
        return Tasks;
    }
    public void displayMatches(JTextArea textArea,HashMap<String,List<String>>map){
        textArea.setText(" ");
        for(String task:map.keySet()){
            textArea.append(task+"            ");
            for(int i=0;i<map.get(task).size();i++){
                textArea.append(map.get(task).get(i)+"      ");
            }
            textArea.append("\n");
        }
    }
}
