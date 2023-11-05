import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map;
import java.util.List;

class Resource{
    String name;
    HashMap<Skill,Integer>skillSet=new HashMap<>();
    public Resource(){
        name=" ";
        skillSet.put(null,-1);
    }
    public Resource(String n){
        name=n;
    }
    public String getName(){
        return name;
    }
    public Resource(String n, Skill[] s, int[] i){
        name=n;
        for(int j=0;j<s.length;j++){
            skillSet.put(s[j], i[j]);
        }
    }
    public void printMySkills(){
        for (Map.Entry<Skill, Integer> entry : skillSet.entrySet()) {
            Skill key = entry.getKey();
            Integer value = entry.getValue();
            if(key!=null) {
                System.out.println("Key: "); key.print(); System.out.println(  ", Value: " + value);
            }
        }
    }
    public HashMap<Skill,Integer> getSkills(){
        return skillSet;
    }
    public void addSkill(Skill s, int ex){
        skillSet.put(s,ex);
    }
}

class Skill{
    String name;
    public Skill(){
        name=" ";
    };
    public Skill(String n){
        name=n;
    }
    public String getName(){
        return name;
    }
    public void print(){
        System.out.println(name);
    }
}

class Task{                     //I am assuming that experience of 0<b>=1 i=2, e>2.
    String name;
    HashMap<Skill, String>skillSet=new HashMap<>();
    public Task(){
        name=" ";
    }
    public Task(String n){
        name=n;
    }
    public String getName(){
        return name;
    }
    public Task(String n,Skill[]s,String[]str){
        name=n;
        for(int i=0;i<s.length;i++){
            skillSet.put(s[i],str[i]);
        }
    }
    public void printSkillsRequired(){
        for (Map.Entry<Skill, String> entry : skillSet.entrySet()) {
            Skill key = entry.getKey();
            String value = entry.getValue();
            if(key!=null) {
                System.out.println("Key: "); key.print(); System.out.println(  ", Value: " + value);
            }
        }
    }
    public void addSkill(Skill s, String ex){
        skillSet.put(s,ex);
    }
    public HashMap<Skill,Integer>getSkills(){
        return getSkillSetUtility();
    }

    public HashMap<Skill, Integer> getSkillSetUtility() {
        Integer[] i=new Integer[skillSet.size()];
        int count=0;
        HashMap<Skill,Integer>tempMap=new HashMap<>();
        for(Skill s: skillSet.keySet()){
            String val=skillSet.get(s);
            if(s!=null) {
                if (val.equals("i")) {
                    tempMap.put(s, 2);
                } else if (val.equals("b")) {
                    tempMap.put(s, 1);
                } else {
                    tempMap.put(s, 3);
                }
            }
        }
        return tempMap;
    }
}

interface MatchingStrategy{
    HashMap<String,List<String>> match(Task[]t,Resource[]R);
}

class SkillOnlyMatch implements MatchingStrategy{
    @Override
    public HashMap<String,List<String>> match(Task[]t,Resource[]R){
        int count=0;
        boolean flag=false;
        String[]array=new String[5]; int aCount=0;
        HashMap<String,List<String>>selected=new HashMap<>();
        for (Task task : t) {
            if (task != null) {
                //System.out.print(2);
                for (Resource resource : R) {
                    if (resource != null) {
                        if (task.getSkills().size() <= resource.getSkills().size()) {
                            count = 0;
                            for (Skill key : task.getSkills().keySet()) {
                                if(key!=null) {
                                    for (Skill s : resource.getSkills().keySet()) {
                                        if(s!=null) {
                                            if (key.getName().equals(s.getName())) {
                                                //key.print();
                                                ++count;
                                                //System.out.print(count);
                                                if(count==task.getSkills().size()){
                                                    //System.out.print(task.getName());
                                                    //System.out.print(resource.getName());
                                                    for(String str: selected.keySet()){

                                                        if (task.getName().equals(str)) {
                                                            flag = true;
                                                            break;
                                                        }
                                                        else flag=false;
                                                    }
                                                    if(flag){
                                                        //System.out.print(task.getName());
                                                        //  System.out.print(resource.getName());
                                                        if(selected.get(task.getName())!=null){
                                                            selected.get(task.getName()).add(resource.getName());
                                                            //System.out.println(selected.get(task.getName()));
                                                        }
                                                    }
                                                    else{

                                                        if(task.getName()!=null){
                                                            selected.put(task.getName(),new ArrayList<>());
                                                            selected.get(task.getName()).add(resource.getName());
                                                        }
                                                        System.out.print(task.getName());
                                                    }

                                                }
                                                break;
                                            }
                                        }
                                    }
                                }
                                //System.out.println(count);
                            }
                        }
                    }
                }
            }
        }
        return selected;
    }
}

class ExactMatch implements MatchingStrategy{
    @Override
    public HashMap<String,List<String>> match(Task[] t, Resource[]r) {
        int count=0;
        boolean flag=false;
        HashMap<String,List<String>>selected=new HashMap<>();
        for (Task task : t) {
            if (task != null) {
                for (Resource resource : r) {
                    if (resource != null) {
                        if (task.getSkills().size() <= resource.getSkills().size()) {
                            count = 0;
                            for (Skill key : task.getSkills().keySet()) {
                                if(key!=null) {
                                    int value = task.getSkills().get(key);
                                    for (Skill s : resource.getSkills().keySet()) {
                                        if(s!=null) {
                                            int val = resource.getSkills().get(s);
                                            if (key.getName().equals(s.getName()) && val >= value) {
                                                //key.print();
                                                ++count;
                                                if(count==task.getSkills().size()){
                                                    for(String str: selected.keySet()){
                                                        if (task.getName().equals(str)) {
                                                            flag = true;
                                                            break;
                                                        }
                                                        else flag=false;
                                                    }
                                                    if(flag){
                                                        selected.get(task.getName()).add(resource.getName());
                                                    }
                                                    else if(task.getName()!=null){
                                                        selected.put(task.getName(),new ArrayList<>());
                                                        selected.get(task.getName()).add(resource.getName());
                                                    }
                                                }
                                                break;
                                            }

                                        }
                                    }
                                }
                                //System.out.println(count);
                            }
                        }
                    }
                }
            }
        }
        return selected;
    }
}

public class Main {
    public static void writeFile(HashMap<String,List<String>>map, String filename){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false))) {
            map.forEach((key, value) -> {
                try {
                    if (key != null) {
                        writer.write(key + ": ");
                        value.forEach(str -> {
                            try {
                                writer.write(str + " ");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        writer.write("\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void readFile( String file, boolean flag , Resource[] R, Task[]T) {
        String filename = file;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int rCount=0;

            int tCount=0;
            while ((line = reader.readLine()) != null) {
                Pattern pattern = Pattern.compile("([^|]+) \\| (.*)");
                Matcher matcher = pattern.matcher(line);

                Skill[] skillArray=new Skill[5];

                int[] intArray=new int[5];

                String[] charArray=new String[5];

                int counter=0;
                String name=" ";

                if (matcher.matches()) {
                    name = matcher.group(1);
                    String skillSet = matcher.group(2);

                    String[] skills = skillSet.split(",");

                    for (int k=0;k<skills.length;k++) {

                        String[] keyValue = skills[k].split(":");

                        if (keyValue.length == 2) {
                            String lang = keyValue[0].trim();
                            skillArray[counter]=new Skill(lang);
                            if(flag){
                                int exp = Integer.parseInt(keyValue[1].trim());
                                intArray[counter++]=exp;
                            }
                            else{
                                String exp = keyValue[1].trim();
                                charArray[counter++]=exp;
                            }

                        }
                    }
                }
                //Creating Resources
                if (flag) {
                    Resource r= new Resource(name, skillArray, intArray);
                    R[rCount++]=r;
                    //System.out.println("Resource created.");
                    // r.printMySkills();
                }

                else{
                    Task t= new Task(name, skillArray,charArray);
                    T[tCount++]=t;
                    //System.out.println("Task created.");
                    // t.printSkillsRequired();
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public static HashMap<String,List<String>>getSkillOnlyMatch(SkillOnlyMatch s,Resource[]R,Task[]T){
        return s.match(T,R);
    }
    public static HashMap<String,List<String>>getExactMatch(ExactMatch s,Resource[]R,Task[]T){
        return s.match(T,R);
    }
    public static Resource[] getTotalResources(){
        Task[] T=new Task[5];
        Resource[]R=new Resource[5];

        readFile("C:\\Users\\Memona\\IdeaProjects\\SCD_Assignment\\src\\resourceFile.txt", true,R,T);
        readFile("C:\\Users\\Memona\\IdeaProjects\\SCD_Assignment\\src\\taskFile.txt", false,R,T);

        return R;
    }

    public static Task[] getTotalTasks(){
        Task[] T=new Task[5];
        Resource[]R=new Resource[5];

        readFile("C:\\Users\\Memona\\IdeaProjects\\SCD_Assignment\\src\\resourceFile.txt", true,R,T);
        readFile("C:\\Users\\Memona\\IdeaProjects\\SCD_Assignment\\src\\taskFile.txt", false,R,T);

        return T;
    }

    public static void main(String[] args) {
        Resource[] R=new Resource[5];
        Task[] T=new Task[5];
        Resource[] R1=new Resource[5];
        Task[] T1=new Task[5];
        HashMap<String,List<String>>map;

        readFile("C:\\Users\\Memona\\IdeaProjects\\SCD_Assignment\\src\\resourceFile.txt", true,R,T);
        readFile("C:\\Users\\Memona\\IdeaProjects\\SCD_Assignment\\src\\taskFile.txt", false,R,T);

        readFile("C:\\Users\\Memona\\IdeaProjects\\SCD_Assignment\\src\\resourceFile.txt", true,R1,T1);
        readFile("C:\\Users\\Memona\\IdeaProjects\\SCD_Assignment\\src\\taskFile.txt", false,R1,T1);

        ExactMatch e1=new ExactMatch();
        SkillOnlyMatch s1=new SkillOnlyMatch();

        map=e1.match(T,R);
        writeFile(map,"exactMatchResult.txt");

        map.clear();

        map=s1.match(T,R);
        writeFile(map,"skillOnlyMatchResult.txt");


    }
}