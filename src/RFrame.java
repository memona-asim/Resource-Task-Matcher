import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import static java.lang.Integer.parseInt;

public class RFrame extends JPanel {
    static DefaultListModel<Resource> resourceDefaultListModel;
    RFrame(DefaultListModel<Resource>rDM) {
        //this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setSize(500, 500);

        JPanel resourcePanel = new JPanel(new BorderLayout());
       // DefaultListModel<Task> taskDefaultListModel = new DefaultListModel<>();
        resourceDefaultListModel = rDM;
        DefaultListModel<String> resourceNameDefaultListModel = new DefaultListModel<>();

        populate(resourceNameDefaultListModel, resourceDefaultListModel);

        JList<String> resourceJList = new JList<>(resourceNameDefaultListModel);

        JButton addResourceButton = new JButton("Add");
        addResourceButton.setFocusable(false);

        JButton addSkillButton=new JButton("Add");
        addSkillButton.setFocusable(false);

        JScrollPane scrollPane = new JScrollPane(resourceJList);

        JTextField resourceNameTextField = new JTextField(20);
        JPanel inputPanel = new JPanel(new BorderLayout());

        JTextField skillTextField=new JTextField(20);
        JTextField experienceTextField=new JTextField(3);

        Map<Resource, Map<Skill,Integer>> skillMap = new HashMap<>();
        populateMap(skillMap);

        JTextArea textArea = new JTextArea(10, 30);
        textArea.setEditable(false);

        addSkillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() instanceof JButton){
                    if(!skillTextField.getText().isEmpty()){
                        if(!experienceTextField.getText().isEmpty()){
                            textArea.append(skillTextField.getText() + "   " + experienceTextField.getText() + "\n");

                            String r= resourceJList.getSelectedValue();
                            Resource rr=null;

                            for(int i=0;i<resourceDefaultListModel.getSize();i++){
                                if(Objects.equals(r, resourceDefaultListModel.get(i).getName())){
                                    //System.out.println("meona");
                                    rr=resourceDefaultListModel.get(i);
                                    //System.out.println(rr.getName());
                                }
                            }
                            boolean flag = false;
                            Skill skill=null;

                            assert rr != null;
                            HashMap<Skill,Integer>skills=rr.getSkills();

                            for(Resource resource:skillMap.keySet()){
                                if(r!=null){
                                    for(Skill s:skillMap.get(resource).keySet()){
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
                                skill=updateSkills(skillTextField.getText(),experienceTextField.getText(),rr);
                                for(Resource resource:skillMap.keySet()){
                                    if(r!=null){
                                        if (Objects.equals(resource.getName(), rr.getName())) {
                                            //System.out.println("memona");
                                            skillMap.get(resource).put(skill,parseInt(experienceTextField.getText()));
                                        }
                                    }
                                }
                            }
                            else{
                                for(Resource resource:skillMap.keySet()){
                                    if(r!=null){
                                       for(Skill s:skillMap.get(resource).keySet()){
                                           skillMap.get(resource).put(s, Integer.valueOf(experienceTextField.getText()));
                                       }
                                    }
                                }
                            }

                            skillTextField.setText("");
                            experienceTextField.setText("");
                        }
                    }
                    else{
                            //JOptionPane.showMessageDialog(getContentPane(), "INVALID INPUT.", "ERROR", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        addResourceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JButton) {
                    boolean flag=false;
                    if (!(resourceNameTextField.getText()).isEmpty()) {
                        String newResourceName = resourceNameTextField.getText();
                        resourceNameDefaultListModel.addElement(newResourceName);
                        resourceNameTextField.setText("");

                            Resource r=new Resource(newResourceName);
                            resourceDefaultListModel.addElement(r);

                            populateAgain(resourceDefaultListModel,skillMap);

                            //System.out.println("sdjhfukafaskjfskhfd");

                    }
                }
            }
        });

        resourceJList.addListSelectionListener((new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    textArea.setText(" ");
                    String selectedItem = resourceJList.getSelectedValue();

                    Map<Skill, Integer> innerMap = null;

                    for (Map.Entry<Resource, Map<Skill, Integer>> entry : skillMap.entrySet()) {
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
                                textArea.append(n + "   " + innerEntry.getValue() + "\n");
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
        skillExperiencePanel.add(experienceTextField);
        skillExperiencePanel.add(addSkillButton);
        skillExperiencePanel.setLayout(new FlowLayout());

        inputPanel.add(new JLabel("Resource"));
        inputPanel.add(resourceNameTextField);
        inputPanel.add(addResourceButton);
        inputPanel.setLayout(new FlowLayout());

        JPanel totalSkillPanel=new JPanel(new BorderLayout());
        totalSkillPanel.add(skillExperiencePanel,BorderLayout.NORTH);
        totalSkillPanel.add(skillPanel,BorderLayout.CENTER);

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.add(scrollPane, BorderLayout.CENTER);

        resourcePanel.add(inputPanel, BorderLayout.NORTH);
        resourcePanel.add(listPanel, BorderLayout.CENTER);

        JPanel totalResourcePanel=new JPanel();
        totalResourcePanel.add(resourcePanel,BorderLayout.WEST);
        totalResourcePanel.add(totalSkillPanel,BorderLayout.EAST);

        this.setLayout(new BorderLayout());
        this.add(totalResourcePanel,BorderLayout.NORTH);

        this.setVisible(true);
        //this.pack();
    }
    public DefaultListModel<Resource>getAllResources(){
        return resourceDefaultListModel;
    }
    void populateAgain(DefaultListModel<Resource>rL, Map<Resource, Map<Skill,Integer>>map){
        for(int i=0;i<rL.getSize();i++){
            System.out.println(rL.get(i).getName());
        }
        for (int i=0;i<rL.getSize();i++) {
            map.put(rL.get(i), null);
        }
        map.replaceAll((r, v) -> r.getSkills());
    }
    Skill updateSkills(String s, String e,Resource r){

            Skill skill = new Skill(s);
            int ex = parseInt(e);
            r.addSkill(skill, ex);
            return skill;
    }
    void populate(DefaultListModel<String> rS, DefaultListModel<Resource> r) {
        for (int i = 0; i < r.getSize(); i++) {
            Resource resource = r.getElementAt(i);
            if (resource != null) {
                rS.addElement(resource.getName());
                //System.out.println(rS.getElementAt(i));
            }
        }
    }

    void populateMap(Map<Resource, Map<Skill,Integer>>map){
        Resource[]resources=Main.getTotalResources();
        for (Resource resource : resources) {
            map.put(resource, null);
        }
        map.replaceAll((r, v) -> r.getSkills());
    }
}
