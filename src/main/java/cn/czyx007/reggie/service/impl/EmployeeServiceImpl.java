package cn.czyx007.reggie.service.impl;

import cn.czyx007.reggie.bean.Employee;
import cn.czyx007.reggie.mapper.EmployeeMapper;
import cn.czyx007.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : 张宇轩
 * @createTime : 2022/12/19 - 23:26
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
