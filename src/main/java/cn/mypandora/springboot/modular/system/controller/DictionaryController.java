package cn.mypandora.springboot.modular.system.controller;

import cn.mypandora.springboot.core.base.PageInfo;
import cn.mypandora.springboot.core.exception.CustomException;
import cn.mypandora.springboot.modular.system.model.po.Dictionary;
import cn.mypandora.springboot.modular.system.service.DictionaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * DictionaryController
 * <p>
 * 因使用application/json格式传输数据，故使用@RequestParam @PathVariable @RequestBody等注解收集参数。
 * 批量删除收集前台数组参数时(复杂参数)，只能使用Map或者对应的POJO接收，为了省写代码，使用Map方式了。
 *
 * @author hankaibo
 * @date 2019/6/14
 */
@Api(tags = "字典管理")
@RestController
@RequestMapping("/api/v1/dictionaries")
public class DictionaryController {

    private DictionaryService dictionaryService;

    @Autowired
    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    /**
     * 查询分页字典数据。
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @return 分页数据
     */
    @ApiOperation(value = "字典列表", notes = "分页查询字典列表。")
    @GetMapping
    public PageInfo<Dictionary> pageDictionary(@RequestParam(value = "current", defaultValue = "1") @ApiParam(value = "页码", required = true) int pageNum,
                                               @RequestParam(value = "pageSize", defaultValue = "10") @ApiParam(value = "条数", required = true) int pageSize,
                                               Dictionary dictionary) {
        return dictionaryService.pageDictionary(pageNum, pageSize, dictionary);
    }

    /**
     * 新建字典。
     *
     * @param dictionary 字典数据
     */
    @ApiOperation(value = "字典新建", notes = "根据数据新建一个字典。")
    @PostMapping
    public void addDictionary(@RequestBody @ApiParam(name = "dictionary", value = "字典数据", required = true) Dictionary dictionary) {
        // 如果没有parentId为空，那么就创建为一个根节点，parentId是null。
        if (dictionary.getParentId() == null || dictionary.getParentId() < 1) {
            dictionary.setParentId(null);
        }
        dictionaryService.addDictionary(dictionary);
    }


    /**
     * 查询字典某项的详细数据。
     *
     * @param id 字典主键id
     * @return 新建结果
     */
    @ApiOperation(value = "字典详情", notes = "根据字典id查询字典详情。")
    @GetMapping("/{id}")
    public Dictionary getDictionaryById(@PathVariable("id") @ApiParam(value = "字典主键", required = true) Long id) {
        Dictionary dictionary = dictionaryService.getDictionary(id);
        if (dictionary == null) {
            throw new CustomException(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase());
        }
        return dictionary;
    }

    /**
     * 更新字典。
     *
     * @param dictionary 字典数据
     */
    @ApiOperation(value = "字典更新", notes = "根据字典数据更新一个字典。")
    @PutMapping("/{id}")
    public void updateDictionary(@RequestBody @ApiParam(value = "字典数据", required = true) Dictionary dictionary) {
        dictionaryService.updateDictionary(dictionary);
    }

    /**
     * 启用|禁用字典。
     *
     * @param id  字典主键id
     * @param map 状态(启用:1，禁用:0)
     */
    @ApiOperation(value = "字典状态启用禁用", notes = "根据字典id启用或禁用其状态。")
    @PatchMapping("/{id}")
    public void enableDictionary(@PathVariable("id") @ApiParam(value = "字典主键id", required = true) Long id,
                                 @RequestBody @ApiParam(value = "启用(1)，禁用(0)", required = true) Map<String, Integer> map) {
        Integer status = map.get("status");
        dictionaryService.enableDictionary(id, status);
    }

    /**
     * 删除字典。
     *
     * @param dictId 字典主键id
     */
    @ApiOperation(value = "字典删除", notes = "根据字典Id删除一个字典。")
    @DeleteMapping("/{id}")
    public void deleteDictionary(@PathVariable("id") @ApiParam(value = "字典主键id", required = true) Long dictId) {
        dictionaryService.deleteDictionary(dictId);
    }

    /**
     * 批量删除字典。
     *
     * @param map 字典id数组
     */
    @ApiOperation(value = "字典删除(批量)", notes = "根据字典Id批量删除字典。")
    @DeleteMapping
    public void deleteBatchDictionary(@RequestBody @ApiParam(value = "字典主键数组ids", required = true) Map<String, long[]> map) {
        dictionaryService.deleteBatchDictionary(map.get("ids"));
    }

}
