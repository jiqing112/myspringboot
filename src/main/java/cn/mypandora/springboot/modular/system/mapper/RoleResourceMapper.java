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
     * 赋予角色资源。
     *
     * @param roleId         角色Id
     * @param resourceIdList 资源id集合
     */
    void grantRoleResource(@Param(value = "roleId") Long roleId, @Param(value = "resourceIdList") long[] resourceIdList);

    /**
     * 删除角色某些资源。
     *
     * @param roleId         角色Id
     * @param resourceIdList 资源id集合
     */
    void deleteRoleSomeResource(@Param(value = "roleId") Long roleId, @Param(value = "resourceIdList") long[] resourceIdList);

    /**
     * 删除角色所有资源。
     *
     * @param roleId 角色Id
     */
    void deleteRoleAllResource(Long roleId);

    /**
     * 删除角色所有资源（批量）。
     *
     * @param roleIdList 角色Id集合
     * @see <a href="https://chenzhou123520.iteye.com/blog/1921284">mybatis foreach参数问题</a>
     */
    void deleteBatchRoleAllResource(@Param(value = "roleIdList") long[] roleIdList);

    /**
     * 删除资源所有的角色。
     *
     * @param resourceId 资源Id
     */
    void deleteResourceAllRole(Long resourceId);

    /**
     * 删除资源所有角色（批量）。
     *
     * @param resourceIdList 资源Id集合
     */
    void deleteBatchResourceAllRole(@Param(value = "resourceIdList") long[] resourceIdList);
}
