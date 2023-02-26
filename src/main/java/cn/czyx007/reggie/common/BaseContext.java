package cn.czyx007.reggie.common;

/**
 * @author : 张宇轩
 * @createTime : 2022/12/21 - 18:19
 */
public class BaseContext {
    private BaseContext(){}

    private static ThreadLocal<Long> local = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        local.set(id);
    }

    public static Long getCurrentId(){
        return local.get();
    }
}
