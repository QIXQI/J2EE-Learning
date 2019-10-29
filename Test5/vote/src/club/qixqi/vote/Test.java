package club.qixqi.vote;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import com.alibaba.fastjson.JSONObject;

public class Test{

    /**
     * 在流和对象模型之间使用混合模式
     * 将 teachers.json 中的对象一个一个的读入内存
     */
    
    public static void main(String[] args){
        // readStream();
        // testJson();
        testSession();
    }

    public static void readStream(){
        try{
            // String filePath = JsonReader.class.getClassLoader().getResource("json/teachers.json").getPath();
            String filePath = "json/teachers.json";
            InputStream stream = new FileInputStream(new File(filePath));

            JsonReader reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
            Gson gson = new GsonBuilder().create();

            // 流模式读取文件
            reader.beginArray();
            while(reader.hasNext()){
                // 对象模式读取文件
                Teacher teacher = gson.fromJson(reader, Teacher.class);
                if(teacher.getId() != null){
                    System.out.println("流模式：" + teacher);
                }
            }
            reader.close();
        } catch(UnsupportedEncodingException uex){
            System.out.println("UnsupportedEncodingException: " + uex.getMessage());
        } catch(IOException ex){
            System.out.println("IOException: " + ex.getMessage());
        }
    }

    public static void testJson(){
        JSONObject object = new JSONObject();
        object.put("string", "string");
        object.put("int", 2);
        object.put("boolean", true);
        List<Integer> list = Arrays.asList(1,2,3);
        object.put("list", list);
        object.put("null", null);
        System.out.println(object);
    }

    public static void testSession(){
        List<JSONObject> id = new ArrayList<JSONObject>();
        JSONObject object1 = new JSONObject();
        JSONObject object2 = new JSONObject();
        object1.put("name", "male");
        object1.put("value", 20);
        object2.put("name", "female");
        object2.put("value", 10);
        id.add(object1);
        id.add(object2);
        String str_Id = id.toString();
        System.out.println(id); 
        System.out.println(str_Id);
    }
}