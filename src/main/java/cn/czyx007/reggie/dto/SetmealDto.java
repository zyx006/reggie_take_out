package cn.czyx007.reggie.dto;

import cn.czyx007.reggie.bean.Setmeal;
import cn.czyx007.reggie.bean.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
