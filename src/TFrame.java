import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TFrame extends JPanel {
    static DefaultListModel<Task> taskDefaultListModel;
    TFrame(DefaultListModel<Task>tDM) {
        //this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setSize(500, 500);

        JPanel taskPanel = new JPanel(new BorderLayout());
        taskDefaultListModel = tDM;
        //DefaultListModel<Resource> resourceDefaultListModel = new DefaultListModel<>();
        DefaultListModel<String> taskNameDefaultListModel = new DefaultListModel<>();

        //initialize(taskDefaultListModel, resourceDefaultListModel);
        populate(taskNameDefaultListModel, taskDefaultListModel);

        JList<String> taskJlist = new JList<>(taskNameDefaultListModel);

        JButton addTaskButton = new JButton("Add");
        addTaskButton.setFocusable(false);

        JButton addSkillButton=new JButton("Add");
        addSkillButton.setFocusable(false);

        JScrollPane scrollPane = new JScrollPane(taskJlist);

        JTextField taskNameTextField = new JTextField(20);
        JPanel inputPanel = new JPanel(new BorderLayout());

        JTextField skillTextField=new JTextField(20);
        //JTextField experienceTextField=new JTextField(20);

        Map<Task, Map<Skill,Integer>> skillMap = new HashMap<>();
        populateMap(skillMap);

        JTextArea textArea = new JTextArea(10, 30);
        textArea.setEditable(false);

        String[] experience = {"Basic", "Intermediate", "Expert"};
        JComboBox<String> expComboBox = new JComboBox<>(experience);

        addSkillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() instanceof JButton){
                    if(!skillTextField.getText().isEmpty()){
                        if(expComboBox.getSelectedIndex()!=-1){
                            textArea.append(skillTextField.getText() + "   " + expComboBox.getSelectedItem() + "\n");

                            String t= taskJlist.getSelectedValue();
                            Task tt=null;

                            for(int i=0;i<taskDefaultListModel.getSize();i++){
                                if(Objects.equals(t, taskDefaultListModel.get(i).getName())){
                                    //System.out.println("meona");
                                    tt=taskDefaultListModel.get(i);
                                    System.out.println(tt.getName());
                                }
                            }
                            Skill skill=null;
                            boolean flag=false;
                            for(Task task:skillMap.keySet()){
                                if(task!=null){
                                    for(Skill s:skillMap.get(task).keySet()){
                                        if(s!=null){
                                            if(Objects.equals(s.getName(),skillTextField.getText())){
                                                skill=s;
                                                flag=true;
                                            }
                                        }
                                    }
                                }
                            }
                            if(!flag){
                                assert tt!=null;
                                skill=updateSkills(skillTextField.getText(), (String) expComboBox.getSelectedItem(),tt);

                                System.out.println(tt.getName());

                                for(Task task:skillMap.keySet()){
                                    if(task!=null){
                                        if (Objects.equals(task.getName(), tt.getName())) {
                                            //System.out.println("memona");
                                            int exp=checkLevel((String) Objects.requireNonNull(expComboBox.getSelectedItem()));
                                            skillMap.get(task).put(skill,exp);
                                        }
                                    }
                                }
                            }
                            else{
                                for(Task task:skillMap.keySet()){
                                    if(task!=null){
                                        for(Skill s:skillMap.get(task).keySet()){
                                            skillMap.get(task).put(s, checkLevel((String) Objects.requireNonNull(expComboBox.getSelectedItem())));
                                        }
                                    }
                                }
                            }

                            skillTextField.setText("");
                            //experienceTextField.setText("");
                        }
                    }
                    else{
                        //JOptionPane.showMessageDialog(this, "INVALID INPUT.", "ERROR", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        addTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JButton) {
                    if (!(taskNameTextField.getText()).isEmpty()) {
                        String newTaskName = taskNameTextField.getText();
                        taskNameDefaultListModel.addElement(newTaskName);
                        taskNameTextField.setText("");

                        Task t = new Task(newTaskName);
                        taskDefaultListModel.addElement(t);
                        System.out.println(t.getName());

                        populateAgain(taskDefaultListModel, skillMap);
                    }
                }
            }
        });

        taskJlist.addListSelectionListener((new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    textArea.setText(" ");
                    String selectedItem = taskJlist.getSelectedValue();

                    //populateMap(skillMap);

                    Map<Skill, Integer> innerMap = null;

                    for (Map.Entry<Task, Map<Skill, Integer>> entry : skillMap.entrySet()) {
                        if(entry.getKey()!=null){
                            if(entry.getKey().getName().equals(selectedItem)){
                                innerMap = entry.getValue();
                            }
                        }
                    }
                    if (innerMap != null) {
                        for (Map.Entry<Skill, Integer> innerEntry : innerMap.entrySet()) {
                            if(innerEntry.getKey()!=null){
                                String n = (innerEntry.getKey()).getName();
                                textArea.append(n + "   " + checkLevelString(innerEntry.getValue()) + "\n");
                            }
                        }
                    }
                }

            }
        }));

        JPanel skillPanel=new JPanel(new BorderLayout());
        skillPanel.add(textArea, BorderLayout.CENTER);

        JPanel skillExperiencePanel=new JPanel(new FlowLayout());
        skillExperiencePanel.add(new JLabel("Skill"));
        skillExperiencePanel.add(skillTextField);
        skillExperiencePanel.add(new JLabel("Experience"));
        skillExperiencePanel.add(expComboBox);
        skillExperiencePanel.add(addSkillButton);
        skillExperiencePanel.setLayout(new FlowLayout());

        inputPanel.add(new JLabel("Task"));
        inputPanel.add(taskNameTextField);
        inputPanel.add(addTaskButton);
        inputPanel.setLayout(new FlowLayout());

        JPanel totalSkillPanel=new JPanel(new BorderLayout());
        totalSkillPanel.add(skillExperiencePanel,BorderLayout.NORTH);
        totalSkillPanel.add(skillPanel,BorderLayout.CENTER);

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.add(scrollPane, BorderLayout.CENTER);

        taskPanel.add(inputPanel, BorderLayout.NORTH);
        taskPanel.add(listPanel, BorderLayout.CENTER);

        JPanel totalTaskPanel=new JPanel();
        totalTaskPanel.add(taskPanel,BorderLayout.WEST);
        totalTaskPanel.add(totalSkillPanel,BorderLayout.EAST);

        this.add(totalTaskPanel);
        this.setVisible(true);
       // this.pack();
    }
    public DefaultListModel<Task>getAllTasks(){
        return taskDefaultListModel;
    }
    void populateAgain(DefaultListModel<Task>tL, Map<Task, Map<Skill,Integer>>map){
        for(int i=0;i<tL.getSize();i++){
            System.out.println(tL.get(i).getName());
        }
        for (int i=0;i<tL.getSize();i++) {
            map.put(tL.get(i), null);
        }
        for(Task t: map.keySet()){
            if (map.containsKey(t)) {
                if(t!=null){
                    map.put(t, t.getSkills());
                }
            }
        }
    }
    int checkLevel(String str){
            if (str.equals("Intermediate")) {
               return 1;
            } else if (str.equals("Basic")) {
                return 2;
            } else {
                return 3;
            }
    }

    String checkLevelString(int level){
        if (level==1) {
            return "Intermediate";
        } else if (level==2) {
            return "Basic";
        } else {
            return "Expert";
        }
    }

    Skill updateSkills(String s, String e,Task t){
        Skill skill=new Skill(s);
        t.addSkill(skill,e);
        System.out.println(skill.getName());
        t.printSkillsRequired();
        return skill;
    }
    void populate(DefaultListModel<String> rS, DefaultListModel<Task> r) {
        for (int i = 0; i < r.getSize(); i++) {
            Task task = r.getElementAt(i);
            if (task != null) {
                rS.addElement(task.getName());
                //System.out.println(rS.getElementAt(i));
            }
        }
    }

    void populateMap(Map<Task, Map<Skill,Integer>>map){
        Task[]tasks=Main.getTotalTasks();
        for (Task task : tasks) {
            map.put(task, null);
        }
        for(Task t: map.keySet()){
            if (map.containsKey(t)) {
                if(t!=null){
                    map.put(t, t.getSkills());
                }
            }
        }
    }
}
