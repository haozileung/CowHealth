/**
 * 
 * <b>项目名：</b>CowHealth<br />
 * <b>包名：</b>com.haozileung.scau.server.service.impl<br />
 * <b>文件名：</b>EquipmentServiceImpl.java<br />
 * <b>文件描述：</b>TODO<br />
 * <b>创建人：</b>lianghaopeng<br />
 * <b>修改人：</b>lianghaopeng<br />
 * <b>修改时间：</b>2013-1-30 下午4:01:38<br />
 * <b>修改备注：</b><br />
 * 
 * @version 1.0.0
 * 
 */
package com.haozileung.scau.server.service.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haozileung.scau.server.domain.Equipment;
import com.haozileung.scau.server.domain.support.EquipmentDoToDtoConvertor;
import com.haozileung.scau.server.dto.EquipmentInfo;
import com.haozileung.scau.server.repository.IEquipmentRepository;
import com.haozileung.scau.server.service.IEquipmentService;

/**
 * 
 * <b>类名称：</b>EquipmentServiceImpl<br/>
 * <b>类描述：</b>TODO<br/>
 * <b>创建人：</b>lianghaopeng<br/>
 * <b>修改人：</b>lianghaopeng<br/>
 * <b>修改时间：</b>2013-1-30 下午4:01:38<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0
 * 
 */
@Service("EquipmentService")
public class EquipmentServiceImpl implements IEquipmentService {

	private final static Logger logger = LoggerFactory
			.getLogger(EquipmentServiceImpl.class);

	@Autowired
	private IEquipmentRepository equipmentRepository;

	@Override
	public boolean addEquipment(EquipmentInfo equipmentInfo) {
		Equipment equipment = new Equipment(equipmentInfo);
		Equipment eq = null;
		try {
			eq = equipmentRepository.save(equipment);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		if (eq == null) {
			return false;
		}
		return true;
	}

	@Override
	public boolean updateEquipment(EquipmentInfo equipmentInfo) {
		if (equipmentInfo.getEquipmentId() != null
				&& !equipmentInfo.getEquipmentId().isEmpty()) {
			return addEquipment(equipmentInfo);
		}
		return false;
	}

	@Override
	public boolean deleteEquipmentById(ObjectId oId) {
		if (oId != null) {
			equipmentRepository.delete(oId);
			return true;
		}
		return false;
	}

	@Override
	public List<EquipmentInfo> getAllEquipment() {
		return EquipmentDoToDtoConvertor.getInstance().dos2Dtos(
				equipmentRepository.findAll());
	}

	@Override
	public EquipmentInfo getEquipmentById(ObjectId oId) {
		return EquipmentDoToDtoConvertor.getInstance().doToDto(
				equipmentRepository.findOne(oId));
	}

	public IEquipmentRepository getEquipmentRepository() {
		return equipmentRepository;
	}

	public void setEquipmentRepository(IEquipmentRepository equipmentRepository) {
		this.equipmentRepository = equipmentRepository;
	}

	@Override
	public EquipmentInfo getEquipmentByCowId(String cowId) {
		if (cowId != null && !cowId.isEmpty()) {
			return EquipmentDoToDtoConvertor.getInstance().doToDto(
					equipmentRepository.findByCowId(cowId));
		}
		return null;
	}

}
