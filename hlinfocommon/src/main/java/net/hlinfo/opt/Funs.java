package net.hlinfo.opt;

import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.crypto.digests.SHA3Digest;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.json.JsonIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Funs {

    /**
     * 中国国家商用密码杂凑算法SM3
     *
     * @author 呐喊
     * @param str
     * @return
     */
    public static String sm3(String str) {
        str = (str == null) ? "" : str;
        byte[] pmdata = string2byte(str);
        SM3Digest sm3 = new SM3Digest();
        sm3.update(pmdata, 0, pmdata.length);
        byte[] hash = new byte[sm3.getDigestSize()];
        sm3.doFinal(hash, 0);
        String rsSM3 = ByteUtils.toHexString(hash);
        return rsSM3;
    }

    /**
     * 中国国家商用密码杂凑算法SM3 HMAC消息认证
     *
     * @author 呐喊
     * @param str
     * @return
     */
    public static String sm3_hmac(String key, String str) {
        key = (key == null) ? "" : key;
        str = (str == null) ? "" : str;
        KeyParameter kp = new KeyParameter(string2byte(key));
        byte[] pmdata = string2byte(str);
        SM3Digest sm3 = new SM3Digest();
        HMac hmac = new HMac(sm3);
        hmac.init(kp);
        hmac.update(pmdata, 0, pmdata.length);
        byte[] hash = new byte[hmac.getMacSize()];
        hmac.doFinal(hash, 0);
        String rsSM3 = ByteUtils.toHexString(hash);
        return rsSM3;
    }

    public static String sha3(String str) {
        if (str == null) {
            str = "";
        }
        byte[] pmdata = string2byte(str);
        SHA3Digest sha3 = new SHA3Digest();
        sha3.update(pmdata, 0, pmdata.length);
        byte[] hash = new byte[sha3.getDigestSize()];
        sha3.doFinal(hash, 0);
        String resultSha3 = ByteUtils.toHexString(hash);
        return resultSha3;
    }

    public static byte[] string2byte(String s) {
        byte[] pmdata = null;
        try {
            pmdata = s.getBytes("utf-8");
            return pmdata;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return pmdata;
    }

    /**
     * 密码加密
     *
     * @param sourcePwd 原密码
     * @param salt      加密盐
     * @return
     */
    public static String pwdEncry(String sourcePwd, String salt) {
        return sm3_hmac(salt, sourcePwd);
    }

    public static String getRandom(int length) {
        Random random = new Random();
        StringBuilder rs = new StringBuilder();
        for (int i = 0; i < length; i++) {
            rs.append(random.nextInt(10));
        }
        return rs.toString();
    }

    /**
     * 生成15位数ID
     *
     * @return
     */
    public static String getId() {
        long millis = System.currentTimeMillis();
        Random random = new Random();
        int end2 = random.nextInt(99);
        String string = millis + String.format("%02d", end2);
        return string;
    }

    /**
     * 过滤掉手机端输入的表情字符
     *
     * @param source
     * @return
     */
    public static String filterEmoji(String source) {
        if (source == null) {
            return source;
        }
        Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher emojiMatcher = emoji.matcher(source);
        if (emojiMatcher.find()) {
            source = emojiMatcher.replaceAll("*");
            return source;
        }
        return source;
    }
    /**
     * 获取当前网络ip
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("X-Real-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length() = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }
    /**
     * 设置下载文件的Header信息
     * @param request
     * @param response
     * @param fileName 文件名
     * @return
     */
    public static boolean setDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) {
        try {
            response.setContentType("application/octet-stream");
            response.setHeader("content-type", "application/octet-stream");
            String browser = request.getHeader("User-Agent");
            if (-1 < browser.indexOf("MSIE 6.0") || -1 < browser.indexOf("MSIE 7.0")) {
                // IE6, IE7 浏览器
                response.addHeader("content-disposition", "attachment;filename="
                        + new String(fileName.getBytes(), "ISO8859-1"));
            } else if (-1 < browser.indexOf("MSIE 8.0")) {
                // IE8
                response.addHeader("content-disposition", "attachment;filename="
                        + URLEncoder.encode(fileName, "UTF-8"));
            } else if (-1 < browser.indexOf("MSIE 9.0")) {
                // IE9
                response.addHeader("content-disposition", "attachment;filename="
                        + URLEncoder.encode(fileName, "UTF-8"));
            } else if (-1 < browser.indexOf("Chrome")) {
                // 谷歌
                response.addHeader("content-disposition",
                        "attachment;filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8"));
            } else if (-1 < browser.indexOf("Safari")) {
                // 苹果
                response.addHeader("content-disposition", "attachment;filename="
                        + new String(fileName.getBytes(), "ISO8859-1"));
            } else {
                // 火狐或者其他的浏览器
                response.addHeader("content-disposition",
                        "attachment;filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8"));
            }
            return true;
        } catch (Exception e) {
            //log.error(e.getMessage());
            return false;
        }
    }
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
            name = getString(random, girl, girl, name);

        }
        else{
            name = getString(random, girl, boy, name);

        }

        return name;
    }

    private static String getString(Random random, String girl, String boy, String name) {
        int j = random.nextInt(girl.length()-2);
        if (j % 2 == 0) {
            //name = "男-" + name + boy.substring(j, j + 2);
            name = name + boy.substring(j, j + 2);
        } else {
           // name = "男-" + name + boy.substring(j, j + 1);
            name = name + boy.substring(j, j + 1);
        }
        return name;
    }

    /**
     * 将实体对象的属性转为mysql查询语句的select 和from中的字段
     * prex.user_name as fieldPrexuserName
     * @param cls 实体class
     * @param prex 数据字段前缀
     * @param fieldPrex 字段前缀
     * @return
     */
    public static String vo2mysqlField(Class<?> cls
            , String prex
            , String fieldPrex) {
        prex = Funs.isBlank(prex)?"":prex+".";
        fieldPrex = Funs.isBlank(fieldPrex)?"":fieldPrex;
        List<Field> fs = new ArrayList<>();
        Field[] cfs = cls.getDeclaredFields();
        fs.addAll(Arrays.asList(cfs));
        Field[] pfs = {};
        /*if(cls.getSuperclass().getName() == BaseEntity.class.getName()) {
            pfs = cls.getSuperclass().getDeclaredFields();
        }*/
        fs.addAll(Arrays.asList(pfs));
        if(fs.size() <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for(int i = 0; i < fs.size(); i ++) {
            JsonIgnore jsonIgnore = fs.get(i).getAnnotation(JsonIgnore.class);
            if(jsonIgnore != null) {
                continue;
            }
            Column cd = fs.get(i).getDeclaredAnnotation(Column.class);
            String fieldType = fs.get(i).getType().toString();
            if(cd != null) {
                if(Funs.isNotBlank(cd.value())) {
                    if(index > 0) {
                        sb.append(",");
                    }
                    if(fieldType.endsWith("Date")) {
                        sb.append("DATE_FORMAT("+prex+cd.value()+",'%Y-%m-%d %H:%i:%s') as " + fieldPrex + fs.get(i).getName());
                    }else {
                        sb.append(prex+cd.value() + " as " + fieldPrex + fs.get(i).getName());
                    }
                    index ++;
                }
            }
        }
        return sb.toString();
    }
    /**
     * 将实体对象的属性转为Pgsql查询语句的select 和from中的字段
     * prex.user_name as fieldPrexuserName
     * @param cls 实体class
     * @param prex 数据字段前缀
     * @param fieldPrex 字段前缀
     * @return
     */
    public static String vo2PgsqlField(Class<?> cls
            , String prex
            , String fieldPrex) {
        prex = Funs.isBlank(prex)?"":prex+".";
        fieldPrex = Funs.isBlank(fieldPrex)?"":fieldPrex;
        List<Field> fs = new ArrayList<>();
        Field[] cfs = cls.getDeclaredFields();
        fs.addAll(Arrays.asList(cfs));
        Field[] pfs = {};
        /*if(cls.getSuperclass().getName() == BaseEntity.class.getName()) {
            pfs = cls.getSuperclass().getDeclaredFields();
        }*/
        fs.addAll(Arrays.asList(pfs));
        if(fs.size() <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for(int i = 0; i < fs.size(); i ++) {
            JsonIgnore jsonIgnore = fs.get(i).getAnnotation(JsonIgnore.class);
            if(jsonIgnore != null) {
                continue;
            }
            Column cd = fs.get(i).getDeclaredAnnotation(Column.class);
            String fieldType = fs.get(i).getType().toString();
            if(cd != null) {
                if(Funs.isNotBlank(cd.value())) {
                    if(index > 0) {
                        sb.append(",");
                    }
                    if(fieldType.endsWith("Date")) {
                        sb.append("to_char("+prex+cd.value()+",'yyyy-MM-DD HH24:MI:SS') as " + fieldPrex + fs.get(i).getName());
                    }else {
                        sb.append(prex+cd.value() + " as \"" + fieldPrex + fs.get(i).getName()+"\"");
                    }
                    index ++;
                }
            }
        }
        return sb.toString();
    }
    /**
     * 将实体对象的属性转为Pgsql查询语句的select 和from中的字段
     * prex.user_name as fieldPrexuserName
     * @param cls 实体class
     * @param alias 表的别名或表名
     * @param fieldPrex 字段前缀
     * @param filterField 需要过滤的字段，格式为pojo实体名,多个用竖线|分隔，如：userName|userPasswd
     * @param allowField 只允许的字段，格式为pojo实体名,多个用竖线|分隔，如：userName|userPasswd
     * @return
     */
    public static String vo2PgsqlField(Class<?> cls
            , String alias
            , String fieldPrex
            ,String filterField
            ,String allowField) {
        alias = Funs.isBlank(alias)?"":alias+".";
        fieldPrex = Funs.isBlank(fieldPrex)?"":fieldPrex;
        List<Field> fs = new ArrayList<>();
        Field[] cfs = cls.getDeclaredFields();
        fs.addAll(Arrays.asList(cfs));
        Field[] pfs = {};
        /*if(cls.getSuperclass().getName() == BaseEntity.class.getName()) {
            pfs = cls.getSuperclass().getDeclaredFields();
        }*/
        fs.addAll(Arrays.asList(pfs));
        if(fs.size() <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for(int i = 0; i < fs.size(); i ++) {
            JsonIgnore jsonIgnore = fs.get(i).getAnnotation(JsonIgnore.class);
            if(jsonIgnore != null) {
                continue;
            }
            if(Funs.isNotBlank(filterField)) {
                String[] filterFields = filterField.split("\\|");
                if(Arrays.asList(filterFields).contains(fs.get(i).getName())) {
                    continue;
                }
            }
            if(Funs.isNotBlank(allowField)) {
                String[] allowFields = allowField.split("\\|");
                if(!ArrayUtils.contains(allowFields, fs.get(i).getName())) {
                    continue;
                }
            }
            Column cd = fs.get(i).getDeclaredAnnotation(Column.class);
            String fieldType = fs.get(i).getType().toString();
            if(cd != null) {
                if(Funs.isNotBlank(cd.value())) {
                    if(index > 0) {
                        sb.append(",");
                    }
                    if(fieldType.endsWith("Date")) {
                        sb.append("to_char("+alias+cd.value()+",'yyyy-MM-DD HH24:MI:SS') as " + fieldPrex + fs.get(i).getName());
                    }else {
                        sb.append(alias+cd.value() + " as \"" + fieldPrex + fs.get(i).getName()+"\"");
                    }
                    index ++;
                }
            }
        }
        return sb.toString();
    }

    /**
     * 如果此字符串为 null 或者全为空白字符(包含：空格、tab 键、换行符)，则返回 true
     *
     * @param cs
     *            字符串
     * @return 如果此字符串为 null 或者全为空白字符，则返回 true
     */
    public static boolean isBlank(CharSequence cs) {
        if (null == cs)
            return true;
        int length = cs.length();
        for (int i = 0; i < length; i++) {
            if (!(Character.isWhitespace(cs.charAt(i))))
                return false;
        }
        return true;
    }
    /**
     * 如果此字符串不为 null 或者全为空白字符(包含：空格、tab 键、换行符)，则返回 true
     *
     * @param cs
     *            字符串
     * @return 如果此字符串不为 null 或者全为空白字符，则返回 true
     */
    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }
    /**
     * 检查两个字符串是否相等.
     *
     * @param s1
     *            字符串A
     * @param s2
     *            字符串B
     * @return true 如果两个字符串相等,且两个字符串均不为null
     */
    public static boolean equals(String s1, String s2) {
        return s1 == null ? s2 == null : s1.equals(s2);
    }
    /**
     * 检查两个字符串是否不相等.
     *
     * @param s1
     *            字符串A
     * @param s2
     *            字符串B
     * @return 如果两个字符串不相等，返回true
     */
    public static boolean notequals(String s1, String s2) {
        return !equals(s1,s2);
    }


}
