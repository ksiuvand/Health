package com.example.healthy.LoginAndSignUp;

public class User {
    public String name,age,email, weight, height;

    public User(){

    }
    public User(String name, String age, String email, String weight, String height){
        this.name=name;
        this.age=age;
        this.weight=weight;
        this.height=height;
        this.email=email;
    }
}
