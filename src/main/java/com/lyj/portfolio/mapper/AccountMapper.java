package com.lyj.portfolio.mapper;

import com.lyj.portfolio.VO.Account;
import com.lyj.portfolio.account.SignUpForm;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AccountMapper{

    @Select("SELECT EXISTS (SELECT 1 FROM account WHERE user_id = #{signUpForm.user_id})")
    boolean existsUserId(@Param("signUpForm") SignUpForm signUpForm);

    @Insert("INSERT INTO account(user_id, user_password, user_name, user_email)" +
            " VALUES(#{account.user_id}, #{account.password}, #{account.name}, #{account.email})")
    void save(@Param("account") Account account);

    @Insert("UPDATE account SET user_emailCheckToken=#{account.emailCheckToken} WHERE user_email = #{account.email}")
    void insertEmailCheckToken(@Param("account") Account account);

    @Select("SELECT * from account WHERE user_email = #{email}")
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

    @Update("UPDATE account SET user_joinedAt=#{account.joinedAt}, user_emailVerified=#{account.emailVerified} WHERE user_email=#{account.email}")
    void verifiedAccount(@Param("account") Account account);

    @Select("SELECT * from account WHERE user_id = #{username}")
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
    Account findByUserId(String username);

    @Insert("UPDATE account SET user_findPasswordToken=#{account.findPasswordToken} WHERE user_email = #{account.email}")
    void insertFindPasswordToken(@Param("account") Account account);

    @Insert("UPDATE account SET user_password=#{password}, user_findPasswordToken=null WHERE user_email = #{email}")
    void updatePassword(String password, String email);
}
