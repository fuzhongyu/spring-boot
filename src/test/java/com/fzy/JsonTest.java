package com.fzy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * @auther fucai
 * @date 2020/9/28
 **/
public class JsonTest {

    public static void main(String[] args) {
        T3 t3 = new T3("c");
        List<T3> t3List = new ArrayList<>();
        t3List.add(t3);
        T2 t2 = new T2("a", 1,t3List);
        T2 t21 = new T2("b", 2,new ArrayList<>());
        List<T2> list = new ArrayList<>();
        list.add(t2);
        list.add(t21);
        T1  t1 = new T1("1001", list);
        String str = JSON.toJSONString(t1);
        System.out.println(str);
        T1 fj = JSON.parseObject(str, T1.class);
        System.out.println(fj.getList().get(0).getList().get(0).getMessage());

    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class T1 {
    private String code;
    private List<T2> list;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class T2 {
    private String name;
    private Integer age;
    private List<T3> list;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class T3 {
    private String message;
}
