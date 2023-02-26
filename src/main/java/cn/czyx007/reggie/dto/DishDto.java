package cn.czyx007.reggie.dto;

import cn.czyx007.reggie.bean.Dish;
import cn.czyx007.reggie.bean.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
