package com.lyj.portfolio.mapper;

import com.lyj.portfolio.VO.Account;
import com.lyj.portfolio.account.SignUpForm;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AccountMapper{

    @Select("SELECT EXISTS (SELECT 1 FROM mydb.account WHERE user_id = #{signUpForm.user_id})")
    boolean existsUserId(@Param("signUpForm") SignUpForm signUpForm);

    @Select("SELECT EXISTS (SELECT 1 FROM mydb.account WHERE user_email = #{signUpForm.email})")
    boolean existsEmail(@Param("signUpForm") SignUpForm signUpForm);

    @Insert("INSERT INTO mydb.account(user_id, user_password, user_name, user_email)" +
            " VALUES(#{account.user_id}, #{account.password}, #{account.name}, #{account.email})")
    void save(@Param("account") Account account);

    @Insert("INSERT INTO mydb.account(user_id, user_name, user_email, user_emailVerified)" +
            " VALUES(#{account.user_id}, #{account.name}, #{account.email}, #{account.emailVerified})")
    void saveForOauth(@Param("account") Account account);

    @Update("UPDATE mydb.account SET user_emailCheckToken=#{account.emailCheckToken} WHERE user_email = #{account.email}")
    void insertEmailCheckToken(@Param("account") Account account);

    @Select("SELECT * from mydb.account WHERE user_email = #{email}")
//    @ResultType(Account.class) 안써도 됨.
    @Results({
            @Result (property="user_id", column="user_id"),
            @Result (property="password", column="user_password"),
            @Result (property="name", column="user_name"),
            @Result (property="email", column="user_email"),
            @Result (property="emailVerified", column="user_emailVerified"),
            @Result (property="emailCheckToken", column="user_emailCheckToken"),
            @Result (property="joinedAt", column="user_JoinedAt"),
            @Result (property="findPasswordToken", column="user_findPasswordToken")
    })
    Account findByEmail(String email);

    @Update("UPDATE mydb.account SET user_joinedAt=#{account.joinedAt}, user_emailVerified=#{account.emailVerified} WHERE user_email=#{account.email}")
    void verifiedAccount(@Param("account") Account account);

    @Select("SELECT * from mydb.account WHERE user_id = #{user_id}")
    @Results({
            @Result (property="user_id", column="user_id"),
            @Result (property="password", column="user_password"),
            @Result (property="name", column="user_name"),
            @Result (property="email", column="user_email"),
            @Result (property="emailVerified", column="user_emailVerified"),
            @Result (property="emailCheckToken", column="user_emailCheckToken"),
            @Result (property="joinedAt", column="user_JoinedAt"),
            @Result (property="findPasswordToken", column="user_findPasswordToken")
    })
    Account findByUserId(String user_id);

    @Insert("UPDATE mydb.account SET user_findPasswordToken=#{account.findPasswordToken} WHERE user_email = #{account.email}")
    void insertFindPasswordToken(@Param("account") Account account);

    @Insert("UPDATE mydb.account SET user_password=#{password}, user_findPasswordToken=null WHERE user_email = #{email}")
    void updatePassword(String password, String email);

    @Update("UPDATE mydb.account SET user_password=#{signUpForm.password}, user_name=#{signUpForm.name} WHERE user_id = #{user_id}")
    void updateAccount(@Param("signUpForm")SignUpForm signUpForm, String user_id);

    @Update("UPDATE mydb.account SET user_name=#{signUpForm.name} WHERE user_id = #{user_id}")
    void updateWithoutPwd(@Param("signUpForm")SignUpForm signUpForm, String user_id);

    @Update("UPDATE mydb.account SET user_id=#{account.user_id}, user_name=#{account.name} WHERE user_email = #{account.email}")
    void updateForOauth(@Param("account") Account account);
}
