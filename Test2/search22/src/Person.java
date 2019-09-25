/**
 * Person 类
 */
class Person{
    String id;
    String name;
    String phone;
    String qq;
    String email;
    Person(){

    }
    Person(String id, String name, String phone){
        this.id = id;
        this.name = name;
        this.phone = phone;
    }
    Person(String id, String name, String phone, String qq, String email){
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.qq = qq;
        this.email = email;
    }

    String getId(){
        return this.id;
    }
    void setId(String id){
        this.id = id;
    }
    String getName(){
        return this.name;
    }
    void setName(String name){
        this.name = name;
    }
    String getPhone(){
        return this.phone;
    }
    void setPhone(String phone){
        this.phone = phone;
    }
    String getQQ(){
        return this.qq;
    }
    void setQQ(String qq){
        this.qq = qq;
    }
    String getEmail(){
        return this.email;
    }
    void setEmail(String email){
        this.email = email;
    }
    /* 字符串匹配 
       返回值： 
            2 精确匹配
            1 模糊匹配
            0 不匹配 
    */  
    int match(String search){
        int relation = 0;
        if(search == null || search == "") {
        	return relation;
        }
        if(search.equals(this.id)){			// 防止 this.id 为空时出现空指针异常
            relation = 2;
            return relation;
        }else if(this.id != null && (this.id).indexOf(search) != -1){
            relation = 1;
        }
        if(search.equals(this.name)){
            relation = 2;
            return relation;
        }else if(this.name != null && (this.name).indexOf(search) != -1){
            relation = 1;
        }
        if(search.equals(this.phone)){
            relation = 2;
            return relation;
        }else if(this.phone != null && (this.phone).indexOf(search) != -1){
            relation = 1;
        }
        if(search.equals(this.qq)){
            relation = 2;
            return relation;
        }else if(this.qq != null && (this.qq).indexOf(search) != -1){
            relation = 1;
        }
        if(search.equals(this.email)){
            relation = 2;
            return relation;
        }else if(this.email != null && (this.email).indexOf(search) != -1){
            relation = 1;
        }
        return relation;
    }
}
