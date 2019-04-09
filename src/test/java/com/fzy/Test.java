package com.fzy;

/**
 * @author: fucai
 * @Date: 2019-01-25
 */
public class Test {
    public static void main(String[] args) {
        String str = "SELECT AA.uid uid,AA.itype,itype,CASE WHEN(AA.appkey like '%Android%') then 'CSAndroid'\n" +
                "          WHEN (AA.appkey like '%Ios%') then 'CSIos'\n" +
                "          ELSE 'xxx' END AS appkey, \n" +
                "    AA.appsource appsource, \n" +
                "    AA.last_login_time last_login_time, \n" +
                "    AA.parms parms\n" +
                "FROM (SELECT A.uid uid,A.itype itype, \n" +
                "        CASE  WHEN (A.itype not in (1, 2, 3) and A.appsource!='xxx') then A.appkey\n" +
                "          ELSE COALESCE(B.appkey, 'xxx')\n" +
                "        END AS appkey,\n" +
                "        CASE WHEN (A.itype not in (1, 2, 3) and A.appsource!='xxx') then A.appsource\n" +
                "          ELSE COALESCE(B.appsource, 'xxx')\n" +
                "        END AS appsource,   A.last_login_time last_login_time, A.parms parms\n" +
                "    FROM crazw_tmp_type_of_all_user_{1}_{2}_{3} A LEFT JOIN (\n" +
                "      select uid, appkey, appsource \n" +
                "      from tab_today_not_xxx_appsource_of_uid_{1}_{2}_{3}\n" +
                "    ) B ON A.uid=B.uid\n" +
                ") AA;";

        String s = str.replaceAll("[\n\t]", " ").replaceAll("\\s+", " ");
        System.out.println(s);

    }
}
