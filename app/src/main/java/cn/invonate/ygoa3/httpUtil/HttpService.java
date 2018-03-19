package cn.invonate.ygoa3.httpUtil;


import java.util.List;
import java.util.Map;

import cn.invonate.ygoa3.Entry.Amount;
import cn.invonate.ygoa3.Entry.Approved;
import cn.invonate.ygoa3.Entry.ChangePass;
import cn.invonate.ygoa3.Entry.Contacts;
import cn.invonate.ygoa3.Entry.CyContacts;
import cn.invonate.ygoa3.Entry.Department;
import cn.invonate.ygoa3.Entry.Friend;
import cn.invonate.ygoa3.Entry.Fund;
import cn.invonate.ygoa3.Entry.Group;
import cn.invonate.ygoa3.Entry.Group_member;
import cn.invonate.ygoa3.Entry.InitPassMessage;
import cn.invonate.ygoa3.Entry.Lomo;
import cn.invonate.ygoa3.Entry.Member;
import cn.invonate.ygoa3.Entry.Mission;
import cn.invonate.ygoa3.Entry.Property;
import cn.invonate.ygoa3.Entry.Salary;
import cn.invonate.ygoa3.Entry.Task;
import cn.invonate.ygoa3.Entry.TaskCopy;
import cn.invonate.ygoa3.Entry.TaskLine;
import cn.invonate.ygoa3.Entry.User;
import cn.invonate.ygoa3.Entry.Welfare;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by liyangyang on 17/3/14.
 */
public interface HttpService {
    // 登录
    @GET("/ygoa/ydpt/loginYdpt.action")
    Observable<User> login(
            @Query("userName") String userName,
            @Query("password") String password
    );

    // 通讯录查询部门
    @GET("/ygoa/ydpt/getAlldepartment.action")
    Observable<List<Department>> getDepartment(
            @Query("id_") String id
    );

    // 通讯录部门成员
    @GET("/ygoa/ydpt/getUserByDeptID.action")
    Observable<List<Contacts>> getContacts(
            @Query("id_") String id
    );

    // 通讯录模糊查询
    @GET("/ygoa/ydpt/queryUserBycodeAndName.action")
    Observable<Member> getMembers(
            @Query("nameOrCode") String keywords,
            @Query("page") int page,
            @Query("row") int row
    );

    // 忘记密码
    @GET("/ygoa/ydpt/authenticationByCard.action")
    Observable<InitPassMessage> initPass(
            @Query("code") String code,
            @Query("Identification_card") String identify
    );

    // 修改密码
    @GET("/ygoa/ydpt/updateUserPasswordByOldPassword.action")
    Observable<ChangePass> changePass(
            @Query("oldPassword_") String oldPassword_,
            @Query("password") String password,
            @Query("code") String code
    );

    // 获取好友
    @GET("/ygoa/ydpt/getChatFriends.action")
    Observable<Friend> getFriends(
            @Query("code") String code
    );

    // 获取群组
    @GET("/ygoa/ydpt/getChatRoom.action")
    Observable<Group> getGroups(
            @Query("code") String code
    );

    // 获取群组成员
    @GET("/ygoa/ydpt/getGroupMembers.action")
    Observable<Group_member> getGroup_members(
            @Query("id") String id
    );

    // 获取待办任务
    @GET("/ygoa/ydpt/loadMyTask.action")
    Observable<Mission> getTasks(
            @Query("sessionId") String sessionId
    );

    // 待办详情
    @GET("/ygoa/ydpt/loadMyTaskxx.action")
    Observable<String> getTaskDetail(
            @Query("sessionId") String sessionId,
            @Query("businessId") String businessId,
            @Query("taskId") String id,
            @Query("workflowType") String workflowType
    );

    // 审批流程
    @GET("/ygoa/ydpt/loadApprovedHistory.action")
    Observable<TaskLine> getTaskLine(
            @Query("businessId") String businessId
    );

    // 已审批流程
    @GET("/ygoa/ydpt/loadAllApproved.action")
    Observable<Approved> getApproved(
            @Query("sessionId") String sessionId,
            @Query("page") int page
    );

    // 已发起
    @GET("/ygoa/ydpt/loadMyStartTask.action")
    Observable<Approved> getStartTask(
            @Query("sessionId") String sessionId,
            @Query("page") int page
    );

    // 处理任务
    @POST
    @FormUrlEncoded
    Observable<Task> processTask(
            @Url String url,
            @FieldMap Map<String, String> params
    );

    // 抄送任务
    @GET("/ygoa/ydpt/loadMyCc.action")
    Observable<TaskCopy> getCopyTask(
            @Query("sessionId") String sessionId
    );

    // 随后拍列表
    @POST("/ygoa/ydptlomo/queryLomo.action")
    @FormUrlEncoded
    Observable<Lomo> getLomoList(
            @Field("page") int page,
            @Field("rows") int rows
    );

    // 个人资产
    @GET("/ygoa/ydpt/queryPersonAsset.action")
    Observable<Property> getProperty(
            @Query("sessionId") String sessionId
    );

    // 工资
    @GET("/ygoa/ydpt/loadSailSafe.action")
    Observable<Salary> getSalary(
            @Query("sessionId") String sessionId,
            @Query("newValue") int newValue
    );

    // 福利
    @GET("/ygoa/ydpt/loadOther.action")
    Observable<Welfare> getWelfare(
            @Query("sessionId") String sessionId
    );

    // 电子钱包
    @GET("/ygoa/ydpt/queryAccountTranactionFlow.action")
    Observable<Fund> getFund(
            @Query("sessionId") String sessionId,
            @Query("start_date") String start_date,
            @Query("end_date") String end_date,
            @Query("page_Index") int page_Index,
            @Query("page_Size") int page_size
    );

    // 电子钱包余额
    @GET("/ygoa/ydpt/queryAcctountAmt.action")
    Observable<Amount> getAmount(
            @Query("sessionId") String sessionId
    );

    // 获取常用联系人
    @GET("/ygoa/ydpt/doQueryCylxr.action")
    Observable<CyContacts> getCyContacts(
            @Query("sessionId") String sessionId,
            @Query("page") int page,
            @Query("size") int size
    );

    // 导入手机通讯录
    @POST("/ygoa/ydpt/addExternalUser.action")
    @FormUrlEncoded
    Observable<String> save_contacts(
            @Field("jsonData") String jsonData
    );

    // 删除手机联系人
    @POST("/ygoa/ydpt/deleteCylxr.action")
    @FormUrlEncoded
    Observable<String> delete_contacts(
            @Field("jsonData") String jsonData
    );

}
