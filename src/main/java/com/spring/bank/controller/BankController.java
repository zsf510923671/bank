package com.spring.bank.controller;


import com.google.code.kaptcha.Constants;
import com.spring.bank.pojo.Account;
import com.spring.bank.pojo.Record;
import com.spring.bank.service.BankService;
import com.spring.bank.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class BankController {
    @Autowired
    BankService bankService;

    String date1time;
    String date2time;
    @GetMapping("/")
    public String tologin(){
        return "login";
    }

    @GetMapping("/index")
    public String index(){
        return "login";
    }

    //登录验证
    @PostMapping("/dologin")
    public String dologin(@RequestParam("codenum") String codenum, @RequestParam("pwd") String pwd,
                          @RequestParam("gencode") String gencode, Model model, HttpSession session) {
        Account bank = bankService.getlogin(codenum, pwd);
        //取图片的值验证是否一致
        String codes = "1234";
        if (bank != null && gencode != null) {
            if (codes.equals(gencode)) {
                session.setAttribute("bankstatus",bank.getStatus());
                int status=(int)session.getAttribute("bankstatus");
                //判断账户是否冻结
                if(status!=0) {

                    session.setAttribute("bank", bank);
                    session.setAttribute("banknum", bank.getCardId());
                    session.setAttribute("bankid", bank.getId());
                    session.setAttribute("bankmoney", bank.getBalance());
                    return "index";
                }else{
                    model.addAttribute("error", "登录失败，账户被冻结！");
                    return "login";
                }
            } else {
                model.addAttribute("error", "验证码错误！");
                return "login";
            }

        } else {
            model.addAttribute("error", "用户名或密码错误！");
            return "login";
        }
    }

    //退出注销
    @RequestMapping("/loginout")
    public String loginout(HttpSession session){
        session.invalidate();
        return "redirect:/index";
    }

    //查询余额
    @RequestMapping("/selmoney")
    public String selmoney(HttpSession session){
        int id= (int) session.getAttribute("bankid");
        int rs=bankService.Selmoney(id);
        session.setAttribute("rs",rs);
        return "Selmoney";
    }

    //跳转转账页面
    @GetMapping("/toexpense")
    public String toexpense(){
        return "Expense";
    }

    //转账
    @RequestMapping("/expense")
    public String expense(@RequestParam("codenum") String codenum,@RequestParam("money") String money,
                          HttpSession session) {
        //获取登录账号信息
        int accoutid= (int) session.getAttribute("bankid");
        int moneys=bankService.Selmoney(accoutid);
        String codenums= (String) session.getAttribute("banknum");
        //计算金额
        Double sum = Double.valueOf(moneys) - Double.valueOf(money);
        System.out.println(sum);
        //判断转账账户是否为空
        Account selcodenum = bankService.Selcodenum(codenum);
        if (selcodenum != null) {
            //判断转账后登录用户金额是否大于或等于0
            if (sum >= 0) {
                //添加账单数据--存入
                Record record = new Record();
                record.setCardno(codenum);
                record.setBalance(sum);
                record.setIncome(Double.valueOf(money));
                int rs = bankService.addRecord(record);
                if (rs > 0) {
                    //添加账单数据--支出
                    Record income = new Record();
                    income.setCardno(codenums);
                    income.setBalance(sum);
                    income.setExpense(Double.valueOf(money));
                    bankService.addincome(income);
                    //修改accout表金额
                    Account accout=new Account();
                    accout.setId(accoutid);
                    accout.setBalance(sum);
                    int uprs=bankService.UpdBalance(accout);
                    if(uprs>0) {

                        return "Success";
                    }
                }else{
                    session.setAttribute("error","转账账号或金额有误");
                    return "Expense";
                }
            } else {
                session.setAttribute("error","转账账号或金额有误");
                return "Expense";
            }
        }

        return "Expense";
    }

    //查询交易记录
    @GetMapping("/time")
    public String time(){
        return "Time";
    }

    //分页查询交易记录
    @RequestMapping("/totime")
    public String totime(@RequestParam(value = "date1",required = false) String date1,@RequestParam(value = "date2",required = false) String date2,
                         HttpSession session,@RequestParam(value = "pageno",required = false) String pageno,
                         @RequestParam(value = "pageSize",required = false) String pageSize,Model model){
        session.setAttribute("date1",date1);
        session.setAttribute("date2",date2);
        //给全局变量赋值 之后取值取全局变量
        if(session.getAttribute("date1")!=null && session.getAttribute("date2")!=null) {
            date1time = date1;
            date2time = date2;
        }
        if(pageno==null){
            pageno="1";
        }
        if(pageSize==null){
            pageSize="4";
        }
        int count=bankService.Count(date1time,date2time);
        int num=(Integer.valueOf(pageno)-1)*(Integer.valueOf(pageSize));
        Page page=new Page();
        page.setPageCount(count);
        page.setPageno(Integer.valueOf(pageno));
        model.addAttribute("page",page);
        List<Record> userlist=bankService.getList(date1time,date2time,num,Integer.valueOf(pageSize));
        model.addAttribute("userlist",userlist);
        return "TimeList";
    }

    //修改密码--跳转页面
    @RequestMapping("/toupdpwd")
    public String toupdpwd(){
        return "UpdPwd";
    }

    //修改密码
    @PostMapping("/updpwd")
    public String updpwd(@RequestParam("pwd1") String pwd1,@RequestParam("pwd2") String pwd2,
                         Model model,HttpSession session){
        //获取登录账号id
        int accoutid= (int) session.getAttribute("bankid");
        //判断原密码是否存在
        Account selpwd=bankService.getPwd(pwd1,accoutid);
        if(selpwd!=null){
            //修改密码
            Account accout=new Account();
            accout.setId(accoutid);
            accout.setPassword(pwd2);
            int rs=bankService.UpdPwd(accout);
            if(rs>0){
                return "Success";
            }
        }
        model.addAttribute("error","请输入正确的原密码");
        return "UpdPwd";
    }
}
