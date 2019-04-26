package com.spring.bank.service;

import com.spring.bank.dao.BankMapper;
import com.spring.bank.pojo.Account;
import com.spring.bank.pojo.Record;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BankService {

    @Resource
    BankMapper mapper;

    public Account getlogin(@Param("cardId") String cardId, @Param("password") String password) {
        return mapper.getlogin(cardId,password);
    }

    public int Selmoney(@Param("id") int id){
        return mapper.Selmoney(id);
    }

    public int addRecord(Record record){
        return mapper.addRecord(record);
    }

    public Account Selcodenum(@RequestParam("cardId") String cardId){
        return mapper.Selcodenum(cardId);
    }

    public int UpdBalance(Account accout){
        return mapper.UpdBalance(accout);
    }

    public List<Record> getList(@RequestParam("transaction_date1") String transaction_date1, @RequestParam("transaction_date2") String transaction_date2, @RequestParam("pageno") int pageno, @RequestParam("pageSize") int pageSize){
        return mapper.getList(transaction_date1,transaction_date2,pageno,pageSize);
    }

    public int Count(@RequestParam("transaction_date1") String transaction_date1,@RequestParam("transaction_date2") String transaction_date2){
        return mapper.Count(transaction_date1,transaction_date2);
    }

    public int addincome(Record record){
        return mapper.addincome(record);
    }

    public Account getPwd(@RequestParam("password") String password,@RequestParam("id") int id){
        return mapper.getPwd(password,id);
    }

    public int UpdPwd(Account accout){
        return mapper.UpdPwd(accout);
    }
}
