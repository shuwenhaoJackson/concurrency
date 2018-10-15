package com.mmall.concurrency.example.threadLocal;

public class RequestHodle  {

    private static ThreadLocal<Long> requestHodle = new ThreadLocal<>();

    public static void add(Long id){
        requestHodle.set(id);
    }

    public static Long getId(){
        return requestHodle.get();
    }

    public static void remove(){
        requestHodle.remove();
    }
}
