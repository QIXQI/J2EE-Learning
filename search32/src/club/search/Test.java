package club.search;

import java.util.*;

public class Test{
    public static void main(String [] args){
        List<Person> people = new ArrayList<Person>();
        List<Person> list = new ArrayList<Person>();
        System.out.println(people.size());
        people.add(new Person("201792179", "郑翔", "19818965597", "2826791069", "zhengxiang4056@gmail.com"));
        people.add(new Person("201792122", "七七", "18742024106", "2510591928", "2510591928@qq.com"));
        list.add(new Person("123", "雪莉", "000", "000", "000@com"));
        list.add(new Person("321", "雪莉", "111", "111", "111@com"));
        people.addAll(list);
        System.out.println(people.size());
        for(int i=0; i<people.size(); i++){
            System.out.println(people.get(i).getId());
            System.out.println(people.get(i).getName());
            System.out.println(people.get(i).getPhone());
            System.out.println(people.get(i).getQQ());
            System.out.println(people.get(i).getEmail());
            System.out.println();
        }
    }
}