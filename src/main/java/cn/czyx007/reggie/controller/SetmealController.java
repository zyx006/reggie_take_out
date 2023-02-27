package cn.czyx007.reggie.controller;

import cn.czyx007.reggie.bean.Category;
import cn.czyx007.reggie.bean.Dish;
import cn.czyx007.reggie.bean.Setmeal;
import cn.czyx007.reggie.bean.SetmealDish;
import cn.czyx007.reggie.common.CustomException;
import cn.czyx007.reggie.common.R;
import cn.czyx007.reggie.dto.DishDto;
import cn.czyx007.reggie.dto.SetmealDto;
import cn.czyx007.reggie.service.CategoryService;
import cn.czyx007.reggie.service.DishService;
import cn.czyx007.reggie.service.SetmealDishService;
import cn.czyx007.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : 张宇轩
 * @createTime : 2023/1/19 - 11:51
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealDishService setmealDishService;

    //清理setmealCache分类下的所有缓存数据
    @CacheEvict(value = "setmealCache", allEntries = true)
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }

    @Cacheable(value = "setmealCache", key = "#setmeal.categoryId + '_' + #setmeal.status")
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);

        return R.success(list);
    }

    @GetMapping("/page")
    public R<Page<SetmealDto>> page(long page, long pageSize, String name){
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.hasLength(name), Setmeal::getName, name);
        lqw.orderByDesc(Setmeal::getUpdateTime);
        Page<Setmeal> pageInfo = setmealService.page(new Page<>(page, pageSize), lqw);

        Page<SetmealDto> dtoPage = new Page<>();
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Category category = categoryService.getById(item.getCategoryId());
            if(category != null){
                setmealDto.setCategoryName(category.getName());
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }

    @GetMapping("/{id}")
    public R<SetmealDto> get(@PathVariable long id){
        return R.success(setmealService.getByIdWithDishes(id));
    }

    @CacheEvict(value = "setmealCache", key = "#setmealDto.categoryId + '_' + #setmealDto.status")
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        setmealService.updateWithDishes(setmealDto);
        return R.success("修改套餐成功");
    }

    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable int status, @RequestParam("ids") List<Long> ids){
        ids.forEach((id) -> {
            Setmeal setmeal = setmealService.getById(id);
            setmeal.setStatus(status);
            setmealService.updateById(setmeal);
        });
        return R.success("套餐状态已经更改成功！");
    }

    //清理setmealCache分类下的所有缓存数据
    @CacheEvict(value = "setmealCache", allEntries = true)
    @Transactional
    @DeleteMapping
    public R<String> delete(@RequestParam("ids") List<Long> ids){
        LambdaQueryWrapper<Setmeal> qw = new LambdaQueryWrapper<>();
        qw.in(Setmeal::getId, ids);
        qw.eq(Setmeal::getStatus, 1);
        if (setmealService.count(qw)>0) {
            throw new CustomException("套餐正在售卖中，不能删除");
        }

        setmealService.removeByIds(ids);

        LambdaQueryWrapper<SetmealDish> lqw = new LambdaQueryWrapper<>();
        lqw.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(lqw);

        return R.success("套餐删除成功");
    }

    @GetMapping("/dish/{id}")
    public R<List<DishDto>> showImageDetail(@PathVariable long id){
        LambdaQueryWrapper<SetmealDish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> records = setmealDishService.list(lqw);
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            Long dishId = item.getDishId();
            Dish dish = dishService.getById(dishId);
            BeanUtils.copyProperties(dish, dishDto);
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(list);
    }
}
