package com.fzy.core.util;

import com.fzy.core.base.ServiceException;
import com.fzy.core.config.ErrorsMsg;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则验证
 *
 * @author Fucai
 * @date 2018/3/24
 */
public class RagularUtil {

    /**
     * 身份证匹配规则
     */
    public final static String idCardRegEx = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";

    /**
     * 手机号匹配规则
     */
    public final static String mobileRegEx = "^1[34578]\\d{9}$";

    /**
     * 邮箱匹配规则
     */
    public final static String emailRegEx = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";

    /**
     * 匹配中文字符
     */
    public final static String chineseCharRegEx = "[\\u4e00-\\u9fa5]";

    /**
     * 匹配帐号是否合法(字母开头，允许5-16字节，允许字母数字下划线)
     */
    public final static String accountRegEx = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$";

    /**
     * 固定电话
     */
    public final static String telphoneRegEx = "^(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)$";

    /**
     * qq号
     */
    public final static String qqRegEx = "^\\d{5,10}$";


    /**
     * 验证手机号
     *
     * @param mobile
     */
    public static void validateMobile(String mobile) {
        if (mobile == null || !RagularUtil.regularFind(mobile, RagularUtil.mobileRegEx)) {
            throw new ServiceException(ErrorsMsg.ERR_1002, "手机号有误，请检查");
        }
    }

    /**
     * 验证邮箱
     *
     * @param email
     */
    public static void validateEmail(String email) {
        if (email == null || !RagularUtil.regularFind(email, RagularUtil.emailRegEx)) {
            throw new ServiceException(ErrorsMsg.ERR_1002, "邮箱有误，请检查");
        }
    }


    /**
     * 正则匹配
     *
     * @param validateString 需要验证的字符
     * @param regEx          匹配规则
     * @return true-能匹配  false-不能匹配
     */
    public static Boolean regularMatching(String validateString, String regEx) {
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(validateString);
        return matcher.matches();

    }

    /**
     * 正则查找是否含有某字符串
     *
     * @param validateString 需要验证的字符
     * @param regEx          匹配规则
     * @return true-有  false-没有
     */
    public static Boolean regularFind(String validateString, String regEx) {
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(validateString);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 正则查找匹配的字符
     *
     * @param validateString 需要验证的字符
     * @param regEx          匹配规则
     * @return 匹配字符
     */
    public static String regularFindWrite(String validateString, String regEx) {
        StringBuilder builder = new StringBuilder();
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(validateString);
        while (matcher.find()) {
            builder.append(matcher.group());
        }
        return builder.toString();
    }

    /**
     * 正则查找匹配的字符
     *
     * @param validateString 需要验证的字符
     * @param regEx          匹配规则
     * @return 匹配字符列表
     */
    public static List<String> regularFindWriteList(String validateString, String regEx) {
        List<String> returnList = new ArrayList<>();
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(validateString);
        while (matcher.find()) {
            returnList.add(matcher.group());
        }
        return returnList;
    }
}
