package cn.czyx007.reggie.controller;

import cn.czyx007.reggie.bean.Category;
import cn.czyx007.reggie.bean.Dish;
import cn.czyx007.reggie.bean.DishFlavor;
import cn.czyx007.reggie.bean.Setmeal;
import cn.czyx007.reggie.common.CustomException;
import cn.czyx007.reggie.common.R;
import cn.czyx007.reggie.dto.DishDto;
import cn.czyx007.reggie.service.CategoryService;
import cn.czyx007.reggie.service.DishFlavorService;
import cn.czyx007.reggie.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : 张宇轩
 * @createTime : 2023/1/18 - 13:49
 */
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    @GetMapping("/page")
    public R<Page<DishDto>> page(int page, int pageSize, String name){
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.hasLength(name), Dish::getName, name);
        lqw.orderByDesc(Dish::getUpdateTime);

        Page<Dish> pageInfo = dishService.page(new Page<>(page, pageSize), lqw);
        Page<DishDto> dishDtoPage = new Page<>();
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Category category = categoryService.getById(item.getCategoryId());

            if(category != null){
                dishDto.setCategoryName(category.getName());
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable long id){
        return R.success(dishService.getByIdWithFlavor(id));
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品成功");
    }

    /**
     * 更新菜品起售停售状态
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable int status, @RequestParam("ids") List<Long> ids){
        ids.forEach((id) -> {
            Dish dish = dishService.getById(id);
            dish.setStatus(status);
            dishService.updateById(dish);
        });
        return R.success("菜品状态已经更改成功！");
    }

    /**
     * 删除菜品
     * @param ids
     * @return
     */
    @Transactional
    @DeleteMapping
    public R<String> delete(@RequestParam("ids") List<Long> ids){
        LambdaQueryWrapper<Dish> qw = new LambdaQueryWrapper<>();
        qw.in(Dish::getId, ids);
        qw.eq(Dish::getStatus, 1);
        if (dishService.count(qw)>0) {
            throw new CustomException("菜品正在售卖中，不能删除");
        }

        dishService.removeByIds(ids);

        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        lqw.in(DishFlavor::getDishId, ids);
        dishFlavorService.remove(lqw);

        return R.success("删除成功！");
    }

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        lqw.eq(Dish::getStatus, 1);
        lqw.like(StringUtils.hasLength(dish.getName()), Dish::getName, dish.getName());
        lqw.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> dishes = dishService.list(lqw);
        List<DishDto> dtoList = dishes.stream().map((item) ->
            dishService.getByIdWithFlavor(item.getId())
        ).collect(Collectors.toList());

        return R.success(dtoList);
    }
}