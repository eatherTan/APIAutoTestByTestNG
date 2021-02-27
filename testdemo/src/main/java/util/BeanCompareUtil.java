package util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanCompareUtil {
    /**
     * 用于两个对象进行比较(还没有完善，对于String类型的属性，如果updateObject该属性值是null，则默认把该属性值设置为空)
     * @param object
     * @param updateObject
     * @param className
     * @param <T>
     * @return
     */
    public static <T>T beanCompare(T object,T updateObject,String className){
        try {
            T t = (T) Class.forName(className).newInstance();
            Class<? extends Object> c = t.getClass();
            Class<? extends Object> rtClass = object.getClass();
            Field[] fields = rtClass.getDeclaredFields();//取得所有类成员变量
            Field[] field2 = updateObject.getClass().getDeclaredFields();//取得所有类成员变量
            Field[] field3 = updateObject.getClass().getDeclaredFields();//取得所有类成员变量

            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                Field resField = field3[i];
                boolean flag = field.isAccessible();
                try {
                    //设置该属性总是可访问
                    field.setAccessible(true);
                    field2[i].setAccessible(true);
                    if (field2[i].get(updateObject) != null && !field2[i].get(updateObject).equals(field.get(object))) {
                        String fieldSetName = parSetName(field.getName());
                        Method fieldSetMet = c.getMethod(fieldSetName, resField.getType());
                        fieldSetMet.invoke(t, field2[i].get(updateObject));
                    }
                    if(field2[i].getType().getTypeName().equals(field.getType()) && field2[i].getType().getTypeName().equals(String.class.getTypeName()) && field.get(object) != null && field2[i].get(updateObject)==null){
                        String fieldSetName = parSetName(field.getName());
                        Method fieldSetMet = c.getMethod(fieldSetName, resField.getType());
                        fieldSetMet.invoke(t, "");
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                //还原可访问权限
                field.setAccessible(flag);
                field2[i].setAccessible(flag);
            }
            return t;
        } catch (ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String parSetName(String fieldName) {
        if (null == fieldName || "".equals(fieldName)) {
            return null;
        }
        return "set" + fieldName.substring(0, 1).toUpperCase()+ fieldName.substring(1);
    }
}
