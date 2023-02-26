package cn.czyx007.reggie.mapper;

import cn.czyx007.reggie.bean.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author : 张宇轩
 * @createTime : 2023/1/19 - 15:29
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
