package org.alex.platform.mapper;

import org.alex.platform.pojo.InterfaceAssertDO;
import org.alex.platform.pojo.InterfaceAssertVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterfaceAssertMapper {
    void insertAssert(InterfaceAssertDO interfaceAssertDO);

    void insertAssertList(List<InterfaceAssertDO> interfaceAssertDOList);

    void updateAssert(InterfaceAssertDO interfaceAssertDO);

    void deleteAssertByCaseId(Integer caseId);

    void deleteAssertByAssertId(Integer assertId);

    List<InterfaceAssertDO> selectAssertList(InterfaceAssertDO assertDO);

    List<InterfaceAssertDO> checkAssertType(InterfaceAssertDO assertDO);

    List<Integer> selectAllAssertId(Integer caseId);
}
