package com.spring.bank.dao;

import com.spring.bank.pojo.Account;
import com.spring.bank.pojo.Record;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Mapper
public interface BankMapper {
    //登录功能
    @Select("SELECT * FROM `accout` WHERE `cardId`=#{cardId} and `password`=#{password}")
    public Account getlogin(@Param("cardId") String cardId, @Param("password") String password);

    //查询余额
    @Select("SELECT `balance` FROM `accout` WHERE `id`=#{id}")
    public int Selmoney(@Param("id") int id);

    //转账--存入
    @Insert("INSERT INTO `transaction_record`(`cardno`,`transaction_date`,`income`,`balance`,`transaction_type`) VALUES(#{cardno},now(),#{income},#{balance},\"存款\")")
    public int addRecord(Record record);

    //转账--支出
    @Insert("INSERT INTO `transaction_record`(`cardno`,`transaction_date`,`expense`,`balance`,`transaction_type`) VALUES(#{cardno},now(),#{expense},#{balance},\"取款\")")
    public int addincome(Record record);

    //修改Accout表的金额---转账
    @Update("UPDATE `accout` SET `balance`=#{balance} where `id`=#{id}")
    public int UpdBalance(Account accout);

    //查询转账账号是否存在---转账
    @Select("SELECT * FROM `accout` WHERE `cardId`=#{cardId}")
    public Account Selcodenum(@RequestParam("cardId") String cardId);

    //分页查询交易记录
    @Select("SELECT * FROM `transaction_record` WHERE `transaction_date` BETWEEN #{transaction_date1} and #{transaction_date2} limit #{pageno},#{pageSize}")
    public List<Record> getList(@RequestParam("transaction_date1") String transaction_date1,@RequestParam("transaction_date2") String transaction_date2,@RequestParam("pageno") int pageno,@RequestParam("pageSize") int pageSize);

    //查询总条数
    @Select("SELECT COUNT(*) FROM `transaction_record` WHERE `transaction_date` BETWEEN #{transaction_date1} and #{transaction_date2}")
    public int Count(@RequestParam("transaction_date1") String transaction_date1,@RequestParam("transaction_date2") String transaction_date2);

    //查询密码是否正确--修改密码
    @Select("SELECT * FROM `accout` WHERE `password`=#{password} and `id`=#{id}")
    public Account getPwd(@RequestParam("password") String password,@RequestParam("id") int id);

    //修改密码
    @Update("UPDATE `accout` SET `password`=#{password} where `id`=#{id}")
    public int UpdPwd(Account accout);

}
