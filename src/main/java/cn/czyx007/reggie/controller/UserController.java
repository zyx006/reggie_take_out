package cn.czyx007.reggie.controller;

import cn.czyx007.reggie.bean.User;
import cn.czyx007.reggie.common.R;
import cn.czyx007.reggie.service.UserService;
import cn.czyx007.reggie.utils.SendEmailUtils;
import cn.czyx007.reggie.utils.ValidateCodeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author : 张宇轩
 * @createTime : 2023/1/19 - 16:00
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 发送验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号/邮箱
        String phone = user.getPhone();
        if (StringUtils.hasLength(phone)) {
            //生成随机的4位验证码
            String code = ValidateCodeUtils.generateValidateCode4String(4);
            //发送邮件验证码
            SendEmailUtils.sendAuthCodeEmail(phone, code);
            //将生成的验证码保存到Redis用于校验，并且设置有效期为5分钟
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
//            redisTemplate.opsForValue().set(phone, "1234", 5, TimeUnit.MINUTES);
            return R.success("验证码发送成功");
        }
        return R.error("验证码发送失败");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map<String, String> map, HttpSession session){
        //获取手机号
        String phone = map.get("phone");
        //获取验证码
        String code = map.get("code");
        //从Redis中获取保存的验证码
        String codeInRedis = redisTemplate.opsForValue().get(phone);

        //codeInRedis = "1234";

        //将两个验证码进行比对
        if(StringUtils.hasLength(codeInRedis) && codeInRedis.equals(code)){
            //若比对成功，则登录成功
            //若数据库中无对应的手机号/邮箱则进行注册
            LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
            lqw.eq(User::getPhone, phone);
            User user = userService.getOne(lqw);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            //登录成功，删除Redis中缓存的验证码
            redisTemplate.delete(phone);

            return R.success(user);
        }
        return R.error("登录失败");
    }

    @PostMapping("/loginout")
    public R<String> logout(HttpSession session){
        session.removeAttribute("user");
        return R.success("退出成功");
    }

}
