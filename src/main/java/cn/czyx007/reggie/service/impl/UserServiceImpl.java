package cn.czyx007.reggie.service.impl;

import cn.czyx007.reggie.bean.User;
import cn.czyx007.reggie.mapper.UserMapper;
import cn.czyx007.reggie.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author : 张宇轩
 * @createTime : 2023/1/19 - 15:30
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
