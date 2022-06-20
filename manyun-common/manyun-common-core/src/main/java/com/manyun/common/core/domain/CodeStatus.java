package com.manyun.common.core.domain;

/**
 * @Author:yanwei
 * @Date: 2020/8/5 - 12:03
 */

public enum  CodeStatus {

        DICT_EXISTED(400, "字典已经存在"),
        ERROR_CREATE_DICT(500, "创建字典失败"),
        ERROR_WRAPPER_FIELD(500, "包装字典属性失败"),
        ERROR_CODE_EMPTY(500, "字典类型不能为空"),

        /**
         * 常规返回码
         */

        SUCCESS(200, "操作成功"),
        FAIL(201, "操作失败"),
        PARMAS(203, "参数缺失"),
        ILLEGAL_PARAMETER(204,"非法的请求参数"),

        /**
         * 文件上传
         */
        FILE_READING_ERROR(400, "FILE_READING_ERROR!"),
        FILE_NOT_FOUND(400, "FILE_NOT_FOUND!"),
        UPLOAD_ERROR(500, "上传图片出错"),

        /**
         * 权限和数据问题
         */
        DB_RESOURCE_NULL(400, "数据库中没有该资源"),
        NO_PERMITION(405, "权限异常"),
        REQUEST_INVALIDATE(400, "请求数据格式不正确"),
        INVALID_KAPTCHA(400, "验证码不正确"),
        INVALID_KAPTCHA_OVERDUE(400, "验证码已过期"),
        CANT_DELETE_ADMIN(600, "不能删除超级管理员"),
        CANT_FREEZE_ADMIN(600, "不能冻结超级管理员"),
        CANT_CHANGE_ADMIN(600, "不能修改超级管理员角色"),

        /**
         * app登录
         */
        NO_BIND_TELPHONE(100, "没有绑定手机号"),
        TELPHONE_NO_VERIFICODE(101, "请获取验证码！"),
        TELPHONE_CHECK_FAIL(102, "手机号码验证失败"),
        TELPHONE_ALREADY_EXISTS(103, "该手机号已经被注册"),
        WECHAT_NO_AUTH(104, "你还没有微信授权！"),
        ALREADY_SIGN(105, "今天已经签到，请明天再来！"),

        /**
         * 账户问题
         */
        THIS_CARD_ALREADY_USE(412,"该身份证已经被实名"),
        REALNAME_INCORRECT(410,"身份验证失败"),
        NOT_LOGIN(401, "当前用户未登录"),
        ADD_NOT_LOGIN(40100, "不能多登录"),
        USER_ALREADY_REG(401, "该用户已经注册"),
        NO_THIS_USER(400, "没有此用户"),
        USER_NOT_EXISTED(400, "没有此用户"),
        ACCOUNT_FREEZED(401, "账号被冻结"),
        OLD_PWD_NOT_RIGHT(402, "原密码不正确"),
        TWO_PWD_NOT_MATCH(405, "两次输入密码不一致"),
        THISMONTH_ALREADY_UNBINDDING(410,"本月已经解绑一次，不能再解绑"),
        ACCOUNT_FORMAT_ERROR(417, "手机号格式错误"),

        /**
         * 金额
         */
        LACK_OF_BALANCE(406,"余额不足"),
        AMOUNT_ERROR(407,"金额错误"),


        /**
         * 视频类
         */
        PLEASE_WAIT(408,"操作频繁，请等待几秒后重试"),

        TODAY_TIMES_HAS_USED(409,"今天看视频次数已用完"),
        /**
         * 错误的请求
         */
        MENU_PCODE_COINCIDENCE(400,"菜单编号和副编号不能一致") {
        },

        EXISTED_THE_MENU(400,"菜单编号重复，不能添加") {
        },

        DICT_MUST_BE_NUMBER(400,"字典的值必须为数字") {
        },

        REQUEST_NULL(400,"请求有错误") {
        },

        SESSION_TIMEOUT(400,"会话超时") {
        },

        SERVER_ERROR(500,"服务器异常") {
        },

        /**
         * token异常
         */
        TOKEN_EXPIRED(700,"token过期") {
        },

        TOKEN_ERROR(700,"token验证失败") {
        },

        /**
         * 签名异常
         */
        SIGN_ERROR(700,"签名验证失败") {
        },
        QR_EMPYT(700,"暂无二维码"){

        },
        /**
         * 其他
         */
        AUTH_REQUEST_ERROR(400, "账号密码错误"),
        VALUE_LACK(400,"余量不足"),
        QR_ERROR(201,"未返回"),

        /**
         * nxfc 用户
         */
        NXFC_USER_EX(300,"NXFC未登录");

        CodeStatus(int code, String message) {
            this.code = code;
            this.message = message;
        }

        private Integer code;

        private String message;


        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }


        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }



}
