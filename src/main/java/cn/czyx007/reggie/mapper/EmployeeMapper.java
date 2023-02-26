package cn.czyx007.reggie.mapper;

import cn.czyx007.reggie.bean.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author : 张宇轩
 * @createTime : 2022/12/19 - 23:25
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
