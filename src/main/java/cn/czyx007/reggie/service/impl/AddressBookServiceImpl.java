package cn.czyx007.reggie.service.impl;

import cn.czyx007.reggie.bean.AddressBook;
import cn.czyx007.reggie.mapper.AddressBookMapper;
import cn.czyx007.reggie.service.AddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author : 张宇轩
 * @createTime : 2023/1/19 - 17:57
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
