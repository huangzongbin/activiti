package com.krt.common.constant;


/**
 * 工作流系统常量
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月25日
 */
public class ActConstant {

    /**
     * 工作流连线的默认名称
     */
    public static final String DEFAULT_OUTCOME = "提交";

    /**
     * 业务状态字典
     */
    public static final String BIZ_STATUS = "biz_status";

    /**
     * 流程定义状态
     */
    public enum PdStatus {
        /**
         * 激活
         */
        ACTIVE("active"),
        /**
         * 挂起
         */
        SUSPEND("suspend");

        private String value;

        PdStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 流程节点类型
     */
    public enum NodeType {
        /**
         * 开始节点
         */
        START("1"),
        /**
         * 审批节点
         */
        EXAMINE("2"),

        /**
         * 分支
         */
        BRUNCH("3"),
        /**
         * 连线
         */
        LINE("4"),
        /**
         * 结束
         */
        END("5");

        private String value;

        NodeType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 流程节点类型
     */
    public enum PvmType {
        /**
         * 开始节点
         */
        START_EVENT("startEvent"),
        /**
         * 任务节点
         */
        USER_TASK("userTask"),

        /**
         * 互斥网关
         */
        EXCLUSIVE_GATEWAY("exclusiveGateway"),
        /**
         * 并行网关
         */
        PARALLEL_GATEWAY("parallelGateway"),
        /**
         * 结束
         */
        END_EVENT("endEvent");

        private String value;

        PvmType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 业务树类型
     */
    public enum ActBusType {
        /**
         * 分组
         */
        GROUP("1"),
        /**
         * 业务类
         */
        BUS("2"),
        /**
         * 回调
         */
        BACK("3");

        private String value;

        ActBusType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 审批者类型
     */
    public enum ExamineType {
        /**
         * 用户
         */
        USER("1"),
        /**
         * 角色
         */
        ROLE("2"),

        /**
         * 组织
         */
        ORGAN("3");

        private String value;

        ExamineType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 流程状态
     */
    public enum ActStauts {
        /**
         * 草稿
         */
        DRAFT("1"),
        /**
         * 审批中
         */
        APPROVAL("2"),
        /**
         * 结束
         */
        END("3");

        private String value;

        ActStauts(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 流程任务审批结果
     */
    public enum ActTaskResult {
        /**
         * 提交
         */
        SUBMIT("0"),
        /**
         * 同意
         */
        AGREE("1"),
        /**
         * 反对
         */
        NO_AGREE("2"),
        /**
         * 弃权
         */
        ABSTAINED("3"),
        /**
         * 驳回
         */
        TURN_DOWN("4"),
        /**
         * 转办
         */
        TURN_DO("5"),
        /**
         * 终止
         */
        END("6"),

        /**
         * 挂起
         */
        SUSPEND("7");

        private String value;

        ActTaskResult(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 整个流程的审批结果
     */
    public enum ActResult {
        /**
         * 同意
         */
        AGREE("1"),
        /**
         * 不同意
         */
        NO_AGREE("2"),
        /**
         * 审批中
         */
        DISAGREE("3"),
        /**
         * 终止
         */
        END("4");

        private String value;

        ActResult(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 字段类型
     */
    public enum FieldType {
        /**
         * 1=可写(可写的也能读)
         */
        WRITE("1"),
        /**
         * 2=参与连线判断
         */
        JUDG("2");

        private String value;

        FieldType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
