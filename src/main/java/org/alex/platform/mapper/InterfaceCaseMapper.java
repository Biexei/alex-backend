package org.alex.platform.mapper;

import org.alex.platform.pojo.InterfaceCaseDO;
import org.alex.platform.pojo.InterfaceCaseInfoVO;
import org.alex.platform.pojo.InterfaceCaseListDTO;
import org.alex.platform.pojo.InterfaceCaseListVO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface InterfaceCaseMapper {
    Integer insertInterfaceCase(InterfaceCaseDO interfaceCaseDO);

    void updateInterfaceCase(InterfaceCaseDO interfaceCaseDO);

    void removeInterfaceCase(Integer interfaceCaseId);

    ArrayList<InterfaceCaseListVO> selectInterfaceCaseList(InterfaceCaseListDTO interfaceCaseListDTO);

    InterfaceCaseInfoVO selectInterfaceCaseByCaseId(Integer caseId);

    InterfaceCaseDO selectInterfaceCase(Integer caseId);

}
