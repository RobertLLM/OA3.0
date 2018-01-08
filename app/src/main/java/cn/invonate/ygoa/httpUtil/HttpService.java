package cn.invonate.ygoa.httpUtil;


import java.util.List;

import cn.invonate.ygoa.Entry.Contacts;
import cn.invonate.ygoa.Entry.Department;
import cn.invonate.ygoa.Entry.User;
import retrofit2.http.GET;
import retrofit2.http.Query;
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
    Observable<String> getMembers(
            @Query("nameOrCode") String keywords,
            @Query("page") int page,
            @Query("row") int row
    );

}
