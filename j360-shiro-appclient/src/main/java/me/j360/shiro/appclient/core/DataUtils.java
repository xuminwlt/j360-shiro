package me.j360.shiro.appclient.core;

import java.util.Date;
import java.util.Map;

public class DataUtils {

    /**
     *
     * @param map
     * @param field
     * @return
     */
    public static String getDataAsString(Map<String,Object> map,String field){
        if(map==null)
            return "";
        return map.get(field)==null?"":map.get(field).toString();
    }

    public static Float getDataAsFloat(Map<String,Object> map,String field){
        if(map==null)
            return 0F;
        return map.get(field)==null?0:Float.valueOf(map.get(field).toString());
    }

    public static Long getDataAsLong(Map<String,Object> map,String field){
        if(map==null)
            return 0L;
        return map.get(field)==null?0:Long.valueOf(map.get(field).toString());
    }

    public static Integer getDataAsInteger(Map<String,Object> map,String field){
        if(map==null)
            return 0;
        return map.get(field)==null?0:Integer.valueOf(map.get(field).toString());
    }

    public static Date getDataAsDate(Map<String,Object> map,String field){
        if(map==null)
            return null;
        if(map.get(field)==null){
            return null;
        }else{
            Date time =  (Date) map.get(field);
            Long timestamp =  time.getTime()+8*3600*1000;
            return new Date(timestamp);
        }
    }

    public static Date getDataAsNoUTCDate(Map<String,Object> map,String field){
        if(map==null)
            return null;
        if(map.get(field)==null){
            return null;
        }else{
            Date time =  (Date) map.get(field);
            Long timestamp =  time.getTime();
            return new Date(timestamp);
        }
    }

    public static long getTimestampAsUTC(long timestamp){
            return timestamp+8*3600*1000;
    }

    public static Byte getDataAsByte(Map<String,Object> map,String field){
        if(map==null)
            return 0;
        return map.get(field)==null?0:Byte.valueOf(map.get(field).toString());
    }

    public static Date local2UTC(){
        Date date = new Date();
        long time = date.getTime()-8*3600*1000;
        return new Date(time);
    }

}
