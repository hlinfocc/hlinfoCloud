package net.hlinfo.opt;

import org.nutz.lang.Times;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Funs {

    /**
     * DateFormat: yyyy/MM/dd
     * @return
     */
    public final static String getDate(){
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String rzDate = df.format(new Date());
        return rzDate;
    }
    /**
     * DateFormat: yyyyMM
     * @return
     */
    public final static String getDate4ym(){
        DateFormat df = new SimpleDateFormat("yyyyMM");
        String rzDate = df.format(new Date());
        return rzDate;
    }
    /**
     * DateFormat: yyyy年MM月dd
     * @return
     */
    public final static String getDates(){
        DateFormat df = new SimpleDateFormat("yyyy年MM月dd");
        String rzDate = df.format(new Date());
        return rzDate;
    }
    /**
     * DateFormat: HH:mm:ss
     * @return
     */
    public final static String getTime(){
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        String rzDate = df.format(new Date());
        return rzDate;
    }
    /**
     * DateFormat: yyyyMMddHHmmss
     * @return
     */
    public final static String getNowDateTime4Num(){
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String rzDate = df.format(new Date());
        return rzDate;
    }

    public static String uuid(){
        String s=java.util.UUID.randomUUID().toString();
        return s.replaceAll("-", "");
    }
    /**
     * DateFormat: yyyy/MM/dd HH:mm:ss
     * @return
     */
    public final static String getNowDateTime(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String rzDate = df.format(new Date());
        return rzDate;
    }
    public static String concatHref(Map map){
        Set set  = map.keySet();
        Iterator keys = set.iterator();
        String href="";
        while(keys.hasNext()){
            String key=keys.next().toString();
            if(map.get(key)!=null)
                href+=(href.equals("")?"":"&")+key+"="+map.get(key);
        }
        return href;
    }
    public final static String getYear(){
        DateFormat df = new SimpleDateFormat("yyyy");
        String rzDate = df.format(new Date());
        return rzDate;
    }
    /**
     * 计算指定时间到现在有多久
     * @param date
     * @return
     */
    public static String getTimesToNow(String date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String now = format.format(new Date());
        String returnText = null;
        try {
            long from = format.parse(date).getTime();
            long to = format.parse(now).getTime();

            int days = (int) ((to - from)/(1000 * 60 * 60 * 24));
            if(days == 0){//一天以内，以分钟或者小时显示
                int hours = (int) ((to - from)/(1000 * 60 * 60));
                if(hours == 0){
                    int minutes = (int) ((to - from)/(1000 * 60));
                    if(minutes == 0){
                        returnText = "刚刚";
                    }else{
                        returnText = minutes + "分钟前";
                    }
                }else{
                    returnText = hours + "小时前";
                }
            } else if(days == 1){
                returnText = "昨天";
            }else{
                returnText = days + "天前";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnText;
    }
    /**
     * 根据实体获取表名
     * @param obj
     * @param camel2und 是否将驼峰命名转下划线
     * @return
     */
    public static String getTableName(Object obj,boolean camel2und) {
        String tableName = obj.getClass().getName();
        tableName=tableName.substring(tableName.lastIndexOf(".")+1);
        tableName=tableName.replaceFirst(tableName.substring(0,1), tableName.substring(0,1).toLowerCase());
        if(camel2und) {
            tableName = camel2under(tableName);
        }
        return tableName;
    }

    /**
     * 根据实体获取表名
     * @param obj
     * @param camel2und 是否将驼峰命名转下划线
     * @return
     */
    public static String getTableName(Class obj,boolean camel2und) {
        String clzzTableName = obj.getName();
        clzzTableName=clzzTableName.substring(clzzTableName.lastIndexOf(".")+1);
        clzzTableName=clzzTableName.replaceFirst(clzzTableName.substring(0,1), clzzTableName.substring(0,1).toLowerCase());
        if(camel2und) {
            clzzTableName = camel2under(clzzTableName);
        }
        return clzzTableName;
    }
    /**
     * 功能：下划线命名转驼峰命名
     * 将下划线替换为空格,将字符串根据空格分割成数组,再将每个单词首字母大写
     * @param s
     * @return
     */
    public static String under2camel(String s)
    {
        String separator = "_";
        String under="";
        s = s.toLowerCase().replace(separator, " ");
        String sarr[]=s.split(" ");
        for(int i=0;i<sarr.length;i++)
        {
            String w=sarr[i].substring(0,1).toUpperCase()+sarr[i].substring(1);
            under +=w;
        }
        return under;
    }
    /**
     * 功能：驼峰命名转下划线命名
     * 小写和大写紧挨一起的地方,加上分隔符,然后全部转小写
     */
    public static String camel2under(String c)
    {
        String separator = "_";
        c = c.replaceAll("([a-z])([A-Z])", "$1"+separator+"$2").toLowerCase();
        return c;
    }
    public static String getText(String text,String regex){
        Pattern pattern= Pattern.compile(regex);
        Matcher matcher=pattern.matcher(text);
        if(matcher.find())return matcher.group(1);
        return null;
    }
    public static int string2int(String s){
        if(s==null)return 0;
        s=getText(s,"(\\d+)");
        if("".equals(s)||s==null)return 0;
        return Integer.parseInt(s);
    }
    public static Long string2Long(String s){
        if(s==null)return 0l;
        s=getText(s,"(\\d+)");
        if("".equals(s)||s==null)return 0l;
        return Long.parseLong(s);
    }
    public static long obj2int(Object obj){
        if(obj==null)return 0;
        try {
            return Long.parseLong(obj.toString());
        } catch (NumberFormatException e) {
            //e.printStackTrace();
            System.out.println("error=");
            return 0;
        }
    }

    public static String getRandom(int length) {
        Random random = new Random();
        StringBuilder rs = new StringBuilder();
        for (int i = 0; i < length; i++) {
            rs.append(random.nextInt(10));
        }
        return rs.toString();
    }
    public static String getName() {
        Random random = new Random();
        String[] Surname = {"赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "褚", "卫", "蒋", "沈", "韩", "杨", "朱", "秦", "尤", "许",
                "何", "吕", "施", "张", "孔", "曹", "严", "华", "金", "魏", "陶", "姜", "戚", "谢", "邹", "喻", "柏", "水", "窦", "章", "云", "苏", "潘", "葛", "奚", "范", "彭", "郎",
                "鲁", "韦", "昌", "马", "苗", "凤", "花", "方", "俞", "任", "袁", "柳", "酆", "鲍", "史", "唐", "费", "廉", "岑", "薛", "雷", "贺", "倪", "汤", "滕", "殷",
                "罗", "毕", "郝", "邬", "安", "常", "乐", "于", "时", "傅", "皮", "卞", "齐", "康", "伍", "余", "元", "卜", "顾", "孟", "平", "黄", "和",
                "穆", "萧", "尹", "姚", "邵", "湛", "汪", "祁", "毛", "禹", "狄", "米", "贝", "明", "臧", "计", "伏", "成", "戴", "谈", "宋", "茅", "庞", "熊", "纪", "舒",
                "屈", "项", "祝", "董", "梁", "杜", "阮", "蓝", "闵", "席", "季"};
        String girl = "秀娟英华慧巧美娜静淑惠珠翠雅芝玉萍红娥玲芬芳燕彩春菊兰凤洁梅琳素云莲真环雪荣爱妹霞香月莺媛艳瑞凡佳嘉琼勤珍贞莉桂娣叶璧璐娅琦晶妍茜秋珊莎锦黛青倩婷姣婉娴瑾颖露瑶怡婵雁蓓纨仪荷丹蓉眉君琴蕊薇菁梦岚苑婕馨瑗琰韵融园艺咏卿聪澜纯毓悦昭冰爽琬茗羽希宁欣飘育滢馥筠柔竹霭凝晓欢霄枫芸菲寒伊亚宜可姬舒影荔枝思丽 ";
        String boy = "伟刚勇毅俊峰强军平保东文辉力明永健世广志义兴良海山仁波宁贵福生龙元全国胜学祥才发武新利清飞彬富顺信子杰涛昌成康星光天达安岩中茂进林有坚和彪博诚先敬震振壮会思群豪心邦承乐绍功松善厚庆磊民友裕河哲江超浩亮政谦亨奇固之轮翰朗伯宏言若鸣朋斌梁栋维启克伦翔旭鹏泽晨辰士以建家致树炎德行时泰盛雄琛钧冠策腾楠榕风航弘";
        int index = random.nextInt(Surname.length - 1);
        String name = Surname[index]; //获得一个随机的姓氏
        int i = random.nextInt(3);//可以根据这个数设置产生的男女比例
        if(i==2){
            int j = random.nextInt(girl.length()-2);
            if (j % 2 == 0) {
                //name = "女-" + name + girl.substring(j, j + 2);
                name = name + girl.substring(j, j + 2);
            } else {
                //name = "女-" + name + girl.substring(j, j + 1);
                name = name + girl.substring(j, j + 1);
            }

        }
        else{
            int j = random.nextInt(girl.length()-2);
            if (j % 2 == 0) {
                //name = "男-" + name + boy.substring(j, j + 2);
                name = name + boy.substring(j, j + 2);
            } else {
               // name = "男-" + name + boy.substring(j, j + 1);
                name = name + boy.substring(j, j + 1);
            }

        }

        return name;
    }

    public static void main(String[] args) {

    }

}
