package cn.mypandora.springboot.modular.system.service.impl;

import cn.mypandora.springboot.modular.system.mapper.DepartmentMapper;
import cn.mypandora.springboot.modular.system.model.po.Department;
import cn.mypandora.springboot.modular.system.model.po.Resource;
import cn.mypandora.springboot.modular.system.service.DepartmentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * DepartmentServiceImpl
 *
 * @author hankaibo
 * @date 2019/9/25
 */
@Service("Department")
public class DepartmentServiceImpl implements DepartmentService {

    private DepartmentMapper departmentMapper;

    @Autowired
    public DepartmentServiceImpl(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }

    @Override
    public List<Department> loadFullDepartment() {
        return departmentMapper.loadFullTree();
    }

    @Override
    public List<Department> getDepartmentDescendant(Long id) {
        return departmentMapper.getDescendant(id);
    }

    @Override
    public List<Department> getDepartmentChild(Long id) {
        return departmentMapper.getChild(id);
    }

    @Override
    public Department getDepartmentParent(Long id) {
        return departmentMapper.getParent(id);
    }

    @Override
    public List<Department> getDepartmentAncestry(Long id) {
        return departmentMapper.getAncestry(id);
    }

    @Override
    public List<Department> getDepartmentSibling(Long id) {
        return departmentMapper.getSiblings(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addDepartment(Department department) {
        Date now = new Date(System.currentTimeMillis());
        department.setCreateTime(now);
        departmentMapper.lftPlus2(department.getParentId());
        departmentMapper.rgtPlus2(department.getParentId());
        departmentMapper.insert(department);
        departmentMapper.parentRgtPlus2(department.getParentId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delDepartment(Long id) {
        Department department = new Department();
        department.setId(id);
        // 先求出要删除的节点的所有信息，利用左值与右值计算出要删除的节点数量。
        // 删除节点数=(节点右值-节点左值+1)/2
        Department info = departmentMapper.selectByPrimaryKey(department);
        Integer deleteAmount = info.getRgt() - info.getLft() + 1;
        // 求出要删除的节点所有子孙节点
        Map<String, Number> map = new HashMap<>(2);
        map.put("id", id);
        List<Department> willDelDepartmentList = departmentMapper.getDescendant(id);
        List<Long> idList = willDelDepartmentList.stream().map(item -> item.getId()).collect(Collectors.toList());
        idList.add(id);
        String ids = StringUtils.join(idList, ",");
        // 更新此节点之后的相关节点左右值
        departmentMapper.lftMinus(id, deleteAmount);
        departmentMapper.rgtMinus(id, deleteAmount);
        // 批量删除节点及子孙节点
        departmentMapper.deleteByIds(ids);
    }

    @Override
    public void moveDepartment(Long sourceId, Long targetId) {
        // 先取出源节点与目标节点两者的信息
        Department targetDepartment = new Department();
        Department sourceDepartment = new Department();

        targetDepartment.setId(targetId);
        sourceDepartment.setId(sourceId);

        Department targetInfo = departmentMapper.selectByPrimaryKey(targetDepartment);
        Department sourceInfo = departmentMapper.selectByPrimaryKey(sourceDepartment);

        int targetAmount = targetInfo.getRgt() - targetInfo.getLft() + 1;
        int sourceAmount = sourceInfo.getRgt() - sourceInfo.getLft() + 1;

        List<Long> sourceIdList = getDescendantId(sourceId);
        List<Long> targetIdList = getDescendantId(targetId);

        // 确定方向，目标大于源：下称；反之：上移。
        if (targetInfo.getRgt() > sourceInfo.getRgt()) {
            targetAmount *= 1;
            sourceAmount *= -1;
        } else {
            targetAmount *= -1;
            sourceAmount *= 1;
        }
        // 源节点及子孙节点左右值 targetAmount
        departmentMapper.selfAndDescendant(sourceIdList, targetAmount);
        // 目标节点及子孙节点左右值 sourceAmount
        departmentMapper.selfAndDescendant(targetIdList, sourceAmount);
    }

    @Override
    public Department findDepartmentById(Long id) {
        Department department = new Department();
        department.setId(id);
        return departmentMapper.selectByPrimaryKey(department);
    }

    @Override
    public void updateDepartment(Department department) {
        Date now = new Date(System.currentTimeMillis());
        department.setUpdateTime(now);
        departmentMapper.updateByPrimaryKeySelective(department);
    }

    /**
     * 获取此节点及其子孙节点的id
     *
     * @param id 节点
     * @return 节点集合
     */
    private List<Long> getDescendantId(Long id) {
        List<Department> departmentList = departmentMapper.getDescendant(id);
        List<Long> idList = departmentList.stream().map(item -> item.getId()).collect(Collectors.toList());
        idList.add(id);
        return idList;
    }
}