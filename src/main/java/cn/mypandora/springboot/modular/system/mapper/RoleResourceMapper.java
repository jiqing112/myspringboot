package cn.mypandora.springboot.modular.system.mapper;

import cn.mypandora.springboot.core.base.MyBaseMapper;
import cn.mypandora.springboot.modular.system.model.po.RoleResource;
import org.apache.ibatis.annotations.Param;

/**
 * RoleResourceMapper
 *
 * @author hankaibo
 * @date 2019/6/14
 */
public interface RoleResourceMapper extends MyBaseMapper<RoleResource> {

    /**
     * 赋予角色某些资源。
     *
     * @param roleId         角色Id
     * @param resourceListId 资源id集合
     * @return 成功与否
     */
    int giveRoleResource(@Param(value = "roleId") Long roleId, @Param(value = "resourceListId") Long[] resourceListId);

    /**
     * 删除角色某些资源。
     *
     * @param roleId 角色Id
     */
    void deleteRoleResource(Long roleId);

    /**
     * 批量删除角色某些资源。
     *
     * @param roleListId 角色Id
     * @see <a href="https://chenzhou123520.iteye.com/blog/1921284">mybatis foreach参数问题</a>
     */
    void deleteBatchRoleResource(@Param(value = "roleListId") Long[] roleListId);
}
